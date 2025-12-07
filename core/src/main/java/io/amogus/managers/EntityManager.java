package io.amogus.managers;

import io.amogus.entities.Entity;
import io.amogus.entities.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityManager implements io.amogus.managers.IEntityEvents {
    private final List<Entity> entites;
    private final HashMap<String, Player> players;
    private Player localPlayer;
    private Player remotePlayer;
    private static int lastId;

    private final ServerManager svm;

    private static EntityManager instace;

    public static EntityManager getInstace() {
        if (instace == null) instace = new EntityManager();
        return instace;
    }

    private EntityManager() {
        entites = new ArrayList<Entity>();
        players = new HashMap<String, Player>();
        lastId = 0;
        svm = ServerManager.getInstance();
        svm.setEntityEvents(this);
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

    public void spawnPlayer(Player player) {
        player.setPlayerNumber(++lastId);
        players.put(player.getPlayerId(), player);
        entites.add(player);

        svm.spawnPlayer(player);
    }

    public void spawnLocalPlayer(Player player) {
        player.setPlayerNumber(++lastId);
        players.put(player.getPlayerId(), player);
        entites.add(player);
        localPlayer = player;

        svm.spawnPlayer(player);
    }

    public void spawnEntity(Entity e) {
        entites.add(e);
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
