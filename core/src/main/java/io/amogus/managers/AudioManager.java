package io.amogus.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class AudioManager {
    private static AudioManager aum;
    public static AudioManager getInstance() {
        if (aum == null) aum = new AudioManager();
        return aum;
    }

    private final HashMap<String, Sound> sounds = new HashMap<>();
    private final HashMap<String, Long> playing = new HashMap<>();

    private AudioManager() {
        registerSounds();
    }

    public long play(String sound) {
        Sound s = sounds.get(sound);
        return s == null ? -1L : s.play();
    }

    public long loop(String sound) {
        Sound s = sounds.get(sound);
        return s == null ? -1L : s.loop();
    }

    public void stop(String sound) {
        Sound s = sounds.get(sound);
        if (s != null) s.stop();
    }

    public void stop(String sound, long id) {
        Sound s = sounds.get(sound);
        if (s != null && id != -1L) s.stop(id);
    }

    private void registerSounds() {
        sounds.put("minigun_windup", Gdx.audio.newSound(Gdx.files.internal("assets/sounds/minigun_windup.ogg")));
        sounds.put("minigun_wind_loop", Gdx.audio.newSound(Gdx.files.internal("assets/sounds/minigun_wind_loop.ogg")));
        sounds.put("minigun_firing", Gdx.audio.newSound(Gdx.files.internal("assets/sounds/minigun_firing.ogg")));


    }
}
