package io.amogus.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.amogus.entities.Entity;
import io.amogus.entities.Projectile;
import io.amogus.managers.Managers;
import io.amogus.managers.TextManager;

import java.util.HashMap;

public class Shotgun extends Item {
    private final HashMap<Modifiers, Integer> modifiers;
    private long firingId = -1L;
    private long reloadId = -1L;

    private int magCount = 5;
    private int magCapacity = 5;
    private int ammoCount = 30;

    private boolean canFire = true;
    private boolean reloading = false;
    private boolean chambering = false;
    private boolean playedChamberSound = false;

    private float secondHandXpos = 12f;
    private float chamberingDur = 1f;

    public Shotgun(Entity owner) {
        super(10, "Shotgun", owner.getX(), owner.getY(), 32, 32, "shotgun");
        this.owner = owner;
        modifiers = new HashMap<>();

        modifiers.put(Modifiers.SHOTGUN_MAG, 1);
        modifiers.put(Modifiers.SHOTGUN_BULLETS, 1);
        modifiers.put(Modifiers.SHOTGUN_BOUNCE, 1);
        modifiers.put(Modifiers.SHOTGUN_CHOKE, 5);
    }

    @Override
    public void updateScreen() {
        float textureSize = 128f;
        float scrCtrW = (float) Gdx.graphics.getWidth() / 2;

        // Draw magazine stat
        sm.drawScreen(scrCtrW - (3 * textureSize + 32f), 32, textureSize, textureSize, "shotgun_shells");

        TextManager.draw(magCount + "/" + magCapacity, 48, Color.WHITE, true, scrCtrW - (4 * textureSize ), 96);

        sm.drawScreen(scrCtrW - ((2 * textureSize) + (2 * 16)), 32, (textureSize * 4) + (3 * (textureSize / 4)), textureSize, "modifiers_gui");

        sm.drawScreen(scrCtrW - ((2 * textureSize) + (2 * 24 )), 0, textureSize * 1.5f, textureSize * 1.5f, "choke");
        sm.drawScreen(scrCtrW - (textureSize + 32), 0, textureSize * 1.5f, textureSize * 1.5f, "extended_magazine");

        sm.drawScreen(scrCtrW - ((2 * textureSize) + (2 * 16)), 32, (textureSize * 4) + (3 * (textureSize / 4)), textureSize, "modifiers_gui_overlay");
    }

    @Override
    public void updateWorld() {
        // Compute aim angle
        float dx = vm.getWorldMouseX() - (owner.getX() + 16f);
        float dy = vm.getWorldMouseY() - (owner.getY() + 16f);
        rotation = (float) Math.toDegrees(Math.atan2(dy, dx));

        // Draw Shotgun
        boolean flipX = Gdx.input.getX() < Gdx.graphics.getWidth() / 2;
        sm.draw(owner.getX(), owner.getY(), 32f, 32f, rotation - (flipX ? 135f : 45f), flipX, texture);
        sm.draw(owner.getX() + (flipX ? (12f - secondHandXpos) : (-12f + secondHandXpos)), owner.getY(), 32f, 32f, rotation - (flipX ? 135f : 45f), flipX, "shotgun_hanker");

        handleUse();
        chamber();

        drawHands(flipX, rotation, new Vector2(owner.getX(), owner.getY()), new Vector2(-1, -2), new Vector2(secondHandXpos, -2));
    }

    private void handleUse() {
        // Shooting
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && magCount > 0 && canFire) {
            // Extra bullets
            int extra = modifiers.getOrDefault(Modifiers.SHOTGUN_BULLETS, 0);
            int count = (extra * 2) + 3;

            // Choke mod
            float choke = modifiers.containsKey(Modifiers.SHOTGUN_CHOKE)
                ? modifiers.get(Modifiers.SHOTGUN_CHOKE)
                : 0f;

            float baseHalfSpreadDeg = 15f;
            float halfSpreadDeg = Math.max(0f, baseHalfSpreadDeg - choke);

            float start = rotation - halfSpreadDeg;

            float step = (count <= 1) ? 0f : (2f * halfSpreadDeg) / (count - 1);

            // Projectile spawn
            for (int i = 0; i < count; i++) {
                float rot = start + i * step;
                em.spawnEntity(new Projectile(owner.getX() + 16f, owner.getY() + 16f, 10, rot, "projectile", 1));
            }
            this.magCount--;
            canFire = false;
            firingId = Managers.aum.play("shotgun_firing");
            Managers.aum.setVolume("shotgun_firing", firingId, Managers.aum.getGlobalVolume());
        } else if (magCount == 0 &&Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            reloadId = Managers.aum.play("empty_mag");
            Managers.aum.setVolume("empty_mag", firingId, Managers.aum.getGlobalVolume());
        }

        if (!canFire && magCount >= 0 && !chambering) {
            chambering = true;
        }
    }

    private void chamber() {

        if (chambering && chamberingDur > 0) {
            float dt = Gdx.graphics.getDeltaTime();
            float speed = 6f;
            chamberingDur -= dt * speed;

            if (chamberingDur < 0.5f && !playedChamberSound) {
                reloadId = Managers.aum.play("shotgun_chamber");
                Managers.aum.setVolume("shotgun_chamber", firingId, Managers.aum.getGlobalVolume());
                playedChamberSound = true;
            }

            if (chamberingDur <= 0f) {
                chamberingDur = 1f;
                chambering = false;
                playedChamberSound = false;
                secondHandXpos = 12f;
                canFire = true;
            } else {
                float t = 1f - chamberingDur;
                float s = (float) Math.sin(Math.PI * t);
                secondHandXpos = 12f - s * 4f;
            }
        }
    }

    @Override
    public void onCollide(Entity e) {

    }
}
