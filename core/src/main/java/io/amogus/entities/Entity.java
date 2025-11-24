package io.amogus.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import io.amogus.managers.SpriteManager;

public abstract class Entity {
    protected Vector2 prevPos;
    protected Vector2 pos;
    protected String texture;
    protected Sprite sprite;
    protected int health;
    protected int damage;
    protected float speed;
    protected boolean isDestroy;
    protected SpriteManager sm;

    public Entity(float x, float y, String texture) {
        this.texture = texture;
        pos = new Vector2(x, y);
        sprite = new Sprite();
        sprite.setSize(32f, 32f);
        prevPos = new Vector2(getX(), getY());
        sm = SpriteManager.getInstance();
        this.isDestroy = false;
    }

    public Entity(float x, float y, String texture, int health , int damage, float speed) {
        sprite = new Sprite();
        sprite.setPosition(x, y);
        sprite.setSize(32f, 32f);
        prevPos = new Vector2(getX(), getY());
        this.health = health;
        this.damage = damage;
        this.speed = speed;
        sm = SpriteManager.getInstance();
        this.texture = texture;
        this.isDestroy = false;
    }

    public abstract void update();

    public abstract void onCollide(Entity e);

    public void Destroy() {
        this.isDestroy = true;
    }

    public boolean isDestroy() {
        return isDestroy;
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

    public Vector2 getPos() {
        return pos;
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
