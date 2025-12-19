package io.amogus.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.amogus.Main;
import io.jetbeans.GameServer;

public class Lwjgl3Launcher {

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;

        GameServer gameServer = new GameServer();
        Thread serverThread = new Thread(gameServer::start, "game-server");
        serverThread.setDaemon(true);
        serverThread.start();

        new Lwjgl3Application(new Main(gameServer), getDefaultConfiguration());
        Runtime.getRuntime().addShutdownHook(new Thread(gameServer::stop, "server-shutdown"));


        System.exit(0);
    }

    private static Lwjgl3Application createApplication(GameServer gameServer) {
        return new Lwjgl3Application(new Main(gameServer), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Zaba v Studni");
        configuration.setWindowIcon("icons/logo.ico");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowedMode(1920, 1080);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        configuration.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.ANGLE_GLES20, 0, 0);

        return configuration;
    }
}
