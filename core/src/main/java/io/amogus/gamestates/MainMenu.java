package io.amogus.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import io.amogus.leveleditor.Region;
import io.amogus.managers.LevelManager;
import io.amogus.managers.TextManager;

import java.util.HashMap;

public class MainMenu extends Level {

    private final HashMap<Region, Runnable> regions;

    public MainMenu(LevelManager lm) {
        super(E_Gamestate.MAIN_MENU, lm);
        regions = new HashMap<>();
        initRegions();
    }

    @Override
    public void updateWorld() {
        float tileSize = 32f;
        float worldWidth = vm.getWorldViewport().getWorldWidth() * vm.getWorldCamera().zoom;
        float worldHeight = vm.getWorldViewport().getWorldHeight() * vm.getWorldCamera().zoom;

        int cols = (int) Math.ceil(worldWidth  / tileSize) + 2;
        int rows = (int) Math.ceil(worldHeight / tileSize) + 2;

        float startX = vm.getWorldCamera().position.x - worldWidth  / 2f - tileSize;
        float startY = vm.getWorldCamera().position.y - worldHeight / 2f - tileSize;

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                sm.draw(startX + i * tileSize,startY + j * tileSize, tileSize, tileSize,"brick_wall");
            }
        }



    }

    @Override
    public void updateScreen() {
        sm.drawScreen(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), "central_gradient_dark" );
        sm.drawScreen((Gdx.graphics.getWidth() / 2f) - 128f, Gdx.graphics.getHeight() / 2f, 256f, 64f, "frame");
        TextManager.draw("Test", 36, Color.WHITE,true,  (Gdx.graphics.getWidth() / 2f) - 64f, (Gdx.graphics.getHeight() / 2f) + 48f);
    }

    @Override
    public void handleInput() {
        for (Region region : regions.keySet()) {
            if (isHovered(getUiMouse().x, getUiMouse().y, region.x, region.y, region.w, region.h) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                regions.get(region).run();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            lm.setGameState(E_Gamestate.TESTING);
        }
    }

    private void initRegions() {
        regions.put(new Region(Gdx.graphics.getWidth() / 2f - 128f, Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth() / 2f + 128f, (Gdx.graphics.getHeight() / 2f) + 64f),
            ()->{lm.setGameState(E_Gamestate.TESTING);
                System.out.println("Changing GameState");});
    }
}
