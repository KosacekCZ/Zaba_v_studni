package io.amogus.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Entity {
    protected Vector2 prevPos;
    protected Texture texture;
    protected Sprite sprite;
    protected int health;
    protected int damage;
    protected float speed;

    public Entity(float x, float y, Texture texture) {
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(sprite.getWidth(), sprite.getHeight());
        prevPos = new Vector2(getX(), getY());
    }

    public boolean hasMoved() {
        if (prevPos.x != getX() || prevPos.y != getY()) {
            prevPos.x =  getX();
            prevPos.y =  getY();
            return true;
        }
        return false;
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public void setX(float x) {
        sprite.setX(x);
    }

    public void setY(float y) {
        sprite.setY(y);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
