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
import com.lemoninc.nimbusrun.Networking.Server.TapTapServer;
import com.lemoninc.nimbusrun.NimbusRun;
import com.lemoninc.nimbusrun.Sprites.GameMap;
import com.lemoninc.nimbusrun.scenes.HUD;

import java.io.IOException;

public class PlayScreen implements Screen{

    private NimbusRun game;

    private GameMap gamemap;

    private final boolean isHost;
//    private final String ipAddress;
    private String playerName;

    private TapTapClient client;
    private TapTapServer server;
    private HUD hud;

    /**
     *
     * @param game
     * @param isHost
     * @param
     * @param playerName
     */
    public PlayScreen(NimbusRun game, boolean isHost, String playerName, TapTapClient client, TapTapServer server){

        this.game = game;
        this.isHost = isHost;

        hud=new HUD(game.batch,playerName);

        this.client = client;
        if (isHost) {
            this.server = server;
        }
    }

    /**
     * Called when this screen becomes the current screen for a Game.
     * when the Play screen appears, get the map from the existing client
     *
     * Initialise gameplay by creating Players from the dummy Players
     *
     * If player is a host, initialise the server too
     */
    @Override
    public void show() {
        //create Players from dummyPlayers
        gamemap = client.getMap();
        gamemap.initPlayers(); //called before gamemap.render

        if (isHost) {
            server.initPlayers();
        }
    }

    @Override
    public void render(float delta) {

        gamemap.update(delta);
        gamemap.render();

        if(isHost){
            server.update(delta);
        }
        hud.update(delta);

        hud.render();
        hud.stage.draw();

        if(hud.worldTimer==0){
            gameOver();
        }
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
        gamemap.dispose();
    }

    @Override
    public void resume() {    }

    @Override
    public void pause() {    }

    @Override
    public void dispose() {
    }
}
