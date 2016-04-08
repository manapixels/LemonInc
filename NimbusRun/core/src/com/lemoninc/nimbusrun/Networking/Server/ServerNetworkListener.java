package com.lemoninc.nimbusrun.Networking.Server;

/*********************************
 * FILENAME : ServerNetworkListener.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       void    connected(Connection c)
 *       void    disconnected(Connection c)
 *       void    received(Connection c, Object o)
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lemoninc.nimbusrun.Networking.Packet;

public class ServerNetworkListener extends Listener {

    public ServerNetworkListener() {

    }

    public void connected(Connection c) {
        System.out.println("Someone has connected");

    }

    public void disconnected(Connection c) {
        System.out.println("Someone has disconnected");

    }

    //when receive, object o is what is being sent, if we receive packet01message,

    /**
     * This method listens for any packet received from any client.
     *
     * If the packet is a Packet02Message, it prints out the client's name and the message
     *
     * @param c The connection between the server and the client
     * @param o The packet sent by the client
     */
    public void received(Connection c, Object o) {
        //if server receives Packet00 from client,
        //set Packet01.accepted = true and send Packet01 to client.
        if (o instanceof Packet.Packet00Request) {
            System.out.println("received a request packet");
            Packet.Packet01RequestAnswer answer = new Packet.Packet01RequestAnswer();

            answer.accepted = true;

            c.sendTCP(answer);
            System.out.println("setn the answer packet ");
        }
        if (o instanceof Packet.Packet02Message) {
            Packet.Packet02Message p = (Packet.Packet02Message) o;

            System.out.println("[" + p.clientName + "]: " + p.message);
        }
    }
}
