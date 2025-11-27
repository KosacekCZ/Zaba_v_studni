package io.jetbeans;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Map;

public class GameServer {

    private final List<Player> players = new CopyOnWriteArrayList<>();
    private SocketIOServer server;

    public void start() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(3003);

        server = new SocketIOServer(config);

        server.addConnectListener(this::onConnect);
        server.addDisconnectListener(this::onDisconnect);
        server.addEventListener("playerMoved", Player.class, (client, data, ackSender) -> onPlayerMoved(client, data));

        server.start();
        System.out.println("[WELL] Server is now running.");
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    private void onConnect(SocketIOClient client) {
        System.out.println("Player has connected to the server.");

        String id = client.getSessionId().toString();

        client.sendEvent("socketID", Map.of("id", id));
        client.sendEvent("getPlayers", players);
        client.getNamespace()
            .getBroadcastOperations()
            .sendEvent("newPlayer", new Player(id, 100, 100));

        players.add(new Player(id, 100, 100));
    }

    private void onDisconnect(SocketIOClient client) {
        String id = client.getSessionId().toString();
        System.out.println("Player has disconnected.");

        client.getNamespace()
            .getBroadcastOperations()
            .sendEvent("playerDisconnected", Map.of("id", id));

        players.removeIf(p -> p.getId().equals(id));
    }

    private void onPlayerMoved(SocketIOClient client, Player data) {
        String id = client.getSessionId().toString();
        data.setId(id);

        client.getNamespace()
            .getBroadcastOperations()
            .sendEvent("playerMoved", data);

        for (Player p : players) {
            if (p.getId().equals(id)) {
                p.setX(data.getX());
                p.setY(data.getY());
                break;
            }
        }
    }

    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.start();
    }
}
