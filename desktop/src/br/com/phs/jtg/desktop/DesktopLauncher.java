package br.com.phs.jtg.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import br.com.phs.jtg.GameMain;
import br.com.phs.jtg.helpers.GameInfo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = (int) GameInfo.WIDTH;
		config.height = (int) GameInfo.HEIGHT;
		config.resizable = false;

		new LwjglApplication(new GameMain(), config);
	}
}
