package io.amogus.entities.enemies;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.amogus.entities.Entity;
import io.amogus.entities.Player;
import io.amogus.entities.logic.State;
import io.amogus.managers.Managers;

public class Enemy extends Entity {
    private State state = State.WANDER;
    private float idleTimer = 0f;
    private float wanderAngle = 0f;
    private float wanderTimer = 0f;
    private Player target;
    private float attackCooldown = 0.6f;
    private float attackInterval = 1.0f;
    private float attackTimer = 0f;
    private float attackRange = 15f;

    private String head;
    private String body;
    private String jaw;

    public Enemy(float x, float y, int health, int damage, float speed, String head, String body) {
        super(x, y, head, health, damage, speed);
        this.w = 32f;
        this.h = 32f;
        this.radius = 4f;
        this.head = head;
        this.body = body;
    }

    public Enemy(float x, float y, int health, int damage, float speed, String head, String body, String jaw) {
        super(x, y, head, health, damage, speed);
        this.w = 32f;
        this.h = 32f;
        this.radius = 4f;
        this.head = head;
        this.body = body;
        this.jaw = jaw;
    }

    @Override
    public void updateWorld() {
        updateMovement();
        boolean moving = (state == State.WANDER || state == State.FOLLOW);

        if (moving) {
            sm.draw(x, y, 32, 32, 0, false, Managers.am.animateSprite(16, Gdx.graphics.getDeltaTime(), "enemy_body_1_animated"));
            sm.draw(x, y, 32, 32, 0, false, Managers.am.animateSprite(16, Gdx.graphics.getDeltaTime(), head));
        } else {
            sm.draw(x, y, 32, 32, body);
            sm.draw(x, y, 32, 32, head);
        }
        if (jaw != null) {
            sm.draw(x, y, 32, 32, jaw);
        }
        clampToRegion(Managers.lm.getCurrentState().getBounds());
    }

    @Override
    public void updateScreen() {

    }

    public void updateMovement() {
        float dt = Gdx.graphics.getDeltaTime();
        attackTimer = Math.max(0f, attackTimer - dt);

        Player nearest = Managers.em.getNearestPlayer(x, y);
        target = nearest;

        if (nearest != null) {
            float dx = (nearest.getX() + nearest.getW() * 0.5f) - (x + w * 0.5f);
            float dy = (nearest.getY() + nearest.getH() * 0.5f) - (y + h * 0.5f);
            float dist2 = dx * dx + dy * dy;

            float attackR = attackRange;
            float followR = 80f;

            if (dist2 < attackR * attackR) {
                state = State.ATTACK;
            } else if (dist2 < followR * followR) {
                state = State.FOLLOW;
            } else {
                state = (idleTimer > 0f) ? State.IDLE : State.WANDER;
            }
        } else {
            state = (idleTimer > 0f) ? State.IDLE : State.WANDER;
        }

        switch (state) {
            case IDLE -> idle(dt);
            case WANDER -> wander(dt);
            case FOLLOW -> follow(nearest, dt);
            case ATTACK -> attack(nearest);
        }
    }

    private void wander(float dt) {
        wanderTimer -= dt;

        if (wanderTimer <= 0f) {
            if (Math.random() < 0.3f) {
                idleTimer = 1.5f + (float)Math.random();
                return;
            }

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

    private void idle(float dt) {
        idleTimer -= dt;

        if (idleTimer <= 0f) {
            idleTimer = 0f;
        }
    }

    private void attack(Player p) {
        if (p == null) return;
        if (attackTimer > 0f) return;

        float dx = (p.getX() + p.getW() * 0.5f) - (x + w * 0.5f);
        float dy = (p.getY() + p.getH() * 0.5f) - (y + h * 0.5f);
        float dist2 = dx * dx + dy * dy;

        if (dist2 <= attackRange * attackRange) {
            p.onCollide(this);
            attackTimer = attackInterval;
        }
    }

    @Override
    public void onCollide(Entity e) {

    }
}
