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
        //pm.addParticle(new TraceParticle(x + 4, y + 4, 2, 2, 4));
        sm.renderSpotlight(x + 4f, y + 4f, 8f, new Color(0.7f, 0.7f, 0.4f, 1), 5.0f);

        projectileSpin = (projectileSpin + 16f) % 360;
        sm.draw(x, y, 8f, 8f, projectileSpin, texture);
        this.x += (float) Math.cos(Math.toRadians(this.rotation)) * speed;
        this.y += (float) Math.sin(Math.toRadians(this.rotation)) * speed;

        float minX = levelBounds.x;
        float maxX = levelBounds.w;
        float minY = levelBounds.y;
        float maxY = levelBounds.h;


        float w = 4f, h = 4f;

        boolean hitVertical = false, hitHorizontal = false;

        if (x < minX) { x = minX; hitVertical = true; }
        else if (x + w > maxX) { x = maxX - w; hitVertical = true; }

        if (y < minY) { y = minY; hitHorizontal = true; }
        else if (y + h > maxY) { y = maxY - h; hitHorizontal = true; }


        if (hitVertical || hitHorizontal) {
            bounceCount++;
            float angle = getRotation();

            if (hitVertical) {
                angle = 180f - angle;
            }
            if (hitHorizontal) {
                angle = -angle;
            }

            // normalize if you want: angle = (angle % 360f + 360f) % 360f;
            setRotation(angle);
        }

        if (bounceCount == maxBounces) Destroy();
    }

    @Override
    public void onCollide(Entity e) {

    }
}

