package io.amogus.particles;

import com.badlogic.gdx.graphics.Color;

public class TraceParticle extends Particle
{
    public TraceParticle(float x, float y, float w, float h, int lifespan) {
        super(x, y, w, h, lifespan);
    }

    public void update(float dt) {
        float scale;
        float alpha;

        if (lifespan < 4 && lifespan > 3) { scale = 1.00f;  alpha = 0.7f; }
        else if (lifespan < 3 && lifespan > 2) { scale = 0.75f; alpha = 0.6f; }
        else if (lifespan < 2 && lifespan > 1) { scale = 0.50f; alpha = 0.5f; }
        else if (lifespan < 1 && lifespan > 0) { scale = 0.25f; alpha = 0.4f; }
        else { scale = 0.125f; alpha = 0.1f; }

        float newW = w * scale;
        float newH = h * scale;

        float drawX = x + (w - newW) * 0.5f;
        float drawY = y + (h - newH) * 0.5f;

        sm.drawRect(drawX, drawY, newW, newH, new Color(1f, 1f, 0.5f, alpha));

        lifespan -= dt * 100;
        if (lifespan <= 0) Destroy();
    }
}
