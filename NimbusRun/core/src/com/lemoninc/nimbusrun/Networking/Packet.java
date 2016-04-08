package com.lemoninc.nimbusrun.Networking;

/*********************************
 * FILENAME : Packet
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       none
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

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
