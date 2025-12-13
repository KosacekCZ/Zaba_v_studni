package io.amogus.managers;

import com.badlogic.gdx.math.Rectangle;
import io.amogus.entities.Entity;
import io.amogus.entities.Player;
import io.amogus.entities.Projectile;
import io.amogus.leveleditor.Region;

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
    private Region levelBounds;

    private static final ServerManager svm = ServerManager.getInstance();

    private static EntityManager instance;

    public static EntityManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("EntityManager not initialized");
        }
        return instance;
    }

    private EntityManager() {
        entities = new ArrayList<Entity>();
        tempBuffer = new ArrayList<Entity>();
        players = new HashMap<String, Player>();
        lastId = 0;
        svm.setEntityEvents(this);
    }

    public void update() {
        for (Entity e : entities) {
            e.update();

            for (Entity e2 : entities) {
                if (!e.equals(e2)) {
                    if (e.getSprite().getBoundingRectangle().overlaps(e2.getSprite().getBoundingRectangle())) {
                        e.onCollide(e2);
                        e2.onCollide(e);
                    }
                }
            }

            /*
            if (e instanceof Projectile) {
                Rectangle r = e.getSprite().getBoundingRectangle();

                float minX = levelBounds.x;
                float maxX = levelBounds.x + levelBounds.w;
                float minY = levelBounds.y;
                float maxY = levelBounds.y + levelBounds.h;

                boolean hitVertical = false;
                boolean hitHorizontal = false;

                if (r.x < minX) {
                    e.getSprite().setX(minX);
                    hitVertical = true;
                } else if (r.x + r.width > maxX) {
                    e.getSprite().setX(maxX - r.width);
                    hitVertical = true;
                }

                if (r.y < minY) {
                    e.getSprite().setY(minY);
                    hitHorizontal = true;
                } else if (r.y + r.height > maxY) {
                    e.getSprite().setY(maxY - r.height);
                    hitHorizontal = true;
                }

                if (hitVertical || hitHorizontal) {
                    float angle = e.getRotation();

                    if (hitVertical) {
                        angle = 180f - angle;
                    }
                    if (hitHorizontal) {
                        angle = -angle;
                    }

                    // normalize if you want: angle = (angle % 360f + 360f) % 360f;
                    e.setRotation(angle);
                    e.setRotation(angle);
                }
            }
             */
        }


        entities.removeIf(Entity::isDestroy);
        entities.addAll(tempBuffer);
        tempBuffer.clear();
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
