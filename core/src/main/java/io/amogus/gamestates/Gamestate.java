package io.amogus.gamestates;

import io.amogus.managers.GameStateManager;
import io.amogus.managers.SpriteManager;
import io.amogus.managers.ViewportManager;

public abstract class Gamestate {
    public E_Gamestate state;

    protected final SpriteManager sm;
    protected final GameStateManager gsm;
    protected final ViewportManager vm;

    public Gamestate(E_Gamestate state) {
        sm = SpriteManager.getInstance();
        gsm = GameStateManager.getInstance();
        vm = ViewportManager.getInstance();
        this.state = state;
    }

    public abstract void updateWorld();

    public abstract void updateScreen();
}
