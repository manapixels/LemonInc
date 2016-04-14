package com.lemoninc.nimbusrun.Screens;

/*********************************
 * FILENAME : WaitScreen.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       void    playGame()
 *       void    show()
 *       void    update(float delta)
 *       void    render(float delta)
 *       void    resize(int width, int height)
 *       void    pause()
 *       void    resume()
 *       void    hide()
 *       void    dispose()
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lemoninc.nimbusrun.NimbusRun;

import java.awt.TextField;
import java.util.Random;

//import com.lemoninc.nimbusrun.Networking.Networking;

//import com.lemoninc.nimbusrun.Networking.Networking;

public class WaitScreen implements Screen{
    private NimbusRun game;
    private Camera gamecam;
    private Viewport gameport;

    private long startTime;
    private int playernumber;

    private float BUTTON_WIDTH;
    private float BUTTON_HEIGHT;

    private SpriteBatch batch;
    private Texture background;
    private Sprite sprite;

    private Stage stage;
    //private Skin skin;
    private TextButton hostbutton;
    private TextButton clientbutton;

    private com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle textFieldStyle;
    private TextButton.TextButtonStyle style;
    private float gameWidth;
    private float gameHeight;

//    private Networking network;
    private TextField playerIP;
    private TextField playername;
    private Random random = new Random();

    /**
     * This constructor instantiates the Sprites, Viewport, Camera, etc
     * @param game The Game object
     */
    public WaitScreen(NimbusRun game) {
        this.game = game;
        this.gameHeight=game.V_HEIGHT;
        this.gameWidth=game.V_WIDTH;

        //skin=new Skin(Gdx.files.internal("data/uiskin.json"),new TextureAtlas("data/uiskin.atlas"));

        BUTTON_HEIGHT=50;
        BUTTON_WIDTH=120;
//
//        textFieldStyle=new com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle();
//        textFieldStyle.fontColor=Color.MAROON;
//        textFieldStyle.font=new BitmapFont(Gdx.files.internal("Fonts/crimesFont48Black.fnt"));


        style=new TextButton.TextButtonStyle();
        style.font=new BitmapFont(Gdx.files.internal("Fonts/crimesFont48Black.fnt"));
        style.font.setColor(Color.RED);
        style.font.getData().setScale(0.65f, 0.65f);
        style.up=new TextureRegionDrawable(new TextureRegion(new Texture("button_up.png")));
        style.down=new TextureRegionDrawable(new TextureRegion(new Texture("button_down.png")));

        gamecam=new PerspectiveCamera();
        gameport=new FitViewport(gameWidth,gameHeight,gamecam);
        stage= new Stage(new ExtendViewport(gameWidth,gameHeight));
        hostbutton=new TextButton("Join as Host",style);
        clientbutton=new TextButton("Join as Client",style);


        //initialise network
        //connect to server and configure socket events (receive client ID, all the other player's ID when they join, half empty hashmap
//        network = new Networking();
//        network.connectToServer();
//        network.configSocketEvents();
        Gdx.app.log("WaitScreen", "Finished connecting & configuring events");
        playernumber=1;
        show();
    }

    /**
     * Play game as host for now
     */
    private void playGame() {
        hostGame();
    }

    /**
     * UI stuff to go in here
     */
    @Override
    public void show() {

        batch= new SpriteBatch();
        background= new Texture("whitebackground.png");
        background.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        sprite=new Sprite(background);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


//        playerIP=new TextField("");
//        playerIP.setSize(150, 50);
//        //playerIP.setPosition(this.gameWidth / 2, 300, Align.center);
//        playerIP.setText("Enter Ip address");
//        stage.addActor(playerIP);
//
//        playername=new TextField("");
//        playername.setSize(150,50);
//        //playername.setCaretPosition(this.gameWidth/2,200,Align.center);
//        playername.setText("Enter your name");
//        stage.addActor(playername);

        hostbutton.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        hostbutton.setPosition(this.gameWidth / 3 * 2, 300, Align.center);
        stage.addActor(hostbutton);

        clientbutton.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        clientbutton.setPosition(this.gameWidth / 3 * 2, 200, Align.center);
        stage.addActor(clientbutton);

        hostbutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.setScreen(new TutorialScreen(game, gameWidth, gameHeight));
                playGame();
            }
        });

        clientbutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                game.setScreen(new StoryLineScreen(game, gameWidth, gameHeight));
            }
        });

        Gdx.input.setInputProcessor(stage);
    }
    public void update(float dt) {
        handleInput();
        gamecam.update();
    }

    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            playGame();
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            game.setScreen(new PlayScreen(game, false, "localhost", getName()));
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        batch.setProjectionMatrix(gamecam.combined);
        batch.begin();
        sprite.draw(batch);
        batch.end();


        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
        gamecam.position.set(gamecam.viewportWidth / 2, gamecam.viewportHeight / 2, 0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        sprite.getTexture().dispose();
    }

    /**
     * join a game room
     */
    private void joinGame(){
        game.setScreen(new PlayScreen(game, false, "HOSTSIPADDRESS", getName()));
        //TODO: setscreen(new playscreen(game, host-false, the IP address you are joining, getname())
        //save preferences
    }

    /**
     * play game as a host
     */
    private void hostGame(){
        game.setScreen(new PlayScreen(game, true, "localhost", getName()));
        //TODO: setscreen(new playscreen(game, host-true, the IP address you are hosting from - localhost, getname())
        //save preferences
    }

    /**
     * This method gets the name of the player from the name textfield.
     * If the name is empty, the player is assigned a random name. This random name is then saved in the textfield.
     *
     * @return player's name
     */
    //TODO: textfield
    private String getName(){
        String name = "";
//        String name = get text from the textfield in the waiting screen
        if (name.isEmpty()) {
            name = "Player" + random.nextInt(10000);
        }
        //textfieldname.settext(name);
        return name;
    }
}