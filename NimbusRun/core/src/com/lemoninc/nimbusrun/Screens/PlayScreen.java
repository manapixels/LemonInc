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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;
import com.lemoninc.nimbusrun.Networking.Client.TapTapClient;
import com.lemoninc.nimbusrun.Networking.Server.TapTapServer;
import com.lemoninc.nimbusrun.NimbusRun;
import com.lemoninc.nimbusrun.Sprites.GameMap;
import com.lemoninc.nimbusrun.scenes.HUD;

import java.util.List;

public class PlayScreen implements Screen{

    private NimbusRun game;

    public GameMap gamemap;

    private final boolean isHost;
//    private final String ipAddress;
    private String playerName;
    private List<Integer> rankings;
    private TapTapClient client;
    private TapTapServer server;
    private HUD hud;

    Boolean playmusic;
    Music music;
    private Sound gongSound;
    private long startTime;
    int charactername;

    PlayScreen playScreen;


    /**
     *
     * @param game
     * @param isHost
     * @param
     * @param playerName
     */


    public PlayScreen(NimbusRun game, boolean isHost, String playerName, TapTapClient client, TapTapServer server,Boolean playmusic,int charactername){
        this.charactername=charactername;
        this.playmusic=playmusic;
        this.game = game;
        this.isHost = isHost;
        this.playerName = playerName;
//        hud = new HUD(game.batch,playerName,gamemap,charactername);
        startTime = TimeUtils.millis();

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


        gamemap.createEnv(); //create ground, ceiling, etc
//        Log.info(playerName + "namenamenamename");
        hud = new HUD(this,game.batch,playerName,gamemap,charactername);
        gamemap.passHUD(hud);
        startTime = TimeUtils.millis();

        music=Gdx.audio.newMusic(Gdx.files.internal("Sounds/gamescreen.mp3"));
        music.setVolume(0.5f);                 // sets the volume to half the maximum volume
        music.setLooping(true);
        gongSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/chineseGong.mp3"));

        if(playmusic){
            music.play();
        }
        if (isHost) {
            server.initPlayers();
        }

        Gdx.input.setInputProcessor(gamemap.playerLocal);
    }

    @Override
    public void render(float delta) {
        gamemap.update(delta);
        gamemap.render();

        if (isHost) {
            server.update(delta);
        }
        hud.update(delta);
        hud.render();
        hud.stage.draw();

        if (hud.worldTimer == 0 || gamemap.getAllFinished()) {
            music.stop();
            gameOver();
            if (hud.count_final == 0){
                dispose();
                game.setScreen(new MenuScreen(game,NimbusRun.V_WIDTH,NimbusRun.V_HEIGHT));
            }
        }
    }
    public synchronized void setRankings(List<Integer> rankings){
        this.rankings = rankings;
    }


    public void gameOver() {
        dispose();
        music.stop();
        gongSound.play();
        hud.gameOver();
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
        music.dispose();
        gongSound.dispose();
    }
}
