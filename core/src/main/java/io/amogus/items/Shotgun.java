package io.amogus.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.amogus.entities.Entity;
import io.amogus.entities.Projectile;

import java.util.HashMap;

public class Shotgun extends Item {
    private final HashMap<Modifiers, Integer> modifiers;

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
        float screenWidth = (float) Gdx.graphics.getWidth() / 2;

        sm.drawScreen(screenWidth - ((2 * textureSize) + (2 * 16)), 32, (textureSize * 4) + (3 * (textureSize / 4)), textureSize, "modifiers_gui");

        sm.drawScreen(screenWidth - ((2 * textureSize) + (2 * 24)), 0, textureSize * 1.5f, textureSize * 1.5f, "choke");
        sm.drawScreen(screenWidth - (textureSize + 32), 0, textureSize * 1.5f, textureSize * 1.5f, "extended_magazine");

        sm.drawScreen(screenWidth - ((2 * textureSize) + (2 * 16)), 32, (textureSize * 4) + (3 * (textureSize / 4)), textureSize, "modifiers_gui_overlay");
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


        // Shooting
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            int extra = modifiers.containsKey(Modifiers.SHOTGUN_BULLETS)
                ? modifiers.get(Modifiers.SHOTGUN_BULLETS)
                : 0;

            int count = (extra * 2) + 3;

            float choke = modifiers.containsKey(Modifiers.SHOTGUN_CHOKE)
                ? modifiers.get(Modifiers.SHOTGUN_CHOKE)
                : 0f;

            float baseHalfSpreadDeg = 15f;
            float halfSpreadDeg = Math.max(0f, baseHalfSpreadDeg - choke);

            float start = rotation - halfSpreadDeg;

            float step = (count <= 1) ? 0f : (2f * halfSpreadDeg) / (count - 1);

            for (int i = 0; i < count; i++) {
                float rot = start + i * step;
                em.spawnEntity(new Projectile(owner.getX() + 16f, owner.getY() + 16f, rot, "projectile"));
            }
        }
    }

    @Override
    public void onCollide(Entity e) {

    }
}
