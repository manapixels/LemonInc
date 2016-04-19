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
 *       private    makePlatformsBG()
 *       public     getPlayers()
 *       public synchronized void logInfo(String string)
 * NOTES :
 * LAST UPDATED: 18/4/2016 11:27
 *
 * ********************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lemoninc.nimbusrun.Networking.Client.TapTapClient;
import com.lemoninc.nimbusrun.Networking.Network;
import com.lemoninc.nimbusrun.Networking.Server.TapTapServer;
import com.lemoninc.nimbusrun.NimbusRun;
import com.lemoninc.nimbusrun.scenes.HUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap{

    private TapTapClient client; // only if I'm the client
    private TapTapServer server; // only if I'm internal to the server

    private boolean isClient;

    private Map<Integer, Player> players = new HashMap<Integer, Player>(); //playerId, Player
    private Map<Integer, DummyPlayer> dummyPlayers = new HashMap<Integer, DummyPlayer>(); //playerId, Player
    private boolean gameMapReadyForHUD;
    private HUD hud;

    private OrthographicCamera gamecam;
    private Viewport gameport;

    private SpriteBatch batch;
    private TextureAtlas img;
    private Texture bgTexture;
    private Sprite bgSprite;
    private float bgHeight, bgWidth, bgStartX, bgStartY;
    private Sprite flashbg;

    private Texture bgTextureFlat, bgTextureMountain, bgTexturePit, bgTexturePlateau;
    private List<Sprite> bgPlatformSprites;

    private Sprite finishLine;

    private World world;
    private Box2DDebugRenderer b2dr;
    private Ground ground;
    private Ceiling ceiling;
    private StartWall startWall;
    private EndWall endWall;
    private int[] mapData;
    public static final int NUMPLATFORMS = 8;

    private Player playerLocal;
    private DummyPlayer dummyLocal;

    private int sourceX;

    /**
     * This constructor is called inside TapTapClient
     */
    public GameMap(TapTapClient client, int[] mapData) {

        this.client = client;
        this.isClient = true;
        this.mapData = mapData;

        //instantiate HUD, GameSounds, BitmapFont, Camera, SpriteBatch ...
        initCommon();
        gameMapReadyForHUD = false;

        //font for player names on avatars
//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("PlayScreen/SF Atarian System.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 10;
//        font = generator.generateFont(parameter);
//        font.setColor(Color.WHITE);
//        font.getData().setScale(0.1f, 0.1f);
//        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//        generator.dispose();

        //set starting pos of bgSprites after setting cam
        bgStartX = -gameport.getWorldWidth() * 1.5f;
        bgStartY = -gameport.getWorldHeight() * 1.5f;
//        Log.info(bgStartY + " y pos");
        batch = new SpriteBatch();

        // initialise all background sprites
        bgTexture = new Texture("4_PlayScreen/bg_dark.png");
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bgSprite = new Sprite(new TextureRegion(bgTexture, bgTexture.getWidth()*19, bgTexture.getHeight()*3));
        bgWidth = bgTexture.getWidth() / NimbusRun.PPM * 1.5f * 19;
        bgHeight = bgTexture.getHeight() / NimbusRun.PPM * 1.5f * 3;
        bgSprite.setX(bgStartX);
        bgSprite.setY(bgStartY);
        bgSprite.setSize(bgWidth, bgHeight);

        bgTextureFlat = new Texture("4_PlayScreen/platform_flat.png");
        bgTexturePlateau = new Texture("4_PlayScreen/platform_plateau.png");
        bgTextureMountain = new Texture("4_PlayScreen/platform_mountain.png");
        bgTexturePit = new Texture("4_PlayScreen/platform_pit.png");

        bgPlatformSprites = new ArrayList<Sprite>();

        finishLine = new Sprite(new Texture("4_PlayScreen/finishLine.png"));

        //TODO: these are created by Server and server sends GameMapStatus to clients

//        logInfo("GameMap initialised");
        Gdx.app.log("GDX GameMap", "GameMap instantiated in Client");

    }

    /**
     * This constructor is called inside TapTapServer
     */
    public GameMap(TapTapServer server, int[] mapData) {
        this.server = server;
        this.isClient = false;
        this.mapData = mapData;

        initCommon();

//        logInfo("GameMap initialised");
        Gdx.app.log("GDX GameMap", "GameMap instantiated in Server");
    }

    /**
     * Create box2d world and DebugRenderer
     */
    private void initCommon(){
        world = new World(new Vector2(0, -10), true); //box2d world with gravity
        b2dr = new Box2DDebugRenderer();

        gamecam = new OrthographicCamera();
        gameport = new FitViewport(NimbusRun.V_WIDTH * 2f / NimbusRun.PPM, NimbusRun.V_HEIGHT * 2f / NimbusRun.PPM, gamecam);
    }

    public void initPlayers() {
        //create Players from dummyPlayers
        for (Map.Entry<Integer, DummyPlayer> playerEntry : dummyPlayers.entrySet()) {
            DummyPlayer curPlayer = playerEntry.getValue();
            if (curPlayer.isLocal) {
                playerLocal = new Player(this, getImg(curPlayer.character), curPlayer.x, curPlayer.y, true, curPlayer.character);
                playerLocal.setId(curPlayer.playerID);
                playerLocal.setName(curPlayer.playerName);
                players.put(curPlayer.playerID, playerLocal);
                //rankings.add(curPlayer.playerID);
            }
            else {
                Player newPlayer= new Player(this, getImg(curPlayer.character), curPlayer.x, curPlayer.y, false, curPlayer.character);
                newPlayer.setId(curPlayer.playerID);
                newPlayer.setName(curPlayer.playerName);
                players.put(curPlayer.playerID, newPlayer);
                //rankings.add(curPlayer.playerID);

            }
        }
        gameMapReadyForHUD = true;
    }
    public void passHUD(HUD hud){
        this.hud = hud;
    }
    public HUD getHud(){
        return this.hud;
    }

    public void createEnv() {
        //add these sprites to the world
        ground = new Ground(this, mapData, NUMPLATFORMS);
        ceiling = new Ceiling(this);
        startWall = new StartWall(this);
        endWall = new EndWall(this);
        setFinishLine();
    }

    private void setFinishLine(){
        finishLine.setPosition(gameport.getWorldWidth() * 18.5f, 0);
        finishLine.setSize(finishLine.getWidth() * 0.01f, finishLine.getHeight() * 0.01f);
    }

    private TextureAtlas getImg(int character) {
        // Load up all sprites into spriteMap from textureAtlas
        switch(character){
            // 1. LAUGHING BUDDHA
            // 2. SHESHNAH WITH KRISHNA
            // 3. NINE-TAILED FOX
            // 4. KAPPA
            // 5. PONTIANAK
            // 6. MADAME WHITE SNAKE
            case 1: img = new TextureAtlas(Gdx.files.internal("Spritesheets/LBspritesheet.atlas")); break;
            case 2: img = new TextureAtlas(Gdx.files.internal("Spritesheets/SKspritesheet.atlas")); break;
            case 3: img = new TextureAtlas(Gdx.files.internal("Spritesheets/FXspritesheet.atlas")); break;
            case 4: img = new TextureAtlas(Gdx.files.internal("Spritesheets/KPspritesheet.atlas")); break;
            case 5: img = new TextureAtlas(Gdx.files.internal("Spritesheets/PTspritesheet.atlas")); break;
            case 6: img = new TextureAtlas(Gdx.files.internal("Spritesheets/MWSspritesheet.atlas")); break;
            default: img = new TextureAtlas(Gdx.files.internal("Spritesheets/PTspritesheet.atlas")); break;
        }
        return img;
    }

    public Network.MapDataPacket getMapDataPacket() {
        return new Network.MapDataPacket(mapData);
    }

    /**
     * HAS TO BE CALLED BEFORE GROUND is instantiated
     * @param mapData
     */
    public void setMapData(int[] mapData) {
        this.mapData = mapData;
    }

    public synchronized void playerMoved(Network.MovementState msg) {
        //TODO: players should be ConcurrentHashMap?
        Player player = players.get(msg.playerId);
        if (player != null) {
            Gdx.app.log("GDX GameMap", "Player "+player.getName()+" moved");
            player.setMovementState(msg);
        }
    }

    public void playerAttacked(Network.PlayerAttack msg) {
        //apply effect of the attack on every other playrs

        // 1. LAUGHING BUDDHA
        // 2. SHESHNAH WITH KRISHNA
        // 3. NINE-TAILED FOX
        // 4. KAPPA
        // 5. PONTIANAK
        // 6. MADAME WHITE SNAKE

//        Player attacker = getPlayerById(msg.id);

        switch(msg.character) {
            case 1: stunExceptId(msg.id); break;
            case 2: flashExceptId(msg.id); break;
            case 3: confuseExceptId(msg.id); break;
            case 4: reverseExceptId(msg.id); break;
            case 5: terrorExceptId(msg.id); break;
            case 6: poisonExceptId(msg.id); break;
            default: stunExceptId(msg.id); break;

        }
    }

    public void stunExceptId(int id) {
        for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
            Player curPlayer = playerEntry.getValue();
            if (curPlayer.getId() != id) {
                curPlayer.stun();
            }
        }
    }

    public void poisonExceptId(int id) {
        for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
            Player curPlayer = playerEntry.getValue();
            if (curPlayer.getId() != id) {
                curPlayer.poison();
            }
        }
    }

    public void reverseExceptId(int id) {
        for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
            Player curPlayer = playerEntry.getValue();
            if (curPlayer.getId() != id) {
                curPlayer.reverse();
            }
        }
    }

    public void terrorExceptId(int id) {
        for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
            Player curPlayer = playerEntry.getValue();
            if (curPlayer.getId() != id) {
                curPlayer.terror();
            }
        }
    }

    public void flashExceptId(int id) {
        for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
            Player curPlayer = playerEntry.getValue();
            if (curPlayer.getId() != id) {
                curPlayer.flash();
            }
        }
    }

    public void confuseExceptId(int id) {
        for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
            Player curPlayer = playerEntry.getValue();
            if (curPlayer.getId() != id) {
                curPlayer.confuse();
            }
        }
    }

    /**
     * Client receives PlayerJoinLeave from server containing player ID, name, initial x and y
     * @param msg
     */
    public void onConnect(Network.PlayerJoinLeave msg) {

        if (this.dummyLocal == null) {
            dummyLocal = new DummyPlayer(client.id, msg.name, msg.initial_x, msg.initial_y, true);
            dummyPlayers.put(dummyLocal.playerID, dummyLocal);
            //hud.setPlayerLocal(playerLocal);
            //setStatus("Connected to " + client.remoteIP);
            Gdx.app.log("GDX GameMap", "local player created at "+msg.initial_x+" "+msg.initial_y);
        } else {
            Gdx.app.log("GDX GameMap onConnect", "setNetworkClient called twice");
        }
    }

    public boolean onPlayerAttack(Network.PlayerAttack msg) {
        Gdx.app.log("GDX GameMap onPlayerAttack", "");

        Player player = getPlayerById(msg.id);

        if (player != null) {
            if (!player.attack()) {
                return false;
            }
        }
        else {
            Gdx.app.log("GDX GameMap onPlayerAttack", "player was null");
        }
        return true;
    }

    /**
     * This method is only called in Character Selection screen
     * @param msg
     */

    public synchronized void addPlayer(Network.PlayerJoinLeave msg) {
        //create new player from msg
        DummyPlayer newDummy = new DummyPlayer(msg.playerId, msg.name, msg.initial_x, msg.initial_y, false);

        dummyPlayers.put(newDummy.playerID, newDummy);
//        logInfo("Player " +msg.playerId+" added to players!");
    }



    /**
     * Destroy the disconnected player's body from world
     * Remove disconnected player from players
     * Can be called from both CS screen and PlayScreen
     *
     * @param msg
     */
    public synchronized void removePlayer(Network.PlayerJoinLeave msg) {
        dummyPlayers.remove(msg.playerId);

        if (players.get(msg.playerId) != null) {
            world.destroyBody(players.get(msg.playerId).b2body);
            players.remove(msg.playerId);
        }
    }


    public synchronized Player getPlayerById(int id){
        return players.get(id);
    }

    public synchronized DummyPlayer getDummyById(int id) {
        return dummyPlayers.get(id);
    }

    public boolean allDummyReady() {
        for (Map.Entry<Integer, DummyPlayer> playerEntry : dummyPlayers.entrySet()) {
            DummyPlayer curPlayer = playerEntry.getValue();
            if (!curPlayer.isReady()) {
                Gdx.app.log("GDX GameMap allDummyReady", "Hi from "+curPlayer.playerName);
                return false;
            }
        }

        return true;
    }

    public void declareCharacter(int charactername) {
        dummyLocal.setCharacter(charactername);
    }

    public void setCharacter(int playerId, int charactername) {
        dummyPlayers.get(playerId).setCharacter(charactername);
    }

    public World getWorld(){
        return this.world;
    }
    public boolean getGameMapReadyForHUD() { return gameMapReadyForHUD; }

    public Viewport getGameport() { return this.gameport; }

    /**
     * Update GameMap's state.
     * This method is only called in PlayScreen
     *
     * Should the box world be rendered here?
     *
     * @param delta
     */
    public synchronized void update(float delta) {
        //If client is created and local player has spawned
        if (client != null && playerLocal != null) {
            if (playerLocal.handleInput()) { // (arrow key has been pressed by player)
                client.sendMessageUDP(playerLocal.getMovementState()); //send movement state to server
                Gdx.app.log("GDX GameMap", "Sent MovementState to Server");
            }
            //gamecam constantly to follow playerLocal
            gamecam.position.set(playerLocal.getX(), playerLocal.getY(), 0);
            gamecam.update();
        }

        //Update player
        //TODO: should the box2d world be rendered here?
        for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
            Player curPlayer = playerEntry.getValue();
            if (client!=null && curPlayer != null) {
                curPlayer.update(delta);
            }
            //if(curPlayer != playerLocal) curPlayer.renderNameTag(spriteBatch, fontNameTag);
        }

        //Server-only logic
        if (!isClient) {

        }

        //TODO: I put this in update for the server to do its calculation

    }

    /**
     * Only called in PlayScreen by Clients
     */
    public synchronized void render() {
        //clears screen first, set color to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        b2dr.render(world, gamecam.combined);
        //--------------START batch
        batch.setProjectionMatrix(gamecam.combined);
        batch.begin();

        // Render seamless bg and platforms
        bgSprite.draw(batch);
        for (Sprite sprite : bgPlatformSprites) {
            sprite.draw(batch);
        }
        // finishLine sprite rendered after background & platforms but before players
        finishLine.draw(batch);
        // Render Players
        for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
            Player curPlayer = playerEntry.getValue();
            curPlayer.draw(batch);
            //font.draw(batch, "PlayerTest", curPlayer.getX(), curPlayer.getY());
            //  Render flashbang if flashed
            if (curPlayer.isFlashed()) {
                Gdx.gl.glClearColor(1, 1, 1, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            }
            //if(curPlayer != playerLocal) curPlayer.renderNameTag(spriteBatch, fontNameTag);
        }
        //----------------END batch
        batch.end();
        world.step(1 / 60f, 6, 2);
    }

    public void resize(int width, int height) {
        gameport.update(width, height);
        gamecam.position.set(gamecam.viewportWidth / 2, gamecam.viewportHeight / 2, 0);
    }

    public void makePlatformsBG(float startX, float endX, char type){
        Sprite sprite;
        float width = endX-startX;
        float height;

        switch(type){
            case 'F': sprite = new Sprite(bgTextureFlat);
                height = width/1000*390;
                sprite.setPosition(startX, -height);
                sprite.setSize(width, height);
                bgPlatformSprites.add(sprite); break;

            case 'P': sprite = new Sprite(bgTexturePlateau);
                height = width/1000*789;
                sprite.setPosition(startX, -height*0.7366f);
                sprite.setSize(width, height);
                bgPlatformSprites.add(sprite); break;

            case 'M': sprite = new Sprite(bgTextureMountain);
                height = width/1000*869;
                sprite.setPosition(startX, -height*0.473f);
                sprite.setSize(width, height);
                bgPlatformSprites.add(sprite); break;

            case 'T': sprite = new Sprite(bgTexturePit);
                height = width/1000*605;
                sprite.setPosition(startX, -height);
                sprite.setSize(width, height);
                bgPlatformSprites.add(sprite); break;
        }
    }

    public Map<Integer, Player> getPlayers(){
        return players;
    }
    public Map<Integer, DummyPlayer> getDummyPlayers(){
        return dummyPlayers;
    }

    public synchronized void logInfo(String string) {
       // Log.info("[GameMap]: " + (isClient ? "[Client] " : "[Server] ") + string);
    }

    public void clientSendMessage(Object msg) {
        client.sendMessage(msg);
    }

    public void dispose() {
        world.dispose();
        b2dr.dispose();
        batch.dispose();
        //dispose textures
        img.dispose();
        //players are disposed at endscreen
    }

    public void onDisconnect() {
        this.client = null;
        this.dummyPlayers.clear();
        this.players.clear();
    }

    public class DummyPlayer {
        private int playerID;
        public String playerName;
        public float x;
        public float y;
        public boolean isLocal;
        private int character = 99;

        private DummyPlayer(int playerID, String playerName, float x, float y, boolean isLocal) {
            this.playerID = playerID;
            this.playerName = playerName;
            this.x = x;
            this.y = y;
            this.isLocal = isLocal;
        }

        public void setCharacter(int character) {
            this.character = character;
        }

        public boolean isReady() {
            if (this.character != 99) {
                return true;
            }
            else {
                return false;
            }
        }
    }
}
