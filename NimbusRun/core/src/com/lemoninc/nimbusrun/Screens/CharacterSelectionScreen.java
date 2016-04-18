/*********************************
 * FILENAME : CharacterSelectionScreen.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *
 * NOTES : The screen takes input from the waitscreen and moves to waitscreen
 * TODO: Create a waiting function for all the players to connect and wait for each other,
 * TODO: get the input in the screen a string of the host ip adress, (right now IP=string localhost)
 * TODO: The wait can be onclick of the joingame button
 * TODO: Implement the counter on the basis of the players connected
 * The function has a string name of the player it has chosen, pass this to the playscreen/player to choose the specific character.
 * LAST UPDATED:
 *
 * ********************************/


package com.lemoninc.nimbusrun.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lemoninc.nimbusrun.Networking.Client.TapTapClient;
import com.lemoninc.nimbusrun.Networking.Network;
import com.lemoninc.nimbusrun.Networking.Server.TapTapServer;
import com.lemoninc.nimbusrun.NimbusRun;
import com.lemoninc.nimbusrun.Sprites.GameMap;

import java.io.IOException;
import java.util.Random;


/**
 * Created by Nikki on 12/4/2016.
 */
public class CharacterSelectionScreen implements Screen{
    private SpriteBatch batcher;
    private Sprite sprite;
    private final NimbusRun game;
    private float gameWidth;
    private float gameHeight;
    private Viewport viewport;
    private Camera camera;
    final Boolean isHost;
    String ipAddress;
    String playername;
    private long startTime;
    private ImageButton Buddha,foxy,kappa,krishna,madame,ponti;
    TextureAtlas atlas1,atlas2,atlas3;
    private Skin skin;
    ImageButton.ImageButtonStyle BuddhabtnStyle,foxybtnStyle,kappabtnStyle,krishnabtnStyle,madamebtnStyle,pontibtnStyle;
    private Stage stage;
    float BUTTON_HEIGHT,BUTTON_WIDTH;
    Label Title;
    Sprite playercharacter;
    TextButton joingame,goback;
    TextButton.TextButtonStyle style;
    //Dialog dialog;
    CharSequence Playerability;
    Boolean playmusic;
    Music music;
    Sound soundclick;
    private int charactername = 99;
    private String myIP;

    private TapTapClient client;
    private TapTapServer server;
    private GameMap gamemap;

    private int[] mapData;

