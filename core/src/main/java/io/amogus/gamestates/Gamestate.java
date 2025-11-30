package io.amogus.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import io.amogus.managers.GameStateManager;
import io.amogus.managers.ServerManager;
import io.amogus.managers.SpriteManager;
import io.amogus.managers.ViewportManager;

public abstract class Gamestate extends InputAdapter {
    public E_Gamestate state;

    protected float scrollDeltaX = 0f;
    protected float scrollDeltaY = 0f;

    protected final SpriteManager sm;
    protected final GameStateManager gsm;
    protected final ViewportManager vm;
    protected final ServerManager svm;

    public Gamestate(E_Gamestate state) {
        sm = SpriteManager.getInstance();
        gsm = GameStateManager.getInstance();
        vm = ViewportManager.getInstance();
        svm = ServerManager.getInstance();
        this.state = state;
        Gdx.input.setInputProcessor(this);
    }

    public abstract void updateWorld();

    public abstract void updateScreen();

    public abstract void handleInput();

    @Override
    public boolean scrolled(float amountX, float amountY) {
        scrollDeltaY += amountY;
        scrollDeltaX += amountX;
        return false;
    }
}
