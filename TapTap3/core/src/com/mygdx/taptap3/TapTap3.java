package com.mygdx.taptap3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.taptap3.Screens.PlayScreen;

public class TapTap3 extends Game {
	public static final int V_HEIGHT = 480;
	public static final int V_WIDTH = 800;
	public static final float PPM = 100f;
	public SpriteBatch batch;

	@Override
	public void create () {
//		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

//	@Override
//	public void render () {
//		super.render();
//	}
}

//TODO double jump, upper bounds, fill screen with background, HUD