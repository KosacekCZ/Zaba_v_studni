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
    private static SpriteManager sm;
    private static LevelManager lm;
    private static ViewportManager vm;
    private static TextureManager tm;
    private static ServerManager svm;

    private final GameServer gameServer;

    public Main(GameServer gameServer) {
        this.gameServer = gameServer;
    }


    @Override
    public void create() {
        sm = SpriteManager.getInstance();
        lm =  LevelManager.getInstance();
        vm =  ViewportManager.getInstance();
        tm =  TextureManager.getInstance();
        svm = ServerManager.getInstance();

        tm.loadTextures();
        lm.setGameState(E_Gamestate.TESTING);

        sm.setGlobalIllumination(0.6f);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        sm.setWorldProjection(vm.getWorldCombined());
        sm.setUiProjection(vm.getUiCombined());

        lm.handleInput();

        // World drawing
        sm.beginWorld();
        lm.updateWorld();
        sm.end();

        // Screen drawing
        sm.beginScreen();
        lm.updateScreen();
        sm.end();


    }

    @Override
    public void dispose() {
        sm.dispose();
        svm.disconnectSocket();
        if (gameServer != null) gameServer.stop();

        java.util.Map<Thread, StackTraceElement[]> all = Thread.getAllStackTraces();
        for (java.util.Map.Entry<Thread, StackTraceElement[]> e : all.entrySet()) {
            Thread t = e.getKey();
            if (!t.isAlive() || t.isDaemon()) continue;

            String n = t.getName();
            if (n.contains("OkHttp Dispatcher") || n.startsWith("Timer-") || n.contains("globalEventExecutor")) {
                System.out.println("=== " + n + " state=" + t.getState() + " ===");
                StackTraceElement[] trace = e.getValue();
                for (int i = 0; i < trace.length; i++) {
                    System.out.println("  at " + trace[i]);
                }
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        vm.resize(width, height);
        SpriteManager.getInstance().resize(width, height);

    }
}
