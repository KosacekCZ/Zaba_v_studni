package io.amogus.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import io.amogus.leveleditor.Region;
import io.amogus.managers.GameStateManager;
import io.amogus.managers.ServerManager;
import io.amogus.managers.SpriteManager;
import io.amogus.managers.ViewportManager;

public abstract class Gamestate extends InputAdapter {
    public E_Gamestate state;

    protected float scrollDeltaX;
    protected float scrollDeltaY;

    protected final SpriteManager sm;
    protected final GameStateManager gsm;
    protected final ViewportManager vm;
    protected final ServerManager svm;

    private float uiScale;
    private float uiOffsetX;
    private float uiOffsetY;

    protected final float REF_WIDTH = 1920f;
    protected final float REF_HEIGHT = 1080f;

    protected Region bounds;

    public Gamestate(E_Gamestate state, GameStateManager gsm) {
        sm = SpriteManager.getInstance();
        this.gsm = gsm;
        vm = ViewportManager.getInstance();
        svm = ServerManager.getInstance();
        this.state = state;
        scrollDeltaY = 0f;
        scrollDeltaX = 0f;
    }

    public abstract void updateWorld();

    public abstract void updateScreen();

    public abstract void handleInput();

    protected void updateUiTransform() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        uiScale = Math.min(w / REF_WIDTH, h / REF_HEIGHT);

        float vpW = REF_WIDTH * uiScale;
        float vpH = REF_HEIGHT * uiScale;

        uiOffsetX = (w - vpW) * 0.5f;
        uiOffsetY = (h - vpH) * 0.5f;
    }

    protected Vector2 getUiMouse() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        float mx = Gdx.input.getX();
        float my = screenH - Gdx.input.getY();

        float uiX = (mx - uiOffsetX) / uiScale;
        float uiY = (my - uiOffsetY) / uiScale;

        return new Vector2(uiX, uiY);
    }

    protected boolean isHovered(float mx, float my, float x1, float y1, float x2, float y2) {
        return (mx >= x1 && mx <= x2) && (my >= y1 && my <= y2);
    }

    public Region getBounds() {
        return bounds;
    }

    public void setBounds(Region bounds) {
        this.bounds = bounds;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        this.scrollDeltaY += amountY;
        this.scrollDeltaX += amountX;

        return true;
    }
}
