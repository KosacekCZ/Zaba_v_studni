package io.amogus.gamestates;

import com.badlogic.gdx.graphics.Color;

public class Game extends Gamestate {

    public Game() {
        super(E_Gamestate.GAME);
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
