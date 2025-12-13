package io.amogus.managers;

import com.badlogic.gdx.Gdx;
import io.amogus.gamestates.*;
import io.amogus.leveleditor.LevelEditor;

import java.util.HashMap;

public class LevelManager {
    private static LevelManager instance;
    private final HashMap<E_Gamestate, Level> gamestates;
    private Level currentState;

    public static LevelManager getInstance() {
        if(instance == null) instance = new LevelManager();
        return instance;
    }

    private LevelManager() {
        gamestates = new HashMap<>();
        registerGameStates();
    }

    private void registerGameStates() {
        addGameState(new MainMenu(this));
        addGameState(new Lobby(this));
        addGameState(new Game(this));
        addGameState(new LevelEditor(this));
        addGameState(new TestingArea(this));
    }

    public void handleInput() {
        currentState.handleInput();
    }

    public void updateWorld() {
        currentState.updateWorld();
    }

    public void updateScreen() {
        currentState.updateScreen();
    }

    public void addGameState(Level gameState) {
        gamestates.putIfAbsent(gameState.state, gameState);
    }

    public Level getCurrentState() {
        return currentState;
    }

    public void setGameState(E_Gamestate newState) {
        Level state = gamestates.get(newState);
        if (state == null) {
            throw new IllegalStateException("No Gamestate registered for " + newState);
        }
        currentState = state;
        Gdx.input.setInputProcessor(currentState);
    }


}
