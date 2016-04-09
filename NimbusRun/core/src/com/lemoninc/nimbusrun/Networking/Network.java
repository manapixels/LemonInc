package com.lemoninc.nimbusrun.Networking;

/*********************************
 * FILENAME : Network.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       void    registerClasses(EndPoint endPoint)
 *       private static void logInfo(String string)
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.minlog.Log;

public class Network {

    public static int PORT = 8080;
    public static int PORTUDP = 8082;
    /**
     * the classes that are going to be sent over the network must be registered for both server and client
     *
     */
    public static void registerClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.register(Network.Login.class);
        kryo.register(Network.PlayerJoinLeave.class);
    }

    static public class Login {
        public String name;

        public Login() {

        }

        public Login(String name) {
            this.name = name;

            Network.logInfo("Login initialised by Client "+name);
        }
    }

    /**
     * message packet that describes the player to be added to the GameMap.
     * the 'joined' boolean variable indicates if the player is to be added to or deleted from the GameMap
     *
     */
    static public class PlayerJoinLeave {
        public int playerId;
        public String name;
        public boolean hasJoined;


        public PlayerJoinLeave(int playerId, String playerName, boolean joined) {
            this.playerId = playerId;
            name = playerName;
            hasJoined = joined;

            Network.logInfo("PlayerJoinLeave initialised by Server for Client "+playerId+" "+playerName);
        }
    }

    private static void logInfo(String string) {
        Log.info(string);
    }
}