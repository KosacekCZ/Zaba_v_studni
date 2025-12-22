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
    /*
    private static final SpriteManager sm = Managers.sm;
    private static final LevelManager lm = Managers.lm;
    private static final ViewportManager vm = Managers.vm;
    private static final TextureManager tm = Managers.tm;
    private static final ServerManager svm = Managers.svm;
*/
    private final GameServer gameServer;

    public Main(GameServer gameServer) {
        this.gameServer = gameServer;
    }


    @Override
    public void create() {
        Managers.init();

        Managers.tm.loadTextures();

        Managers.lm.registerGameStates();
        Managers.lm.setup();
        Managers.lm.setGameState(E_Gamestate.TESTING);


        Managers.sm.setGlobalIllumination(0.6f);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        Managers.sm.setWorldProjection(Managers.vm.getWorldCombined());
        Managers.sm.setUiProjection(Managers.vm.getUiCombined());

        Managers.lm.handleInput();

        // World drawing
        Managers.sm.beginWorld();
        Managers.lm.updateWorld();
        Managers.sm.end();

        // Screen drawing
        Managers.sm.beginScreen();
        Managers.lm.updateScreen();
        Managers.sm.end();


    }

    @Override
    public void dispose() {
        Managers.sm.dispose();
        Managers.svm.disconnectSocket();
        if (gameServer != null) gameServer.stop();

        java.util.Map<Thread, StackTraceElement[]> all = Thread.getAllStackTraces();
        for (java.util.Map.Entry<Thread, StackTraceElement[]> e : all.entrySet()) {
            Thread t = e.getKey();
            if (!t.isAlive() || t.isDaemon()) continue;

            String n = t.getName();
            if (n.contains("OkHttp Dispatcher") || n.startsWith("Timer-") || n.contains("globalEventExecutor")) {
                System.out.println("=== " + n + " state=" + t.getState() + " ===");
                StackTraceElement[] trace = e.getValue();
                for (StackTraceElement stackTraceElement : trace) {
                    System.out.println("  at " + stackTraceElement);
                }
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        Managers.vm.resize(width, height);
        SpriteManager.getInstance().resize(width, height);

    }
}
