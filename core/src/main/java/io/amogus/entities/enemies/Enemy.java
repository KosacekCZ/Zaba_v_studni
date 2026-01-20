package io.amogus.entities.enemies;

import io.amogus.entities.Entity;

public class Enemy extends Entity {

    public Enemy(float x, float y, String texture, int health, int damage, float speed) {
        super(x, y, texture, health, damage, speed);
    }

    @Override
    public void updateWorld() {
        sm.draw(x, y, 32, 32, "poo");
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void onCollide(Entity e) {

    }
}