    public CharacterSelectionScreen(final NimbusRun game, final boolean isHost, String ipAddress, String playerName, final Boolean playmusic){


    /**
     *
     * @param game
     * @param isHost
     * @param ipAddress to connect the client to the server
     * @param playerName
     */
        this.game = game;
        this.isHost = isHost;
        this.ipAddress = ipAddress;
        this.playername = playerName;
        this.gameWidth = NimbusRun.V_WIDTH;
        this.gameHeight = NimbusRun.V_HEIGHT;
        this.playmusic=playmusic;
        this.mapData = null;
//        myIP=ipAddress;

        BUTTON_HEIGHT=165;
        BUTTON_WIDTH=140;

        soundclick=Gdx.audio.newSound(Gdx.files.internal("Sounds/click.mp3"));

        music=Gdx.audio.newMusic(Gdx.files.internal("Sounds/characterselectionscreen.mp3"));
        music.setVolume(0.5f);                 // sets the volume to half the maximum volume
        music.setLooping(true);
        if(playmusic){
            music.play();
        }

        camera=new PerspectiveCamera();
        viewport=new FitViewport(gameWidth,gameHeight,camera);
        skin=new Skin(Gdx.files.internal("data/uiskin.json"));
        atlas1=new TextureAtlas(Gdx.files.internal("3_CharSelScreen/charicons.pack"));
        atlas2=new TextureAtlas(Gdx.files.internal("3_CharSelScreen/zoomicons.pack"));
        atlas3=new TextureAtlas(Gdx.files.internal("3_CharSelScreen/buttonsupdown.pack"));
        skin.addRegions(atlas1);
        skin.addRegions(atlas2);
        skin.addRegions(atlas3);

        style=new TextButton.TextButtonStyle();
        style.font=new BitmapFont(Gdx.files.internal("Fonts/crimesFont48Black.fnt"));
        style.font.setColor(Color.DARK_GRAY);
        style.font.getData().setScale(0.65f, 0.65f);
        style.up=skin.getDrawable("button_up");
        style.over=skin.getDrawable("button_down");
        style.down=skin.getDrawable("button_down");
//        style.up=new TextureRegionDrawable(new TextureRegion(new Texture("button_up.png")));
//        style.down=new TextureRegionDrawable(new TextureRegion(new Texture("button_down.png")));

        stage= new Stage(new ExtendViewport(gameWidth,gameHeight));
        stage.clear();

        final Table table = new Table();
        table.right();
        table.setFillParent(true);

        //Title=new Label(String.format("%03d","Choose your Avatar"),new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/crime.fnt")), Color.DARK_GRAY));
        Title=new Label("Choose Your Character",new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/crimesFont48Black.fnt")), Color.DARK_GRAY));
        Title.setPosition(gameWidth / 2, gameHeight - gameWidth / 20, Align.center);
        Title.setSize(gameWidth / 15, gameHeight / 15);
        table.addActor(Title);

        BuddhabtnStyle=new ImageButton.ImageButtonStyle();
        foxybtnStyle=new ImageButton.ImageButtonStyle();
        kappabtnStyle=new ImageButton.ImageButtonStyle();
        krishnabtnStyle=new ImageButton.ImageButtonStyle();
        madamebtnStyle=new ImageButton.ImageButtonStyle();
        pontibtnStyle=new ImageButton.ImageButtonStyle();

        BuddhabtnStyle.imageUp = skin.getDrawable("btn_buddha");
        BuddhabtnStyle.imageOver=skin.getDrawable("btn_buddha_sel");
        BuddhabtnStyle.imageDown = skin.getDrawable("btn_buddha_sel");
        BuddhabtnStyle.imageChecked=skin.getDrawable("btn_buddha_sel");

        foxybtnStyle.imageUp = skin.getDrawable("btn_foxy");
        foxybtnStyle.imageDown = skin.getDrawable("btn_foxy_sel");
        foxybtnStyle.imageOver = skin.getDrawable("btn_foxy_sel");
        foxybtnStyle.imageChecked=skin.getDrawable("btn_foxy_sel");

        kappabtnStyle.imageUp = skin.getDrawable("btn_kappa");
        kappabtnStyle.imageDown = skin.getDrawable("btn_kappa_sel");
        kappabtnStyle.imageOver = skin.getDrawable("btn_kappa_sel");
        kappabtnStyle.imageChecked=skin.getDrawable("btn_kappa_sel");

        krishnabtnStyle.imageUp = skin.getDrawable("btn_krishna");
        krishnabtnStyle.imageDown= skin.getDrawable("btn_krishna_sel");
        krishnabtnStyle.imageOver = skin.getDrawable("btn_krishna_sel");
        krishnabtnStyle.imageChecked=skin.getDrawable("btn_krishna_sel");

        madamebtnStyle.imageUp = skin.getDrawable("btn_madame");
        madamebtnStyle.imageDown = skin.getDrawable("btn_madame_sel");
        madamebtnStyle.imageOver = skin.getDrawable("btn_madame_sel");
        madamebtnStyle.imageChecked=skin.getDrawable("btn_madame_sel");

        pontibtnStyle.imageUp = skin.getDrawable("btn_ponti");
        pontibtnStyle.imageDown = skin.getDrawable("btn_ponti_sel");
        pontibtnStyle.imageOver = skin.getDrawable("btn_ponti_sel");
        pontibtnStyle.imageChecked=skin.getDrawable("btn_ponti_sel");

        Buddha= new ImageButton(BuddhabtnStyle);
        foxy=new ImageButton(foxybtnStyle);
        kappa=new ImageButton(kappabtnStyle);
        krishna=new ImageButton(krishnabtnStyle);
        madame=new ImageButton(madamebtnStyle);
        ponti=new ImageButton(pontibtnStyle);

        Buddha.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        foxy.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        kappa.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        krishna.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        madame.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        ponti.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        Buddha.setPosition(viewport.getScreenWidth() / 4, 400, Align.left);
        foxy.setPosition(viewport.getScreenWidth() / 4, 260, Align.left);
        kappa.setPosition(viewport.getScreenWidth() / 4, 120, Align.left);
        krishna.setPosition(viewport.getScreenWidth() / 4 + 250, 360, Align.right);
        madame.setPosition(viewport.getScreenWidth() / 4 + 250, 220, Align.right);
        ponti.setPosition(viewport.getScreenWidth() / 4 + 250, 80, Align.right);

        joingame=new TextButton("Join Game",style);
        joingame.setSize(gameWidth / 5, gameHeight / 8);
        joingame.setPosition(gameWidth / 3, gameHeight / 5);

        goback=new TextButton("Go Back",style);
        goback.setSize(gameWidth/5,gameHeight/8);
        goback.setPosition(gameWidth * 0.85f, gameHeight * 0.75f);

        table.addActor(Title);
        table.addActor(joingame);
        table.addActor(goback);
        table.addActor(Buddha);
        table.addActor(foxy);
        table.addActor(kappa);
        table.addActor(krishna);
        table.addActor(madame);
        table.addActor(ponti);
        stage.addActor(table);

        Playerability = "STUN";

        Buddha.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetbuttons();
                System.out.println("touched");
                Gdx.app.log("Button pressed", "Buddha Button Pressed");
                playercharacter = skin.getSprite("bg_Buddha");

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                Playerability = "STUN";
                charactername = 1;
                System.out.println("touched");

            }
        });

        foxy.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetbuttons();
