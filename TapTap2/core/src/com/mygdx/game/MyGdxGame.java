package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Starship;

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
	private Socket socket;
	Starship player; ///////////
	Texture playerShip; ///////////////
	Texture friendlyShip; //////////////////
	HashMap<String, Starship> friendlyPlayers;

	@Override
	public void create () {
		//texture: image to be pasted on the game screen
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
		playerShip = new Texture("playerShip2.png"); //////////
		friendlyShip = new Texture("playerShip.png"); ///////////
		friendlyPlayers = new HashMap<String, Starship>();
		connectSocket();
		configSocketEvents();
	}


	@Override
	public void render () {
		super.render();
//		handleInput(Gdx.graphics.getDeltaTime()); ////////////////
		if (player != null) { /////////////////
			player.draw(batch); //////////////
		} ////////////////////
		//draws every player's starship in the hashmap
		for (HashMap.Entry<String, Starship> entry: friendlyPlayers.entrySet()) {
			entry.getValue().draw(batch);
		}

	}

	public void handleInput(float dt) {
		if (player != null) {
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				player.setPosition(player.getX() + (-200 * dt), player.getY());
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				player.setPosition(player.getX() + (200 * dt), player.getY());
			}
		}
	}

	public void connectSocket() {
		try {
			//client connects to server at port 8080
			socket = IO.socket("http://localhost:8080");
			socket.connect();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void configSocketEvents() {
		//when there is connection, log it to the console
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "Connected");
				player = new Starship(playerShip); /////////////////
			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "ID: " + id);
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error from getting ID");
				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "New Player Connected: " + id);
					friendlyPlayers.put(id, new Starship(friendlyShip)); //////////////
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error from getting New Player's ID");
				}
			}
		});
	}

	@Override
	public void dispose() {
		super.dispose();
		playerShip.dispose(); ////////////////
		friendlyShip.dispose(); //////////////////
	}
}
