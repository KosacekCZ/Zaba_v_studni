package io.amogus.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import io.amogus.leveleditor.Region;
import io.amogus.particles.TraceParticle;

public class Projectile extends Entity {
    private float projectileSpin;
    private final Region levelBounds;
    private int bounceCount = 0;
    private int maxBounces = 2;


    public Projectile(float x, float y, float speed, float rotation, String texture) {
        super(x, y, rotation, texture);
        this.levelBounds = lm.getCurrentState().getBounds();
        this.speed = 60f * 10 * Gdx.graphics.getDeltaTime();
        this.projectileSpin = rotation;
        this.speed = speed * Gdx.graphics.getDeltaTime() * 60;
    }

    public Projectile(float x, float y, float speed, float rotation, String texture, int maxBounces) {
        super(x, y, rotation, texture);
        this.levelBounds = lm.getCurrentState().getBounds();
        this.speed = 60f * 10 * Gdx.graphics.getDeltaTime();
        this.projectileSpin = rotation;
        this.maxBounces = maxBounces;
        this.speed = speed * Gdx.graphics.getDeltaTime() * 60;
    }

    public void updateScreen() {

    }

    @Override
    public void updateWorld() {
        sm.renderSpotlight(x + 4f, y + 4f, 8f, new Color(0.7f, 0.7f, 0.4f, 1f), 5.0f);

        projectileSpin = (projectileSpin + 16f) % 360f;
        sm.draw(x, y, 8f, 8f, projectileSpin, texture);

        float dt = Gdx.graphics.getDeltaTime();
        x += (float) Math.cos(Math.toRadians(rotation)) * speed;
        y += (float) Math.sin(Math.toRadians(rotation)) * speed;

        float minX = levelBounds.x;
        float minY = levelBounds.y;
        float maxX = levelBounds.x + levelBounds.w;
        float maxY = levelBounds.y + levelBounds.h;

        float pw = 8f;
        float ph = 8f;

        boolean hitVertical = false;
        boolean hitHorizontal = false;

        if (x < minX) {
            x = minX;
            hitVertical = true;
        } else if (x + pw > maxX) {
            x = maxX - pw;
            hitVertical = true;
        }

        if (y < minY) {
            y = minY;
            hitHorizontal = true;
        } else if (y + ph > maxY) {
            y = maxY - ph;
            hitHorizontal = true;
        }

        if (hitVertical || hitHorizontal) {
            bounceCount++;

            float angle = rotation;
            if (hitVertical) angle = 180f - angle;
            if (hitHorizontal) angle = -angle;
            angle = (angle % 360f + 360f) % 360f;

            setRotation(angle);

            if (bounceCount >= maxBounces) {
                Destroy();
            }
        }
    }

    @Override
    public void onCollide(Entity e) {

    }
}

