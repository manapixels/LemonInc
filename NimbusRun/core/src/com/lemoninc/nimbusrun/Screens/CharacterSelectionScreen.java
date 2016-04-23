package com.lemoninc.nimbusrun.Screens;

/*********************************
 * FILENAME : CharacterSelectionScreen.java
 * DESCRIPTION : Player selects his character here. The character's name will be
 *               passed to PlayScreen/Player to choose the specific character.
 *               Consists mainly button listeners
 * PUBLIC FUNCTIONS :
 *
    --SCREEN TRANSITION--
 *      void    resetbuttons
 *      void    playGame
 *      void    goToMenu
 *
    -- LIBGDX METHODS
 *      void    show
 *      void    render
 *      void    resize
 *      void    pause
 *      void    resume
 *      void    hide
 *      void    dispose

 * NOTES : The screen takes input from the WaitScreen and moves to PlayScreen
 * LAST UPDATED: 23/4/2016 09:02
 *
 * ********************************/

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

public class CharacterSelectionScreen implements Screen{
    public SpriteBatch batch;
    private NimbusRun game;
    private float gameWidth, gameHeight;
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
    Boolean playmusic;
    Music music;
    Sound soundclick;
    public int charactername = 99;
    private String myIP;

    private TapTapClient client;
    private TapTapServer server;
    private GameMap gamemap;

    private int[] mapData;

    public CharacterSelectionScreen(final NimbusRun game, SpriteBatch batch, final boolean isHost, String playerName, final Boolean playmusic){
        this.game = game;
        this.batch = batch;
        this.isHost = isHost;
        this.playername = playerName;
        this.gameWidth = NimbusRun.V_WIDTH;
        this.gameHeight = NimbusRun.V_HEIGHT;
        this.playmusic=playmusic;

        charactername=1; //default character is Buddha

        BUTTON_HEIGHT=165;
        BUTTON_WIDTH=140;

        //myIP=ipAddress;
        charactername=1; //default character is Buddha
        BUTTON_HEIGHT=150;
        BUTTON_WIDTH=125;
        soundclick=Gdx.audio.newSound(Gdx.files.internal("Sounds/click.mp3"));

        music=Gdx.audio.newMusic(Gdx.files.internal("Sounds/characterselectionscreen.mp3"));
        music.setVolume(0.5f);   // sets the volume to half the maximum volume
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

        style = new TextButton.TextButtonStyle();
        style.font=new BitmapFont(Gdx.files.internal("Fonts/crimesFont48Black.fnt"));
        style.font.setColor(Color.DARK_GRAY);
        style.font.getData().setScale(0.65f, 0.65f);
        style.up=skin.getDrawable("button_up");
        style.over=skin.getDrawable("button_down");
        style.down=skin.getDrawable("button_down");

        stage = new Stage(new ExtendViewport(gameWidth,gameHeight));
        stage.clear();

        final Table table = new Table();
        table.right();
        table.setFillParent(true);

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


        Buddha.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetbuttons();
                playercharacter = skin.getSprite("bg_Buddha");
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                charactername = 1;
            }
        });

        foxy.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetbuttons();
                playercharacter= skin.getSprite("bg_Foxy");
                playercharacter.setPosition(0, 0);
                playercharacter.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                charactername=3;
            }
        });

        kappa.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetbuttons();
                playercharacter= skin.getSprite("bg_Kappa");
                playercharacter.setPosition(0, 0);
                playercharacter.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                charactername=4;
            }
        });
        krishna.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetbuttons();
                playercharacter= skin.getSprite("bg_Krishna");
                playercharacter.setPosition(0, 0);
                playercharacter.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                charactername=2;
            }
        });
        madame.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetbuttons();
                playercharacter= skin.getSprite("bg_Madame");
                playercharacter.setPosition(0, 0);
                playercharacter.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                charactername=6;
            }
        });
        ponti.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                resetbuttons();
                playercharacter= skin.getSprite("bg_Pontianak");
                playercharacter.setPosition(0, 0);
                playercharacter.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                charactername=5;
            }
        });
        joingame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundclick.play();
                music.stop();
                
                Gdx.app.log("GDX CSscreen", "Character " + charactername + " selected for the player");
                //send server charactername packet
                if (charactername != 99) { //if character is chosen
                    Network.Ready ready = new Network.Ready(charactername);
                    client.sendMessage(ready);
                    gamemap.declareCharacter(charactername);
                    Gdx.app.log("GDX CSscreen", "I declared my character to GameMap");

                }
                if (isHost) {
                    //if received charactername from all players, play game
                    if (server.allDummyReady()) {
                        //send to server GameReady
                        Network.GameReady gameready = new Network.GameReady();
                        client.sendMessage(gameready);
                        playGame();
                    }
                    else {
                        Gdx.app.log("GDX CSscreen", "Not all dummies ready");
                    }
                }
            }
        });

        goback.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundclick.play();
                goToMenu();
                music.stop();
            }
        });


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        playercharacter=new Sprite(skin.getSprite("bg_Buddha"));
        style.font = new BitmapFont(Gdx.files.internal("Fonts/crimesFont36Black.fnt"));
        style.font.getData().setScale(0.7f, 0.7f);
        style.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        style.font.setColor(Color.DARK_GRAY);
        startTime = TimeUtils.millis();


        //instantiate server, client here

        if (isHost) {
            mapData = new int[GameMap.NUMPLATFORMS];
            Random rand = new Random();
            for (int i = 0; i < 8; i++){
                mapData[i] = rand.nextInt(3);
            }
            Gdx.app.log("GDX CSscreen", "Mapdata only created by the Host");
            client = new TapTapClient(game, this, playername, mapData);
            gamemap = client.getMap();
            //start my server and connect my client to my server
            try {
                server = new TapTapServer(mapData);
                client.connect("localhost");
            } catch (IOException e) {
                Gdx.app.log("GDX CSscreen", "Host cannot connect to server, setting to WaitScreen");
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new WaitScreen(game, batch, playmusic));

                    }
                });
            }
        }
        else {
            client = new TapTapClient(game, this, playername, mapData);
            gamemap = client.getMap();
            //client connects to ipAddress
            try {
                Gdx.app.log("GDX CSscreen", "Player connecting to LAN.");
                client.connectLAN();
            } catch (IOException e) {
                Gdx.app.log("GDX CSscreen", "Player cannot connect to server");
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new WaitScreen(game,batch,playmusic));

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
        game.setScreen(new PlayScreen(game, batch, isHost, playername, client, server,playmusic,charactername));
    }

    public void goToMenu(){
        stage.clear();
        client.shutdown();
        if (isHost) server.shutdown();
        game.setScreen(new MenuScreen(game, batch, gameWidth, gameHeight));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        if (myIP != null)
            style.font.draw(batch, "Host IP address: " + myIP, viewport.getScreenWidth()/3,viewport.getScreenHeight()/7);
        playercharacter.setPosition(viewport.getScreenWidth()/4,viewport.getScreenHeight()/12);
        playercharacter.setSize(viewport.getScreenWidth()*0.75f,viewport.getScreenHeight()*0.75f);

        playercharacter.draw(batch);
        batch.end();

        stage.act();
        stage.draw();
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
        music.dispose();
        soundclick.dispose();
        playercharacter.getTexture().dispose();
    }
}
