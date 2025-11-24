package io.amogus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import io.amogus.entities.Entity;
import io.amogus.entities.Player;
import io.amogus.managers.EntityManager;
import io.amogus.managers.ServerManager;
import io.amogus.managers.SpriteManager;
import io.amogus.managers.TextureManager;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private ServerManager svm;
    private SpriteManager sm;
    private EntityManager em;
    private TextureManager tm;

    @Override
    public void create() {
        tm = TextureManager.getInstance();
        tm.loadTextures();

        svm = ServerManager.getInstance();
        sm = SpriteManager.getInstance();
        em = EntityManager.getInstace();

        svm.connectSocket();
        svm.configSocketEvents();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        sm.begin();

        em.update();
        svm.updateServer(Gdx.graphics.getDeltaTime());

        sm.end();
    }

    @Override
    public void dispose() {
        sm.dispose();
    }
}
