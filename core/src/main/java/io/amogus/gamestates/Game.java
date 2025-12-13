package io.amogus.gamestates;

import com.badlogic.gdx.graphics.Color;
import io.amogus.managers.LevelManager;

public class Game extends Level {

    public Game(LevelManager lm) {
        super(E_Gamestate.GAME, lm);
        setup();
    }

    public void setup() {

    }

    @Override
    public void updateWorld() {
        for (int i = -1024; i <= 1024; i++) {
            if (i%32 == 0) {
                sm.drawLine(i, i, -1024, 1024, Color.WHITE);
                sm.drawLine(-1024, 1024, i, i, Color.WHITE);
            }
        }
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void handleInput() {

    }
}
