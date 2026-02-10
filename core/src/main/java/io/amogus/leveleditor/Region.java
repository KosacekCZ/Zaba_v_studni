package io.amogus.leveleditor;

import com.badlogic.gdx.math.Vector2;

public class Region {
    public float x;
    public float y;
    public float w;
    public float h;

    public Region(float x, float y, float w, float h) {
        this.x = (float) Math.floor(x);
        this.y = (float) Math.floor(y);
        this.w = (float) Math.ceil(w);
        this.h = (float) Math.ceil(h);
    }

    public Region(Vector2 vec, float w, float h) {
        this.x = (float) Math.floor(vec.x);
        this.y = (float) Math.floor(vec.y);
        this.w = (float) Math.ceil(w);
        this.h = (float) Math.ceil(h);
    }

    public float minX() { return x; }
    public float minY() { return y; }
    public float maxX() { return x + w; }
    public float maxY() { return y + h; }


    @Override
    public String toString() {
        return "Region(" + x + ", " + y + ", " + w + ", " + h + ")";
    }
}
