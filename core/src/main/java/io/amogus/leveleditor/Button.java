package io.amogus.leveleditor;

import com.badlogic.gdx.graphics.Color;
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
    public String btnText;
    public boolean isPressed;
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
        this.h = h;
        this.w = w;
        this.y = y;
        this.x = x;
    }

    public void onClick() {
        fn.run();
        isPressed = true;
    }

    public void draw() {
        if (!isPressed || pressedTexture == null) {
            Managers.sm.drawScreen(x, y, w, h, "btn");
        } else {
            Managers.sm.drawScreen(x, y, w, h, "btn");
        }

        if (btnText == null && iconTexutre != null)  {
            Managers.sm.drawScreen(x, y, w, h, iconTexutre);
        } else {
            TextManager.draw(btnText, 8, Color.WHITE, false, x, y);
        }
    }

}
