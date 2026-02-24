package io.amogus.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.amogus.levels.E_Gamestate;
import io.amogus.levels.Level;
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
    private final ArrayList<Button> buttons;
    private final HashMap<String, ArrayList<Button>> placeables;

    float sw = Gdx.graphics.getWidth();
    float sh = Gdx.graphics.getHeight();
    float rtw = REF_WIDTH / 32f;
    private float uiScale = Math.min(sw / REF_WIDTH, sh / REF_HEIGHT);
    int tw = Math.round(rtw * uiScale);
    float m4  = 4f  * uiScale;
    float m8  = 8f  * uiScale;
    float m16 = 16f * uiScale;

    private final float bw = tw;
    private final float padding = m8;
    private final float pbw = bw + padding;



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

        buttons = new ArrayList<>();
        placeables = new HashMap<>();
    }

    @Override
    public void setup() {
        initButtons();
        initPlaceables();
        vm.getWorldCamera().translate(0, 0);
        vm.getWorldCamera().update();
    }

    public void initPlaceables() {
        // Blocks
        placeables.put("Blocks", new ArrayList<>());
        placeables.get("Blocks").add(new Button(2 * pbw, m16, tw, tw,  "blocks", () -> {

        }));

        // Boxes
        placeables.put("Boxes", new ArrayList<>());
        placeables.get("Boxes").add(new Button(2 * pbw, m16, tw, tw,  "boxes", () -> {

        }));

        // Background
        placeables.put("Backgrounds", new ArrayList<>());
        placeables.get("Backgrounds").addAll(List.of(
            new Button(2 * pbw, m16, tw, tw,  "background", () -> {

            }),
            new Button(3 * pbw, m16, tw, tw,  "brick_wall", () -> {}),
            new Button(4 * pbw, m16, tw, tw,  "bricks_gray", () -> {}),
            new Button(5 * pbw, m16, tw, tw,  "bricks_gray_light", () -> {}),
            new Button(6 * pbw, m16, tw, tw,  "floor_1", () -> {}),
            new Button(7 * pbw, m16, tw, tw,  "outer_floor", () -> {}),
            new Button(8 * pbw, m16, tw, tw,  "outer_floor_2", () -> {}),
            new Button(9 * pbw, m16, tw, tw,  "outer_floor_3", () -> {}),
            new Button(10 * pbw, m16, tw, tw,  "wall_corner", () -> {}),
            new Button(11 * pbw, m16, tw, tw,  "wall_doorway", () -> {}),
            new Button(12 * pbw, m16, tw, tw,  "wall_straight", () -> {})
        ));



        // Details
        placeables.put("Details", new ArrayList<>());
        placeables.get("Details").add(new Button(2 * pbw, m16, tw, tw,  "details", () -> {

        }));

        // Entities
        placeables.put("Entities", new ArrayList<>());
        placeables.get("Entities").add(new Button(2 * pbw, m16, tw, tw,  "entities", () -> {

        }));
    }

    public void initButtons() {
        // Block cycler
        buttons.add(new Button(pbw, m16, tw, tw, "arrows_change", () -> {
            if (!(categoryCycler == categories.size() -1)) {
                categoryCycler++;
            } else {
                categoryCycler = 0;
            }
            category = categories.get(categoryCycler);
        }));

        // Top-right
        buttons.add(new Button(sw - tw - m8, sh - tw - m8, tw, tw,  "save", () -> {

        }));

        buttons.add(new Button(sw - 2 * tw - 2 * m8,sh - tw - m8, tw, tw,"cross", () -> {

        }));

        buttons.add(new Button(sw - 3 * tw - 3 * m8,sh - tw - m8, tw, tw, "step_back", () -> {

        }));


        // Top-left
        buttons.add(new Button(m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "arrow_move", () -> {
            action = Action.MOVE;
        }));

        buttons.add(new Button(tw + 2 * m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "hand", () -> {
            action = Action.HAND;

        }));

        buttons.add(new Button(2 * tw + 3 * m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "collisions", () -> {
            action = Action.COLLISIONS;

        }));

        buttons.add(new Button(3 * tw + 4 * m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "action_zoning", () -> {
            action = Action.ZONE;

        }));

        buttons.add(new Button(4 * tw + 5 * m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "delete", () -> {
            action = Action.DELETE;

        }));
    }

    @Override
    public void handleInput() {

        switch (action) {
            case HAND:
            case MOVE:
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (mouseDragStartVec == null) mouseDragStartVec = new Vector2();
                    mouseDragStartVec.set(Gdx.input.getX(), Gdx.input.getY());
                }

                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    var cam = vm.getWorldCamera();

                    float sx0 = mouseDragStartVec.x;
                    float sy0 = mouseDragStartVec.y;
                    float sx1 = Gdx.input.getX();
                    float sy1 = Gdx.input.getY();

                    com.badlogic.gdx.math.Vector3 w0 = new com.badlogic.gdx.math.Vector3(sx0, sy0, 0);
                    com.badlogic.gdx.math.Vector3 w1 = new com.badlogic.gdx.math.Vector3(sx1, sy1, 0);

                    cam.unproject(w0, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    cam.unproject(w1, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

                    cam.translate(w0.x - w1.x, w0.y - w1.y, 0);
                    cam.update();

                    mouseDragStartVec.set(sx1, sy1);
                }
                break;
            case PLACE:
                break;
            case DELETE:
                break;
            case ZONE:
                break;
            case COLLISIONS:
                break;
        }

        float scroll = this.scrollDeltaY;
        if (scroll != 0f) {
            float zoom = vm.get_zoom();
            float zoomStep = 0.1f;

            zoom += scroll * zoomStep;
            vm.set_zoom(zoom);

            vm.getWorldCamera().update();

            this.scrollDeltaY = 0f;
        }


        // Keybinds
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            action = Action.ZONE;
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
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();

        // Toolbar
        sm.drawScreen(m8, m8, sw - 2 * m8, tw + m16, "toolbar_transparent_512");

        // Draw buttons
        for (Button button : buttons) {
            button.draw();

            boolean hovered =
                mx >= button.x && mx <= button.x + button.w &&
                    my >= button.y && my <= button.y + button.h;

            if (hovered && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                button.onClick();
            }
        }

        switch (category) {
            case BLOCKS:
                for (Button b : placeables.get("Blocks")) {
                    b.draw();
                }
                break;

            case BOXES:
                for (Button b : placeables.get("Boxes")) {
                    b.draw();
                }

                break;

            case BACKGROUNDS:
                for (Button b : placeables.get("Backgrounds")) {
                    b.draw();
                }

                break;

            case DETAILS:
                for (Button b : placeables.get("Details")) {
                    b.draw();
                }

                break;

            case ENTITIES:
                for (Button b : placeables.get("Entities")) {
                    b.draw();
                }
        }

        // Active button framing
        switch (action) {
            case MOVE:
                sm.drawScreen(m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "frame");
                break;
            case HAND:
                sm.drawScreen(tw + 2 * m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "frame");
                break;
            case COLLISIONS:
                sm.drawScreen(2 * tw + 3 * m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "frame");
                break;
            case ZONE:
                sm.drawScreen(3 * tw + 4 * m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "frame");
                break;
            case DELETE:
                sm.drawScreen(4 * tw + 5 * m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "frame");
                break;
        }

        TextManager.draw("X: " + getUiMouse().x + " Y: " + getUiMouse().y, 20, Color.WHITE, false, g.getWidth() / 2f - 64, g.getHeight() - 32);
    }
}
