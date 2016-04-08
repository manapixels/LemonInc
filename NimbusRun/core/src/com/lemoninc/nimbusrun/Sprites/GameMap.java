package com.lemoninc.nimbusrun.Sprites;

/*********************************
 * FILENAME : GameMap.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       void    update(float delta)
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.lemoninc.nimbusrun.Networking.Client.TapTapClient;
import com.lemoninc.nimbusrun.Networking.Server.TapTapServer;

public class GameMap {

    private TapTapClient client; // only if I'm the client
    private TapTapServer server; // only if I'm internal to the server

    private boolean isClient;

    /**
     * This constructor is called inside TapTapClient
     */
    public GameMap(TapTapClient client) {
        this.client = client;
        this.isClient = true;

        initCommon();

        //instantiate HUD, GameSounds, BitmapFont, Camera, SpriteBatch ...
    }

    /**
     * This constructor is called inside TapTapServer
     */
    public GameMap(TapTapServer server) {
        this.server = server;
        this.isClient = false;

        initCommon();
    }

    private void initCommon(){
        // Load up all sprites into spriteMap from textureAtlas
    }

    private void handleInput() {

    }

    public void update(float delta) {

    }


}
