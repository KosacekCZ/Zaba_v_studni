package io.amogus.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class LightingManager {
    private final SpriteBatch batch;

    private final ShaderProgram lightmapShader;
    private final ShaderProgram compositeShader;

    private FrameBuffer sceneFbo;
    private FrameBuffer lightFbo;
    private TextureRegion sceneRegion;
    private TextureRegion lightRegion;

    private Texture whiteTex;

    private final Matrix4 screenMatrix = new Matrix4();
    private int screenW = 1;
    private int screenH = 1;

    private float ambient = 0.25f;
    private boolean sceneActive = false;

    private static final class Spotlight implements Pool.Poolable {
        float x, y;
        float radius;
        float r, g, b;
        float intensity;

        @Override public void reset() {
            x = y = 0f;
            radius = 0f;
            r = g = b = 1f;
            intensity = 1f;
        }
    }

    private final Array<Spotlight> frameLights = new Array<Spotlight>(false, 512);
    private final Pool<Spotlight> lightPool = new Pool<Spotlight>() {
        @Override protected Spotlight newObject() { return new Spotlight(); }
    };

    public LightingManager(SpriteBatch batch) {
        this.batch = batch;

        ShaderProgram.pedantic = false;
        lightmapShader = new ShaderProgram(
            Gdx.files.internal("shaders/sprite_pos.vert"),
            Gdx.files.internal("shaders/lightmap.frag")
        );
        if (!lightmapShader.isCompiled()) throw new IllegalStateException(lightmapShader.getLog());

        compositeShader = new ShaderProgram(
            Gdx.files.internal("shaders/composite.vert"),
            Gdx.files.internal("shaders/composite.frag")
        );
        if (!compositeShader.isCompiled()) throw new IllegalStateException(compositeShader.getLog());

        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(1f, 1f, 1f, 1f);
        p.fill();
        whiteTex = new Texture(p);
        p.dispose();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void resize(int width, int height) {
        screenW = Math.max(1, width);
        screenH = Math.max(1, height);

        if (sceneFbo != null) sceneFbo.dispose();
        if (lightFbo != null) lightFbo.dispose();

        sceneFbo = new FrameBuffer(Pixmap.Format.RGBA8888, screenW, screenH, false);

        if (Gdx.gl30 != null) {
            lightFbo = new GLFrameBuffer.FrameBufferBuilder(screenW, screenH)
                .addColorTextureAttachment(GL30.GL_RGBA16F, GL30.GL_RGBA, GL30.GL_FLOAT)
                .build();
        } else {
            lightFbo = new FrameBuffer(Pixmap.Format.RGBA8888, screenW, screenH, false);
        }

        sceneRegion = new TextureRegion(sceneFbo.getColorBufferTexture());
        sceneRegion.flip(false, true);

        lightRegion = new TextureRegion(lightFbo.getColorBufferTexture());
        lightRegion.flip(false, true);

        screenMatrix.setToOrtho2D(0f, 0f, screenW, screenH);
    }


    public void setGlobalIllumination(float ambient) {
        if (ambient < 0f) ambient = 0f;
        if (ambient > 1f) ambient = 1f;
        this.ambient = ambient;
    }

    public void beginScene(Matrix4 worldProjection) {
        if (sceneActive) throw new IllegalStateException("beginScene() called twice without endSceneAndComposite().");
        if (batch.isDrawing()) throw new IllegalStateException("beginScene() while SpriteBatch is drawing.");

        clearFrameLights();
        sceneActive = true;

        sceneFbo.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(worldProjection);
    }

    public void renderSpotlight(float x, float y, float radius, Color color, float intensity) {
        if (!sceneActive) throw new IllegalStateException("renderSpotlight() requires beginScene()..endSceneAndComposite().");
        Spotlight s = lightPool.obtain();
        s.x = x;
        s.y = y;
        s.radius = radius;
        s.r = color.r;
        s.g = color.g;
        s.b = color.b;
        s.intensity = intensity;
        frameLights.add(s);
    }

    public void endSceneAndComposite(Matrix4 screenOrUiProjection) {
        if (!sceneActive) throw new IllegalStateException("endSceneAndComposite() without beginScene().");
        if (batch.isDrawing()) throw new IllegalStateException("endSceneAndComposite() while SpriteBatch is drawing.");

        sceneFbo.end();

        lightFbo.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setShader(lightmapShader);
        batch.begin();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);

        for (int i = 0; i < frameLights.size; i++) {
            Spotlight s = frameLights.get(i);

            batch.flush();

            lightmapShader.setUniformf("u_lightPos", s.x, s.y);
            lightmapShader.setUniformf("u_lightColor", s.r, s.g, s.b);
            lightmapShader.setUniformf("u_radius", s.radius);
            lightmapShader.setUniformf("u_intensity", s.intensity);

            float size = s.radius * 2f;
            batch.draw(whiteTex, s.x - s.radius, s.y - s.radius, size, size);
        }
        batch.flush();


        batch.end();
        lightFbo.end();

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.setProjectionMatrix(screenOrUiProjection != null ? screenOrUiProjection : screenMatrix);
        batch.setShader(compositeShader);
        batch.begin();

        sceneRegion.getTexture().bind(0);
        lightRegion.getTexture().bind(1);
        compositeShader.setUniformi("u_scene", 0);
        compositeShader.setUniformi("u_light", 1);
        compositeShader.setUniformf("u_ambient", ambient);

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

        batch.draw(sceneRegion, 0f, 0f, (float)screenW, (float)screenH);

        batch.end();
        batch.setShader(null);

        clearFrameLights();
        sceneActive = false;
    }

    private void clearFrameLights() {
        for (int i = 0; i < frameLights.size; i++) lightPool.free(frameLights.get(i));
        frameLights.clear();
    }

    public void dispose() {
        if (sceneFbo != null) sceneFbo.dispose();
        if (lightFbo != null) lightFbo.dispose();
        if (lightmapShader != null) lightmapShader.dispose();
        if (compositeShader != null) compositeShader.dispose();
        if (whiteTex != null) whiteTex.dispose();
    }
}
