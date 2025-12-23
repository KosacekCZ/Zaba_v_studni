package io.amogus.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.amogus.Main;
import io.amogus.entities.Entity;
import io.amogus.entities.Projectile;
import io.amogus.managers.Managers;

import java.util.HashMap;

public class Minigun extends Item{
    private boolean wasRmb;
    private boolean wasLmb;
    private float windupTimer;
    private boolean windLoopStarted;
    private long windupId = -1L;
    private long windLoopId = -1L;
    private long firingId = -1L;
    private final HashMap<Modifiers, Integer> modifiers;

    public Minigun(Entity owner) {
        super(13, "Minigun", owner.getX(), owner.getY() - 4f, 32, 32, "minigun");
        this.owner = owner;
        modifiers = new HashMap<>();
    }

    @Override
    public void updateWorld() {
        float dx = vm.getWorldMouseX() - (owner.getX() + 16f);
        float dy = vm.getWorldMouseY() - (owner.getY() + 16f);
        rotation = (float) Math.toDegrees(Math.atan2(dy, dx));

        boolean flipX = Gdx.input.getX() < Gdx.graphics.getWidth() / 2;

        boolean rmb = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        boolean lmb = Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isKeyPressed(Input.Keys.SPACE);
        float dt = Gdx.graphics.getDeltaTime();

        if (rmb && !wasRmb) {
            windupTimer = 0f;
            windLoopStarted = false;
            windupId = Managers.aum.play("minigun_windup");

        }

        if (rmb) {
            windupTimer += dt;

            if (!windLoopStarted && windupTimer >= 0.5f) {
                windLoopId = Managers.aum.loop("minigun_wind_loop");
                windLoopStarted = true;
            }

            sm.draw(owner.getX(), owner.getY() - 4f, 32f, 32f, rotation - (flipX ? 135f : 45f), flipX,
                Managers.am.animateSprite(6, dt, "minigun_spin"));

            if (lmb && !wasLmb) {
                firingId = Managers.aum.loop("minigun_firing");
            }
            if (!lmb && wasLmb) {
                Managers.aum.stop("minigun_firing", firingId);
                firingId = -1L;
            }

            if (lmb && windLoopStarted) {
                float spreadDeg = 6f;
                float r = rotation + com.badlogic.gdx.math.MathUtils.random(-spreadDeg, spreadDeg);
                em.spawnEntity(new Projectile(owner.getX() + 12f, owner.getY() + 8f, 15, r, "projectile", 1));
            }
        } else {
            sm.draw(owner.getX(), owner.getY() - 4f, 32f, 32f, rotation - (flipX ? 135f : 45f), flipX, texture);
        }

        if (!rmb && wasRmb) {
            Managers.aum.stop("minigun_windup", windupId);
            windupId = -1L;

            Managers.aum.stop("minigun_wind_loop", windLoopId);
            windLoopId = -1L;

            Managers.aum.stop("minigun_firing", firingId);
            firingId = -1L;

            windupTimer = 0f;
            windLoopStarted = false;
        }

        wasRmb = rmb;
        wasLmb = lmb;

        drawHands(flipX, rotation, new Vector2(owner.getX(), owner.getY() - 4f), new Vector2(0, 6), new Vector2(8, 6));
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void onCollide(Entity e) {

    }
}
