package io.amogus.managers;

import com.badlogic.gdx.Gdx;
import io.amogus.entities.Player;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Gdx.app.log("SocketIO", "Connected");
                String id = socket.id();
                em.addPlayer(new Player(id, 100f, 100f,  100, 10, 10f, "player_green"));
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data =  (JSONObject) objects[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: " + id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data =  (JSONObject) objects[0];
                try {
                    String playerId = data.getString("id");
                    Gdx.app.log("SocketIO", "New Player Connected: " + playerId);
                    em.addPlayer(new Player(playerId, (float) data.getDouble("x"), (float) data.getDouble("y"),  100, 10, 10f, "player_piss"));
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting new PlayerID");
                }
            }
        }).on("playerDisconnect", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data =  (JSONObject) objects[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "New Player Connected: " + id);
                    em.removePlayer(id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject data =  (JSONObject) objects[0];
                try {
                    String playerId = data.getString("id");
                    double x = data.getDouble("x");
                    double y = data.getDouble("y");
                    if (em.getPlayers().get(playerId) != null) {
                        em.getPlayers().get(playerId).setX((float) x);
                        em.getPlayers().get(playerId).setY((float) y);
                    }
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects =  (JSONArray) args[0];
                try {
                    for  (int i = 0; i < objects.length(); i++) {
                        String id = objects.getJSONObject(i).getString("id");
                        em.getPlayers().put(id, new Player(id,
                            (float)objects.getJSONObject(i).getDouble("x"),
                            (float)objects.getJSONObject(i).getDouble("y"),
                            5, 1, 7.5f,  "player_piss"));
                    }
                } catch (Exception ex) {
                    Gdx.app.log("SocketIO", "Error getting players list");
                }
            }
        });
    }

    public void updateServer(float dt) {
        Player p = em.getPlayers().get(socket.id());
        timer += dt;
        if (timer >= UPDATE_TIME && p != null && p.hasMoved()) {
            JSONObject data = new  JSONObject();
            try {
                data.put("x", p.getX());
                data.put("y", p.getY());
                socket.emit("playerMoved", data);
            } catch (Exception ex) {
                Gdx.app.log("Socket.IO", "Error sending update data");
            }
        }
    }
}
