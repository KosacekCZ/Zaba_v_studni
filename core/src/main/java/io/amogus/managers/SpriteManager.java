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

    private enum Mode { WORLD, UI }
    private Mode mode = Mode.WORLD;

    public static SpriteManager getInstance() {
        if (instance == null) {
            instance = new SpriteManager();
        }
        return instance;
    }

    private SpriteManager() {
        batch = new SpriteBatch();
        textures = new HashMap<>();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 1f, 1f, 1f);
        pixmap.fill();
        sd = new ShapeDrawer(batch, new TextureRegion(new Texture(pixmap)));
    }

    public void setWorldProjection(Matrix4 matrix) {
        this.worldMatrix = matrix;
    }

    public void setUiProjection(Matrix4 matrix) {
        this.uiMatrix = matrix;
    }

    public void beginWorld() {
        mode = Mode.WORLD;
        if (worldMatrix != null) {
            batch.setProjectionMatrix(worldMatrix);
        }
        batch.begin();
    }

    public void beginScreen() {
        mode = Mode.UI;
        if (uiMatrix != null) {
            batch.setProjectionMatrix(uiMatrix);
        }
        batch.begin();
    }

    public void end() {
        batch.end();
    }

    public void draw(float x, float y, float w, float h, String textureName) {
        if (mode != Mode.WORLD) {
            throw new IllegalStateException("draw() called outside world pass. Use beginWorld() before draw(), or drawScreen().");
        }
        batch.draw(textures.get(textureName), x, y, w, h);
    }

    public void draw(float x, float y, float w, float h, float rotation, String textureName) {
        if (mode != Mode.WORLD) {
            throw new IllegalStateException("draw() called outside world pass. Use beginWorld() before draw(), or drawScreen().");
        }
        batch.draw(new TextureRegion(textures.get(textureName)), x, y, w / 2f, h / 2f, w, h, 1, 1, rotation);
    }

    public void drawLine(float x, float x2, float y, float y2, Color color) {
        if (mode != Mode.WORLD) {
            throw new IllegalStateException("draw() called outside world pass. Use beginWorld() before draw(), or drawScreen().");
        }
        sd.line(x, y, x2, y2, color);
    }

    public void drawRect(float x, float y, float w, float h, Color color) {
        if (mode != Mode.WORLD) {
            throw new IllegalStateException("draw() called outside world pass. Use beginWorld() before draw(), or drawScreen().");
        }
        sd.filledRectangle(x, y, w, h, color);
    }

    public void drawScreen(float x, float y, float w, float h, String textureName) {
        if (mode != Mode.UI) {
            throw new IllegalStateException("drawScreen() called outside UI pass. Use beginScreen() before drawScreen().");
        }
        batch.draw(textures.get(textureName), x, y, w, h);
    }

    public void drawScreen(float x, float y, float w, float h, float rotation, String textureName) {
        if (mode != Mode.UI) {
            throw new IllegalStateException("drawScreen() called outside UI pass. Use beginScreen() before drawScreen().");
        }
        batch.draw(new TextureRegion(textures.get(textureName)), x, y, w / 2f, h / 2f, w, h, 1, 1, rotation);
    }

    public void dispose() {
        for (Texture t : textures.values()) {
            t.dispose();
        }
        batch.dispose();
    }

    public void loadSprite(String name, String path) {
        textures.put(name, new Texture(path));
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
