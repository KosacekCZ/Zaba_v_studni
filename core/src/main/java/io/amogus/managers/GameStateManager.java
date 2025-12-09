package io.amogus.managers;

import com.badlogic.gdx.Gdx;
import io.amogus.gamestates.*;
import io.amogus.leveleditor.LevelEditor;

import java.util.HashMap;

public class GameStateManager {
    private static GameStateManager instance;
    private final HashMap<E_Gamestate, Gamestate> gamestates;
    private Gamestate currentState;

    public static GameStateManager getInstance() {
        if(instance == null) instance = new GameStateManager();
        return instance;
    }

    private GameStateManager() {
        gamestates = new HashMap<>();
        EntityManager.init(this);
        registerGameStates();
    }

    public void updateWorld() {
        currentState.updateWorld();
    }

    public void updateScreen() {
        currentState.updateScreen();
    }

    public void handleInput() {
        currentState.handleInput();
    }

    public void addGameState(Gamestate gameState) {
        gamestates.putIfAbsent(gameState.state, gameState);
    }

    public Gamestate getCurrentState() {
        return currentState;
    }

    public void setGameState(E_Gamestate newState) {
        Gamestate state = gamestates.get(newState);
        if (state == null) {
            throw new IllegalStateException("No Gamestate registered for " + newState);
        }
        currentState = state;
        Gdx.input.setInputProcessor(currentState);
    }

    private void registerGameStates() {
        addGameState(new MainMenu(this));
        addGameState(new Lobby(this));
        addGameState(new Game(this));
        addGameState(new LevelEditor(this));
        addGameState(new TestingArea(this));
    }
}
