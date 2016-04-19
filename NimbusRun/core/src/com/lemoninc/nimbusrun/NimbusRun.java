package com.lemoninc.nimbusrun;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lemoninc.nimbusrun.Screens.SplashScreen;

public class NimbusRun extends Game {
	public static final int V_HEIGHT = 480;
	public static final int V_WIDTH = 800;
	public static final float PPM = 100f;
	public SpriteBatch batch;

	@Override()
	public void create () {
		batch = new SpriteBatch();
//		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		setScreen(new SplashScreen(this, V_WIDTH, V_HEIGHT));
	}

	@Override
	public void render () {
		super.render();
	}
}

