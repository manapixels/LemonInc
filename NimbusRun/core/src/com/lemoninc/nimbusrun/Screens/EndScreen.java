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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.lemoninc.nimbusrun.Sprites.GameMap;
import com.lemoninc.nimbusrun.Sprites.Player;

import java.util.Map;

public class EndScreen implements Screen{

    private NimbusRun game;
    private GameMap gameMap;
    private OrthographicCamera gamecam;
    private Viewport gameport;
    private Map<Integer, Player> players;

    private SpriteBatch batch;
    private Sprite aspectRatio;
    private TextButton.TextButtonStyle style;
    private TextButton Continue;
    private Stage stage;

    public EndScreen(NimbusRun game, Map<Integer, Player> players){
        this.game = game;
        gamecam = new OrthographicCamera();
        gameport = new FitViewport(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM, gamecam);
        stage= new Stage(new ExtendViewport(game.V_WIDTH, game.V_HEIGHT));
        this.players = players;
        batch = new SpriteBatch();

        aspectRatio = new Sprite(new Texture("5_EndScreen/bg.png"));

        aspectRatio.setPosition(0, 0);
        aspectRatio.setSize(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM);

        style = new TextButton.TextButtonStyle();  //can customize
        style.font = new BitmapFont(Gdx.files.internal("Fonts/crimesFont48Black.fnt"));
        style.font.setColor(Color.BLUE);
        style.font.getData().setScale(0.65f, 0.65f);
        style.up= new TextureRegionDrawable(new TextureRegion(new Texture("5_EndScreen/button_up.png")));
        style.down= new TextureRegionDrawable(new TextureRegion(new Texture("5_EndScreen/button_down.png")));

        Continue = new TextButton("Click to Return", style);
        Continue.setSize(250, 75);
        Continue.setPosition(500, 100, Align.bottomLeft);
        stage.addActor(Continue);

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
        //aspectRatio.draw(batch);

        // Render Players
        for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
            Player curPlayer = playerEntry.getValue();
            curPlayer.draw(batch);
            //if(curPlayer != playerLocal) curPlayer.renderNameTag(spriteBatch, fontNameTag);
        }

        batch.end();

        stage.act();
        stage.draw();
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
    public void show() {
        Continue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, gamecam.viewportWidth, gamecam.viewportHeight));
            }

        });

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void pause() {    }

    @Override
    public void resume() {    }

    @Override
    public void hide() {  dispose();  }

    public Viewport getGamePort(){
        return gameport;
    }

    @Override
    public void dispose() {
        aspectRatio.getTexture().dispose();
        stage.dispose();
    }
}
