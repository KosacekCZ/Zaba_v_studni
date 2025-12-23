package io.amogus.items;

import com.badlogic.gdx.math.Vector2;
import io.amogus.entities.Entity;
import io.amogus.managers.EntityManager;
import io.amogus.managers.Managers;
import io.amogus.managers.SpriteManager;
import io.amogus.managers.ViewportManager;

public abstract class Item {
    protected int id;
    protected String name;
    protected float x;
    protected float y;
    protected float w;
    protected float h;
    protected float rotation;
    protected String texture;
    protected Entity owner;
    protected boolean isActive;
    protected SpriteManager sm;
    protected ViewportManager vm;
    protected EntityManager em;

    public Item(int id, String name, float x, float y, float w, float h, String texture) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.texture = texture;
        this.rotation = 0;
        sm = Managers.sm;
        vm = Managers.vm;
        em = Managers.em;
    }

    protected void drawHands(boolean flipX, float angleDeg, Vector2 gvec, Vector2 h1, Vector2 h2) {
        float gunW = 32f, gunH = 32f;
        float handW = 8f, handH = 8f;

        float h1x = h1.x * (float) Math.cos(Math.toRadians(angleDeg)) - (flipX ? -h1.y : h1.y) * (float) Math.sin(Math.toRadians(angleDeg));
        float h1y = h1.x * (float) Math.sin(Math.toRadians(angleDeg)) + (flipX ? -h1.y : h1.y) * (float) Math.cos(Math.toRadians(angleDeg));

        float h2x = h2.x * (float) Math.cos(Math.toRadians(angleDeg)) - (flipX ? -h2.y : h2.y) * (float) Math.sin(Math.toRadians(angleDeg));
        float h2y = h2.x * (float) Math.sin(Math.toRadians(angleDeg)) + (flipX ? -h2.y : h2.y) * (float) Math.cos(Math.toRadians(angleDeg));

        float hand1X = gvec.x + gunW * 0.5f + h1x - handW * 0.5f;
        float hand1Y = gvec.y + gunH * 0.5f + h1y - handH * 0.5f;

        float hand2X = gvec.x + gunW * 0.5f + h2x - handW * 0.5f;
        float hand2Y = gvec.y + gunH * 0.5f + h2y - handH * 0.5f;

        sm.draw(hand1X, hand1Y, handW, handH, 0f, flipX, "player_hand");
        sm.draw(hand2X, hand2Y, handW, handH, 0f, flipX, "player_hand");
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

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
