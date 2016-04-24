package com.lemoninc.nimbusrun.Networking.Server;

/*********************************
 * FILENAME : TapTapServer.java
 * DESCRIPTION :
 * FUNCTIONS :
    -- GET METHODS
 *      GameMap getMap
 *      String  getIP
 *
    -- NETWORK METHODS
 *      void    update
 *      void    initPlayers
 *      boolean allDummyReady
 *      void    shutdown

 *  class PlayerJoinFactory()

 * NOTES :
 * LAST UPDATED: 24/4/2016 15:20
 ********************************/

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.lemoninc.nimbusrun.Networking.Client.TapTapClient;
import com.lemoninc.nimbusrun.Networking.Network;
import com.lemoninc.nimbusrun.Sprites.GameMap;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TapTapServer {

    //Kryonet Server object
    Server server;

    private GameMap map;
    private List<Network.PlayerJoinLeave> players; //player's id, x and y position
    private Object playersLock = new Object();
    private int PLAYERS = 0;
    private final int MAXPLAYERS = 4;

    /**
     * Constructor starts a server on port 8080 and adds a listener to the server
     */
    public TapTapServer(int[] mapData) {
        server = new Server() {
            protected Connection newConnection() {
                //Provide our own implementation of connection so that we can refer
                // to each connection by the client's name in the Listener
                return new TapTapConnection();
            }
        };

        map = new GameMap(this, mapData);

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
                    Gdx.app.log("GDX Server", "Login received");
                    Network.PlayerJoinLeave newPlayer;

                    Network.Login msg = ((Network.Login) message);

                    if (connection.name != null) {
                        connection.close();
                    }
                    String name = msg.name;
                    if (name == null) {
                        connection.close();
                    }
                    name = name.trim();
                    if (name.length() == 0) { //if name contains no letters
                        connection.close();
                    }

                    //name this connection as the clientname
                    connection.name = name;

                    synchronized (playersLock) {
                        if (PLAYERS < MAXPLAYERS) {
                            PLAYERS++;
                            Gdx.app.log("GDX Server", "Added a player");
                        } else { //there is more than or equal to 4 players in the game room
                            Network.GameRoomFull roomfull = new Network.GameRoomFull();
                            connection.sendTCP(roomfull);
                            Gdx.app.log("GDX GDX Server", "Sent a GameRoomFull");
                            return;
                        }
                        Log.info("blingblang" + connection.getID());
                        //if the login is the first guy, send him 1st place
                        newPlayer = new PlayerJoinFactory().makePlayerJoin(connection.getID(), connection.name, PLAYERS);
                    }

                    //tell the new client about mapData
                    connection.sendTCP(map.getMapDataPacket());


                    if (newPlayer != null) {
                        //tell new client about his position
                        connection.sendTCP(newPlayer);
                        Gdx.app.log("GDX Server", "sent Player coordinates to new player");

                        //tell old clients about new client
                        server.sendToAllExceptTCP(connection.getID(), newPlayer);

                        //add this new player to gamemap
                        map.addPlayer(newPlayer); //server stores the new player
                        Gdx.app.log("GDX Server", "Stored new player in Server's map");
                    } else {
                        return;
                    }

                    //tell new client about old clients
                    for (Connection con : server.getConnections()) { //upon connection, every client's name is stored in Player
                        TapTapConnection conn = (TapTapConnection) con;
                        if (conn.getID() != connection.getID() && conn.name != null) { // Not self, Have logged in
                            GameMap.DummyPlayer herePlayer = map.getDummyById(conn.getID());
                            Network.PlayerJoinLeave hereMsg = new Network.PlayerJoinLeave(conn.getID(), herePlayer.playerName, true, herePlayer.x, herePlayer.y);
                            Gdx.app.log("GDX Server", "Telling " + connection.name + " about old client " + herePlayer.playerName);
                            connection.sendTCP(hereMsg); // basic info
                        }
                    }
                }
                else if(message instanceof Network.MovementState) {
                    Network.MovementState msg = (Network.MovementState)message;
                    Gdx.app.log("GDX TapTapServer MovementState", "MovementState received");
                    msg.playerId = connection.getID();
                    // Server updates its copy of player from what its told
                    map.playerMoved(msg);
                    server.sendToAllExceptTCP(connection.getID(), new Network.MovementState(msg.playerId, msg.position, msg.linearVelocity));
                }
                else if (message instanceof Network.Ready) {
                    Network.Ready msg = (Network.Ready) message;
                    msg.setPlayerId(connection.getID());
                    map.setCharacter(msg.playerId, msg.charactername);
                    server.sendToAllExceptTCP(connection.getID(), msg);
                    Gdx.app.log("GDX TapTapServer receivedReady", "Set character for Client "+connection.getID());
                }
                else if (message instanceof Network.GameReady) {
                    Network.GameReady msg = (Network.GameReady) message;
                    server.sendToAllExceptTCP(connection.getID(), msg);
                    Gdx.app.log("GDX Server", "Let the game begin");
                }
                else if (message instanceof Network.PlayerAttack) {
                    Gdx.app.log("GDX TapTapServer PlayerAttack", "PlayerAttack received");
                    Network.PlayerAttack msg = (Network.PlayerAttack) message;
                    //server checks if player can attack
                    if (map.onPlayerAttack(msg)) {
                        Gdx.app.log("GDX TapTapServer PlayerAttack", "Player can attack");
                        server.sendToAllExceptTCP(connection.getID(), msg);
                    }
                }
            }

            public void disconnected(Connection c) {
                TapTapConnection connection = (TapTapConnection) c;
                if (connection.name != null) { //Login was received from the connection
                    if (connection.name != "gameroomfull") {
                        // Announce to everyone that someone has left.
                        Network.PlayerJoinLeave reply = new Network.PlayerJoinLeave(connection.getID(), connection.name, false, 0f, 0f);
                        server.sendToAllExceptTCP(connection.getID(), reply);
                        map.removePlayer(reply); //if such player exists
                    }
                }

            }
        });

        try {
            server.bind(Network.PORT, Network.PORTUDP);

        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();


        Gdx.app.log("GDX Server", "Server instantiated");

    }

    public void update(float delta) {
        map.update(delta);
    }

    public void initPlayers() {
        map.initPlayers();
    }

    public boolean allDummyReady() {
        return map.allDummyReady();
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
        Network.PlayerJoinLeave makePlayerJoin(int ID, String playerName, int players) {
            Log.info("brangbrang" + playerName);
            Network.PlayerJoinLeave msg;
            switch (players) {
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



