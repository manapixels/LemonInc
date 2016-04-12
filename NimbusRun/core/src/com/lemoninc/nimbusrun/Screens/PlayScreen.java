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
import com.badlogic.gdx.InputProcessor;
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
import com.lemoninc.nimbusrun.scenes.HUD;
import com.lemoninc.nimbusrun.Sprites.GameMap;
import com.lemoninc.nimbusrun.NimbusRun;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//import com.lemoninc.nimbusrun.Networking.Networking;

    private NimbusRun game;
public class PlayScreen implements Screen,InputProcessor {

    private TapTap3 game;
    private OrthographicCamera gamecam;
    private Viewport gameport;

    private GameMap gamemap;

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
    private HUD hud;

    /**
     *
     * @param game
     * @param isHost
     * @param ipAddress Server's IP address (only relevant to the Client)
     * @param playerName
     */
    public PlayScreen(NimbusRun game, boolean isHost, String ipAddress, String playerName){
        logInfo("My name is "+playerName);

    class TouchInfo {
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
    }
    private Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();

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
        hud=new HUD(game.batch,playerName);
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

        Gdx.input.setInputProcessor(this);

        for(int i = 0; i < 2; i++){
            touches.put(i, new TouchInfo());
        }

    }

    /**
     * Called when this screen becomes the current screen for a Game.
     * when the screen appears, create a new Client, get the map from the client.
     * If player is a host, create a new Server, connect the newly created client to the server.
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
        if(Gdx.input.justTouched()) {
            System.out.println("Points are: X=" + Gdx.input.getX() + "Y=" + Gdx.input.getY());
            int x=Gdx.input.getX();
            int y=Gdx.input.getY();
            if(x>game.V_WIDTH/2){
                player1.jump();
            }
            else{
                //player1.defence;
               // TODO: Implement method for defence
            }
        }
        if(touches.get(0).touched&&touches.get(1).touched){
            if(touches.get(0).touchX<(game.V_WIDTH/2)&&touches.get(1).touchX>(game.V_WIDTH-(game.V_WIDTH/2))){
               // TODO: Implement method for attack
                //player1.attack;
            }
            else if(touches.get(1).touchX<(game.V_WIDTH/2)&&touches.get(0).touchX>(game.V_WIDTH-(game.V_WIDTH/2))) {
                //TODO: Implement method for attack
                //player1.attack
            }
            else{
            }
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
        hud.render();
        hud.stage.draw();
        if(hud.worldTimer==0){
            gameOver();
        }


    }

    public void gameOver() {
    //TODO: WEISHENG: gamemap?
    public void update(float delta) {
        handleInput();
        hud.update(delta);
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
    public void dispose() {    }

    private void logInfo(String string) {
        Log.info("[PlayScreen]: "+string);
        Log.info(string);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 2){
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 2){
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
