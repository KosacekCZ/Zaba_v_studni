package io.amogus.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;

public class TextManager {
    static SpriteManager sm = SpriteManager.getInstance();

    private static HashMap<Integer, BitmapFont> fontStash = new HashMap<>();

    public static void draw(String text, int size, Color color, boolean shadow, float x, float y) {
        if (fontStash.get(size) == null) fontStash.put(size, generateFont(size, color, shadow));
        fontStash.get(size).draw(sm.getBatch(), text, x, y);

    }


    private static BitmapFont generateFont(int size, Color color, boolean shadow) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/font/slkscrb.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.borderWidth = 0;
        parameter.color = color;
        if (shadow) {
            parameter.shadowOffsetX = 2;
            parameter.shadowOffsetY = 2;
            parameter.shadowColor = new Color(0.2f, 0.2f, 0.2f, 0.85f);
        }
        BitmapFont customFont = generator.generateFont(parameter);
        generator.dispose();
        return customFont;
    }
}
