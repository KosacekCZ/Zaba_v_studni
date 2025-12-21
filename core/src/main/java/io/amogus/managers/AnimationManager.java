package io.amogus.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AnimationManager {
    private static AnimationManager instance;
    public static AnimationManager getInstance() {
        if (instance == null) instance = new AnimationManager();
        return instance;
    }
    private final SpriteManager sm = SpriteManager.getInstance();


    private static final float MONITOR_FPS = Gdx.graphics.getFramesPerSecond();

    private static final class AnimState {
        float monitorFrameAcc;
        int frameIndex;
    }
    private final HashMap<String, AnimState> states = new HashMap<>();
    private final HashMap<String, TextureRegion[]> framesCache = new HashMap<>();

    private AnimationManager() {}

    /**
     * Looks up provided texture name and animates it with provided parameters.
     * @param fpf Frames-per-Frame: For how many screen frames the sprite frame stays on screen.
     * @param dt delta time - Gdx implementation of delta time, controls the sprite frame timing.
     * @param texture texture name
     * @return TextureRegion from provided spritesheet
     */
    public TextureRegion animateSprite(float fpf, float dt, String texture) {
        TextureRegion[] frames = framesCache.computeIfAbsent(texture, this::loadFramesForTexture);
        if (frames == null || frames.length == 0) return null;

        AnimState st = states.computeIfAbsent(texture, k -> new AnimState());

        float safeFpf = Math.max(1f, fpf);
        st.monitorFrameAcc += dt * MONITOR_FPS;

        int advance = (int)(st.monitorFrameAcc / safeFpf);
        if (advance > 0) {
            st.monitorFrameAcc -= advance * safeFpf;
            st.frameIndex = (st.frameIndex + advance) % frames.length;
        }

        return frames[st.frameIndex];
    }

    private TextureRegion[] loadFramesForTexture(String texture) {
        return sm.getRegions(texture, 6);
    }

    public void reset(String texture) {
        states.remove(texture);
    }


}
