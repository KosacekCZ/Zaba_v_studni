package io.amogus.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import io.amogus.gamestates.Game;

public abstract class Gameobject {
    protected float x;
    protected float y;
    protected float w;
    protected float h;
    protected String texture;
    protected boolean collision;
    protected int renderLayer;

    public Gameobject(float x, float y, float w, float h, boolean collision, int renderLayer, String texture) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.renderLayer = renderLayer;
        this.collision = collision;
        this.texture = texture;
    }

    public Gameobject(float x, float y, boolean collision, int renderLayer, String texture) {
        this.x = x;
        this.y = y;
        this.w = 32f;
        this.h = 32f;
        this.renderLayer = renderLayer;
        this.collision = collision;
        this.texture = texture;
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

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public Sprite getSprite() {
        Sprite s = new Sprite();
        s.setBounds(x, y, w, h);
        s.setOrigin(w / 2, h / 2);
        s.setCenter(w / 2, h / 2);
        return s;
    }

    public int getRenderLayer() {
        return renderLayer;
    }

    public void setRenderLayer(int renderLayer) {
        this.renderLayer = renderLayer;
    }

    @Override
    public String toString() {
        return x + ";" +  y + ";" + w + ";" + h + ";" + renderLayer + ";" + collision + ";" + texture + "\n";
    }
}
