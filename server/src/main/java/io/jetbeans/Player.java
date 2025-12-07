package io.jetbeans;

public class Player {

    private String id;
    private double x;
    private double y;
    private String texture;
    private int health;
    private int damage;
    private boolean isDestroy;

    public Player(String id, double x, double y, int health, int damage, String texture) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;
        this.texture = texture;
        isDestroy = false;
    }

    public void update(float x, float y, int health, int damage) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
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

    public boolean isDestroy() {
        return isDestroy;
    }

    public void setDestroy(boolean destroy) {
        isDestroy = destroy;
    }
}

