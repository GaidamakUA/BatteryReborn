package com.blogspot.androidgaidamak.desktop;

import b.b.Battery;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 510;
		config.height = 510;
		new LwjglApplication(new Battery(), config);
	}
}
