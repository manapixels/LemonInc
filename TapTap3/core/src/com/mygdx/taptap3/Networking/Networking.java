package com.mygdx.taptap3.Networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.taptap3.Sprites.Player;
import com.mygdx.taptap3.Sprites.Starship;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by kevin on 3/25/2016.
 */

//TODO: Starship should be replaced by the actual game characters

public class Networking {
    private Socket socket;
    //stores other players' id and starship sprite
//    public HashMap<String, Player> friendlyPlayers;
    public HashMap<String, Starship> friendlyPlayers;

    public Starship coopPlayer;
    public Texture playerShip;
    public Texture friendlyShip;
    public Starship player;

    private final float UPDATE_TIME = 1/60f;
    float timer;

    public Networking() {
        playerShip = new Texture("playerShip2.png");
        friendlyShip = new Texture("playerShip.png");
//        friendlyPlayers = new HashMap<String, Player>();
        friendlyPlayers = new HashMap<String, Starship>();
    }

    public void connectToServer() {
        try {
            //client connects to server at port 8080
            socket = IO.socket("http://localhost:8080");
            socket.connect();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void configSocketEvents() {

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            //connection event
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected");
                //create player starship
//                player = new Player(playerShip);
                player = new Starship(playerShip);
                System.out.println("player sprite created");
            }
            //receive client ID from server
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
            //new player has joined
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    Gdx.app.log("SocketIO", "New Player Connected: " + playerId);
                    //make other players' ships
                    friendlyPlayers.put(playerId, new Starship(friendlyShip));
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error from getting New Player's ID");
                }
            }
        //other player has disconnected
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "this player disconnected: " + id);
                    friendlyPlayers.remove(id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error from getting Disconnected Player's ID");
                }
            }
        //
        }).on("getPlayers", new Emitter.Listener() {
            //called when player first joins the game to update the location of all other players
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0]; //first element that gets sent
                try {
                    for (int i = 0;i<objects.length();i++) {
                        coopPlayer = new Starship(friendlyShip);
                        Vector2 position = new Vector2();
                        position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                        position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                        coopPlayer.setPosition(position.x, position.y);

                        friendlyPlayers.put(objects.getJSONObject(i).getString("id"), coopPlayer);
                    }
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error from getting New Player's ID");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerID = data.getString("id");
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    if (friendlyPlayers.get(playerID) != null) {
                        friendlyPlayers.get(playerID).setPosition(x.floatValue(), y.floatValue());

                    }
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error from getting Disconnected Player's ID");
                }
            }
            //
        });
    }

    //update server of client's new player position
    public void updateServer(float dt) {
        timer += dt;
        if (timer >= UPDATE_TIME && player != null && player.hasMoved()) {
            JSONObject data = new JSONObject();
            try {
                data.put("x", player.getX());
                data.put("y", player.getY());
                socket.emit("playerMoved", data);
            } catch (JSONException e) {
                Gdx.app.log("Socket.IO", "Error sending update data");
            }
        }
    }

    public Starship getPlayerStarship() {
        return player;
    }

    public Starship getCoopPlayerStarship() {
        return coopPlayer;
    }

    public void dispose() {
        playerShip.dispose();
        friendlyShip.dispose();
    }
}