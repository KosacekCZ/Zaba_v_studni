package io.amogus.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.amogus.items.Shotgun;

import java.util.HashMap;

public class Player extends Entity {
    private int playerNumber;
    private String playerName;
    private String playerId;
    private Vector2 dashVelocity;
    float decel = 0.125f;

    public Player(String id, float x, float y, int health, int damage, float speed, String texture) {
        super(x, y, texture, health, damage, speed);
        this.w = 32f;
        this.h = 32f;
        this.playerId = id;
        this.dashVelocity = new Vector2(0, 0);

        inventory.put(10, new Shotgun(this));
        inHand = 10;
        inventory.get(inHand).setActive(true);


    }

    public void updateScreen() {
        inventory.get(inHand).updateScreen();
    }

    public void updateWorld() {
        sm.draw(getX(), getY(), getWidth(), getHeight(), texture);
        inventory.get(inHand).updateWorld();


        // Dashing
        if (!dashVelocity.isZero()) {
            this.x += dashVelocity.x * Gdx.graphics.getDeltaTime();
            this.y += dashVelocity.y * Gdx.graphics.getDeltaTime();



            if (dashVelocity.x > 0) {
                dashVelocity.x -= decel;
                if (dashVelocity.x < 0) dashVelocity.x = 0;
            } else if (dashVelocity.x < 0) {
                dashVelocity.x += decel;
                if (dashVelocity.x > 0) dashVelocity.x = 0;
            }

            if (dashVelocity.y > 0) {
                dashVelocity.y -= decel;
                if (dashVelocity.y < 0) dashVelocity.y = 0;
            } else if (dashVelocity.y < 0) {
                dashVelocity.y += decel;
                if (dashVelocity.y > 0) dashVelocity.y = 0;
            }
        }

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
}
