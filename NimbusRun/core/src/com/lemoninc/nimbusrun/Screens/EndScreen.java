package com.lemoninc.nimbusrun.Screens;

/*********************************
 * FILENAME : EndScreen.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       void       render(float delta)
 *       void       update(float delta)
 *       void       resize(int width, int height)
 *       void       show()
 *       void       pause()
 *       void       review()
 *       void       hide()
 *       Viewport   getGamePort(()
 *       void       dispose()
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lemoninc.nimbusrun.NimbusRun;

public class EndScreen implements Screen{

    private NimbusRun game;
    private OrthographicCamera gamecam;
    private Viewport gameport;

    private SpriteBatch batch;
    private Sprite aspectRatio;

    public EndScreen(NimbusRun game){
        this.game = game;
        gamecam = new OrthographicCamera();
        gameport = new FitViewport(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM, gamecam);

        batch = new SpriteBatch();
        aspectRatio = new Sprite(new Texture("whitebackground.png"));
        aspectRatio.setPosition(0, 0);
        aspectRatio.setSize(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM);
    }

    protected void handleInput() {
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

    public void update(float delta) {
        handleInput();
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

    public Viewport getGamePort(){
        return gameport;
    }

    @Override
    public void dispose() {
        aspectRatio.getTexture().dispose();
    }
}
