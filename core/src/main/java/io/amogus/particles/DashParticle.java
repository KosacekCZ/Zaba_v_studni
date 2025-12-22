package io.amogus.particles;

import com.badlogic.gdx.graphics.Color;

public class DashParticle extends Particle {

    public DashParticle(float x, float y, float w, float h, float lifespan) {
        super(x, y, w, h, lifespan);
    }

    @Override
    public void update(float dt) {
        float scale;
        float alpha;

        if (lifespan >= 14) { scale = 0.1f; alpha = 0.4f; }
        else if (lifespan < 12 && lifespan > 10) { scale = 0.15f;  alpha = 0.3f; }
        else if (lifespan < 10 && lifespan > 8) { scale = 0.2f; alpha = 0.2f; }
        else if (lifespan < 8 && lifespan > 6) { scale = 0.25f; alpha = 0.15f; }
        else if (lifespan < 6 && lifespan > 4) { scale = 0.3f; alpha = 0.1f; }
        else {scale = 0.35f; alpha = 0.05f;}

        float newW = w * scale;
        float newH = h * scale;

        float drawX = x + (w - newW) * 0.5f;
        float drawY = y + (h - newH) * 0.5f;

        sm.drawRect(x + 16f, y + 8f, newW, newH, new Color(0.8f, 0.8f, 0.8f, alpha));

        lifespan -= dt * 100;
        if (lifespan <= 0) Destroy();
    }
}
