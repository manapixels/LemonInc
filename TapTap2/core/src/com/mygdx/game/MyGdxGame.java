package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.PlayScreen;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MyGdxGame extends Game {
	public static final int V_HEIGHT = 480;
	public static final int V_WIDTH = 800;
	public static final float PPM = 100f;
	public SpriteBatch batch;
	private Socket socket;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
		connectSocket();
		configSocketEvents();
	}

	@Override
	public void render () {
		super.render();
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
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error from getting New Player's ID");
				}
			}
		});
	}
}
