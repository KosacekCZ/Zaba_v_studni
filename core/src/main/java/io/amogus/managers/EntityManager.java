package io.amogus.managers;

import io.amogus.entities.Entity;
import io.amogus.entities.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityManager implements io.amogus.managers.IEntityEvents {
    private final List<Entity> entities;
    private final List<Entity> tempBuffer;
    private final HashMap<String, Player> players;
    private Player localPlayer;
    private Player remotePlayer;
    private static int lastId;

    private static ServerManager svm;

    private static EntityManager instance;

    public static EntityManager getInstance() {
        if (instance == null) instance = new EntityManager();
        return instance;
    }

    private EntityManager() {
        svm = Managers.svm;

        entities = new ArrayList<Entity>();
        tempBuffer = new ArrayList<Entity>();
        players = new HashMap<String, Player>();
        lastId = 0;
        svm.setEntityEvents(this);
    }

    public void updateWorld() {
        for (Entity e : entities) {
            e.updateWorld();

            for (Entity e2 : entities) {
                if (!e.equals(e2)) {
                    if (e.getSprite().getBoundingRectangle().overlaps(e2.getSprite().getBoundingRectangle())) {
                        e.onCollide(e2);
                        e2.onCollide(e);
                    }
                }
            }
        }


        entities.removeIf(Entity::isDestroy);
        entities.addAll(tempBuffer);
        tempBuffer.clear();
    }

    public void updateScreen() {
        for (Entity e : entities) {
            e.updateScreen();
        }
    }

    public void spawnPlayer(Player player) {
        player.setPlayerNumber(++lastId);
        players.put(player.getPlayerId(), player);
        entities.add(player);

        svm.spawnPlayer(player);
    }

    public void spawnLocalPlayer(Player player) {
        player.setPlayerNumber(++lastId);
        players.put(player.getPlayerId(), player);
        tempBuffer.add(player);
        localPlayer = player;

        svm.spawnPlayer(player);
    }

    public void spawnEntity(Entity e) {
        tempBuffer.add(e);
    }

    public void removePlayer(String playerId) {
        players.remove(playerId);
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }
}
