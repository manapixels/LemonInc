package com.lemoninc.nimbusrun.Sprites;

/*********************************
 * FILENAME : GameMap.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       public synchronized void addPlayer(Network.PlayerJoinLeave msg)
 *       private void initCommon()
 *       private void handleInput()
 *       void    update(float delta)
 *       public synchronized void logInfo(String string)
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.esotericsoftware.minlog.Log;
import com.lemoninc.nimbusrun.Networking.Client.TapTapClient;
import com.lemoninc.nimbusrun.Networking.Network;
import com.lemoninc.nimbusrun.Networking.Server.TapTapServer;

import java.util.HashMap;
import java.util.Map;

public class GameMap {

    private TapTapClient client; // only if I'm the client
    private TapTapServer server; // only if I'm internal to the server

    private boolean isClient;

    private Map<Integer, Player> players = new HashMap<Integer, Player>(); //playerId, Player


    /**
     * This constructor is called inside TapTapClient
     */
    public GameMap(TapTapClient client) {

        this.client = client;
        this.isClient = true;

        initCommon();

        //instantiate HUD, GameSounds, BitmapFont, Camera, SpriteBatch ...
        logInfo("GameMap initialised");
    }

    /**
     * This constructor is called inside TapTapServer
     */
    public GameMap(TapTapServer server) {
        this.server = server;
        this.isClient = false;

        initCommon();

        logInfo("GameMap initialised");

    }

    //called by client/server to add a player into its GameMap
    public synchronized void addPlayer(Network.PlayerJoinLeave msg) {
        logInfo("Player added to players!");
        //create new player from msg
        //newPlayer.setID(msg.playerId);
        //newPlayer.setName(msg.name);
        //players.put(msg.playerId, newPlayer);

    }

    private void initCommon(){
        // Load up all sprites into spriteMap from textureAtlas
    }

    private void handleInput() {

    }

    public void update(float delta) {

    }

    public synchronized void logInfo(String string) {
        Log.info((isClient ? "[Client] " : "[Server] ") + string);
    }


}
