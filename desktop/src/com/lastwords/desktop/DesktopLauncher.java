package com.lastwords.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lastwords.LastWords;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = LastWords.TITLE;
        config.width = LastWords.WIDTH;
        config.height = LastWords.HEIGHT;
        new LwjglApplication(new LastWords(), config);
    }
}
