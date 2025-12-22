package io.amogus.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.amogus.Main;
import io.amogus.entities.Entity;
import io.amogus.entities.Projectile;
import io.amogus.managers.Managers;

import java.util.HashMap;

public class Minigun extends Item{
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

        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            sm.draw(owner.getX(), owner.getY() - 4f, 32f, 32f, rotation - (flipX ? 135f : 45f), flipX, Managers.am.animateSprite(6, Gdx.graphics.getDeltaTime(), "minigun_spin"));

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                float spreadDeg = 6f;
                float r = rotation + com.badlogic.gdx.math.MathUtils.random(-spreadDeg, spreadDeg);
                em.spawnEntity(new Projectile(owner.getX() + 12f, owner.getY() + 8f, 15, r, "projectile", 1));
            }
        } else {
            sm.draw(owner.getX(), owner.getY() - 4f, 32f, 32f, rotation - (flipX ? 135f : 45f), flipX, texture);
        }
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void onCollide(Entity e) {

    }
}
