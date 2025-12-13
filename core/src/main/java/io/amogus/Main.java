package io.amogus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;
import io.amogus.gamestates.*;
import io.amogus.managers.*;
import io.jetbeans.GameServer;
import org.w3c.dom.Text;

public class Main extends ApplicationAdapter {
    private static SpriteManager sm = SpriteManager.getInstance();
    private static LevelManager lm = LevelManager.getInstance();
    private static TextureManager tm = TextureManager.getInstance();
    private static ViewportManager vm = ViewportManager.getInstance();
    private static ServerManager svm = ServerManager.getInstance();

    private GameServer server;

    public Main(GameServer server) {
        this.server = server;
    }

    @Override
    public void create() {
        tm = TextureManager.getInstance();
        tm.loadTextures();
        svm.connectSocket();
        svm.configSocketEvents();
        lm.setGameState(E_Gamestate.TESTING);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            server.stop();
            System.exit(0);
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        sm.setWorldProjection(vm.getWorldCombined());
        sm.setUiProjection(vm.getUiCombined());

        sm.beginWorld();
        lm.updateWorld();
        sm.end();

        sm.beginScreen();
        lm.updateScreen();
        sm.end();

        lm.handleInput();
        svm.updateServer(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        sm.dispose();
    }

    @Override
    public void resize(int width, int height) {
        vm.resize(width, height);
    }
}
