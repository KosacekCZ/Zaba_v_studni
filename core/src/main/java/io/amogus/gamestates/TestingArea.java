package io.amogus.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.amogus.entities.Player;
import io.amogus.managers.EntityManager;
import io.amogus.managers.GameStateManager;
import io.amogus.managers.TextManager;

public class TestingArea extends Gamestate {

    private final EntityManager em;
    private final int worldSize = 256;
    private Player p;
    private final Input in = Gdx.input;


    public TestingArea(GameStateManager gsm) {
        super(E_Gamestate.TESTING, gsm);
        em = EntityManager.getInstace();
        p = em.getLocalPlayer();
        setup();
    }

    private void setup() {

    }

    @Override
    public void updateWorld() {

        if (p != null) {
            vm.camFollow(p.getX() + 16f, p.getY() + 16f);

            drawBackground();
            em.update();
        } else {
            p = em.getLocalPlayer();
        }
    }

    @Override
    public void updateScreen() {

    }

    @Override
    public void handleInput() {
        // Player controls
        Vector2 dir = new Vector2(0, 0);

        if (in.isKeyPressed(Input.Keys.W) && p.getDashVelocity().isZero()) {
            dir.y += 1;
        }
        if (in.isKeyPressed(Input.Keys.S) && p.getDashVelocity().isZero()) {
            dir.y -= 1;
        }
        if (in.isKeyPressed(Input.Keys.A) && p.getDashVelocity().isZero()) {
            dir.x -= 1;
        }
        if (in.isKeyPressed(Input.Keys.D) && p.getDashVelocity().isZero()) {
            dir.x += 1;
        }

        if (!dir.isZero() && p.getDashVelocity().isZero()) {
            dir.nor();
            p.setX(p.getX() + dir.x * p.getSpeed());
            p.setY(p.getY() + dir.y * p.getSpeed());

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

            if (i%32 == 0 || i==0) {
                TextManager.draw(String.valueOf(i), 8, Color.WHITE, false, i, 0);
                TextManager.draw(String.valueOf(i), 8, Color.WHITE, false,0, i);
            }
        }

        for (int i = -worldSize - (4 * 32); i <= worldSize + (4 * 32); i++) {
            for (int j = -worldSize - (4 * 32); j <= worldSize + (4 * 32); j++) {
                if ((i < worldSize && j < worldSize) ||
                    (i > worldSize && j < worldSize) ||
                    (i < worldSize && j > worldSize) ||
                    (i > worldSize && j > worldSize)) {
                    sm.draw(i, j, 32, 32,"brick_wall");
                }
            }
        }


    }

}
