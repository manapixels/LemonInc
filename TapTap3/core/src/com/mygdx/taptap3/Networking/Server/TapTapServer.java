package com.mygdx.taptap3.Networking.Server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.taptap3.Networking.Network;
import com.mygdx.taptap3.Networking.Packet;

import java.io.IOException;

/**
 * Created by kevin on 4/4/2016.
 */
public class TapTapServer {
    //Connection info
    String IPConnection = "localhost";
    int ServerPort = 8080;

    //Kryonet Server object
    Server server;
    ServerNetworkListener snl;

    /**
     * Constructor starts a server on port 8080 and adds a listener to the server
     */
    public TapTapServer() {
        server = new Server();
        snl = new ServerNetworkListener();

        Network.registerPackets(server);
        server.addListener(snl);

        try {
            server.bind(ServerPort);

        } catch (IOException e) {
            e.printStackTrace();
        }


        server.start();
    }

    public void shutdown() {
        server.close();
        server.stop();
    }
}
