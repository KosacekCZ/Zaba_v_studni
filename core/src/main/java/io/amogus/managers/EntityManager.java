package io.amogus.managers;

import io.amogus.entities.Entity;
import io.amogus.entities.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityManager {
    private final List<Entity> entites;
    private final HashMap<String, Player> players;
    private static int lastId;

    private static EntityManager instace;
    public static EntityManager getInstace() {
        if (instace == null) instace = new EntityManager();
        return instace;
    }

    private EntityManager() {
        entites = new ArrayList<Entity>();
        players = new HashMap<String, Player>();
        lastId = 0;
    }

    public void update() {
        for (Entity e : entites) {
            e.update();

            for (Entity e2 : entites) {
                if (!e.equals(e2)) {
                    if (e.getSprite().getBoundingRectangle().overlaps(e2.getSprite().getBoundingRectangle())) {
                        e.onCollide(e2);
                        e2.onCollide(e);
                    }
                }
            }

            entites.removeIf(Entity::isDestroy);
        }
    }

    public void addPlayer(Player player) {
        System.out.println(player.getX() + ", " + player.getY() + ", " + player.getWidth() + ", " + player.getHeight() + player.getTexture());
        player.setPlayerNumber(++lastId);
        players.put(player.getPlayerId(), player);
        entites.add(player);

        System.out.println("Added new player " + player.getPlayerId());
        System.out.println("Entities: " + entites.size());
        System.out.println("Players: " + players.size());
    }

    public void removePlayer(String playerId) {
        players.remove(playerId);
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }
}
