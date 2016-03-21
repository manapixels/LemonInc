package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Networking.Networking;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Starship;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MyGdxGame extends Game {
	public static final int V_HEIGHT = 480;
	public static final int V_WIDTH = 800;
	public static final float PPM = 100f;
	public SpriteBatch batch;

//	Starship player; ///////////
//	Texture playerShip; ///////////////
//	Texture friendlyShip; //////////////////
	public Networking network;


	@Override
	public void create () {
		Gdx.app.log("Game", "created");
		//texture: image to be pasted on the game screen
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));

//		network = new Networking();
//		network.connectToServer();
//		network.configSocketEvents();
	}


	@Override
	public void dispose() {
		super.dispose();
//		network.dispose();
	}
}
