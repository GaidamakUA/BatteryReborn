package com.blogspot.androidgaidamak.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.blogspot.androidgaidamak.BatteryGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 510;
		config.height = 510;
		new LwjglApplication(new BatteryGame(), config);
	}
}
