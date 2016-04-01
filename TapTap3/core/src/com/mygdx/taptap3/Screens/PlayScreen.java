package com.mygdx.taptap3.Screens;

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
import com.mygdx.taptap3.Networking.Networking;
import com.mygdx.taptap3.Sprites.Ceiling;
import com.mygdx.taptap3.Sprites.EndWall;
import com.mygdx.taptap3.Sprites.Ground;
import com.mygdx.taptap3.Sprites.Player;
import com.mygdx.taptap3.Sprites.StartWall;
import com.mygdx.taptap3.TapTap3;
import java.util.*;

/*
There is a connection between client and server at this stage
 */
public class PlayScreen implements Screen {

    private TapTap3 game;
    private OrthographicCamera gamecam;
    private Viewport gameport;

    private SpriteBatch batch;
    private Sprite background;

    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player, player2, player3, player4;
    private Ground ground;
    private Ceiling ceiling;
    private StartWall startWall;
    private EndWall endWall;

    private Networking network;

    public PlayScreen(TapTap3 game, Networking network){
        this.game = game;
        gamecam = new OrthographicCamera();
        gameport = new FitViewport(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM, gamecam);

        batch = new SpriteBatch();
        background = new Sprite(new Texture("TapTap_BGseamless_long.png"));
        background.setPosition(-gameport.getWorldWidth(), 0);
        background.setSize(background.getWidth() / game.PPM, background.getHeight() / game.PPM);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        //create user's character
        player = new Player(this, "LaughingBuddha.png", 32, 200);

        //pass in the parameters to the other players
//        player2 = new Player(this, "Foxy.png", 150, 200);
//        player3 = new Player(this, "Sheshnag&Krishna.png", -150, 200);
//        player4 = new Player(this, "Madam White Snake.png", 250, 200);

        //initialise walls and ceilings
        ground = new Ground(this);
        ceiling = new Ceiling(this);
        startWall = new StartWall(this);
        endWall = new EndWall(this);

        //TODO: initialise all the other players here
        int i=0;
        for (Map.Entry<String, Player> entry: network.friendlyPlayers.entrySet()) {
            i++;
            entry.getValue().initialise(this, "Foxy.png", i*50 +50, 200 );
        }

        //waitscreen passed network to playscreen
        this.network = network;
    }

    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            player.speed();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            player.slow();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            gameOver();
        }
    }

    @Override
    public void render(float delta) {
        //update the player's coordinates to server realtime
        network.updateServer(delta, player);
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   //clear the screen to black
        b2dr.render(world, gamecam.combined);   //render box2d world lines

        batch.setProjectionMatrix(gamecam.combined);    //set batch to camera
        batch.begin();  //start batch stuff
        background.draw(batch); //draw the background file
        player.draw(batch);
        player2.draw(batch);
        player3.draw(batch);
        player4.draw(batch);

        //TODO: batch draws the other players instead of drawing 4 other dead characters
        for (Map.Entry<String, Player> entry: network.friendlyPlayers.entrySet()) {
            entry.getValue().draw(batch);
        }

        batch.end();    //stop batch stuff

    }

    //TODO: when disconnected, the ball doesn't terminate
    public void update(float dt) {
        handleInput();

        //set the x y coordinate of the player
        player.update(dt);
        if (player.b2body.getPosition().y <= 0){
            gameOver();
        }
        player2.update(dt);
        player3.update(dt);
        player4.update(dt);
        for (Map.Entry<String, Player> entry: network.friendlyPlayers.entrySet()) {
            entry.getValue().update(dt);
        }
        world.step(1 / 60f, 6, 2);
        gamecam.position.set(player.getX(), gamecam.viewportHeight / 2, 0);
        gamecam.update();
    }

    public void gameOver(){
        game.setScreen(new EndScreen(game));
    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
        gamecam.position.set(gamecam.viewportWidth/2, gamecam.viewportHeight/2, 0);
    }

    @Override
    public void show() {    }

    @Override
    public void pause() {    }

    @Override
    public void resume() {    }

    @Override
    public void hide() {    }

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
    }
}
