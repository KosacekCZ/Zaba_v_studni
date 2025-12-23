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

    private float globalVolume = 1f;

    private final HashMap<String, Sound> sounds = new HashMap<>();

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

    public void setVolume(String sound, long id, float volume) {
        sounds.get(sound).setVolume(id, volume);
    }

    private void registerSounds() {
        sounds.put("minigun_windup", Gdx.audio.newSound(Gdx.files.internal("assets/sounds/minigun_windup.ogg")));
        sounds.put("minigun_wind_loop", Gdx.audio.newSound(Gdx.files.internal("assets/sounds/minigun_wind_loop.ogg")));
        sounds.put("minigun_firing", Gdx.audio.newSound(Gdx.files.internal("assets/sounds/minigun_firing.ogg")));
        sounds.put("shotgun_firing", Gdx.audio.newSound(Gdx.files.internal("assets/sounds/shotgun_firing.ogg")));
        sounds.put("shotgun_reload", Gdx.audio.newSound(Gdx.files.internal("assets/sounds/shotgun_reload.ogg")));
        sounds.put("shotgun_hit", Gdx.audio.newSound(Gdx.files.internal("assets/sounds/shotgun_hit.ogg")));
        sounds.put("shotgun_chamber", Gdx.audio.newSound(Gdx.files.internal("assets/sounds/shotgun_chamber.ogg")));
        sounds.put("empty_mag", Gdx.audio.newSound(Gdx.files.internal("assets/sounds/empty_mag.ogg")));
    }

    public void setGlobalVolume(float volume) {
        this.globalVolume = volume;
    }

    public float getGlobalVolume() {
        return this.globalVolume;
    }

}
