package io.amogus.managers;

import io.amogus.entities.Player;

import java.util.HashMap;
import java.util.List;

public interface IEntityEvents {
    void spawnPlayer(Player player);
    void spawnLocalPlayer(Player player);
    HashMap<String, Player> getPlayers();
    void removePlayer(String playerId);
}
