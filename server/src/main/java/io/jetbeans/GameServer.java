package io.jetbeans;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class GameServer {

    private final Map<String, Player> players = new HashMap<>();
    private SocketIOServer server;
    private volatile boolean running;

    public void start() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(3003);

        server = new SocketIOServer(config);

        server.addConnectListener(this::onConnect);
        server.addDisconnectListener(this::onDisconnect);
        server.addEventListener("playerMoved", Player.class, (client, data, ackSender) -> onPlayerMoved(client, data));
        server.addEventListener("playerUpdated", Player.class, (client, data, ackSender) -> onPlayerUpdated(client, data));

        running = true;
        server.start();
        System.out.println("[WELL] Server is now running.");
    }

    public void stop() {
        running = false;

        SocketIOServer s = this.server;
        this.server = null;

        players.clear();

        if (s == null) return;

        try {
            s.stop();
        } catch (Throwable ignored) {
        }

        try {
            Method shutdown = s.getClass().getMethod("shutdown");
            shutdown.invoke(s);
        } catch (NoSuchMethodException ignored) {
        } catch (Throwable ignored) {
        }

        try {
            Class<?> gee = Class.forName("io.netty.util.concurrent.GlobalEventExecutor");
            Object inst = gee.getField("INSTANCE").get(null);
            Method shutdownGracefully = inst.getClass().getMethod("shutdownGracefully");
            shutdownGracefully.invoke(inst);
        } catch (Throwable ignored) {
        }

        System.out.println("[WELL] Server stopped.");
    }

    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.start();
    }

    private void onConnect(SocketIOClient client) {
        if (!running) {
            client.disconnect();
            return;
        }

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

        client.getNamespace()
            .getBroadcastOperations()
            .sendEvent("playerDisconnected", Map.of("id", id));

        players.remove(id);
    }

    private void onPlayerUpdated(SocketIOClient client, Player player) {
        Player p = players.get(player.getId());
        if (p != null) {
            p.update((float) player.getX(), (float) player.getY(), player.getHealth(), player.getDamage());
        }
        client.getNamespace().getBroadcastOperations().sendEvent("playerUpdated", player);
    }

    private void onPlayerMoved(SocketIOClient client, Player data) {
        String id = client.getSessionId().toString();
        data.setId(id);

        client.getNamespace()
            .getBroadcastOperations()
            .sendEvent("playerMoved", data);

        Player p = players.get(id);
        if (p != null) {
            p.setX(data.getX());
            p.setY(data.getY());
        }
    }
}
