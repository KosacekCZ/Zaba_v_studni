package io.amogus.managers;

import com.badlogic.gdx.Gdx;
import io.amogus.entities.Player;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class ServerManager {
    private Socket socket;
    private IEntityEvents entityEvents;

    private static ServerManager instance;
    private float timer;
    private float UPDATE_TIME = 1/60f;
    private String socketId;

    public static ServerManager getInstance() {
        if (instance == null) instance = new ServerManager();
        return instance;
    }

    private ServerManager() {
    }

    public void setEntityEvents(IEntityEvents entityEvents) {
        this.entityEvents = entityEvents;
    }

    public void connectSocket() {
        try {
            socket = IO.socket("http://localhost:3003");
            socket.connect();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void disconnectSocket() {
        if (this.socket != null) {
            try {
                socket.disconnect();
            } catch (Exception ex) {
                Gdx.app.log("SocketIO", ex.getMessage());
            }
        }
    }

    public void configSocketEvents() {
        socket.on(Socket.EVENT_CONNECT, args -> {
            socketId = socket.id();
            Gdx.app.log("SocketIO", "Connected, id = " + socketId);
            entityEvents.spawnLocalPlayer(new Player(socketId, 0, 0, 100, 10, 15, "player_green"));
        }).on("newPlayer", args -> {
            if (args.length == 0 || !(args[0] instanceof JSONObject)) return;
            JSONObject data = (JSONObject) args[0];
            try {
                String playerId = data.getString("id");
                float x = data.has("x") ? (float) data.getDouble("x") : 100f;
                float y = data.has("y") ? (float) data.getDouble("y") : 100f;
                Gdx.app.log("SocketIO", "New Player Connected: " + playerId);
                entityEvents.spawnPlayer(new Player(playerId, x, y, 100, 10, 10f, "player_piss"));
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting new PlayerID");
            }
        }).on("playerDisconnected", args -> {
            if (args.length == 0 || !(args[0] instanceof JSONObject)) return;
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("id");
                Gdx.app.log("SocketIO", "Player Disconnected: " + id);
                entityEvents.removePlayer(id);
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
            }
        }).on("playerMoved", args -> {
            if (args.length == 0 || !(args[0] instanceof JSONObject)) return;
            JSONObject data = (JSONObject) args[0];
            try {
                String playerId = data.getString("id");
                double x = data.getDouble("x");
                double y = data.getDouble("y");
                if (entityEvents.getPlayers().get(playerId) != null) {
                    entityEvents.getPlayers().get(playerId).setX((float) x);
                    entityEvents.getPlayers().get(playerId).setY((float) y);
                }
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting moved Player data");
            }
        }).on("getPlayers", args -> {
            if (args.length == 0 || !(args[0] instanceof JSONArray)) return;
            JSONArray array = (JSONArray) args[0];
            try {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String id = obj.getString("id");
                    float x = (float) obj.getDouble("x");
                    float y = (float) obj.getDouble("y");
                    entityEvents.getPlayers().put(id,
                        new Player(id, x, y, 5, 1, 7.5f, "player_piss"));
                }
            } catch (Exception ex) {
                Gdx.app.log("SocketIO", "Error getting players list");
            }
        });
    }

    public void updateServer(float dt) {

        // Legacy update
        /*Player p = entityEvents.getPlayers().get(socket.id());
        timer += dt;
        if (timer >= UPDATE_TIME && p != null && p.hasMoved()) {
            JSONObject data = new JSONObject();
            try {
                data.put("x", p.getX());
                data.put("y", p.getY());
                socket.emit("playerMoved", data);
            } catch (Exception ex) {
                Gdx.app.log("Socket.IO", "Error sending update data");
            }
            timer = 0f;
        }*/
    }

    public void spawnPlayer(Player p) {
        if (socket == null || !socket.connected()) return;

        JSONObject data = new JSONObject();
        try {
            data.put("id", p.getPlayerId());
            data.put("x", p.getX());
            data.put("y", p.getY());
            data.put("hp", p.getHealth());
            data.put("damage", p.getDamage());
            data.put("texture", p.getTexture());
        } catch (Exception e) {
            Gdx.app.log("Socket.IO", "Error building spawn data");
            return;
        }

        socket.emit("playerSpawned", data);
    }

    public void updatePlayer(Player p) {
        if (socket == null || !socket.connected()) return;
        JSONObject data = new JSONObject();

        try {
            data.put("id", p.getPlayerId());
            data.put("x", p.getX());
            data.put("y", p.getY());
            data.put("hp", p.getHealth());
            data.put("damage", p.getDamage());
        } catch (Exception e) {
            Gdx.app.log("Socket.IO", "Error sending player data");
        }

        socket.emit("playerUpdated", data);
    }

    public String getSocketId() {
        return socketId;
    }
}
