package io.amogus.entities;

public class Player extends Entity {
    private int playerNumber;
    private String playerId;

    public Player(String id, float x, float y, int health, int damage, float speed, String texture) {
        super(x, y, texture, health, damage, speed);
        this.playerId = id;
    }

    public void update() {
        System.out.println(sprite.getX() + " " + sprite.getY() + " " + sprite.getWidth() + " " + sprite.getHeight());
        sm.draw(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight(), texture);



    }

    public int getPlayerNumber() {
        return playerNumber;
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

    public String getTexture() {
        return texture;
    }

    public void onCollide(Entity e) {

    }
}
