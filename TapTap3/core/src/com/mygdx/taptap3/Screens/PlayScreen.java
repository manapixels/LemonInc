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
import com.mygdx.taptap3.Sprites.Ground;
import com.mygdx.taptap3.Sprites.Player;
import com.mygdx.taptap3.Sprites.Starship;
import com.mygdx.taptap3.TapTap3;

import java.util.HashMap;

public class PlayScreen implements Screen {

    private TapTap3 game;
    private OrthographicCamera gamecam;
    private Viewport gameport;

    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player1, player2;
    private Ground ground;

    private SpriteBatch batch;
    private Sprite aspectRatio;

    private Networking network;
    private Starship player;


    public PlayScreen(TapTap3 game){
        this.game = game;
        gamecam = new OrthographicCamera();
        gameport = new FitViewport(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM, gamecam);

        batch = new SpriteBatch();
        aspectRatio = new Sprite(new Texture("TapTap_BGseamless.png"));
        aspectRatio.setPosition(0, 0);
        aspectRatio.setSize(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM);

        world = new World(new Vector2(0, -15), true);
        b2dr = new Box2DDebugRenderer();

        player1 = new Player(this, 32, 100);
        player2 = new Player(this, 32, 200);
        ground = new Ground(this);

        network = new Networking();
        network.connectToServer();
        network.configSocketEvents();
    }

    protected void handleInput() {
//        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
//            player1.jump();
//        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
//            player1.speed();
//        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
//            player1.slow();

        if (player != null) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                player.setPosition(player.getX() + (-20f), player.getY());
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                player.setPosition(player.getX() + (20f), player.getY());
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        network.updateServer(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(gamecam.combined);

        player = network.getPlayerStarship();

        batch.begin();
        aspectRatio.draw(batch);

        //draws player's character
        if ((player ) != null) {
            Gdx.app.log("Networking", "player drawn");
            player.draw(batch);
        }
        //draws every other player's character in the hashmap
        for (HashMap.Entry<String, Starship> entry: network.friendlyPlayers.entrySet()) {
            entry.getValue().draw(batch);
        }


        batch.end();

        b2dr.render(world, gamecam.combined);
    }

    public void update(float dt) {
        handleInput();
        player1.update(dt);
        player2.update(dt);
        world.step(1 / 60f, 6, 2);
        gamecam.position.set(player1.getX(), gamecam.viewportHeight / 2, 0);
        gamecam.update();
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
        aspectRatio.getTexture().dispose();
        network.dispose();
    }
}
