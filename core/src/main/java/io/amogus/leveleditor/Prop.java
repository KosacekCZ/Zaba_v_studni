package io.amogus.leveleditor;

public class Prop {
    public float x;
    public float y;
    public float z;
    public float w;
    public float h;
    public String texture;
    public float rotation;

    public Prop(float x, float y, float w, float h, String texture, float rotation) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.texture = texture;
        this.rotation = rotation;
    }

    public Prop(float x, float y, float z,  float w, float h, String texture, float rotation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.h = h;
        this.texture = texture;
        this.rotation = rotation;
    }

    @Override
    public String toString() {
        return texture + ", " + x + ", " + y + ", " + z + ", " + w + ", " + h + ", " + rotation + ";";
    }
}
