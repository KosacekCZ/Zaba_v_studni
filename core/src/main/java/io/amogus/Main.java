package io.amogus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import io.amogus.entities.Player;
import io.amogus.gamestates.E_Gamestate;
import io.amogus.gamestates.Game;
import io.amogus.gamestates.Lobby;
import io.amogus.gamestates.MainMenu;
import io.amogus.leveleditor.LevelEditor;
import io.amogus.managers.*;
import io.jetbeans.GameServer;

public class Main extends ApplicationAdapter {
    private ServerManager svm;
    private SpriteManager sm;
    private EntityManager em;
    private TextureManager tm;
    private GameStateManager gsm;
    private GameServer server;
    private ViewportManager vm;

    public Main(GameServer server) {
        this.server = server;
        server.stop();
    }

    @Override
    public void create() {
        tm = TextureManager.getInstance();
        tm.loadTextures();

        svm = ServerManager.getInstance();
        sm = SpriteManager.getInstance();
        em = EntityManager.getInstace();
        gsm = GameStateManager.getInstance();
        vm = ViewportManager.getInstance();

        vm.set_zoom(0.5f);

        svm.connectSocket();
        svm.configSocketEvents();


        gsm.addGameState(new MainMenu());
        gsm.addGameState(new Lobby());
        gsm.addGameState(new Game());
        gsm.addGameState(new LevelEditor());
        gsm.setCurrentState(E_Gamestate.EDITOR);

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        vm.update();

        sm.setWorldProjection(vm.getWorldCombined());
        sm.setUiProjection(vm.getUiCombined());

        sm.beginWorld();
        gsm.updateWorld();
        sm.end();

        sm.beginScreen();
        gsm.updateScreen();
        sm.end();

        gsm.handleInput();
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
