package io.amogus.items;

import io.amogus.entities.Entity;
import io.amogus.managers.SpriteManager;

public abstract class Item {
    protected int id;
    protected String name;
    protected float x;
    protected float y;
    protected float w;
    protected float h;
    protected String texture;

    protected SpriteManager sm = SpriteManager.getInstance();

    public Item(int id, String name, float x, float y, float w, float h, String texture) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.texture = texture;
    }

    public abstract void updateWorld();

    public abstract void updateScreen();

    public abstract void onCollide(Entity e);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}
