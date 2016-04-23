package com.lemoninc.nimbusrun.Screens;

/*********************************
 * FILENAME : SplashScreen.java
 * DESCRIPTION : Displays LemonInc company logo
 * PUBLIC FUNCTIONS :
 *
    --LIBGDX METHODS--
 *      void    show
 *      void    render
 *      void    resize
 *      void    pause
 *      void    resume
 *      void    hide
 *      void    dispose

 * NOTES : Enters from start of the game, exits to MenuScreen
 * LAST UPDATED: 23/4/2016 09:03
 *
 * ********************************/

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

public class SplashScreen implements Screen{
    private SpriteBatch batch;
    private Sprite sprite;
    private NimbusRun game;
    private float gameWidth;
    private float gameHeight;
    private Viewport viewport;
    private Camera camera;
    private long startTime;

    public SplashScreen(NimbusRun game, float gameWidth, float gameHeight) {
        this.game = game;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        camera=new PerspectiveCamera();
        viewport=new FitViewport(gameWidth,gameHeight,camera);

    }
    @Override
    public void show() {
        sprite = new Sprite(new Texture("0_SplashScreen/bg.png"));
        //sprite.setColor(1, 1, 1, 0);

        sprite.setPosition(0, 0);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        startTime = TimeUtils.millis();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);
        batch.end();

        if (TimeUtils.millis()>(startTime+100)) game.setScreen(new MenuScreen(game,batch,gameWidth,gameHeight));

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
