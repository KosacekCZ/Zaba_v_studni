package io.amogus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.amogus.entities.Entity;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private float UPDATE_TIME = 1/60f;
    private SpriteBatch batch;
    private Texture image;
    private Socket socket;
    String id;
    Entity player;
    Texture playerTexture;
    Texture player2Texture;
    HashMap<String, Entity> players;
    float timer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        connectSocket();
        configSocketEvents();

        playerTexture = new Texture("player_green.png");
        player2Texture = new Texture("player_piss.png");

        players = new HashMap<>();
    }

    @Override
    public void render() {
        updateServer(Gdx.graphics.getDeltaTime());
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        if (player != null) {
            batch.draw(playerTexture, player.getX(), player.getY(),  player.getWidth(), player.getHeight());

            for (Entity e  : players.values()) {
                e.getSprite().draw(batch);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.W)) player.setY(player.getY() + 10f);
            if (Gdx.input.isKeyPressed(Input.Keys.S)) player.setY(player.getY() - 10f);
            if (Gdx.input.isKeyPressed(Input.Keys.A)) player.setX(player.getX() - 10f);
            if (Gdx.input.isKeyPressed(Input.Keys.D)) player.setX(player.getX() + 10f);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        player2Texture.dispose();
    }

    public void updateServer(float dt) {
        timer += dt;
        if (timer >= UPDATE_TIME && player != null && player.hasMoved()) {
            JSONObject data = new  JSONObject();
            try {
                data.put("x", player.getX());
                data.put("y", player.getY());
                socket.emit("playerMoved", data);
            } catch (Exception ex) {
                Gdx.app.log("Socket.IO", "Error sending update data");
            }
        }

    }

    public void connectSocket() {
        try {
            socket = IO.socket("http://localhost:30003");
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
                player = new Entity(100, 100, playerTexture);
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
                    Gdx.app.log("SocketIO", "New Player Connected: " + id);
                    players.put(playerId, new  Entity(100, 100, player2Texture));
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
                    players.remove(id);
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
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    if (players.get(playerId) != null) {
                        players.get(playerId).setX(x.floatValue());
                        players.get(playerId).setY(y.floatValue());
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
                        players.put(objects.getJSONObject(i).getString("id"), new  Entity(
                            (float)objects.getJSONObject(i).getDouble("x"),
                            (float)objects.getJSONObject(i).getDouble("y"),
                            player2Texture));
                    }
                } catch (Exception ex) {
                    Gdx.app.log("SocketIO", "Error getting players list");
                }
            }
        });
    }
}
