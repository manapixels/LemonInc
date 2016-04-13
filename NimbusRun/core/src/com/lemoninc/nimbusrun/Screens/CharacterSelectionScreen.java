package com.lemoninc.nimbusrun.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lemoninc.nimbusrun.NimbusRun;

/**
 * Created by Nikki on 12/4/2016.
 */
public class CharacterSelectionScreen implements Screen {
    private SpriteBatch batcher;
    private Sprite sprite;
    private NimbusRun game;
    private float gameWidth;
    private float gameHeight;
    private Viewport viewport;
    private Camera camera;
    Boolean isHost;
    String ipAddress;
    String playername;
    private long startTime;

    public CharacterSelectionScreen(NimbusRun game, boolean isHost, String ipAddress, String playerName){
        this.game = game;
        this.isHost=isHost;
        this.ipAddress=ipAddress;
        this.playername=playerName;
        this.gameWidth = NimbusRun.V_WIDTH;
        this.gameHeight = NimbusRun.V_HEIGHT;
        camera=new PerspectiveCamera();
        viewport=new FitViewport(gameWidth,gameHeight,camera);
    }

    @Override
    public void show() {
        sprite = new Sprite(new Texture("whitebackground.png"));
        //sprite.setColor(1, 1, 1, 0);

        sprite.setPosition(0, 0);
        sprite.setSize(gameWidth, gameHeight);
        batcher = new SpriteBatch();
        startTime = TimeUtils.millis();
    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        sprite.draw(batcher);
        batcher.end();
        if (TimeUtils.millis()>(startTime+5000)) game.setScreen(new PlayScreen(game,isHost,ipAddress, playername));;

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
        dispose();
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
        batcher.dispose();
    }
}
