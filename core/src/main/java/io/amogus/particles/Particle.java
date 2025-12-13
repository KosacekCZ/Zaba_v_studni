package io.amogus.particles;

import com.badlogic.gdx.graphics.Color;
import io.amogus.managers.SpriteManager;

public abstract class Particle {
    protected float x;
    protected float y;
    protected float w;
    protected float h;
    protected String texture;
    protected Color color;
    protected float lifespan;
    protected boolean isDestroyed;

    protected SpriteManager sm;

    public Particle(float x, float y, float w, float h, String texture, float lifespan) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.texture = texture;
        this.lifespan = lifespan;

        sm = SpriteManager.getInstance();
    }

    public Particle(float x, float y, float w, float h, Color c, float lifespan) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = c;
        this.lifespan = lifespan;

        sm = SpriteManager.getInstance();
    }

    public Particle(float x, float y, float w, float h, float lifespan) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.lifespan = lifespan;

        sm = SpriteManager.getInstance();
    }

    public abstract void update(float delta);

    public void Destroy() {
        isDestroyed = true;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

}
