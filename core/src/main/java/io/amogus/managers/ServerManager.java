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
    private static ServerManager instance;
    private final EntityManager em;
    private float timer;
    private float UPDATE_TIME = 1/60f;

    public static ServerManager getInstance() {
        if (instance == null) instance = new ServerManager();
        return instance;
    }

    public ServerManager() {
        em = EntityManager.getInstace();
    }

    public void connectSocket() {
        try {
            socket = IO.socket("http://localhost:3003");
            socket.connect();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void configSocketEvents() {
        socket.on(Socket.EVENT_CONNECT, args -> {
            Gdx.app.log("SocketIO", "Connected");
            String id = socket.id();
            em.addPlayer(new Player(id, 100f, 100f, 100, 10, 10f, "player_green"));
        }).on("socketID", args -> {
            if (args.length == 0 || args[0] == null) return;
            Object payload = args[0];
            String id;

            if (payload instanceof JSONObject) {
                JSONObject data = (JSONObject) payload;
                id = data.optString("id", null);
            } else {
                id = payload.toString();
            }

            if (id != null) {
                Gdx.app.log("SocketIO", "My ID: " + id);
            }
        }).on("newPlayer", args -> {
            if (args.length == 0 || !(args[0] instanceof JSONObject)) return;
            JSONObject data = (JSONObject) args[0];
            try {
                String playerId = data.getString("id");
                float x = data.has("x") ? (float) data.getDouble("x") : 100f;
                float y = data.has("y") ? (float) data.getDouble("y") : 100f;
                Gdx.app.log("SocketIO", "New Player Connected: " + playerId);
                em.addPlayer(new Player(playerId, x, y, 100, 10, 10f, "player_piss"));
            } catch (JSONException e) {
                Gdx.app.log("SocketIO", "Error getting new PlayerID");
            }
        }).on("playerDisconnected", args -> {
            if (args.length == 0 || !(args[0] instanceof JSONObject)) return;
            JSONObject data = (JSONObject) args[0];
            try {
                String id = data.getString("id");
                Gdx.app.log("SocketIO", "Player Disconnected: " + id);
                em.removePlayer(id);
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
                if (em.getPlayers().get(playerId) != null) {
                    em.getPlayers().get(playerId).setX((float) x);
                    em.getPlayers().get(playerId).setY((float) y);
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
                    em.getPlayers().put(id,
                        new Player(id, x, y, 5, 1, 7.5f, "player_piss"));
                }
            } catch (Exception ex) {
                Gdx.app.log("SocketIO", "Error getting players list");
            }
        });
    }


    public void updateServer(float dt) {
        Player p = em.getPlayers().get(socket.id());
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
        }
    }

}
