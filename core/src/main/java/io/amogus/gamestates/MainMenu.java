package io.amogus.gamestates;

import com.badlogic.gdx.Gdx;

public class MainMenu extends Gamestate {

    public MainMenu() {
        super(E_Gamestate.MAIN_MENU);

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
    }

    @Override
    public void handleInput() {

    }
}
