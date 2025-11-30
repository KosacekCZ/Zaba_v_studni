package io.amogus.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.amogus.gamestates.E_Gamestate;
import io.amogus.gamestates.Gamestate;
import io.amogus.managers.TextManager;
import io.amogus.managers.ViewportManager;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LevelEditor extends Gamestate {
    private final ViewportManager vm;

    private Vector2 mouseDragStartVec;
    private Action action;
    private Category category;
    private Input in;
    private Graphics g;
    private List<Category> categories;
    private int categoryCycler = 0;



    public LevelEditor() {
        super(E_Gamestate.EDITOR);
        vm = ViewportManager.getInstance();
        mouseDragStartVec =  new Vector2(0, 0);
        action = Action.HAND;
        category = Category.BLOCKS;
        in = Gdx.input;
        g = Gdx.graphics;
        categories = new ArrayList<>();
        categories.add(Category.BLOCKS);
        categories.add(Category.BACKGROUNDS);
        categories.add(Category.BOXES);
        categories.add(Category.DETAILS);
        categories.add(Category.ENTITIES);
    }

    @Override
    public void handleInput() {
        if (action == Action.HAND) {

            // Screen mouse drag
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (mouseDragStartVec == null) {
                    mouseDragStartVec = new Vector2();
                }
                mouseDragStartVec.set(Gdx.input.getX(), Gdx.input.getY());
            }
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                float currentX = Gdx.input.getX();
                float currentY = Gdx.input.getY();

                float dx = mouseDragStartVec.x - currentX;
                float dy = currentY - mouseDragStartVec.y; // flip Y for screen→world feel

                vm.getWorldCamera().translate(dx, dy);

                mouseDragStartVec.set(currentX, currentY);
            }


            // Screen zoom with mousewheel
            int scroll = (int) scrollDeltaY;
            if (scroll != 0f) {
                float zoom = vm.get_zoom();
                float zoomStep = 0.1f;

                zoom += scroll * zoomStep;
                vm.set_zoom(zoom);

                scrollDeltaY = 0f;
            }

            // GUI Triggers
            if ((in.getX() > 104 && in.getX() < 180 && in.getY() > 1260 && in.getY() < 1330) && in.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (!(categoryCycler == categories.size() -1)) {
                    categoryCycler++;
                } else {
                    categoryCycler = 0;
                }
                category = categories.get(categoryCycler);
                System.out.println("cycled category to: " + category);
            }
        }
    }

    @Override
    public void updateWorld() {
        int worldSize = 1024;
        for (int i = -worldSize; i <= worldSize; i++) {
            if (i%32 == 0) {
                sm.drawLine(i, i, -worldSize, worldSize, Color.DARK_GRAY);
                sm.drawLine(-worldSize, worldSize, i, i, Color.DARK_GRAY);
            }

            if (i%128 == 0 || i==0) {
                TextManager.draw(String.valueOf(i), 8, Color.WHITE, false, i, 0);
                TextManager.draw(String.valueOf(i), 8, Color.WHITE, false,0, i);
            }
        }
    }

    @Override
    public void updateScreen() {
        int tileWidth = (int) Math.floor((double) Gdx.graphics.getWidth() / 32);

        // Toolbar
        sm.drawScreen(8, 8, Gdx.graphics.getWidth() - 16f, tileWidth * 1.2f, "toolbar_transparent_512");

        // Colidable walls
        switch (category) {
            case BLOCKS:
                sm.drawScreen(16, 16, tileWidth, tileWidth, "btn");
                sm.drawScreen(16, 16, tileWidth, tileWidth, "blocks");

                sm.drawScreen(16 + tileWidth + 8, 16, tileWidth, tileWidth, "btn");
                sm.drawScreen(16 + tileWidth + 8, 16, tileWidth, tileWidth, "arrows_change");
                break;

            case BOXES:
                sm.drawScreen(16, 16, tileWidth, tileWidth, "btn");
                sm.drawScreen(16, 16, tileWidth, tileWidth, "boxes");

                sm.drawScreen(16 + tileWidth + 8, 16, tileWidth, tileWidth, "btn");
                sm.drawScreen(16 + tileWidth + 8, 16, tileWidth, tileWidth, "arrows_change");
                break;

            case BACKGROUNDS:
                sm.drawScreen(16, 16, tileWidth, tileWidth, "btn");
                sm.drawScreen(16, 16, tileWidth, tileWidth, "background");

                sm.drawScreen(16 + tileWidth + 8, 16, tileWidth, tileWidth, "btn");
                sm.drawScreen(16 + tileWidth + 8, 16, tileWidth, tileWidth, "arrows_change");
                break;

            case DETAILS:
                sm.drawScreen(16, 16, tileWidth, tileWidth, "btn");
                sm.drawScreen(16, 16, tileWidth, tileWidth, "details");

                sm.drawScreen(16 + tileWidth + 8, 16, tileWidth, tileWidth, "btn");
                sm.drawScreen(16 + tileWidth + 8, 16, tileWidth, tileWidth, "arrows_change");
                break;

            case ENTITIES:
                sm.drawScreen(16, 16, tileWidth, tileWidth, "btn");
                sm.drawScreen(16, 16, tileWidth, tileWidth, "entities");

                sm.drawScreen(16 + tileWidth + 8, 16, tileWidth, tileWidth, "btn");
                sm.drawScreen(16 + tileWidth + 8, 16, tileWidth, tileWidth, "arrows_change");
        }


        // Top-right buttons
        sm.drawScreen(Gdx.graphics.getWidth() - tileWidth - 8, Gdx.graphics.getHeight() - tileWidth - 8, tileWidth, tileWidth, "btn");
        sm.drawScreen(Gdx.graphics.getWidth() - tileWidth - 8, Gdx.graphics.getHeight() - tileWidth - 8, tileWidth, tileWidth, "save");

        sm.drawScreen(Gdx.graphics.getWidth() - 2 * tileWidth - 16, Gdx.graphics.getHeight() - tileWidth - 8, tileWidth, tileWidth, "btn");
        sm.drawScreen(Gdx.graphics.getWidth() - 2 * tileWidth - 16, Gdx.graphics.getHeight() - tileWidth - 8, tileWidth, tileWidth, "cross");

        sm.drawScreen(Gdx.graphics.getWidth() - 3 * tileWidth - 24, Gdx.graphics.getHeight() - tileWidth - 8, tileWidth, tileWidth, "btn");
        sm.drawScreen(Gdx.graphics.getWidth() - 3 * tileWidth - 24, Gdx.graphics.getHeight() - tileWidth - 8, tileWidth, tileWidth, "step_back");

        // Top-left buttons
        sm.drawScreen(8, Gdx.graphics.getHeight() - tileWidth - 8, tileWidth, tileWidth, "btn");
        sm.drawScreen(8, Gdx.graphics.getHeight() - tileWidth - 8, tileWidth, tileWidth, "arrow_move");

        sm.drawScreen(tileWidth + 16, Gdx.graphics.getHeight() - tileWidth - 8, tileWidth, tileWidth, "btn");
        sm.drawScreen(tileWidth + 16, Gdx.graphics.getHeight() - tileWidth - 8, tileWidth, tileWidth, "collisions");

        sm.drawScreen(2 * tileWidth + 24, Gdx.graphics.getHeight() - tileWidth - 8, tileWidth, tileWidth, "btn");
        sm.drawScreen(2 * tileWidth + 24, Gdx.graphics.getHeight() - tileWidth - 8, tileWidth, tileWidth, "delete");



        TextManager.draw(String.valueOf("X: " + in.getX() + " Y: " + in.getY()), 20, Color.WHITE, false, g.getWidth() / 2f - 64, g.getHeight() - 32);
    }


}
