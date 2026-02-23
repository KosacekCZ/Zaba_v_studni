package io.amogus.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.amogus.entities.DroppedItem;
import io.amogus.entities.Player;
import io.amogus.leveleditor.Region;
import io.amogus.managers.LevelManager;
import io.amogus.managers.TextManager;

public class TestingArea extends Level {
    private final int worldSize = 256;
    private Player p;
    private final Input in = Gdx.input;


    public TestingArea(LevelManager lm) {
        super(E_Gamestate.TESTING, lm);
    }

    public void setup() {
        vm.set_zoom(0.3f);
        svm.connectSocket();
        svm.configSocketEvents();
        p = em.getLocalPlayer();
        setBounds(new Region(-worldSize + 40f, -worldSize + 40f, (2 * worldSize) - 80f, (2 * worldSize) - 64f));

        em.spawnEntity(new DroppedItem(100, 100, "bullet_modifier"));
    }

    @Override
    public void updateWorld() {
        vm.update();

        if (p != null) {
            vm.camFollow(p.getX() + 16f, p.getY() + 16f);

            drawBackground();
            drawOuter(8);
            drawWalls();

            sm.drawRect(-worldSize + 48, -worldSize + 48, (2 * worldSize) - 96, (2 * worldSize) - 96, false, Color.RED);

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

        if (p != null) {
            p.setMoving(false);
            if (in.isKeyPressed(Input.Keys.W) && p.getDashVelocity().isZero()) {
                dir.y += 1 * Gdx.graphics.getDeltaTime();
                p.setMoving(true);
            }
            if (in.isKeyPressed(Input.Keys.S) && p.getDashVelocity().isZero()) {
                dir.y -= 1 * Gdx.graphics.getDeltaTime();
                p.setMoving(true);
            }
            if (in.isKeyPressed(Input.Keys.A) && p.getDashVelocity().isZero()) {
                dir.x -= 1 * Gdx.graphics.getDeltaTime();
                p.setMoving(true);
            }
            if (in.isKeyPressed(Input.Keys.D) && p.getDashVelocity().isZero()) {
                dir.x += 1 * Gdx.graphics.getDeltaTime();
                p.setMoving(true);
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
    }

    private void drawBackground() {

        for (int i = -(worldSize + 96); i <= (worldSize + 64); i++) {
            for  (int j = -(worldSize + 96); j <= (worldSize + 64); j++) {
                if (i % 32 == 0 && j % 32 == 0) {

                    int gx = i / 32;
                    int gy = j / 32;
                    int hash = gx * 734287 + gy * 912271;
                    int rot = Math.abs(hash) % 4;
                    float rotation = rot * 90f;

                    sm.draw(i, j, 32, 32, rotation, "floor_1");
                }
            }

            if (i % 32 == 0) {
                TextManager.draw(String.valueOf(i), 8, Color.WHITE, false, i, 0);
                TextManager.draw(String.valueOf(i), 8, Color.WHITE, false,0, i);
            }
        }
    }

    private void drawWalls() {
        final int TILE = 128;

        int min = -worldSize + 128;
        int max = worldSize - 128;

        int outerMin = min - TILE;
        int outerMax = max + TILE;

        for (int x = outerMin; x < outerMax; x += TILE) {
            for (int y = outerMin; y < outerMax; y += TILE) {

                if (x >= min && x < max && y >= min && y < max)
                    continue;

                boolean left   = x < min;
                boolean right  = x >= max;
                boolean bottom = y < min;
                boolean top    = y >= max;

                float rotation = 0f;
                String texture;

                // CORNERS
                if ((left || right) && (top || bottom)) {
                    texture = "wall_corner";

                    if (right && top)    rotation = 0f;
                    if (left && top)     rotation = 90f;
                    if (left && bottom)  rotation = 180f;
                    if (right && bottom) rotation = 270f;
                }
                // STRAIGHTS
                else {
                    texture = "wall_straight";

                    if (top)    rotation = 0f;
                    if (left)   rotation = 90f;
                    if (bottom) rotation = 180f;
                    if (right)  rotation = 270f;
                }

                sm.draw(x, y, TILE, TILE, rotation, texture);
            }
        }
    }

    private void drawOuter(int layers) {
        final int TILE = 32;

        int min = -worldSize;
        int max = worldSize;

        int outerMin = min - layers * TILE;
        int outerMax = max + layers * TILE;

        for (int x = outerMin; x < outerMax; x += TILE) {
            for (int y = outerMin; y < outerMax; y += TILE) {
                if (x >= min && x < max && y >= min && y < max)
                    continue;

                int gx = x / TILE;
                int gy = y / TILE;
                int hash = gx * 734287 + gy * 912271;
                int rot = Math.abs(hash) % 4;
                float rotation = rot * 90f;

                sm.draw(x, y, TILE, TILE, rotation, "outer_floor_3");
            }
        }
    }
}
