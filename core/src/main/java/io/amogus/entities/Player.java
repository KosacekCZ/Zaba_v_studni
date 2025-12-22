package io.amogus.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import io.amogus.items.Minigun;
import io.amogus.items.Pistol;
import io.amogus.items.Shotgun;
import io.amogus.managers.Managers;
import io.amogus.particles.DashParticle;

public class Player extends Entity {
    private int playerNumber;
    private String playerName;
    private String playerId;
    private Vector2 dashVelocity;
    float decel = 0.125f;
    private boolean isMoving;
    private boolean isDashing;

    private final Vector3 mouseWorld = new Vector3();
    private final Vector2 toMouse = new Vector2();

    public Player(String id, float x, float y, int health, int damage, float speed, String texture) {
        super(x, y, texture, health, damage, speed);
        this.w = 32f;
        this.h = 32f;
        this.playerId = id;
        this.dashVelocity = new Vector2(0, 0);

        inventory.put(10, new Shotgun(this));
        inventory.put(11, new Pistol(this));
        inventory.put(13, new Minigun(this));
        inHand = 13;
        inventory.get(inHand).setActive(true);


    }

    public void updateScreen() {
        inventory.get(inHand).updateScreen();
    }

    public void updateWorld() {
        handleDashing();
        drawPlayer();
        inventory.get(inHand).updateWorld();
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String value) {
        playerId = value;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }


    public void onCollide(Entity e) {

    }

    public Vector2 getDashVelocity() {
        return dashVelocity;
    }

    public void setDashVelocity(Vector2 velocity) {
        this.dashVelocity = velocity;
    }

    public void setInHand(int inHand) {
        this.inHand = inHand;
    }

    private void drawPlayer() {
        boolean flipX = Gdx.input.getX() < Gdx.graphics.getWidth() / 2;

        // Player draw
        // Base
        if (isMoving) {
            sm.draw(getX(), getY(), getWidth(), getHeight(), 0, flipX, Managers.am.animateSprite(6, Gdx.graphics.getDeltaTime(), "player_base_animated"));
        } else if (isDashing) {
            sm.draw(getX(), getY(), getWidth(), getHeight(), 0, flipX, "player_base_dash");
        } else {
            sm.draw(getX(), getY(), getWidth(), getHeight(), 0, flipX, "player_base");
        }
        // Eyes
        mouseWorld.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
        vm.getWorldCamera().unproject(mouseWorld);

        float cx = getX() + getWidth() * 0.5f;
        float cy = getY() + getHeight() * 0.5f;

        toMouse.set(mouseWorld.x - cx, mouseWorld.y - cy);

        float maxEyeOffsetPx = 0.6f;
        float pixelsPerUnit = 1f;
        float maxEyeOffsetWorld = maxEyeOffsetPx / pixelsPerUnit;

        float deadzonePx = 12f;
        float deadzoneWorld = deadzonePx / pixelsPerUnit;

        float len = toMouse.len();

        if (len <= deadzoneWorld) {
            toMouse.setZero();
        } else {
            float t = (len - deadzoneWorld) / (len);
            toMouse.scl(t);
            if (toMouse.len2() > 0.0001f) toMouse.nor().scl(maxEyeOffsetWorld);
        }

        sm.draw(getX() + toMouse.x, getY() + toMouse.y, getWidth(), getHeight(), 0, flipX, "player_eyes");




        sm.renderSpotlight(getX(), getY(), 300f, Color.WHITE, 0.5f);
    }

    private void handleDashing() {
        float dt = Gdx.graphics.getDeltaTime();

        if (!dashVelocity.isZero()) {
            isDashing = true;

            x += dashVelocity.x * dt * 60;
            y += dashVelocity.y * dt * 60;

            float decelStep = decel * dt * 60;

            if (dashVelocity.x > 0f) dashVelocity.x = Math.max(0f, dashVelocity.x - decelStep);
            else if (dashVelocity.x < 0f) dashVelocity.x = Math.min(0f, dashVelocity.x + decelStep);

            if (dashVelocity.y > 0f) dashVelocity.y = Math.max(0f, dashVelocity.y - decelStep);
            else if (dashVelocity.y < 0f) dashVelocity.y = Math.min(0f, dashVelocity.y + decelStep);

            Managers.pm.addParticle(new DashParticle(x, y, 16, 16, 16));
        } else {
            isDashing = false;
        }
    }


    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }
}
