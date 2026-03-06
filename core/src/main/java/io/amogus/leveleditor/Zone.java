package io.amogus.leveleditor;

public class Zone {
    public float x;
    public float y;
    public float w;
    public float h;
    public int layer;

    public Zone(float x, float y, float w, float h, int layer) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.layer = layer;
    }

    public boolean contains(float px, float py) {
        return px >= x && px <= x + w && py >= y && py <= y + h;
    }

    public boolean contains(float px, float py, float margin)  {
        return px >= x + margin && px <= x + w - margin
            && py >= y + margin && py <= y + h - margin;
    }

    public boolean intersects(float rx, float ry, float rw, float rh) {
        return rx < x + w && rx + rw > x && ry < y + h && ry + rh > y;
    }

    @Override
    public String toString() {
        return "Zone," +  x + "," + y + "," + w + "," + h + "," + layer + ";";
    }
}
