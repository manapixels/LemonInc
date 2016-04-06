package com.mygdx.taptap3.Networking.Client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import com.mygdx.taptap3.Networking.Network;
import com.mygdx.taptap3.Networking.Packet;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by kevin on 4/4/2016.
 */
public class TapTapClient {

    private String name; //Player's name

    //Coonnection info
    int portSocket = 8080;
    String ipAddress = "localhost";
    static Scanner scanner;

    //Kryonet Stuff
    public Client client;
    private ClientNetworkListener cnl;

    /**
     * This constructor is called in PlayScreen when the player plays game as a client
     */
    public TapTapClient(String name) {
        //this.gamemap = new gamemap(this);
        this.name = name;

        client = new Client();
        client.start();

        cnl = new ClientNetworkListener();
        scanner = new Scanner(System.in);

        cnl.init(client);
        Network.registerPackets(client); //register the classes Client uses with Server
        client.addListener(cnl);



    }

    public void connect(String host) throws IOException{
            client.connect(5000, host, Network.PORT, Network.PORTUDP);

    }

//    public static void main(String[] args) {
//        new TapTapClient();
////    }
    private void logInfo(String string) {
        Log.info(string);
    }

    public void shutdown() {
        client.stop();
        client.close();
    }
}
