package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.GameLauncher;
import com.mygdx.game.LabyrinthGenerator;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tarkvara roguelike project";
		config.width = 800;
		config.height = 800;
		new LwjglApplication(new GameLauncher(), config);
	}
}
