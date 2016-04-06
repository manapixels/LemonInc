package com.mygdx.taptap3.Networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * Created by kevin on 4/4/2016.
 */
public class Network {

    public static int PORT = 8080;
    public static int PORTUDP = 8082;
    /**
     * the classes that are going to be sent over the network must be registered for both server and client
     *
     */
    public static void registerPackets(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.register(Packet.Packet00Request.class);
        kryo.register(Packet.Packet01RequestAnswer.class);
        kryo.register(Packet.Packet02Message.class);
    }
}
