package com.lemoninc.nimbusrun.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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

/**
 * Created by Nikki on 10/4/2016.
 */
public class TutorialScreen implements Screen{
    private SpriteBatch batcher;
    private Sprite sprite;
    private NimbusRun game;
    private float gameWidth;
    private float gameHeight;
    private Viewport viewport;
    private com.badlogic.gdx.graphics.Camera camera;
    private float BUTTON_WIDTH;
    private float BUTTON_HEIGHT;
    private Stage stage;

    private TextButton.TextButtonStyle style;
    private TextButton Return;
    Sound soundclick;



    public TutorialScreen(NimbusRun game,float gameWidth,float gameHeight){
        this.game = game;
        this.gameWidth =gameWidth;
        this.gameHeight = gameHeight;
        camera=new PerspectiveCamera();
        viewport=new FitViewport(gameWidth,gameHeight,camera);
        stage= new Stage(new ExtendViewport(gameWidth,gameHeight));

        soundclick=Gdx.audio.newSound(Gdx.files.internal("Sounds/click.mp3"));


        BUTTON_WIDTH = 250;
        BUTTON_HEIGHT = 75;

        style = new TextButton.TextButtonStyle();  //can customize
        style.font = new BitmapFont(Gdx.files.internal("Fonts/crimesFont48Black.fnt"));
        style.font.setColor(Color.BLUE);
        style.font.getData().setScale(0.65f, 0.65f);
        style.up= new TextureRegionDrawable(new TextureRegion(new Texture("button_up.png")));
        style.down= new TextureRegionDrawable(new TextureRegion(new Texture("button_down.png")));


        Return = new TextButton("Return to main menu", style);
    }

    @Override
    public void show() {
        batcher = new SpriteBatch();
        sprite = new Sprite(new Texture("Plain-Brown-Worship-Background.png"));
        //   sprite.setColor(1, 1, 1, 0);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Return.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        Return.setPosition(500, 100, Align.bottomLeft);
        stage.addActor(Return);

        Return.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundclick.play();
                game.setScreen(new MenuScreen(game, gameWidth, gameHeight));
            }

        });

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        sprite.draw(batcher);
        batcher.end();
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
        dispose();
    }

    @Override
    public void dispose() {
        //soundclick.dispose();
        stage.dispose();
    }

}
