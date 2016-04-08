package com.lemoninc.nimbusrun.Networking.Client;

/*********************************
 * FILENAME : ClientNetworkListener.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       void    connected(Connection c)
 *       void    disconnected(Connection c)
 *       void    received(Connection c, Object o)
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lemoninc.nimbusrun.Networking.Packet;

public class ClientNetworkListener extends Listener {
    private Client client;

    public ClientNetworkListener(Client client) {
        this.client = client;
    }

    /**
     * This method listens for the connection between the client and the server.
     * When the connection is established, the client sends the object firstMessage, which contains a clientName and a message
     * @param c Connection between the client and the server
     */
    public void connected(Connection c) {
        System.out.println("Client connected!");

        //send a request packet to server when client and server are connected
        client.sendTCP(new Packet.Packet00Request());
        System.out.println("Sent a request packet");
    }

    public void disconnected(Connection c) {
        System.out.println("Client disconnected!");
    }

    /**
     * This method listens for any received packets from the server.
     *
     * If the received packet is a Packet01Mesage, the client prints it out
     * @param c Connection between server and client
     * @param o The packet sent from the server
     */
    public void received(Connection c, Object o) {

        if (o instanceof Packet.Packet01RequestAnswer) {
            System.out.println("received answer packet from server");
            boolean serverAnswer = ((Packet.Packet01RequestAnswer) o).accepted;

            System.out.println("I received: " +serverAnswer);
            if (serverAnswer) {
                System.out.println("Server accepted my connection!");
                while (true) {
                    if (TapTapClient.scanner.hasNext()) { //if there is text in our command line
                        Packet.Packet02Message messagePacket = new Packet.Packet02Message();
                        messagePacket.clientName = "KevClient";
                        messagePacket.message = TapTapClient.scanner.nextLine();
                        c.sendTCP(messagePacket);
                    }
                }
            } else {
                c.close();
                //c.lookForOtherServer()
            }
        }

        if (o instanceof Packet.Packet02Message) {
            Packet.Packet02Message p = (Packet.Packet02Message) o;

            System.out.println("Server >> " + p.message);
        }
    }

}
