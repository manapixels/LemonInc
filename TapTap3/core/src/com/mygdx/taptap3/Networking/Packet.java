package com.mygdx.taptap3.Networking;

/**
 * Created by kevin on 4/4/2016.
 */
public class Packet {
    public static class Packet00Request {

    }

    public static class Packet01RequestAnswer {public boolean accepted; }
    /**
     * a static Packet02Message class that contains the clientName and the message
     */
    public static class Packet02Message {
        public String clientName;
        public String message;
        }

//    public static class Packet02AcceptClient {
//
//    }

}
