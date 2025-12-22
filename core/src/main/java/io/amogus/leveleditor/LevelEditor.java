package io.amogus.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.amogus.gamestates.E_Gamestate;
import io.amogus.gamestates.Level;
import io.amogus.managers.LevelManager;
import io.amogus.managers.Managers;
import io.amogus.managers.TextManager;
import io.amogus.managers.ViewportManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LevelEditor extends Level {
    private final ViewportManager vm = Managers.vm;

    private Vector2 mouseDragStartVec;
    private Action action;
    private Category category;
    private final Input in;
    private final Graphics g;
    private final List<Category> categories;
    private int categoryCycler = 0;

    private final HashMap<Region, Runnable> regions;
    private Region region;



    private float uiScale;
    private float uiOffsetX;
    private float uiOffsetY;

    float refTileWidth = REF_WIDTH / 32f;
    int tileWidth = Math.round(refTileWidth * uiScale);
    float m8  = 8f  * uiScale;
    float m16 = 16f * uiScale;



    public LevelEditor(LevelManager lm) {
        super(E_Gamestate.EDITOR, lm);
        mouseDragStartVec =  new Vector2(0, 0);
        action = Action.MOVE;
        category = Category.BLOCKS;
        in = Gdx.input;
        g = Gdx.graphics;
        categories = new ArrayList<>();
        categories.add(Category.BLOCKS);
        categories.add(Category.BACKGROUNDS);
        categories.add(Category.BOXES);
        categories.add(Category.DETAILS);
        categories.add(Category.ENTITIES);

        regions = new HashMap<>();
        initRegions();
    }

    @Override
    public void handleInput() {

        switch (action) {
            case HAND:
            case MOVE:
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
                break;
            case PLACE:
                break;
            case DELETE:
                break;
            case ZONE:
                boolean click = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);

                if (region == null) {
                    if (click) {
                        region = new Region(getUiMouse(), 0, 0);
                    }

                } else if (region.w == 0 && region.h == 0) {
                    if (click) {
                        region.w = getUiMouse().x;
                        region.h = getUiMouse().y;
                        System.out.println("new Region(" + region.x + "f, " + region.y + "f, " + region.w + "f, " + region.h + "f);");
                        region = null;
                    }
                }
                break;
            case COLLISIONS:
                break;
        }

        System.out.println(this.scrollDeltaY);

        // Screen zoom with mousewheel
       float scroll = this.scrollDeltaY;
        if (scroll != 0f) {
            float zoom = vm.get_zoom();
            float zoomStep = 0.1f;

            zoom += scroll * zoomStep;
            vm.set_zoom(zoom);
            this.scrollDeltaY = 0f;
        }


        // Keybinds
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            action = Action.ZONE;
        }

        for (Region region : regions.keySet()) {
            if (isHovered(getUiMouse().x, getUiMouse().y, region.x, region.y, region.w, region.h) && in.isButtonJustPressed(Input.Buttons.LEFT)) {
                regions.get(region).run();
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
        sm.drawScreen(tileWidth + 2 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "hand");

        sm.drawScreen(2 * tileWidth + 3 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "btn");
        sm.drawScreen(2 * tileWidth + 3 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "collisions");

        sm.drawScreen(3 * tileWidth + 4 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "btn");
        sm.drawScreen(3 * tileWidth + 4 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "action_zoning");

        sm.drawScreen(4 * tileWidth + 5 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "btn");
        sm.drawScreen(4 * tileWidth + 5 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "delete");

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

        // Active button framing
        switch (action) {
            case MOVE:
                sm.drawScreen(m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "frame");
                break;
            case HAND:
                sm.drawScreen(tileWidth + 2 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "frame");
                break;
            case COLLISIONS:
                sm.drawScreen(2 * tileWidth + 3 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "frame");
                break;
            case ZONE:
                sm.drawScreen(3 * tileWidth + 4 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "frame");                break;
            case DELETE:
                sm.drawScreen(4 * tileWidth + 5 * m8, Gdx.graphics.getHeight() - tileWidth - m8, tileWidth, tileWidth, "frame");                break;
        }


        TextManager.draw("X: " + getUiMouse().x + " Y: " + getUiMouse().y, 20, Color.WHITE, false, g.getWidth() / 2f - 64, g.getHeight() - 32);
    }

    private void initRegions() {
        // Top-Left buttons
        regions.put(new Region(8.0f, 1014.0f, 66.75f, 1072.5f), () -> {action = Action.MOVE;});
        regions.put(new Region(75.0f, 1013.0f, 134.25f, 1072.5f),  () -> {action = Action.HAND;});
        regions.put(new Region(144.0f, 1014.0f, 202.5f, 1071.75f),   () -> {action = Action.COLLISIONS;});
        regions.put(new Region(212.0f, 1014.0f, 270.75f, 1072.5f), () -> {action = Action.ZONE;});
        regions.put(new Region(280.0f, 1015.0f, 338.25f, 1071.75f), () -> {action = Action.DELETE;});

        // Block selector
        regions.put(new Region(83.0f, 18.0f, 140.25f, 72.0f), () -> {
                if (!(categoryCycler == categories.size() -1)) {
                    categoryCycler++;
                } else {
                    categoryCycler = 0;
                }
                category = categories.get(categoryCycler);
        });
    }
}
