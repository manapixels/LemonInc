package com.lemoninc.nimbusrun.Networking.Client;

/*********************************
 * FILENAME : TapTapClient.java
 * DESCRIPTION :
 * FUNCTIONS :
    -- GET METHODS
 *      GameMap getMap
 *      String  getIP
 *
    -- NETWORK METHODS
 *      void    handleConnect
 *      void    handleMessage
 *      void    handleDisonnect
 *      void    connect
 *      boolean connectLAN
 *      void    sendMessage
 *      void    sendMessageUDP
 *      void    shutdown
 *
 * NOTES :
 * LAST UPDATED: 24/4/2016 15:13
 *
 * ********************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lemoninc.nimbusrun.Networking.Network;
import com.lemoninc.nimbusrun.NimbusRun;
import com.lemoninc.nimbusrun.Screens.CharacterSelectionScreen;
import com.lemoninc.nimbusrun.Screens.MenuScreen;
import com.lemoninc.nimbusrun.Screens.WaitScreen;
import com.lemoninc.nimbusrun.Sprites.GameMap;

import java.io.IOException;
import java.net.InetAddress;

public class TapTapClient {
    private NimbusRun game;

    private String name; //Player's name
    public int id; //Player's connection ID
    public String remoteIP;
    private GameMap map;
    private CharacterSelectionScreen currentScreen;
    private MenuScreen menuScreen;

    //Kryonet Stuff
    public Client client;


    /**
     * This constructor is called in CharacterSelectionScreen when the player is connecting to server
     */
    public TapTapClient(NimbusRun game, CharacterSelectionScreen screen, String name, int[] mapData) {
        this.game = game;
        map = new GameMap(this, mapData); //create new GameMap for Client
        Gdx.app.log("GDX TapTapClient", "Client created GameMap");

        this.name = name;

        client = new Client();
        client.start();

        Network.registerClasses(client); //register the classes Client uses with Server

        client.addListener(new Listener() {//add listener for the client
            public void connected(Connection connection) {
                Gdx.app.log("GDX TapTapClient", "Connected to server");
                handleConnect(connection);
            }

            public void received(Connection connection, Object object) {
                Gdx.app.log("GDX TapTapClient", "Received message");
                handleMessage(connection, connection.getID(), object);
            }

            public void disconnected(Connection connection) {
                handleDisonnect(connection);
            }
        });

        currentScreen = screen;

        Gdx.app.log("GDX Client", "Client instantiated");
    }

    public GameMap getMap() {
        return this.map;
    }

    public String getIP() {return remoteIP;}

    /**
     *
     * This method is called when the client establishes connection with server.
     * Method gets connection ID between this cleint and server, remote IP from server.
     * Method sends a Login package containing its name to server.
     * @param connection
     */
    private void handleConnect(Connection connection) {
        id = connection.getID(); //connection id between client and server
        remoteIP = connection.getRemoteAddressTCP().toString(); //Returns the IP address and port of the remote end of the TCP connection, or null if this connection is not connected.

        //send Login to server
        Network.Login clientName = new Network.Login(name);
        client.sendTCP(clientName);
        Gdx.app.log("GDX Client", "Connection handled, sent Login");
    }

    /**
     * This method listens for any received packets from the server.
     * This method handles messages from other players about their activities.
     *
     * @param playerID
     * @param message
     */
    private void handleMessage(Connection connection, int playerID, Object message) {
        if (message instanceof Network.PlayerJoinLeave) {
            Gdx.app.log("GDX TapTapClient", "Client received PlayerJoinLeave");
            Network.PlayerJoinLeave msg = (Network.PlayerJoinLeave) message;
            if (msg.hasJoined) {
                if (msg.playerId == connection.getID()) {
                    Gdx.app.log("GDX Client", "onConnect called");
                    map.onConnect(msg);
                }
                else { //PlayerJoinLeave sent from another client
                    Gdx.app.log("GDX Client", "add player called");
                    map.addPlayer(msg);
                }
            } else {
                map.removePlayer(msg);
            }
        }
        else if (message instanceof Network.MapDataPacket) {
            Gdx.app.log("GDX TapTapClient", "Client received MapDataPacket");
            Network.MapDataPacket msg = (Network.MapDataPacket) message;

            //set its gamemap mapdata accordingly
            map.setMapData(msg.mapData);
        } else if (message instanceof Network.MovementState) {
            Gdx.app.log("GDX TapTapClient", "Client received MovementState");

            Network.MovementState msg = (Network.MovementState) message;

            map.playerMoved(msg);
            
        } else if (message instanceof Network.GameRoomFull) {
            Gdx.app.log("GDX TapTapClient", "Client received GameRoomFull");
            connection.setName("gameroomfull");

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new WaitScreen(game, currentScreen.batch, menuScreen.playmusic)); //CSS.hide() called, TODO: client should be closed
                }
            });

        } else if (message instanceof Network.Ready) {
            Gdx.app.log("GDX TapTapClient", "Client received Ready");
            Network.Ready msg = (Network.Ready) message;
            map.setCharacter(msg.playerId, msg.charactername);
        } else if (message instanceof Network.GameReady) {
            Gdx.app.log("GDX TapTapClient", "Client received GameReady");
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    currentScreen.playGame();
                }
            });
        }
        else if (message instanceof Network.PlayerAttack) {
            Gdx.app.log("GDX TapTapClient", "Client received PlayerAttack");
            Network.PlayerAttack msg = (Network.PlayerAttack) message;
            map.onPlayerAttack(msg);
        }
    }

    private void handleDisonnect(Connection connection) {
        Gdx.app.log("GDX TapTapClient", "Client disconnected from server");
        map.onDisconnect();
    }

    public void connect(String host) throws IOException{
        client.connect(5000, host, Network.PORT, Network.PORTUDP);
    }

    public boolean connectLAN() throws IOException{
        InetAddress address = client.discoverHost(Network.PORTUDP, 5000);

        if (address != null) {
            client.connect(1000, address, Network.PORT, Network.PORTUDP);
            Gdx.app.log("GDX TapTapClient", "Connected to LAN successfully!");
            return true;
        }
        return false;
    }

    public void sendMessage(Object message) {
        if (client.isConnected()) {
            client.sendTCP(message);
        }
    }

    public void sendMessageUDP(Object message) {
        if (client.isConnected()) {
            client.sendUDP(message);
            Gdx.app.log("GDX TapTapClient", "Sent packet UDP");
        }
    }

    public void shutdown() {
        client.stop();
        client.close();
    }
}
