package com.lemoninc.nimbusrun.Screens;

/*********************************
 * FILENAME : PlayScreen.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       void     show()
 *       void     handleInput()
 *       void     render(float delta)
 *       void     gameOver()
 *       void     resize(int width, int height)
 *       void     hide()
 *       void     resume()
 *       void     pause()
 *       void     dispose()
 * NOTES :
 * LAST UPDATED: 9/4/2016 14:00
 *
 * ********************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.esotericsoftware.minlog.Log;
import com.lemoninc.nimbusrun.Networking.Client.TapTapClient;
//import com.lemoninc.nimbusrun.Networking.Networking;
import com.lemoninc.nimbusrun.Networking.Server.TapTapServer;
import com.lemoninc.nimbusrun.Sprites.GameMap;
import com.lemoninc.nimbusrun.NimbusRun;

import java.io.IOException;

public class PlayScreen implements Screen {

    private NimbusRun game;

    private GameMap gamemap;

    private final boolean isHost;
    private final String ipAddress;
    private String playerName;

    private TapTapClient client;
    private TapTapServer server;

    /**
     *
     * @param game
     * @param isHost
     * @param ipAddress Server's IP address (only relevant to the Client)
     * @param playerName
     */
    public PlayScreen(NimbusRun game, boolean isHost, String ipAddress, String playerName){
        logInfo("My name is "+playerName);

        this.game = game;
        this.isHost = isHost;
        if (!ipAddress.isEmpty()) {
            this.ipAddress = ipAddress;
        } else {
            this.ipAddress = "localhost";
        }
        this.playerName = playerName;
    }

    /**
     * Called when this screen becomes the current screen for a Game.
     * when the screen appears, start TapTapClient, TODO:get the map from the client.
     * If player is a host, start TapTapServer, connect itself to the server.
     * If player is joining game, connect client to the ip address.
     */
    @Override
    public void show() {
        client = new TapTapClient(playerName);
        logInfo("Client created!");
        gamemap = client.getMap();

        if (isHost) {
            //start my server and connect my client to my server
            logInfo("Starting server...");
            try {
                server = new TapTapServer();
                logInfo("localClient connecting to Server");
                client.connect("localhost");
            } catch (IOException e) {
                e.printStackTrace();
                logInfo("Can't connect to localhost server");
                game.setScreen(new WaitScreen(game));
            }
        }
        else {
            //client connects to ipAddress
            try {
                client.connect(ipAddress);
            } catch (IOException e) {
                logInfo("Can't connect to server: " + ipAddress);
                game.setScreen(new WaitScreen(game));
            }
        }

    }

    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            gameOver();
        }
    }

    @Override
    public void render(float delta) {
//        logInfo("Rendering");

        handleInput();
        gamemap.update(delta);

//        map.render();
//
//        if(isHost){
//            server.update(delta);
//        }
    }

    public void gameOver() {
        game.setScreen(new EndScreen(game));
    }

    @Override
    public void resize(int width, int height) {
        gamemap.resize(width, height);
    }

    /**
     * This method is called when this screen is no longer the current screen for a Game.
     * We assume that when this method is called, the game is over. Close the client and server. Dispose the gamemap.
     */
    @Override
    public void hide() {
        client.shutdown();
        if (server != null)
            server.shutdown();
//        map.dispose();
    }

    @Override
    public void resume() {    }

    @Override
    public void pause() {    }

    @Override
    public void dispose() {    }

    private void logInfo(String string) {
        Log.info("[PlayScreen]: "+string);
    }
}
