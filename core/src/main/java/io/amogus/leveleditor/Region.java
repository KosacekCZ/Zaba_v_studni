package io.amogus.leveleditor;

import com.badlogic.gdx.math.Vector2;

public class Region {
    public float x;
    public float y;
    public float w;
    public float h;

    public Region(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Region(Vector2 vec, float w, float h) {
        this.x = vec.x;
        this.y = vec.y;
        this.w = w;
        this.h = h;
    }
}
