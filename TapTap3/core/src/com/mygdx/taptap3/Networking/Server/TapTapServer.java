package com.mygdx.taptap3.Networking.Server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.taptap3.Networking.Network;
import com.mygdx.taptap3.Networking.Packet;
import com.mygdx.taptap3.Sprites.GameMap;

import java.io.IOException;

/**
 * Created by kevin on 4/4/2016.
 */
public class TapTapServer {
    //Connection info
    String IPConnection = "localhost";
//    int ServerPort = 8080;

    //Kryonet Server object
    Server server;
    ServerNetworkListener snl;

    private GameMap map;

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

        Network.registerPackets(server);

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

//                if (message instanceof Login) {
////                    Login msg = ((Login) message);
//
//                    //tell the new client about map state (obstacle coordinates ...)
//                    //tell old clients about new client
//
//                    //tell new client about old clients
//                }
            }

            public void disconnected(Connection c) {
                TapTapConnection connection = (TapTapConnection) c;
                //announce to everyone that someone got disconnected
            }
        });

        //TODO: propagate exception or try-catch?
        try {
            server.bind(Network.PORT, Network.PORTUDP);

        } catch (IOException e) {
            e.printStackTrace();
        }


        server.start();
    }

    public void update(float delta) {
        map.update(delta);
    }

    public void shutdown() {
        server.close();
        server.stop();
    }

    static class TapTapConnection extends Connection {
        public String name;
    }
}


