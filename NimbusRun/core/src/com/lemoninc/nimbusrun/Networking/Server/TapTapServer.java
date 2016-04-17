package com.lemoninc.nimbusrun.Networking.Server;

/*********************************
 * FILENAME : TapTapServer.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 * void    update(float delta)
 * void    shutdown()
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 ********************************/

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.lemoninc.nimbusrun.Networking.Network;
import com.lemoninc.nimbusrun.Sprites.GameMap;
import com.lemoninc.nimbusrun.Sprites.Player;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Need GameMap?
 */
public class TapTapServer {

    //Kryonet Server object
    Server server;

    private GameMap map;
    private List<Network.PlayerJoinLeave> players; //player's id, x and y position
    private AtomicInteger PLAYERS = new AtomicInteger(0); //number of players connected to server
    private final int MAXPLAYERS = 4;


    /**
     * Constructor starts a server on port 8080 and adds a listener to the server
     */
    public TapTapServer() {
        server = new Server() {
            protected Connection newConnection() {
                //Provide our own implementation of connection so that we can refer
                // to each connection by the client's name in the Listener
                return new TapTapConnection();
            }
        };

        map = new GameMap(this);

        Network.registerClasses(server);


/**
 * server listens for messages from the clients.
 *
 * Message could be about map state:
 * obstacle coordinates
 * player joined/left
 * etc
 *
 */
        server.addListener(new Listener() {
            public void received(Connection c, Object message) {
                TapTapConnection connection = (TapTapConnection) c;

                if (message instanceof Network.Login) {
                    Gdx.app.log("Server", "Login received");

                    Network.Login msg = ((Network.Login) message);

                    if (PLAYERS.get() < MAXPLAYERS) {
                        PLAYERS.getAndAdd(1);
                        Gdx.app.log("Server", "Added a player");

                    } else { //there is more than or equal to 4 players in the game room
                        Network.GameRoomFull roomfull = new Network.GameRoomFull();
                        connection.sendTCP(roomfull);
                        Gdx.app.log("Server", "Sent a GameRoomFull");

                        return;
                    }
//                    Gdx.app.log("Server", "reached here");
                    if (connection.name != null) {
                        return;
                    }

                    String name = msg.name;
                    if (name == null) {
                        return;
                    }
                    name = name.trim();
                    if (name.length() == 0) {
                        return;
                    }//if name contains no letters
                    //name this connection as the clientname
                    connection.name = name;

                    //tell the new client about map state (obstacle coordinates ...)

                    //if the login is the first guy, send him 1st place

                    Network.PlayerJoinLeave newPlayer = new PlayerJoinFactory().makePlayerJoin(connection.getID(), connection.name, PLAYERS);

                    //tell new client about his position
                    connection.sendTCP(newPlayer);
                    Gdx.app.log("Server", "sent Player coordinates to new player");

                    //tell old clients about new client
                    server.sendToAllExceptTCP(connection.getID(), newPlayer);

                    //add this new player to gamemap
                    map.addPlayer(newPlayer); //server stores the new player
                    Gdx.app.log("Server", "Stored new player in Server's map");


                    //tell new client about old clients
                    for (Connection con : server.getConnections()) { //upon connection, every client's name is stored in Player
                        TapTapConnection conn = (TapTapConnection) con;
                        if (conn.getID() != connection.getID() && conn.name != null) { // Not self, Have logged in
                            Player herePlayer = map.getPlayerById(conn.getID());
                            Network.PlayerJoinLeave hereMsg = new Network.PlayerJoinLeave(conn.getID(), herePlayer.getName(), true, herePlayer.getX(), herePlayer.getY()); //TODO: server's gamemap needs to be updated too
                            Gdx.app.log("Server", "Telling " + connection.name + " about old client " + herePlayer.getName());
                            connection.sendTCP(hereMsg); // basic info
                            connection.sendTCP(herePlayer.getMovementState()); // info about current movement
                        }
                    }
                }
                else if(message instanceof Network.MovementState) {
                    Network.MovementState msg = (Network.MovementState)message;
                    logInfo("MovementState received");
                    msg.playerId = connection.getID();
                    // TODO Server updates its copy of player from what its told
                    map.playerMoved(msg);
//					"SERVER "+msg.playerId+" moved"
                    server.sendToAllExceptUDP(connection.getID(), msg);
                }
            }

            //TODO: what happens here when a player is rejected cos game room is full?
            public void disconnected(Connection c) {
                TapTapConnection connection = (TapTapConnection) c;
                if (connection.name != null) {
                    if (connection.name != "gameroomfull") {
                        // Announce to everyone that someone has left.
                        Network.PlayerJoinLeave reply = new Network.PlayerJoinLeave(connection.getID(), connection.name, false, 0f, 0f);
                        server.sendToAllExceptTCP(connection.getID(), reply);
                        map.removePlayer(reply); //if such player exists
                    }
//                    Gdx.app.log("Server", "testing");
                }

            }
        });

        try {
            server.bind(Network.PORT, Network.PORTUDP);

        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();


        Gdx.app.log("Server", "Server instantiated");

    }

    public void update(float delta) {
        map.update(delta); //TODO:make sure server's map.update doesn't contain rendering
    }

    public void shutdown() {
        server.close();
        server.stop();
    }

    static class TapTapConnection extends Connection {
        public String name;
    }

    private void logInfo(String string) {
//        Log.info("[TapTapServer]: " + string);
    }

    private class PlayerJoinFactory{
        Network.PlayerJoinLeave makePlayerJoin(int ID, String playerName, AtomicInteger players) {
            Network.PlayerJoinLeave msg;
            switch (players.get()) {
                case 1:
                    msg = new Network.PlayerJoinLeave(ID, playerName, true, 70, 200);
                    break;
                case 2:
                    msg = new Network.PlayerJoinLeave(ID, playerName, true, 50, 200);
                    break;
                case 3:
                    msg = new Network.PlayerJoinLeave(ID, playerName, true, 30, 200);
                    break;
                case 4:
                    msg = new Network.PlayerJoinLeave(ID, playerName, true, 10, 200);
                    break;
                default:
                    msg = new Network.PlayerJoinLeave(ID, playerName, true, 10, 200);
                    break;

            }
            return msg;
        }
    }
}



