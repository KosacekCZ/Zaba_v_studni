package io.amogus.leveleditor;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.amogus.managers.Managers;

public class Prop {
    public float x;
    public float y;
    public float z;
    public float w;
    public float h;
    public String texture;
    public TextureRegion textureRegion;
    public float rotation;
    public boolean selected;
    public PropType type;

    public Prop(float x, float y, float w, float h, float rotation, String texture, PropType type) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.texture = texture;
        this.rotation = rotation;
        this.type = type;
    }

    public Prop(float x, float y, float z,  float w, float h, float rotation, String texture, PropType type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.h = h;
        this.texture = texture;
        this.rotation = rotation;
        this.type = type;

    }

    public Prop(float x, float y, float z, float w, float h, float rotation, TextureRegion textureRegion, PropType type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.h = h;
        this.textureRegion = textureRegion;
        this.rotation = rotation;
        this.type = type;
    }

    public void draw() {
        switch (type) {
            case WALL -> Managers.sm.draw(x, y, w, h, texture);
            case FLOOR -> Managers.sm.drawIso(x, y, w, h, 0, 1.0f, texture);
        }
    }

    public void draw(float opacity) {
        switch (type) {
            case WALL -> {
                if (textureRegion != null) {
                    Managers.sm.draw(x, y, w, h, 0, opacity, texture);
                } else  {
                    Managers.sm.draw(x, y, w, h, 0, opacity, textureRegion);
                }
            }
            case FLOOR -> Managers.sm.drawIso(x, y, w, h, 0, opacity, texture);
        }
    }

    public void draw(float opacity, float rotation) {
        switch (type) {
            case WALL -> {
                if (textureRegion != null) {
                    Managers.sm.draw(x, y, w, h, rotation, opacity, texture);
                } else  {
                    Managers.sm.draw(x, y, w, h, rotation, opacity, textureRegion);
                }
            }
            case FLOOR -> Managers.sm.drawIso(x, y, w, h, rotation, opacity, texture);
        }
    }

    @Override
    public String toString() {
        return texture + ", " + x + ", " + y + ", " + z + ", " + w + ", " + h + ", " + rotation + ";";
    }
}
