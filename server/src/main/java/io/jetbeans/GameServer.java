package io.jetbeans;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import java.util.HashMap;
import java.util.Map;

public class GameServer {

    private final Map<String, Player> players = new HashMap<>();
    private SocketIOServer server;

    public void start() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(3003);

        server = new SocketIOServer(config);

        server.addConnectListener(this::onConnect);
        server.addDisconnectListener(this::onDisconnect);
        server.addEventListener("playerMoved", Player.class, (client, data, ackSender) -> onPlayerMoved(client, data));
        server.addEventListener("playerUpdated", Player.class, (client, data, ackSender) -> onPlayerUpdated(client, data));

        server.start();
        System.out.println("[WELL] Server is now running.");
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.start();
    }

    private void onConnect(SocketIOClient client) {
        System.out.println("Player has connected to the server.");

        String id = client.getSessionId().toString();

        client.sendEvent("socketID", Map.of("id", id));
        client.sendEvent("getPlayers", players);
        client.getNamespace()
            .getBroadcastOperations()
            .sendEvent("newPlayer", new Player(id, 100, 100, 100, 10, "player_green"));

        players.put(id, new Player(id, 100, 100, 100, 10, "player_green"));
    }

    private void onDisconnect(SocketIOClient client) {
        String id = client.getSessionId().toString();
        System.out.println("Player has disconnected.");

        client.getNamespace()
            .getBroadcastOperations()
            .sendEvent("playerDisconnected", Map.of("id", id));

        players.remove(id);
    }

    private void onPlayerUpdated(SocketIOClient client, Player player) {
        players.get(player.getId()).update((float) player.getX(), (float) player.getY(), player.getHealth(), player.getDamage());
        client.getNamespace().getBroadcastOperations().sendEvent("playerUpdated", player);
    }

    private void onPlayerMoved(SocketIOClient client, Player data) {
        String id = client.getSessionId().toString();
        data.setId(id);

        client.getNamespace()
            .getBroadcastOperations()
            .sendEvent("playerMoved", data);

        for (Player p : players.values()) {
            if (p.getId().equals(id)) {
                p.setX(data.getX());
                p.setY(data.getY());
                break;
            }
        }
    }


}
