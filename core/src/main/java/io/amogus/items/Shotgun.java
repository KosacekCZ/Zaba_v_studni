package io.amogus.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.amogus.entities.Entity;
import io.amogus.entities.Projectile;

public class Shotgun extends Item {
    public Shotgun(Entity owner) {
        super(10, "Shotgun", owner.getX(), owner.getY(), 32, 32, "shotgun");
        this.owner = owner;

    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void updateWorld() {
        // Compute aim angle
        float dx = vm.getWorldMouseX() - (owner.getX() + 16f);
        float dy = vm.getWorldMouseY() - (owner.getY() + 16f);
        rotation = (float) Math.toDegrees(Math.atan2(dy, dx));
        sm.draw(owner.getX(), owner.getY(), 32f, 32f, rotation - 45, texture);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
           em.spawnEntity(new Projectile(owner.getX() + 16f, owner.getY() + 16f, rotation - 15, "projectile"));
            em.spawnEntity(new Projectile(owner.getX() + 16f, owner.getY() + 16f, rotation, "projectile"));
            em.spawnEntity(new Projectile(owner.getX() + 16f, owner.getY() + 16f, rotation + 15, "projectile"));
        }
    }

    @Override
    public void onCollide(Entity e) {

    }
}
