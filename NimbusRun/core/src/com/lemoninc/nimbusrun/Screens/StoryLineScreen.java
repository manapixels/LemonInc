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
public class StoryLineScreen implements Screen{
    Texture background;
    private SpriteBatch batch;
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
    private TextButton Continue;
    Sound soundclick;



    public StoryLineScreen(NimbusRun game,float gameWidth,float gameHeight){
        this.game = game;
        this.gameWidth =gameWidth;
        this.gameHeight = gameHeight;

        BUTTON_WIDTH = 250;
        BUTTON_HEIGHT = 75;

        soundclick=Gdx.audio.newSound(Gdx.files.internal("Sounds/click.mp3"));

        style = new TextButton.TextButtonStyle();  //can customize
        style.font = new BitmapFont(Gdx.files.internal("Fonts/crimesFont48Black.fnt"));
        style.font.setColor(Color.BLUE);
        style.font.getData().setScale(0.65f, 0.65f);
        style.up= new TextureRegionDrawable(new TextureRegion(new Texture("0_StorylineScreen/button_up.png")));
        style.down= new TextureRegionDrawable(new TextureRegion(new Texture("0_StorylineScreen/button_down.png")));

        camera=new PerspectiveCamera();
        viewport=new FitViewport(gameWidth,gameHeight,camera);
        stage= new Stage(new ExtendViewport(gameWidth,gameHeight));

        Continue = new TextButton("Click to Return", style);
        show();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background=new Texture("0_StorylineScreen/bg.png");
        background.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        sprite = new Sprite(background);
        //   sprite.setColor(1, 1, 1, 0);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Continue.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        Continue.setPosition(500, 100, Align.bottomLeft);
        stage.addActor(Continue);

        Continue.addListener(new ClickListener() {
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

        batch.begin();
        sprite.draw(batch);
        batch.end();

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
        stage.dispose();
       // soundclick.dispose();
    }
}
