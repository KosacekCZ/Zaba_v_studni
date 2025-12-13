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

    public Main(GameServer gameServer) {

    }


    @Override
    public void create() {
        sm = SpriteManager.getInstance();
        lm =  LevelManager.getInstance();
        vm =  ViewportManager.getInstance();
        tm =  TextureManager.getInstance();

        tm.loadTextures();
        lm.setGameState(E_Gamestate.TESTING);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            System.exit(0);
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
    }

    @Override
    public void resize(int width, int height) {
        vm.resize(width, height);
    }
}
