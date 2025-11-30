package io.amogus.managers;

import io.amogus.gamestates.E_Gamestate;
import io.amogus.gamestates.Gamestate;

import java.util.HashMap;

public class GameStateManager {
    private static GameStateManager instance;
    private E_Gamestate currentState;
    private final HashMap<E_Gamestate, Gamestate> gamestates;

    public static GameStateManager getInstance() {
        if(instance == null) instance = new GameStateManager();
        return instance;
    }

    public GameStateManager() {
        gamestates = new HashMap<>();
    }

    public void updateWorld() {
        gamestates.get(currentState).updateWorld();
    }

    public void updateScreen() {
        gamestates.get(currentState).updateScreen();
    }

    public void handleInput() {
        gamestates.get(currentState).handleInput();
    }

    public void addGameState(Gamestate gameState) {
        gamestates.put(gameState.state, gameState);

        System.out.println(gamestates.size());

        for (Gamestate s :  gamestates.values()) {
            System.out.println("State:" + s.state.toString());
        }
    }

    public E_Gamestate getCurrentState() {
        return currentState;
    }

    public void setCurrentState(E_Gamestate currentState) {
        this.currentState = currentState;
    }
}
