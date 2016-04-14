package com.lemoninc.nimbusrun.Sprites;

/*********************************
 * FILENAME : GameMap.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       public synchronized void addPlayer(Network.PlayerJoinLeave msg)
 *       private    void initCommon()
 *       private    void handleInput()
 *       void       update(float delta)
 *       World      getWorld()
 *       Viewport   getGamePort()
 *       public synchronized void logInfo(String string)
 * NOTES :
 * LAST UPDATED: 12/4/2016 10:00
 *
 * ********************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.minlog.Log;
import com.lemoninc.nimbusrun.Networking.Client.TapTapClient;
import com.lemoninc.nimbusrun.Networking.Network;
import com.lemoninc.nimbusrun.Networking.Server.TapTapServer;
import com.lemoninc.nimbusrun.NimbusRun;

import java.util.HashMap;
import java.util.Map;

public class GameMap implements InputProcessor{

    private TapTapClient client; // only if I'm the client
    private TapTapServer server; // only if I'm internal to the server

    private boolean isClient;

    private Map<Integer, Player> players = new HashMap<Integer, Player>(); //playerId, Player

    private OrthographicCamera gamecam;
    private Viewport gameport;

    private SpriteBatch batch;
    private Sprite background;
//    private Texture background;

    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player1, player2, player3, player4;
    private Ground ground;
    private Ceiling ceiling;
    private StartWall startWall;
    private EndWall endWall;

    private Player playerLocal;
    private TextureAtlas img;


    class TouchInfo {
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
    }
    private Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();

    /**
     * This constructor is called inside TapTapClient
     */
    public GameMap(TapTapClient client) {

        this.client = client;
        this.isClient = true;

        //instantiate HUD, GameSounds, BitmapFont, Camera, SpriteBatch ...
        gamecam = new OrthographicCamera();

        gameport = new FitViewport(NimbusRun.V_WIDTH * 1.5f / NimbusRun.PPM, NimbusRun.V_HEIGHT * 1.5f / NimbusRun.PPM, gamecam);

        batch = new SpriteBatch();

        //TODO: 5 refers to the character selected at the main menu
        initCommon(5);

        //add these sprites to the world
        ground = new Ground(this);
        ceiling = new Ceiling(this);
        startWall = new StartWall(this);
        endWall = new EndWall(this);

        logInfo("GameMap initialised");

        Gdx.input.setInputProcessor(this);

        for(int i = 0; i < 2; i++){
            touches.put(i, new TouchInfo());
        }

    }

    /**
     * This constructor is called inside TapTapServer
     * //TODO: make a constructor for server that does not create World, and box2d related stuff but still can store who is connected, who is where, etc
     */
    public GameMap(TapTapServer server) {
        this.server = server;
        this.isClient = false;

        initCommon(5);

        logInfo("GameMap initialised");

    }

    private void initCommon(int whichCharacter){
        //TODO: server needs textureAtls for hwat?

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        // Load up all sprites into spriteMap from textureAtlas
        switch(whichCharacter){
            // 1. LAUGHING BUDDHA
            // 2. SHESHNAH WITH KRISHNA
            // 3. NINE-TAILED FOX
            // 4. KAPPA
            // 5. PONTIANAK
            // 6. MADAME WHITE SNAKE
            case 1: img = new TextureAtlas(Gdx.files.internal("spritesheets/LBspritesheet.atlas")); break;
            case 2: img = new TextureAtlas(Gdx.files.internal("spritesheets/SKspritesheet.atlas")); break;
            case 3: img = new TextureAtlas(Gdx.files.internal("spritesheets/FXspritesheet.atlas")); break;
            case 4: img = new TextureAtlas(Gdx.files.internal("spritesheets/KPspritesheet.atlas")); break;
            case 5: img = new TextureAtlas(Gdx.files.internal("spritesheets/PTspritesheet.atlas")); break;
            case 6: img = new TextureAtlas(Gdx.files.internal("spritesheets/MWSspritesheet.atlas")); break;
            default: img = new TextureAtlas(Gdx.files.internal("spritesheets/PTspritesheet.atlas")); break;
        }
        //background
        background = new Sprite(new Texture("TapTap_BGseamless.png"));
        background.setSize(background.getWidth() / NimbusRun.PPM, background.getHeight() / NimbusRun.PPM);
    }

    //called by server to add a new player into its GameMap
    public synchronized void addPlayer(Network.PlayerJoinLeave msg) {
        logInfo("Player added to players!");
        //create new player from msg
        //Need to look at spacegame

        Player newPlayer = new Player(this, img, msg.initial_x, msg.initial_y); //TODO: this coordinate should be from the msg
        logInfo("check1");
        newPlayer.setId(msg.playerId);
        logInfo("check2");

        newPlayer.setName(msg.name);
        logInfo("check3");

        players.put(msg.playerId, newPlayer);
    }

    /**
     * Destroy the disconnected player's body from world
     * Remove disconnected player from players
     * @param msg
     */
    public synchronized void removePlayer(Network.PlayerJoinLeave msg) {
        world.destroyBody(players.get(msg.playerId).b2body);
        players.remove(msg.playerId);
    }


    /**
     * Upon client-server connection, client calls map.onConnect(name) where a local Player is instantiated and stored in client's map's players
     * @param name
     */
    public void onConnect(String name) {

        if (this.playerLocal == null) {
            // TODO Server should spawn localPlayer too
            playerLocal = new Player(this, img, Network.SPAWN_X, Network.SPAWN_Y);
            this.playerLocal.setId(client.id);
            this.playerLocal.setName(name);
            players.put(client.id, playerLocal);
//            hud.setPlayerLocal(playerLocal);
//            setStatus("Connected to " + client.remoteIP);
        } else {
            logInfo("setNetworkClient called twice");
        }
    }

    public synchronized Player getPlayerById(int id){
        return players.get(id);
    }

    public World getWorld(){
        return this.world;
    }

    public Viewport getGameport() { return this.gameport; }

    private void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            playerLocal.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            playerLocal.moveRight();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            playerLocal.moveLeft();
        if (Gdx.input.isKeyJustPressed(Input.Keys.A))       //NOTE: TESTING PURPOSES ONLY
            playerLocal.stun();
        if (Gdx.input.isKeyJustPressed(Input.Keys.S))       //NOTE: TESTING PURPOSES ONLY
            playerLocal.poison();
        if (Gdx.input.justTouched()) {
            System.out.println("Points are: X=" + Gdx.input.getX() + "Y=" + Gdx.input.getY());
            int x=Gdx.input.getX();
            int y=Gdx.input.getY();
            if(x>NimbusRun.V_WIDTH/2){
                playerLocal.moveRight();
            }
            else{
                playerLocal.jump();
            }
        }
        if(touches.get(0).touched&&touches.get(1).touched){
            if(touches.get(0).touchX<(NimbusRun.V_WIDTH/2)&&touches.get(1).touchX>(NimbusRun.V_WIDTH-(NimbusRun.V_WIDTH/2))){
                // TODO: Implement method for attack
                //player1.attack;
            }
            else if(touches.get(1).touchX<(NimbusRun.V_WIDTH/2)&&touches.get(0).touchX>(NimbusRun.V_WIDTH-(NimbusRun.V_WIDTH/2))) {
                //TODO: Implement method for attack
                //player1.attack
            }
        }
    }

    private void render() {
        //clears screen first, set color to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //configure and start batch
        batch.setProjectionMatrix(gamecam.combined);
        batch.begin();
        background.setPosition(-gameport.getWorldWidth(), -gameport.getWorldHeight());
//        background.setPosition(gamecam.position.x - (gamecam.viewportWidth / 2), -gameport.getWorldHeight() / 2);
        background.draw(batch);

        // Render Players
        for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
            Player curPlayer = playerEntry.getValue();
            curPlayer.draw(batch);
//            if(curPlayer != playerLocal) curPlayer.renderNameTag(spriteBatch, fontNameTag);
        }

        //end batch
        batch.end();

        b2dr.render(world, gamecam.combined);

        //steps box2d world
        world.step(1 / 60f, 6, 2);
        //gamecam constantly to follow player1
        gamecam.position.set(playerLocal.getX(), playerLocal.getY(), 0);
        gamecam.update();
    }

    public void update(float delta) {
        handleInput();
        render();
        playerLocal.recover(1f);
        Log.info("Player State", "isStun: " + playerLocal.isStunned() + " stunTime " + playerLocal.getStunTime());
        Log.info("Player State", "isPoison: " + playerLocal.isPoisoned() + " poisonTime " + playerLocal.getPoisonTime());
    }

    public synchronized void logInfo(String string) {
        Log.info("[GameMap]: " + (isClient ? "[Client] " : "[Server] ") + string);
    }

    public void resize(int width, int height) {
        gameport.update(width, height);
        gamecam.position.set(gamecam.viewportWidth / 2, gamecam.viewportHeight / 2, 0);
    }

    public void dispose() {
        world.dispose();
        b2dr.dispose();

        //dispose textures
        img.dispose();
        //TODO:friendly players textures?
    }

    public void onDisconnect() {
        this.client = null;
        this.players.clear();
        logInfo("on DIsconnection, clear the players Map");
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
