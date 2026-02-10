package io.amogus.entities.enemies;
import io.amogus.entities.Entity;
import io.amogus.entities.Player;
import io.amogus.entities.logic.State;
import io.amogus.managers.Managers;

public class Enemy extends Entity {

    private State state = State.WANDER;
    private float wanderAngle = 0f;
    private float wanderTimer = 0f;

    private String head;
    private String body;

    public Enemy(float x, float y, int health, int damage, float speed, String head, String body) {
        super(x, y, head, health, damage, speed);
        this.head = head;
        this.body = body;
    }

    @Override
    public void updateWorld() {
        sm.draw(x, y, 32, 32, body);
        sm.draw(x, y, 32, 32, head);

        updateMovement();
        clampToRegion(Managers.lm.getCurrentState().getBounds());
    }

    @Override
    public void updateScreen() {

    }

    public void updateMovement() {
        Player nearest = Managers.em.getNearestPlayer(x, y);
        float dt = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        if (nearest != null) {
            float dx = nearest.getX() - x;
            float dy = nearest.getY() - y;
            float dist2 = dx * dx + dy * dy;

            if (dist2 < 15f * 15f) {
                state = State.ATTACK;
            } else if (dist2 < 80f * 80f) {
                state = State.FOLLOW;
            } else {
                state = State.WANDER;
            }
        } else {
            state = State.WANDER;
        }

        switch (state) {
            case WANDER -> wander(dt);
            case FOLLOW -> follow(nearest, dt);
            case ATTACK -> attack(nearest);
        }
    }

    private void wander(float dt) {
        wanderTimer -= dt;

        if (wanderTimer <= 0f) {
            wanderAngle = (float) (Math.random() * Math.PI * 2);
            wanderTimer = 2.5f;
        }

        x += (float) (Math.cos(wanderAngle) * (speed / 2) * dt);
        y += (float) (Math.sin(wanderAngle) * (speed / 2) * dt);
    }

    private void follow(Player p, float dt) {
        if (p == null) return;

        float dx = p.getX() - x;
        float dy = p.getY() - y;
        float len = (float) Math.sqrt(dx * dx + dy * dy);

        if (len != 0f) {
            dx /= len;
            dy /= len;
        }

        x += dx * speed * dt;
        y += dy * speed * dt;
    }

    private void attack(Player p) {
        onAttack(p);
    }

    private void onAttack(Player p) {
    }



    @Override
    public void onCollide(Entity e) {

    }
}
