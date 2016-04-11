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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lemoninc.nimbusrun.TapTap3;

import java.util.Random;

//import com.lemoninc.nimbusrun.Networking.Networking;

public class WaitScreen implements Screen{
    private TapTap3 game;
    private OrthographicCamera gamecam;
    private Viewport gameport;
    private SpriteBatch batch;
    private Sprite aspectRatio;
    private long startTime;
    private int playernumber;
//    private Networking network;

    private Random random = new Random();

    /**
     * This constructor instantiates the Sprites, Viewport, Camera, etc
     * @param game The Game object
     */
    public WaitScreen(TapTap3 game) {
        this.game = game;
        gamecam = new OrthographicCamera();
        gameport = new FitViewport(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM, gamecam);

        batch = new SpriteBatch();
        aspectRatio = new Sprite(new Texture("playerShip.png")); //background
        aspectRatio.setPosition(0, 0);
        aspectRatio.setSize(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM);

        //initialise network
        //connect to server and configure socket events (receive client ID, all the other player's ID when they join, half empty hashmap
//        network = new Networking();
//        network.connectToServer();
//        network.configSocketEvents();
        Gdx.app.log("WaitScreen", "Finished connecting & configuring events");
        playernumber=1;
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

        //create UI stuff
        //Label gameTitle = new Label("Nimbus Run", skin);
        //set color, x y coordinates

        //Button

        //Textfield

        //...

        //stage.addActor(UI stuff);

        //Events
        //setListeners here

    }
    public void update(float dt) {
        handleInput();
        gamecam.update();
    }

    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
           // startTime = TimeUtils.millis();
            //if (TimeUtils.millis()<(startTime+5000)){
              //  game.setScreen(new YourCharacterScreen(game,game.V_WIDTH,game.V_HEIGHT,playernumber));
            //}
                playGame();


        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(gamecam.combined);
        batch.begin();
        aspectRatio.draw(batch);
        batch.end();
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

    }

    @Override
    public void dispose() {
        aspectRatio.getTexture().dispose();
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
