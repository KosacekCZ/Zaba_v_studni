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
    private final ArrayList<Prop> placed;
    private final ArrayList<Prop> undo;

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

    private Prop inHand;

    public LevelEditor(LevelManager lm) {
        super(E_Gamestate.EDITOR, lm);
        sm.setGlobalIllumination(1.0f);
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
        placed = new ArrayList<>();
        undo = new ArrayList<>();
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
            new Button(3 * pbw, m16, tw, tw,  "brick_wall", () -> {
                inHand = new Prop(0, 0, 32, 32, "brick_wall", 0);}),
            new Button(4 * pbw, m16, tw, tw,  "bricks_gray", () -> {
                inHand = new Prop(0, 0, 32, 32, "bricks_gray", 0);
            }),
            new Button(5 * pbw, m16, tw, tw,  "bricks_gray_light", () -> {
                inHand = new Prop(0, 0, 32, 32, "bricks_gray_light", 0);
            }),
            new Button(6 * pbw, m16, tw, tw,  "floor_1", () -> {
                inHand = new Prop(0, 0, 32, 32, "floor_1", 0);
            }),
            new Button(7 * pbw, m16, tw, tw,  "outer_floor", () -> {
                inHand = new Prop(0, 0, 32, 32, "outer_floor", 0);
            }),
            new Button(8 * pbw, m16, tw, tw,  "outer_floor_2", () -> {
                inHand = new Prop(0, 0, 32, 32, "outer_floor_2", 0);
            }),
            new Button(9 * pbw, m16, tw, tw,  "outer_floor_3", () -> {
                inHand = new Prop(0, 0, 32, 32, "outer_floor_3", 0);
            }),
            new Button(10 * pbw, m16, tw, tw,  "wall_corner", () -> {
                inHand = new Prop(0, 0, 128, 128, "wall_corner", 0);
            }),
            new Button(11 * pbw, m16, tw, tw,  "wall_doorway", () -> {
                inHand = new Prop(0, 0, 128, 128, "wall_doorway", 0);
            }),
            new Button(12 * pbw, m16, tw, tw,  "wall_straight", () -> {
                inHand = new Prop(0, 0, 128, 128, "wall_straight", 0);
            })
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

        buttons.add(new Button(2 * tw + 3 * m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "place", () -> {
            action = Action.PLACE;

        }));

        buttons.add(new Button(3 * tw + 4 * m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "collisions", () -> {
            action = Action.COLLISIONS;

        }));

        buttons.add(new Button(4 * tw + 5 * m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "action_zoning", () -> {
            action = Action.ZONE;

        }));

        buttons.add(new Button(5 * tw + 6 * m8, Gdx.graphics.getHeight() - tw - m8, tw, tw, "delete", () -> {
            action = Action.DELETE;

        }));
    }

    @Override
    public void updateWorld() {
        drawGrid();
        drawPlaced();
        handlePlacingDraw();
    }

    @Override
    public void updateScreen() {
        updateUiTransform();

        // Toolbar
        sm.drawScreen(m8, m8, sw - 2 * m8, tw + m16, "toolbar_transparent_512");

        // Draw buttons
        handleButtonInput(buttons);

        switch (category) {
            case BLOCKS:
                handleButtonInput(placeables.get("Blocks"));
                break;

            case BOXES:
                handleButtonInput(placeables.get("Boxes"));
                break;

            case BACKGROUNDS:
                handleButtonInput(placeables.get("Backgrounds"));
                break;

            case DETAILS:
                handleButtonInput(placeables.get("Details"));
                break;

            case ENTITIES:
                handleButtonInput(placeables.get("Entities"));
        }



        TextManager.draw("X: " + getUiMouse().x + " Y: " + getUiMouse().y, 20, Color.WHITE, false, g.getWidth() / 2f - 64, g.getHeight() - 32);
    }

    @Override
    public void handleInput() {
        handleMacros();

        switch (action) {
            case HAND:
            case MOVE:
                handleMouseDrag();
                handleScroll();
                break;
            case PLACE:
                handlePlacing();
                break;
            case DELETE:
                break;
            case ZONE:
                break;
            case COLLISIONS:
                break;
        }



    }

    public void handleMacros() {
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            if (!placed.isEmpty()) {
                undo.add(placed.remove(placed.size() - 1));
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            if (!undo.isEmpty()) {
                placed.add(undo.remove(undo.size() - 1));
            }
        }
    }

    public void handleButtonInput(ArrayList<Button> buttons) {
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();

        for (Button b : buttons) {
            b.draw();

            boolean hovered =
                mx >= b.x && mx <= b.x + b.w &&
                    my >= b.y && my <= b.y + b.h;

            if (hovered && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                b.onClick();
                buttons.forEach(btn -> btn.isPressed = false);
                b.isPressed = true;
            }
        }
    }

    public void handlePlacing() {
        float mouseY = Gdx.input.getY();
        float screenHeight = Gdx.graphics.getHeight();

        if (mouseY > 100f && mouseY < screenHeight - 100f) {
            if (inHand != null && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                placed.add(new Prop(
                    Math.floorDiv((int) vm.getWorldMouseX(), 32) * 32f,
                    Math.floorDiv((int) vm.getWorldMouseY(), 32) * 32f,
                    inHand.w,
                    inHand.h,
                    inHand.texture,
                    inHand.rotation
                ));
                undo.clear();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            inHand.rotation = (inHand.rotation <= 270 ? inHand.rotation + 90 : 90 );
        }
    }

    public void handlePlacingDraw() {
        // Placeable highlight
        if (inHand != null) {
            sm.draw(Math.floorDiv((int) vm.getWorldMouseX(), 32) * 32f,
                Math.floorDiv((int) vm.getWorldMouseY(), 32) * 32f,
                inHand.w,
                inHand.h,
                inHand.rotation,
                0.5f,
                inHand.texture
            );
            sm.drawRect(Math.floorDiv((int) vm.getWorldMouseX(), 32) * 32f,
                Math.floorDiv((int) vm.getWorldMouseY(), 32) * 32f,
                inHand.w,
                inHand.h,
                false,
                Color.GREEN);
        }
    }

    public void handleMouseDrag() {
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
    }

    public void handleScroll() {
        float scroll = this.scrollDeltaY;
        if (scroll != 0f) {
            float zoom = vm.get_zoom();
            float zoomStep = 0.1f;

            zoom += scroll * zoomStep;
            vm.set_zoom(zoom);

            vm.getWorldCamera().update();

            this.scrollDeltaY = 0f;
        }
    }

    public void drawPlaced() {
        placed.forEach(p -> sm.draw(p.x, p.y, p.w, p.h, p.rotation, p.texture));
    }

    public void drawGrid() {
        int worldSize = 1024;
        // Grid
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
}
