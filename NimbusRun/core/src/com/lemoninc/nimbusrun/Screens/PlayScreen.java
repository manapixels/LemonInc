package com.lemoninc.nimbusrun.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.minlog.Log;
import com.lemoninc.nimbusrun.Networking.Client.TapTapClient;
//import com.lemoninc.nimbusrun.Networking.Networking;
import com.lemoninc.nimbusrun.Networking.Server.TapTapServer;
import com.lemoninc.nimbusrun.Sprites.Ceiling;
import com.lemoninc.nimbusrun.Sprites.EndWall;
import com.lemoninc.nimbusrun.Sprites.Ground;
import com.lemoninc.nimbusrun.Sprites.Player;
import com.lemoninc.nimbusrun.Sprites.StartWall;
import com.lemoninc.nimbusrun.TapTap3;

import java.io.IOException;

public class PlayScreen implements Screen {

    private TapTap3 game;
    private OrthographicCamera gamecam;
    private Viewport gameport;

    private SpriteBatch batch;
    private Sprite background;

    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player1, player2, player3, player4;
    private Ground ground;
    private Ceiling ceiling;
    private StartWall startWall;
    private EndWall endWall;

    private final boolean isHost;
    private final String ipAddress;
    private String playerName;

    private TapTapClient client;
    private TapTapServer server;


    public PlayScreen(TapTap3 game, boolean isHost, String ipAddress, String playerName){

        this.game = game;
        this.isHost = isHost;
        if (!ipAddress.isEmpty()) {
            this.ipAddress = ipAddress;
        } else {
            this.ipAddress = "localhost";
        }
        this.playerName = playerName;

        //TODO: WEISHENG: all these under GameMap?
        gamecam = new OrthographicCamera();
        gameport = new FitViewport(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM, gamecam);

        batch = new SpriteBatch();
        background = new Sprite(new Texture("TapTap_BGseamless_long.png"));
        background.setPosition(-gameport.getWorldWidth(), 0);
        background.setSize(background.getWidth() / game.PPM, background.getHeight() / game.PPM);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        //create user's character
        player1 = new Player(this, 5, 32, 200);

        //pass in the parameters to the other players
        player2 = new Player(this, 6, 150, 200);
        player3 = new Player(this, 3, -150, 200);
        //player4 = new Player(this, 4, 250, 200);

        ground = new Ground(this);
        ceiling = new Ceiling(this);
        startWall = new StartWall(this);
        endWall = new EndWall(this);

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
        //get map

        if (isHost) {
            //start my server and connect my client to my server
            try {
                server = new TapTapServer();
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player1.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            player1.speed();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            player1.slow();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            gameOver();
        }
    }

    //TODO: WEISHENG: need cleanup
    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        map.update(delta);
//        map.render();
//
//        if(isHost){
//            server.update(delta);
//        }


//       b2dr.render(world, gamecam.combined);   //render box2d world lines
//
//        batch.setProjectionMatrix(gamecam.combined);    //set batch to camera
//        batch.begin();  //start batch stuff
//        background.draw(batch); //draw the background file
//        player.draw(batch);

        batch.setProjectionMatrix(gamecam.combined);
        batch.begin();
//        draw(TextureRegion region, float x, float y, float width, float height)
//        batch.draw(background,0,0, gameport.getScreenWidth(), gameport.getScreenHeight());
//        batch.draw(background, gamecam.position.x - (gamecam.viewportWidth / 2), 0);
        background.draw(batch);

//        player.draw(batch);

        player1.draw(batch);
        player2.draw(batch);
        player3.draw(batch);
        //player4.draw(batch);

        b2dr.render(world, gamecam.combined);

        batch.end();    //stop batch stuff

    }

    //TODO: WEISHENG: gamemap?
    public void update(float delta) {
        handleInput();
        player1.update(delta);
        if (player1.b2body.getPosition().y <= 0){
            gameOver();
        }

//        for (HashMap.Entry<String, Player> entry: network.friendlyPlayers.entrySet()) {
//            entry.getValue().update(dt);
//        }

        player2.update(delta);
        player3.update(delta);
        //player4.update(delta);
//        for (Map.Entry<String, Player> entry: network.friendlyPlayers.entrySet()) {
//            entry.getValue().update(dt);
//        }

        world.step(1 / 60f, 6, 2);
        gamecam.position.set(player1.getX(), gamecam.viewportHeight / 2, 0);
        gamecam.update();
    }

    public void gameOver(){
        game.setScreen(new EndScreen(game));
    }

    @Override
    public void resize(int width, int height) {
        //TODO: gamemap.resize(width,height)???

        gameport.update(width, height);
        gamecam.position.set(gamecam.viewportWidth / 2, gamecam.viewportHeight / 2, 0);
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


    public World getWorld(){
        return world;
    }
    public Viewport getGamePort(){
        return gameport;
    }

    @Override
    public void dispose() {
        world.dispose();
        background.getTexture().dispose();
        batch.dispose();
        player1.getTxtAtlas().dispose();
        player2.getTxtAtlas().dispose();
        player3.getTxtAtlas().dispose();
        //player4.getTxtAtlas().dispose();
    }

    private void logInfo(String string) {
        Log.info(string);
    }
}
