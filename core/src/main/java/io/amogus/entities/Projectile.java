package io.amogus.entities;

import com.badlogic.gdx.Gdx;

public class Projectile extends Entity {
    private float projectileSpin;
    public Projectile(float x, float y, float rotation, String texture) {
        super(x, y, rotation, texture);
        this.speed = 10f * 10 * Gdx.graphics.getDeltaTime();
        this.projectileSpin = rotation;
    }

    @Override
    public void update() {
        projectileSpin = (projectileSpin + 16f) % 360;
        sm.draw(x, y, 8f, 8f, projectileSpin, texture);
        this.x += (float) Math.cos(Math.toRadians(this.rotation)) * speed;
        this.y += (float) Math.sin(Math.toRadians(this.rotation)) * speed;
    }

    @Override
    public void onCollide(Entity e) {

    }
}
