package io.amogus.managers;

import java.io.File;

public class TextureManager {
    private static TextureManager instance;

    public static TextureManager getInstance() {
        if (instance == null) {
            instance = new TextureManager();
        }
        return instance;
    }

    SpriteManager sm = SpriteManager.getInstance();

    public void loadTextures() {
        File baseFolder = new File("assets/");

        if (!baseFolder.exists() || !baseFolder.isDirectory()) {
            System.err.println("The 'assets' folder does not exist or is not a directory.");
            return;
        }

        File[] subfolders = baseFolder.listFiles(File::isDirectory);
        File[] rootFiles = baseFolder.listFiles(File::isFile);

        if ((rootFiles == null || rootFiles.length == 0)) {
            return;
        }

        if (subfolders != null) {
            for (File sub : subfolders) {

                if (!sub.isDirectory()) {
                    System.out.println("Skipping non-directory file: " + sub.getName());
                    continue;
                }

                File[] files = sub.listFiles();
                if (files == null) {
                    System.err.println("Could not list files in subdirectory: " + sub.getName());
                    continue;
                }

                for (File img : files) {
                    if (img.isFile() && (img.getName().endsWith(".png") || img.getName().endsWith(".gif"))) {
                        sm.loadSprite(img.getName().replace(".png", "").replace(".gif", ""), img.getPath());
                    } else {
                        System.out.println("Skipping non-image file: " + img.getName());
                    }
                }
            }
        }

        for (File img : rootFiles) {
            if (img.isFile() && (img.getName().endsWith(".png") || img.getName().endsWith(".gif"))) {
                sm.loadSprite(img.getName().replace(".png", "").replace(".gif", ""), img.getPath());
            } else {
                System.out.println("Skipping non-image file: " + img.getName());
            }
        }
    }
}
