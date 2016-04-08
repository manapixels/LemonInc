package com.lemoninc.nimbusrun.Networking;

/*********************************
 * FILENAME : Network.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       void    registerPackets(EndPoint endPoint)
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

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

    public static class Login {
        public String name;

        public Login() {

        }

        public Login(String name) {
            this.name = name;
        }
    }
<<<<<<< HEAD:NimbusRun/core/src/com/lemoninc/nimbusrun/Networking/Network.java
}
=======
}
>>>>>>> 2d55319255c217cea398b8e12d2634ec15197989:TapTap3/core/src/com/mygdx/taptap3/Networking/Network.java
