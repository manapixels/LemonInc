package com.mygdx.taptap3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.taptap3.Screens.PlayScreen;

public class TapTap3 extends Game {
	public static final int V_HEIGHT = 480;
	public static final int V_WIDTH = 800;
	public static final float PPM = 100f;
	public SpriteBatch batch;

	@Override()
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}


/* TODO :
	1. synchronizing player characters (wei sheng and kevin)
	2. waiting lobby for connecting players (2 players) (kevin)
	3. put in character design (ws)
	4. buttons : toaster on click, black when cooling down, red when ready (nikki)
	5. character animation (zhenyang)
	6. HUD : timer (nikki)
	7. fixed linear velocity (ws)
	8. background image (ws)
	9. end Screen (ws)
 */