//                Buddha.clearActions();
//                System.out.println("touched");
//                Gdx.app.log("Button pressed", "Foxy Button Pressed");
                playercharacter= skin.getSprite("bg_Foxy");
                playercharacter.setPosition(0, 0);
                playercharacter.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                Playerability="SUCKS BACK";
                charactername=3;
                System.out.println("touched");
            }
        });

        kappa.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetbuttons();
                System.out.println("touched");
                Gdx.app.log("Button pressed", "Kappa Button Pressed");
                playercharacter= skin.getSprite("bg_Kappa");
                playercharacter.setPosition(0, 0);
                playercharacter.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Playerability="FREEZE";
                charactername=4;
                System.out.println("touched");
            }
        });
        krishna.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetbuttons();
                System.out.println("touched");
                Gdx.app.log("Button pressed", "KrishnaButton Pressed");
                playercharacter= skin.getSprite("bg_Krishna");
                playercharacter.setPosition(0, 0);
                playercharacter.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Playerability="FLASH LIGHT TO";
                charactername=2;
                System.out.println("touched");
            }
        });
        madame.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetbuttons();
                System.out.println("touched");
                Gdx.app.log("Button pressed", "madame Button Pressed");
                playercharacter= skin.getSprite("bg_Madame");
                playercharacter.setPosition(0, 0);
                playercharacter.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Playerability="JUMP STOP";
                charactername=6;
                System.out.println("touched");
            }
        });
        ponti.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetbuttons();
                System.out.println("touched");
                Gdx.app.log("Button pressed", "Ponti Button Pressed");
                playercharacter= skin.getSprite("bg_Pontianak");
                playercharacter.setPosition(0, 0);
                playercharacter.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Playerability="DARKEN";
                charactername=5;
                System.out.println("touched");
            }
        });
        joingame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundclick.play();
                music.stop();
                Gdx.app.log("PlayerNumber", "Character " + charactername + " joined game");
//                playGame(charactername);
                //TODO:save the charactername, let server know player is ready to play
                //send server charactername packet
                if (charactername != 99) {
                    Network.Ready ready = new Network.Ready(charactername);
                    client.sendMessage(ready);
                    gamemap.declareCharacter(charactername);
                    Gdx.app.log("CSscreen", "self declare character");
                    //TODO: create check on CS screen

                }
                if (isHost) {
                    //TODO: try to start game
                    //if received charactername from all players, play game
                    if (server.allDummyReady()) {
                        //TODO: send all clients GameReady
                        Network.GameReady gameready = new Network.GameReady();
                        client.sendMessage(gameready);
                        playGame();
                    }
                    else {
                        Gdx.app.log("CSscreen", "Not all dummies ready ");
                    }
                }
            }
        });

        goback.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: SAVE THE LOG OF THE PLAYER ACCORDING TO THE NUMBER
                //charactername= checkbuttonpress();
                soundclick.play();
                game.setScreen(new MenuScreen(game, gameWidth, gameHeight));
                music.stop();
            }
        });



    }

    // 1. LAUGHING BUDDHA
    // 2. SHESHNAH WITH KRISHNA
    // 3. NINE-TAILED FOX
    // 4. KAPPA
    // 5. PONTIANAK
    // 6. MADAME WHITE SNAKE
