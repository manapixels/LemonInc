package com.lemoninc.nimbusrun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lemoninc.nimbusrun.Screens.WaitScreen;

public class NimbusRun extends Game {
	public static final int V_HEIGHT = 480;
	public static final int V_WIDTH = 800;
	public static final float PPM = 100f;
	public SpriteBatch batch;

	@Override()
	public void create () {
		batch = new SpriteBatch();
		//game starts with waiting screen
		setScreen(new WaitScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}

