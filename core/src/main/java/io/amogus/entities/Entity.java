package io.amogus.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import io.amogus.items.Item;
import io.amogus.managers.*;

import java.util.HashMap;

public abstract class Entity {
    protected float x;
    protected float y;
    protected float w;
    protected float h;
    protected float rotation;
    protected Vector2 prevPos;
    protected Vector2 pos;
    protected String texture;
    protected int health;
    protected int damage;
    protected float speed;
    protected boolean isDestroy;
    protected HashMap<Integer, Item> inventory;
    protected int inHand;

    protected SpriteManager sm;
    protected ServerManager svm;
    protected LevelManager lm;
    protected ParticleManager pm;
    protected ViewportManager vm;

    public Entity(float x, float y, String texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        pos = new Vector2(x, y);
        prevPos = new Vector2(getX(), getY());
        inventory = new HashMap<>();

        sm = SpriteManager.getInstance();
        svm = ServerManager.getInstance();
        lm = LevelManager.getInstance();
        pm = ParticleManager.getInstance();
        vm = ViewportManager.getInstance();
    }

    public Entity(float x, float y, float rotation, String texture) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.texture = texture;
        pos = new Vector2(x, y);
        prevPos = new Vector2(getX(), getY());
        inventory = new HashMap<>();

        sm = SpriteManager.getInstance();
        svm = ServerManager.getInstance();
        lm = LevelManager.getInstance();
        pm = ParticleManager.getInstance();
        vm = ViewportManager.getInstance();
    }

    public Entity(float x, float y, String texture, int health , int damage, float speed) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;
        this.speed = speed * 10; // Multiplied due to delta time adjustment
        this.texture = texture;
        prevPos = new Vector2(getX(), getY());
        inventory = new HashMap<>();

        sm = SpriteManager.getInstance();
        svm = ServerManager.getInstance();
        lm = LevelManager.getInstance();
        pm = ParticleManager.getInstance();
        vm = ViewportManager.getInstance();
    }

    public abstract void updateWorld();

    public abstract void updateScreen();

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

    public Sprite getSprite() {
        Sprite s = new Sprite();
        s.setBounds(x, y, w, h);
        s.setOrigin(w / 2, h / 2);
        s.setCenter(w / 2, h / 2);
        return s;
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

    public Vector2 getPos() {
        return pos;
    }

    public float getWidth() {
        return w;
    }

    public float getHeight() {
        return h;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDestroy(boolean destroy) {
        isDestroy = destroy;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