//    public String checkbuttonpress(){
//        if(Buddha.isPressed()) return ("Buddha");
//        else if(krishna.isPressed()) return ("Krishna");
//        else if (foxy.isPressed()) return ("Foxy");
//        else if (kappa.isPressed()) return ("Kappa");
//        else if (ponti.isPressed()) return ("Ponti");
//        else if (madame.isPressed()) return ("Madame");
//        else return ("Buddha");
//    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        sprite = new Sprite(new Texture("3_CharSelScreen/whiteBG.png"));
        //sprite.setColor(1, 1, 1, 0);
        playercharacter=new Sprite(skin.getSprite("bg_Buddha"));
        //sprite.setPosition(0, 0);
        //sprite.setSize(gameWidth, gameHeight);
        style.font=new BitmapFont(Gdx.files.internal("Fonts/crimesFont36Black.fnt"));
        style.font.getData().setScale(0.7f, 0.7f);
        style.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        style.font.setColor(Color.DARK_GRAY);
        batcher = new SpriteBatch();
        startTime = TimeUtils.millis();
        mapData = new int[8];

        //instnatiate server, client here

        
        if (isHost) {
            Random rand = new Random();
            for (int i = 0; i < 8; i++){
                mapData[i] = rand.nextInt(3);
            }
            client = new TapTapClient(game, this, playername, mapData);
            gamemap = client.getMap();
            //start my server and connect my client to my server
            try {
                server = new TapTapServer();
                client.connect("localhost");
            } catch (IOException e) {
                e.printStackTrace();
//                logInfo("Can't connect to localhost server");
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new WaitScreen(game,playmusic));

                    }
                });
            }
        }
        else {
            client = new TapTapClient(game, this, playername, mapData);
            gamemap = client.getMap();
            //client connects to ipAddress
            try {
                client.connect(ipAddress);
            } catch (IOException e) {
//                logInfo("Can't connect to server: " + ipAddress);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new WaitScreen(game,playmusic));

                    }
                });
            }
        }

        myIP = client.getIP();

    }

    public void resetbuttons() {
        Buddha.setChecked(false);
        foxy.setChecked(false);
        ponti.setChecked(false);
        kappa.setChecked(false);
        krishna.setChecked(false);
        madame.setChecked(false);
    }

    public void playGame(){
        stage.clear();
        game.setScreen(new PlayScreen(game, isHost, playername, client, server,playmusic));
    }

//    public void goPlayScreen() {
//        game.setScreen(new PlayScreen(game, isHost, playername, client, server));
//
//    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //BitmapFont font=new BitmapFont(Gdx.files.internal("Fonts/crimesFont36Black"));


        //batcher.setProjectionMatrix(camera.combined);
        batcher.begin();
       // sprite.draw(batcher);

        style.font.draw(batcher, "My Special ability is to", viewport.getScreenWidth() / 3, viewport.getScreenHeight() - 250);
        style.font.draw(batcher, Playerability, viewport.getScreenWidth() / 3, viewport.getScreenHeight() - 275);
        style.font.draw(batcher, "the world",viewport.getScreenWidth()/3,viewport.getScreenHeight()-300);
        style.font.draw(batcher, "Enter IP : " + myIP, viewport.getScreenWidth()/3,viewport.getScreenHeight()/7);
        playercharacter.setPosition(viewport.getScreenWidth()/4,viewport.getScreenHeight()/12);
        playercharacter.setSize(viewport.getScreenWidth()*0.75f,viewport.getScreenHeight()*0.75f);

        playercharacter.draw(batcher);
        batcher.end();

        stage.act();
        stage.draw();


        //if (TimeUtils.millis()>(startTime+15000)) game.setScreen(new PlayScreen(game,isHost,ipAddress, playername));

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        dispose();
    }

    @Override
    public void dispose() {
        atlas1.dispose();
        atlas2.dispose();
        stage.dispose();
        skin.dispose();
     //   sprite.getTexture().dispose();
        batcher.dispose();
        music.dispose();
        soundclick.dispose();
    }
}
