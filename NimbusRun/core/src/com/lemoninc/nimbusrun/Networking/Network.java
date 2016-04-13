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

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.minlog.Log;

public class Network {

    public static int PORT = 8080;
    public static int PORTUDP = 8082;
    public static float SPAWN_X = 32;
    public static float SPAWN_Y = 500;
    /**
     * the classes that are going to be sent over the network must be registered for both server and client
     *
     */
    public static void registerClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.register(Network.Login.class);
        kryo.register(Network.PlayerJoinLeave.class);
        kryo.register(Network.MovementState.class);
        kryo.register(Vector2.class);
    }

    static public class Login {
        public String name;
        public float initial_x;
        public float initial_y;

        public Login() {
        }

        public Login(String name) {
            this.name = name;
            this.initial_x = SPAWN_X;
            this.initial_y = SPAWN_Y;

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
        public float initial_x, initial_y;

        public PlayerJoinLeave() {}

        public PlayerJoinLeave(int playerId, String playerName, boolean joined, float initial_x, float initial_y) {
            this.playerId = playerId;
            name = playerName;
            hasJoined = joined;
            this.initial_x = initial_x;
            this.initial_y = initial_y;

            Network.logInfo("PlayerJoinLeave initialised by Server for Client "+playerId+" "+playerName);
        }
    }

    static public class MovementState {

        public int playerId;
        public Vector2 position;
        public Vector2 linearVelocity;

        public MovementState() {}

        public MovementState(int id, Vector2 position, Vector2 linearVelocity) {

            this.position = position;
            this.playerId = id;
            this.linearVelocity = linearVelocity;
        }
    }

    private static void logInfo(String string) {
        Log.info("[Network]: "+string);
    }
}