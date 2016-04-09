package com.lemoninc.nimbusrun.Networking.Client;

/*********************************
 * FILENAME : TapTapClient.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       void    connect(String host)
 *       void    shutdown()
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.lemoninc.nimbusrun.Networking.Network;
import com.lemoninc.nimbusrun.Sprites.GameMap;

import java.io.IOException;
import java.util.Scanner;

public class TapTapClient {

    private String name; //Player's name
    public int id; //Player's connection ID
    public String remoteIP;
    private GameMap map;

    //Coonnection info
    int portSocket = 8080;
    String ipAddress = "localhost";
    static Scanner scanner;

    //Kryonet Stuff
    public Client client;

    /**
     * This constructor is called in PlayScreen when the player plays game as a client
     */
    public TapTapClient(String name) {
        map = new GameMap(this); //create new GameMap for Client
        this.name = name;

        client = new Client();
        client.start();

        Network.registerClasses(client); //register the classes Client uses with Server
        client.addListener(new Listener() {//add listener for the client
            public void connected(Connection connection) {
                handleConnect(connection);
            }
            public void received(Connection connection, Object object) {
                handleMessage(connection.getID(), object);
            }

            public void disconnected(Connection connection) {
                handleDisonnect(connection);
            }
        });
    }

    public GameMap getMap() {
        return this.map;
    }

    /**
     * TODO:what happens here?
     * This method is called when the client establishes connection with server.
     * Method gets connection ID, remote IP from server,
     * @param connection
     */
    private void handleConnect(Connection connection) {
        id = connection.getID();
        remoteIP = connection.getRemoteAddressTCP().toString(); //Returns the IP address and port of the remote end of the TCP connection, or null if this connection is not connected.

        //send Login to server
        Network.Login clientName = new Network.Login(name);
        client.sendTCP(clientName);
    }

    /**
     * This method listens for any received packets from the server.
     * This method handles messages from other players about their activities.
     *
     * Possible messages:
     * playerjoinleave
     * movementstate
     * gamemapdata
     * roundend
     * roundstart
     *
     * @param playerID
     * @param message
     */
    private void handleMessage(int playerID, Object message) {
//        if (message instanceof PlayerJoinLeave) {
//            PlayerJoinLeave msg = (PlayerJoinLeave) message;
//            if (msg.hasJoined) {
//                map.setStatus(msg.name + " joined");
//                map.addPlayer(msg);
//            } else {
//                map.setStatus(msg.name + " left");
//                map.removePlayer(msg);
//            }
//        }

    }

    private void handleDisonnect(Connection connection) {
//        map.onDisconnect();
    }

    public void connect(String host) throws IOException{
        client.connect(5000, host, Network.PORT, Network.PORTUDP);
        logInfo("Client " + name + " of ID: " + id + " connected to server PORT " + Network.PORT);
    }

    public void shutdown() {
        client.stop();
        client.close();
    }

    private void logInfo(String string) {
        Log.info(string);
    }
}
