package io.amogus.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector4;
import io.amogus.gamestates.E_Gamestate;
import io.amogus.gamestates.Gamestate;
import io.amogus.managers.TextManager;
import io.amogus.managers.ViewportManager;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelEditor extends Gamestate {
    private final ViewportManager vm;

    private Vector2 mouseDragStartVec;
    private Action action;
    private Category category;
    private final Input in;
    private final Graphics g;
    private final List<Category> categories;
    private int categoryCycler = 0;

    private HashMap<Region, Runnable> regions;
    private boolean zoning;
    private Region region;

    private final float REF_WIDTH = 1920f;
    private final float REF_HEIGHT = 1080f;

    private float uiScale;
    private float uiOffsetX;
    private float uiOffsetY;

    float refTileWidth = REF_WIDTH / 32f;
    int tileWidth = Math.round(refTileWidth * uiScale);
    float m8  = 8f  * uiScale;
    float m16 = 16f * uiScale;



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
        zoning = false;

        regions = new HashMap<>();
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
                float dy = currentY - mouseDragStartVec.y;

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
            /* if (in.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (!(categoryCycler == categories.size() -1)) {
                    categoryCycler++;
                } else {
                    categoryCycler = 0;
                }
                category = categories.get(categoryCycler);
            }*/

            if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                zoning = !zoning;
                System.out.println("Zoning mode: " + zoning);
            }
            if (zoning) {
                if (region == null && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    region = new Region(getUiMouse(), 0, 0);

                } else if (region != null && region.w == 0 && region.h == 0 && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    System.out.println();
                    region.w = getUiMouse().x;
                    region.h = getUiMouse().y;
                    System.out.println("new Region(" + region.x + ", " + region.y + ", " + region.w + ", " + region.h + ");");
                    region = null;
                }
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
        updateUiTransform();

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        uiScale = Math.min(screenW / REF_WIDTH, screenH / REF_HEIGHT);

        float refTileWidth = REF_WIDTH / 32f;
        int tileWidth = Math.round(refTileWidth * uiScale);
        float m8  = 8f  * uiScale;
        float m16 = 16f * uiScale;

        // Toolbar
        sm.drawScreen(m8, m8, screenW - 2 * m8, tileWidth + m16, "toolbar_transparent_512");

        // Colidable walls
        switch (category) {
            case BLOCKS:
                sm.drawScreen(m16, m16, tileWidth, tileWidth, "btn");
                sm.drawScreen(m16, m16, tileWidth, tileWidth, "blocks");

                sm.drawScreen(m16 + tileWidth + m8, m16, tileWidth, tileWidth, "btn");
                sm.drawScreen(m16 + tileWidth + m8, m16, tileWidth, tileWidth, "arrows_change");
                break;

            case BOXES:
                sm.drawScreen(m16, m16, tileWidth, tileWidth, "btn");
                sm.drawScreen(m16, m16, tileWidth, tileWidth, "boxes");

                sm.drawScreen(m16 + tileWidth + m8, m16, tileWidth, tileWidth, "btn");
                sm.drawScreen(m16 + tileWidth + m8, m16, tileWidth, tileWidth, "arrows_change");
                break;

            case BACKGROUNDS:
                sm.drawScreen(m16, m16, tileWidth, tileWidth, "btn");
                sm.drawScreen(m16, m16, tileWidth, tileWidth, "background");

                sm.drawScreen(m16 + tileWidth + m8, m16, tileWidth, tileWidth, "btn");
                sm.drawScreen(m16 + tileWidth + m8, m16, tileWidth, tileWidth, "arrows_change");
                break;

            case DETAILS:
                sm.drawScreen(m16, m16, tileWidth, tileWidth, "btn");
                sm.drawScreen(m16, m16, tileWidth, tileWidth, "details");

                sm.drawScreen(m16 + tileWidth + m8, m16, tileWidth, tileWidth, "btn");
                sm.drawScreen(m16 + tileWidth + m8, m16, tileWidth, tileWidth, "arrows_change");
                break;

            case ENTITIES:
                sm.drawScreen(m16, m16, tileWidth, tileWidth, "btn");
                sm.drawScreen(m16, m16, tileWidth, tileWidth, "entities");

                sm.drawScreen(m16 + tileWidth + m8, m16, tileWidth, tileWidth, "btn");
                sm.drawScreen(m16 + tileWidth + m8, m16, tileWidth, tileWidth, "arrows_change");
        }


        // Top-right buttons
        sm.drawScreen(screenW - tileWidth - m8,screenH - tileWidth - m8, tileWidth, tileWidth, "btn");
        sm.drawScreen(screenW - tileWidth - m8,screenH - tileWidth - m8, tileWidth, tileWidth, "save");

        sm.drawScreen(screenW - 2 * tileWidth - 2 * m8,screenH - tileWidth - m8, tileWidth, tileWidth, "btn");
        sm.drawScreen(screenW - 2 * tileWidth - 2 * m8,screenH - tileWidth - m8, tileWidth, tileWidth, "cross");

        sm.drawScreen(screenW - 3 * tileWidth - 3 * m8,screenH - tileWidth - m8, tileWidth, tileWidth, "btn");
        sm.drawScreen(screenW - 3 * tileWidth - 3 * m8,screenH - tileWidth - m8, tileWidth, tileWidth, "step_back");

        // Top-left buttons
        sm.drawScreen(m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "btn");
        sm.drawScreen(m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "arrow_move");

        sm.drawScreen(tileWidth + 2 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "btn");
        sm.drawScreen(tileWidth + 2 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "collisions");

        sm.drawScreen(2 * tileWidth + 3 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "btn");
        sm.drawScreen(2 * tileWidth + 3 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "delete");



        TextManager.draw(String.valueOf("X: " + in.getX() + " Y: " + in.getY()), 20, Color.WHITE, false, g.getWidth() / 2f - 64, g.getHeight() - 32);
    }

    private void updateUiTransform() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        uiScale = Math.min(w / REF_WIDTH, h / REF_HEIGHT);

        float vpW = REF_WIDTH * uiScale;
        float vpH = REF_HEIGHT * uiScale;

        uiOffsetX = (w - vpW) * 0.5f;
        uiOffsetY = (h - vpH) * 0.5f;
    }

    private Vector2 getUiMouse() {
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        float mx = Gdx.input.getX();
        float my = screenH - Gdx.input.getY();

        float uiX = (mx - uiOffsetX) / uiScale;
        float uiY = (my - uiOffsetY) / uiScale;

        return new Vector2(uiX, uiY);
    }

    private boolean isHovered(float mx, float my, float x1, float y1, float x2, float y2) {
        return (mx >= x1 && mx <= x2) && (my >= y1 && my <= y2);
    }


}
