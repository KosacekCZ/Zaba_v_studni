package io.amogus.managers;

import com.badlogic.gdx.Gdx;
import io.amogus.particles.Particle;
import java.util.ArrayList;
import java.util.List;

public class ParticleManager {
    private static ParticleManager instance;
    public static ParticleManager getInstance() {
        if (instance == null) instance = new ParticleManager();
        return instance;
    }

    private final List<Particle> particles;
    private final List<Particle> buffer;

    private ParticleManager() {
        particles = new ArrayList<>();
        buffer = new ArrayList<>();
    }

    public void update() {
        for (Particle p : particles) {
            p.update(Gdx.graphics.getDeltaTime());
        }

        particles.removeIf(Particle::isDestroyed);
        particles.addAll(buffer);
        buffer.clear();
    }

    public void addParticle(Particle p) {
        buffer.add(p);
    }
}
