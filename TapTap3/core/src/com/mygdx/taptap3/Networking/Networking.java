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

public class Networking {
    private Socket socket;
    public HashMap<String, Player> friendlyPlayers;

    public String clientID;
    public Player coopPlayer;

    private final float UPDATE_TIME = 1/60f;
    private float timer;

    public Networking() {
        //create an empty clientID; clientID is issued by the server upon connection
        clientID = "empty";
        //stores other players' id and empty Player objects
        friendlyPlayers = new HashMap<String, Player>();
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
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected");
            }
            //receives client ID from server when connection is established
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    clientID = data.getString("id");
                    Gdx.app.log("SocketIO", "ID: " + clientID);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error from getting ID");
                }
            }
            /**
             * when a new player joins the game in the WaitScreen, the client receives the new player's clientID.
             * An empty Player object is created to represent the new player and the key-value pair is stored in the friendlyPlayers HashMap
             */
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    Gdx.app.log("SocketIO", "New Player Connected: " + playerId);
                    friendlyPlayers.put(playerId, new Player());
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error from getting New Player's ID");
                }
            }
            /**
             *When a player disconnects during any point of the game, the client receives the disconnected player's client ID.
             * The client removes the key-value pair from the friendlyPlayers HashMap
             */
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
            /**
             * When the client first connects to the server, the client receives the client IDs of all the players connected to the server.
             * Client stores the client IDs into the friendlyPlayers HashMap along with empty Player objects positioned at their current positions
             */
        //TODO: current position necessary?
        }).on("getPlayers", new Emitter.Listener() {
            //called when player first joins the game to update the location of all other players
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0]; //receive players array
                try {
                    for (int i = 0; i < objects.length(); i++) {
                        coopPlayer = new Player();
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
            /**
             * The client receives the updated coordinates of the players moved during the gameplay.
             * The client sets the position of the box2d representing the respective players according to the coordinates received in the JSON object.
             */
        //TODO: is there a better way?
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerID = data.getString("id");
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    if (friendlyPlayers.get(playerID) != null) {
                        friendlyPlayers.get(playerID).setBodyPosition(x.floatValue(), y.floatValue());

                    }
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error from getting Disconnected Player's ID");
                }
            }
        });
    }

    /**
     * This method emits a "playerMoved" events to the server.
     * It sends a JSON object to the server when the player moves in PlayScreen.
     * The JSON object contains the x y coordinates of the box2d of the player's Player object.
     * @param dt
     * @param player This is the Player object representing the client
     */
    public void updateServer(float dt, Player player) {
        timer += dt;
        if (timer >= UPDATE_TIME && player != null && player.hasMoved()) {
            JSONObject data = new JSONObject();
            try {
                data.put("x", player.b2body.getPosition().x); //Player.java
                data.put("y", player.b2body.getPosition().y);
                socket.emit("playerMoved", data);
            } catch (JSONException e) {
                Gdx.app.log("Socket.IO", "Error sending update data");
            }
        }
    }


    public void dispose() {
    }
}