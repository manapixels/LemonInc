package com.lemoninc.nimbusrun.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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

public class CharDescrScreen implements Screen{
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
    private TextButton Return,Next;
    Sound soundclick;
    int click=0;



    public CharDescrScreen(NimbusRun game,float gameWidth,float gameHeight){
        this.game = game;
        this.gameWidth =gameWidth;
        this.gameHeight = gameHeight;
        camera=new PerspectiveCamera();
        viewport=new FitViewport(gameWidth,gameHeight,camera);
        stage= new Stage(new ExtendViewport(gameWidth,gameHeight));

        soundclick=Gdx.audio.newSound(Gdx.files.internal("Sounds/click.mp3"));


        BUTTON_WIDTH = 150;
        BUTTON_HEIGHT = 75;

        style = new TextButton.TextButtonStyle();  //can customize
        style.font = new BitmapFont(Gdx.files.internal("Fonts/crimesFont48Black.fnt"));
        style.font.setColor(Color.BLUE);
        style.font.getData().setScale(0.65f, 0.65f);
        style.up= new TextureRegionDrawable(new TextureRegion(new Texture("2_TutorialScreen/button_up.png")));
        style.down= new TextureRegionDrawable(new TextureRegion(new Texture("2_TutorialScreen/button_down1.png")));
        style.over= new TextureRegionDrawable(new TextureRegion(new Texture("2_TutorialScreen/button_down1.png")));


        Next= new TextButton("Next",style);
        Return = new TextButton("Return", style);
    }

    @Override
    public void show() {
        batcher = new SpriteBatch();
        sprite = new Sprite(new Texture("2_CharDescrScreen/TutorialScreens_Buddha.png"));
        //   sprite.setColor(1, 1, 1, 0);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Return.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        Return.pad(0.5f);
        Return.setPosition(gameWidth * 0.75f, gameHeight * 0.2f, Align.topLeft);


        Next.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        Next.pad(0.5f);
        Next.setPosition(gameWidth * 0.75f, gameHeight * 0.2f, Align.topLeft);
        stage.addActor(Next);

        Return.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundclick.play();
                //sprite.setTexture(new Texture("2_TutorialScreen/Tutorials_pg2.png"));
                game.setScreen(new MenuScreen(game, gameWidth, gameHeight));
            }

        });
        Next.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundclick.play();
                click++;
                if(click==1)
                    sprite.setTexture(new Texture("2_CharDescrScreen/TutorialScreens_Foxy.png"));
                if(click==2)
                    sprite.setTexture(new Texture("2_CharDescrScreen/TutorialScreens_Kappa.png"));
                if (click==3)
                    sprite.setTexture(new Texture("2_CharDescrScreen/TutorialScreens_Krishna.png"));
                if(click==4)
                    sprite.setTexture(new Texture("2_CharDescrScreen/TutorialScreens_Pontianak.png"));
                if(click==5) {
                    sprite.setTexture(new Texture("2_CharDescrScreen/TutorialScreens_WhiteSnake.png"));
                    Next.remove();
                    stage.addActor(Return);
                }
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
    }

    @Override
    public void dispose() {
        //soundclick.dispose();
        stage.dispose();
    }
}
