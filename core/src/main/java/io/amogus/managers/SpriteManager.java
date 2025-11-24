package io.amogus.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

public class SpriteManager {
    private static SpriteManager instance;
    private final SpriteBatch batch;

    public HashMap<String, Texture> textures;

    public static SpriteManager getInstance(){
        if(instance == null){
            instance = new SpriteManager();
        }
        return instance;
    }

    private SpriteManager(){
        batch = new SpriteBatch();
        textures = new HashMap<>();
    }

    public void draw(float x, float y, float w, float h, String textureName) {
        batch.draw(textures.get(textureName), x, y, w, h);
    }

    public void begin() {
        batch.begin();
    }

    public void end() {
        batch.end();
    }

    public void dispose() {
        for (Texture t :  textures.values()) {
            t.dispose();
        }

        batch.dispose();
    }

    public void loadSprite(String name, String path) {
        textures.put(name, new Texture(path));
    }
}
