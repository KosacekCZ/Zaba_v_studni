package io.amogus.managers;
import io.amogus.entities.enemies.Enemy;
import io.amogus.leveleditor.Region;
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
        }

        resolveEnemySeparation();

        for (int i = 0; i < entities.size(); i++) {
            Entity a = entities.get(i);
            for (int j = i + 1; j < entities.size(); j++) {
                Entity b = entities.get(j);
                if (a.getSprite().getBoundingRectangle().overlaps(b.getSprite().getBoundingRectangle())) {
                    a.onCollide(b);
                    b.onCollide(a);
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

    private void resolveEnemySeparation() {
        Region bounds = Managers.lm.getCurrentState().getBounds();

        for (int i = 0; i < entities.size(); i++) {
            Entity ea = entities.get(i);
            if (!(ea instanceof Enemy a)) continue;

            for (int j = i + 1; j < entities.size(); j++) {
                Entity eb = entities.get(j);
                if (!(eb instanceof Enemy b)) continue;

                float ax = a.getX() + a.getW() * 0.5f;
                float ay = a.getY() + a.getH() * 0.5f;
                float bx = b.getX() + b.getW() * 0.5f;
                float by = b.getY() + b.getH() * 0.5f;

                float dx = bx - ax;
                float dy = by - ay;
                float ra = Math.min(a.getW(), a.getH()) * 0.5f;
                float rb = Math.min(b.getW(), b.getH()) * 0.5f;
                // This shits
                // float ra = a.getCenterX();
                // float rb = b.getCenterX();
                float minDist = ra + rb;

                float dist2 = dx * dx + dy * dy;
                if (dist2 >= minDist * minDist) continue;

                float dist = (float) Math.sqrt(dist2);
                if (dist < 0.0001f) {
                    dx = 1f;
                    dy = 0f;
                    dist = 1f;
                }

                float nx = dx / dist;
                float ny = dy / dist;

                float penetration = minDist - dist;
                float push = penetration * 0.5f;

                a.setX(a.getX() - nx * push);
                a.setY(a.getY() - ny * push);
                b.setX(b.getX() + nx * push);
                b.setY(b.getY() + ny * push);

                a.clampToRegion(bounds);
                b.clampToRegion(bounds);
            }
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

    public Player getNearestPlayer(float ex, float ey) {
        Player nearest = null;
        float nearestDist2 = Float.MAX_VALUE;

        for (Player p : players.values()) {
            float dx = p.getX() - ex;
            float dy = p.getY() - ey;
            float dist2 = dx * dx + dy * dy;

            if (dist2 < nearestDist2) {
                nearestDist2 = dist2;
                nearest = p;
            }
        }
        return nearest;
    }

}
