package io.amogus.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.HashMap;

public class SpriteManager {
    private final ShapeDrawer sd;
    private static SpriteManager instance;
    private final SpriteBatch batch;

    public HashMap<String, Texture> textures;

    private Matrix4 worldMatrix;
    private Matrix4 uiMatrix;

    private enum Mode { NONE, WORLD, UI }
    private Mode mode = Mode.NONE;

    private final LightingManager lighting;

    public static SpriteManager getInstance() {
        if (instance == null) instance = new SpriteManager();
        return instance;
    }

    private SpriteManager() {
        batch = new SpriteBatch();
        textures = new HashMap<String, Texture>();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 1f, 1f, 1f);
        pixmap.fill();
        Texture white = new Texture(pixmap);
        pixmap.dispose();

        sd = new ShapeDrawer(batch, new TextureRegion(white));

        lighting = new LightingManager(batch);
    }

    public void resize(int width, int height) {
        lighting.resize(width, height);
    }

    public void setWorldProjection(Matrix4 matrix) {
        this.worldMatrix = matrix;
    }

    public void setUiProjection(Matrix4 matrix) {
        this.uiMatrix = matrix;
    }

    public void setGlobalIllumination(float ambient) {
        lighting.setGlobalIllumination(ambient);
    }

    public void beginWorld() {
        if (batch.isDrawing()) throw new IllegalStateException("beginWorld() called while SpriteBatch is drawing. Missing end()?");
        if (mode != Mode.NONE) throw new IllegalStateException("beginWorld() while mode=" + mode);

        mode = Mode.WORLD;
        if (worldMatrix == null) throw new IllegalStateException("World projection not set. Call setWorldProjection(...) before beginWorld().");

        lighting.beginScene(worldMatrix);

        batch.setProjectionMatrix(worldMatrix);
        batch.begin();
    }

    public void beginScreen() {
        if (batch.isDrawing()) throw new IllegalStateException("beginScreen() called while SpriteBatch is drawing. Missing end()?");
        if (mode != Mode.NONE) throw new IllegalStateException("beginScreen() while mode=" + mode);

        mode = Mode.UI;
        if (uiMatrix != null) batch.setProjectionMatrix(uiMatrix);
        batch.begin();
    }

    public void end() {
        if (!batch.isDrawing()) throw new IllegalStateException("end() called but SpriteBatch is not drawing.");
        batch.end();

        if (mode == Mode.WORLD) {
            lighting.endSceneAndComposite(uiMatrix);
        }

        mode = Mode.NONE;
    }

    public void renderSpotlight(float x, float y, float radius, Color color, float intensity) {
        if (mode != Mode.WORLD) throw new IllegalStateException("renderSpotlight() called outside WORLD pass.");
        lighting.renderSpotlight(x, y, radius, color, intensity);
    }

    public void draw(float x, float y, float w, float h, String textureName) {
        if (mode != Mode.WORLD) throw new IllegalStateException("draw() called outside world pass. Use beginWorld() before draw(), or drawScreen().");
        batch.draw(textures.get(textureName), x, y, w, h);
    }

    public void draw(float x, float y, float w, float h, float rotation, String textureName) {
        if (mode != Mode.WORLD) throw new IllegalStateException("draw() called outside world pass. Use beginWorld() before draw(), or drawScreen().");
        batch.draw(new TextureRegion(textures.get(textureName)), x, y, w / 2f, h / 2f, w, h, 1f, 1f, rotation);
    }

    public void draw(float x, float y, float w, float h, float rotation, boolean flipX, String textureName) {
        if (mode != Mode.WORLD) {
            throw new IllegalStateException("draw() called outside world pass. Use beginWorld() before draw(), or drawScreen().");
        }
        TextureRegion r = new TextureRegion(textures.get(textureName));
        batch.draw(r.getTexture(), x, y, w / 2f, h / 2f, w, h, 1f, 1f, rotation, 0, 0, r.getRegionWidth(), r.getRegionHeight(), flipX, false);
    }

    public void drawLine(float x, float x2, float y, float y2, Color color) {
        if (mode != Mode.WORLD) throw new IllegalStateException("drawLine() called outside world pass. Use beginWorld() before drawLine().");
        sd.line(x, y, x2, y2, color);
    }

    public void drawRect(float x, float y, float w, float h, Color color) {
        if (mode != Mode.WORLD) throw new IllegalStateException("drawRect() called outside world pass. Use beginWorld() before drawRect().");
        sd.filledRectangle(x, y, w, h, color);
    }

    public void drawScreen(float x, float y, float w, float h, String textureName) {
        if (mode != Mode.UI) throw new IllegalStateException("drawScreen() called outside UI pass. Use beginScreen() before drawScreen().");
        batch.draw(textures.get(textureName), x, y, w, h);
    }

    public void drawScreen(float x, float y, float w, float h, float rotation, String textureName) {
        if (mode != Mode.UI) throw new IllegalStateException("drawScreen() called outside UI pass. Use beginScreen() before drawScreen().");
        batch.draw(new TextureRegion(textures.get(textureName)), x, y, w / 2f, h / 2f, w, h, 1f, 1f, rotation);
    }

    public void loadSprite(String name, String path) {
        textures.put(name, new Texture(path));
    }

    public TextureRegion[] getRegions(String texture, int frames) {
        Texture t = textures.get(texture);
        if (t == null) return new TextureRegion[0];

        int frameW = 32;
        int frameH = 32;

        int maxFrames = t.getWidth() / frameW;
        int count = Math.min(frames, maxFrames);

        TextureRegion[] regions = new TextureRegion[count];
        for (int i = 0; i < count; i++) {
            regions[i] = new TextureRegion(t, i * frameW, 0, frameW, frameH);
        }
        return regions;
    }


    public SpriteBatch getBatch() {
        return batch;
    }

    public void dispose() {
        for (Texture t : textures.values()) t.dispose();
        textures.clear();
        lighting.dispose();
        batch.dispose();
    }
}
