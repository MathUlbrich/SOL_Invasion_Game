package com.somerithm.games.desktop;

import static game.Settings.TITLE;
import static game.Settings.SCREEN_HEIGHT;
import static game.Settings.SCREEN_WIDTH;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = SCREEN_WIDTH ;
		config.height = SCREEN_HEIGHT;
		//config.useGL30 = true;
		config.title = TITLE;
		new LwjglApplication(new Main(), config);
	}
}
