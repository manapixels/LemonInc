package com.mygdx.taptap3.Sprites;

import com.mygdx.taptap3.Networking.Client.TapTapClient;
import com.mygdx.taptap3.Networking.Server.TapTapServer;

/**
 * Created by kevin on 4/6/2016.
 */
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
