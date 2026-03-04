package io.amogus.leveleditor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.amogus.managers.Managers;
import io.amogus.managers.TextManager;

public class Button {

    public float x;
    public float y;
    public float w;
    public float h;
    public String texture;
    public String pressedTexture;
    public String iconTexutre;
    private TextureRegion iconRegion;
    public String btnText;
    public boolean isPressed;
    public boolean isDissabled;
    public boolean isToggleable;
    public Runnable fn;


    public Button(float x, float y, float w, float h, String texture, String pressedTexture, String iconTexutre, String btnText, Runnable fn) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.texture = texture;
        this.pressedTexture = pressedTexture;
        this.iconTexutre = iconTexutre;
        this.btnText = btnText;
        this.fn = fn;
    }

    public Button( float x, float y, float w, float h, String iconTexutre, Runnable fn) {
        this.fn = fn;
        this.iconTexutre = iconTexutre;
        this.isToggleable = true;
        this.h = h;
        this.w = w;
        this.y = y;
        this.x = x;
    }

    public Button( float x, float y, float w, float h, String iconTexutre, boolean isToggleable, Runnable fn) {
        this.fn = fn;
        this.iconTexutre = iconTexutre;
        this.isToggleable = isToggleable;
        this.h = h;
        this.w = w;
        this.y = y;
        this.x = x;
    }

    public Button(float x, float y, float w, float h, TextureRegion iconRegion, boolean isToggleable, Runnable fn) {
        this.fn = fn;
        this.iconRegion = iconRegion;
        this.isToggleable = isToggleable;
        this.h = h;
        this.w = w;
        this.y = y;
        this.x = x;
    }



    public void onClick() {
        fn.run();
        if (isToggleable) {
            isPressed = true;
        }
    }

    public void draw() {
        Managers.sm.drawScreen(x, y, w, h, "btn");

        if (iconRegion != null) {
            Managers.sm.drawScreen(x, y, w, h, iconRegion);
        } else {
            Managers.sm.drawScreen(x, y, w, h, iconTexutre);
        }

        if (isPressed) {
            Managers.sm.drawScreen(x, y, w, h, "frame");

        }
    }

}
