package io.amogus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;
import io.amogus.entities.enemies.Enemy;
import io.amogus.levels.*;
import io.amogus.managers.*;
import io.jetbeans.GameServer;

public class Main extends ApplicationAdapter {
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
        Managers.lm.setGameState(E_Gamestate.EDITOR);

        Managers.sm.setGlobalIllumination(0.6f);


        //Managers.em.spawnEntity(new Enemy(100, 150, 100, 10, 8f, "enemy_head_1", "enemy_body_1"));
        //Managers.em.spawnEntity(new Enemy(100, 150, 100, 10, 8f, "enemy_head_2", "enemy_body_1"));
        //Managers.em.spawnEntity(new Enemy(150, 150, 100, 10, 8f, "enemy_head_3", "enemy_body_1"));
        Managers.em.spawnEntity(new Enemy(100, 150, 100, 10, 8f, "enemy_head_4", "enemy_body_1", "enemy_jaw_1"));
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
