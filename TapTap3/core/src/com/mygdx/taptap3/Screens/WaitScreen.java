package com.mygdx.taptap3.Screens;

import com.badlogic.gdx.Game;
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
import com.mygdx.taptap3.Networking.Networking;
import com.mygdx.taptap3.TapTap3;

/**
 * Created by kevin on 3/30/2016.
 */
public class WaitScreen implements Screen{
    private TapTap3 game;
    private OrthographicCamera gamecam;
    private Viewport gameport;

    private SpriteBatch batch;
    private Sprite aspectRatio;

    private Networking network;

    public WaitScreen(TapTap3 game) {
        this.game = game;
        gamecam = new OrthographicCamera();
        gameport = new FitViewport(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM, gamecam);

        batch = new SpriteBatch();
        aspectRatio = new Sprite(new Texture("playerShip.png"));
        aspectRatio.setPosition(0, 0);
        aspectRatio.setSize(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM);

        //initialise network
        //connect to server and configure socket events (receive client ID, all the other player's ID when they join, half empty hashmap
        network = new Networking();
        network.connectToServer();
        network.configSocketEvents();
        Gdx.app.log("WaitScreen", "Finished connecting & configuring events");
    }

    private void playGame() {
        game.setScreen(new PlayScreen(game, network));
    }
    @Override
    public void show() {

    }

    public void update(float dt) {
        handleInput();
        gamecam.update();
    }

    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
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
        gamecam.position.set(gamecam.viewportWidth/2, gamecam.viewportHeight/2, 0);
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
}
