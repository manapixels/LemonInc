package com.mygdx.taptap3.Networking.Client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.taptap3.Networking.Network;
import com.mygdx.taptap3.Networking.Packet;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by kevin on 4/4/2016.
 */
public class TapTapClient {
    //Coonnection info
    int portSocket = 8080;
    String ipAddress = "localhost";
    static Scanner scanner;

    //Kryonet Stuff
    public Client client;
    private ClientNetworkListener cnl;

    public TapTapClient() {
        client = new Client();
        cnl = new ClientNetworkListener();
        scanner = new Scanner(System.in);

        cnl.init(client);
        Network.registerPackets(client);
        client.addListener(cnl);

//        client.start();
        new Thread(client).start();

        try {
            //client connects with server
            client.connect(9999, ipAddress, portSocket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new TapTapClient();
    }
}
