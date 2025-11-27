package io.amogus.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ViewportManager {
    private static ViewportManager instance;

    public static final float WORLD_WIDTH = 1280f;
    public static final float WORLD_HEIGHT = 720f;

    private final OrthographicCamera worldCamera;
    private final ExtendViewport worldViewport;

    private final OrthographicCamera uiCamera;

    private final Vector3 tmpVec = new Vector3();

    public static ViewportManager getInstance() {
        if (instance == null) instance = new ViewportManager();
        return instance;
    }

    private ViewportManager() {
        worldCamera = new OrthographicCamera();
        worldViewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, worldCamera);
        worldViewport.apply();
        worldCamera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void update() {
        worldCamera.update();
        uiCamera.update();
    }

    public void resize(int width, int height) {
        worldViewport.update(width, height, true);
        uiCamera.setToOrtho(false, width, height);
        uiCamera.update();
    }

    public void camFollow(float x, float y) {
        float direction = (float) (Math.atan2(x - getCameraX(), -(y - getCameraY())) - (Math.PI / 2));

        if (getCameraX() + Math.cos(direction) * 9f > x - Gdx.graphics.getWidth()
            && getCameraX() + Math.cos(direction) < x + Gdx.graphics.getWidth()) {
            worldCamera.position.x += (float) (Math.cos(direction) * (Math.abs(x - getCameraX()) / 8f));
        }
        if (getCameraY() + Math.sin(direction) * 9f > y - Gdx.graphics.getHeight()
            && getCameraY() + Math.sin(direction) < y + Gdx.graphics.getHeight()) {
            worldCamera.position.y += (float) (Math.sin(direction) * (Math.abs(y - getCameraY()) / 8f));
        }
    }

    public void translate(float x, float y, float z) {
        worldCamera.position.set(x, y, z);
    }

    public void set_zoom(float zoom) {
        worldCamera.zoom = zoom;
    }

    public void adjust_zoom(float zoom) {
        worldCamera.zoom += zoom;
    }

    public OrthographicCamera getWorldCamera() {
        return worldCamera;
    }

    public ExtendViewport getWorldViewport() {
        return worldViewport;
    }

    public Matrix4 getWorldCombined() {
        return worldCamera.combined;
    }

    public float getCameraX() {
        return worldCamera.position.x;
    }

    public float getCameraY() {
        return worldCamera.position.y;
    }

    public float getWorldMouseX() {
        tmpVec.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
        worldViewport.unproject(tmpVec);
        return tmpVec.x;
    }

    public float getWorldMouseY() {
        tmpVec.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
        worldViewport.unproject(tmpVec);
        return tmpVec.y;
    }

    public float getmx() {
        return getWorldMouseX();
    }

    public float getmy() {
        return getWorldMouseY();
    }

    public float getCAtan2() {
        return (float) Math.atan2(getmx(), getmy());
    }

    public boolean isMouseBetween(float x1, float x2, float y1, float y2) {
        return (Gdx.input.getX() >= x1 && Gdx.input.getX() <= x2
            && Gdx.input.getY() >= y1 && Gdx.input.getY() <= y2);
    }

    public float getCamSpd() {
        return 7.5f;
    }

    public OrthographicCamera getUiCamera() {
        return uiCamera;
    }

    public Matrix4 getUiCombined() {
        return uiCamera.combined;
    }
}
