package io.amogus.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.amogus.entities.DroppedItem;
import io.amogus.entities.Player;
import io.amogus.leveleditor.Region;
import io.amogus.managers.EntityManager;
import io.amogus.managers.LevelManager;
import io.amogus.managers.TextManager;

public class TestingArea extends Level {

    private static EntityManager em;
    private final int worldSize = 256;
    private Player p;
    private final Input in = Gdx.input;


    public TestingArea(LevelManager lm) {
        super(E_Gamestate.TESTING, lm);
        em = EntityManager.getInstance();
        p = em.getLocalPlayer();
        setup();
        setBounds(new Region(-worldSize, -worldSize, worldSize, worldSize));
    }

    private void setup() {
        vm.set_zoom(0.5f);
        svm.connectSocket();
        svm.configSocketEvents();

    }

    @Override
    public void updateWorld() {
        vm.update();

        if (p != null) {
            vm.camFollow(p.getX() + 16f, p.getY() + 16f);

            drawBackground();
            drawWalls();

            em.updateWorld();
        } else {
            p = em.getLocalPlayer();
        }
        pm.update();
    }

    @Override
    public void updateScreen() {
        em.updateScreen();
    }

    @Override
    public void handleInput() {
        // Player controls
        Vector2 dir = new Vector2(0, 0);

        if (in.isKeyPressed(Input.Keys.W) && p.getDashVelocity().isZero()) {
            dir.y += 1 * Gdx.graphics.getDeltaTime();
        }
        if (in.isKeyPressed(Input.Keys.S) && p.getDashVelocity().isZero()) {
            dir.y -= 1 * Gdx.graphics.getDeltaTime();
        }
        if (in.isKeyPressed(Input.Keys.A) && p.getDashVelocity().isZero()) {
            dir.x -= 1 * Gdx.graphics.getDeltaTime();
        }
        if (in.isKeyPressed(Input.Keys.D) && p.getDashVelocity().isZero()) {
            dir.x += 1 * Gdx.graphics.getDeltaTime();
        }

        if (!dir.isZero() && p.getDashVelocity().isZero()) {
            dir.nor();
            p.setX(p.getX() + dir.x * p.getSpeed() * Gdx.graphics.getDeltaTime());
            p.setY(p.getY() + dir.y * p.getSpeed() * Gdx.graphics.getDeltaTime());

            if (in.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
                p.setDashVelocity(new Vector2(dir).scl(4f));
            }
        }
    }

    private void drawBackground() {

        for (int i = -worldSize; i <= worldSize; i++) {
            for  (int j = -worldSize; j <= worldSize; j++) {
                if (i % 32 == 0 && j % 32 == 0) {
                    sm.draw(i, j, 32, 32, "bricks_gray_light");
                }
            }

            if (i % 32 == 0) {
                TextManager.draw(String.valueOf(i), 8, Color.WHITE, false, i, 0);
                TextManager.draw(String.valueOf(i), 8, Color.WHITE, false,0, i);
            }
        }
    }

    private void drawWalls() {
        int tileSize = 32;
        int borderTiles = 8;

        int min = -worldSize;
        int max = worldSize;

        int outerMin = min - borderTiles * tileSize;
        int outerMax = max + borderTiles * tileSize;

        // Left + Right strips
        for (int x = outerMin; x < min; x += tileSize) {
            for (int y = outerMin; y < outerMax; y += tileSize) {
                sm.draw(x, y, tileSize, tileSize, "brick_wall");
            }
        }
        for (int x = max; x < outerMax; x += tileSize) {
            for (int y = outerMin; y < outerMax; y += tileSize) {
                sm.draw(x, y, tileSize, tileSize, "brick_wall");
            }
        }

        // Bottom + Top strips (only across the level width to avoid redrawing corners)
        for (int x = min; x < max; x += tileSize) {
            for (int y = outerMin; y < min; y += tileSize) {
                sm.draw(x, y, tileSize, tileSize, "brick_wall");
            }
        }
        for (int x = min; x < max; x += tileSize) {
            for (int y = max; y < outerMax; y += tileSize) {
                sm.draw(x, y, tileSize, tileSize, "brick_wall");
            }
        }
    }
}
