package com.lemoninc.nimbusrun.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lemoninc.nimbusrun.Assetloading.AssetLoader;
import com.lemoninc.nimbusrun.NimbusRun;

/**
 * Created by Nikki on 8/4/2016.
 */
public class MenuScreen implements Screen {
    private Viewport viewport;
    private Camera camera;
    private float gameWidth;
    private float gameHeight;
    private float BUTTON_WIDTH;
    private float BUTTON_HEIGHT;

    private SpriteBatch batch;
    private Texture background;
    private Sprite sprite;

    private TextButton.TextButtonStyle style;

    private Stage stage;

    private TextButton buttonStory;
    private TextButton buttonChooseCharacter;
    private TextButton buttonPlay;
    private TextButton buttonTutorial;

    private Image muteButton;//if sound implemented
    private Image unmuteButton;

    private NimbusRun game;


    public MenuScreen(NimbusRun game,float gameWidth,float gameHeight){
        this.gameWidth=gameWidth;
        this.gameHeight=gameHeight;
        this.game=game;

        BUTTON_HEIGHT=75;
        BUTTON_WIDTH=120;

        style=new TextButton.TextButtonStyle();
        style.font=new BitmapFont(Gdx.files.internal("Fonts/crimesFont48Black.fnt"));
        style.font.setColor(Color.RED);
        style.font.getData().setScale(0.65f, 0.65f);
        style.up=new TextureRegionDrawable(new TextureRegion(new Texture("button_up.png")));
        style.down=new TextureRegionDrawable(new TextureRegion(new Texture("button_down.png")));
        style.over=new TextureRegionDrawable(new TextureRegion(new Texture("button_down1.png")));


        camera=new PerspectiveCamera();
        viewport=new FitViewport(gameWidth,gameHeight,camera);
        stage= new Stage(new ExtendViewport(gameWidth,gameHeight));
        buttonPlay=new TextButton("Start Play",style);
        buttonChooseCharacter=new TextButton("Choose Avatar",style);
        buttonTutorial=new TextButton("Tutorial",style);
        buttonStory=new TextButton("Story", style);

        muteButton=new Image(new TextureRegionDrawable(new TextureRegion(new Texture("muteButton.png"))));
        unmuteButton=new Image(new TextureRegionDrawable(new TextureRegion(new Texture("unmuteButton.png"))));
        show();

    }

    @Override
    public void show() {
        if (AssetLoader.gameMusic != null) {
            AssetLoader.gameMusic.stop();
            AssetLoader.disposeSFX();
        }
        batch= new SpriteBatch();
        background= new Texture("Home_Start Page.png");
        background.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        sprite=new Sprite(background);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        buttonPlay.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        buttonPlay.setPosition(this.gameWidth/3*2+50, 300, Align.center);
        stage.addActor(buttonPlay);

        buttonTutorial.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        buttonTutorial.setPosition(this.gameWidth/3*2+50, 250, Align.center);
        stage.addActor(buttonTutorial);

        buttonStory.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        buttonStory.setPosition(this.gameWidth/3*2+50, 200, Align.center);
        stage.addActor(buttonStory);

        buttonChooseCharacter.setSize(this.BUTTON_WIDTH, this.BUTTON_HEIGHT);
        buttonChooseCharacter.setPosition(gameWidth/3*2+50, 150, Align.center);
        stage.addActor(buttonChooseCharacter);

        muteButton.setPosition(700, 100);
        unmuteButton.setPosition(700, 100);

        if (com.lemoninc.nimbusrun.Assetloading.AssetLoader.VOLUME == 1) {
            stage.addActor(muteButton);
        } else {
            stage.addActor(unmuteButton);
        }

        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //System.out.println("Play Button Pressed");
                // AssetLoader.clickSound.play(AssetLoader.VOLUME);
                // Host multiplayer game
                game.setScreen(new WaitScreen(game));

            }
        });
        buttonTutorial.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
              //  AssetLoader.clickSound.play(AssetLoader.VOLUME);
                game.setScreen(new TutorialScreen(game, gameWidth, gameHeight));
                // TODO Set to tutorial screen
                //  gsm.set(new TutorialScreen(game, gsm));
            }
        });

        buttonStory.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
          //      AssetLoader.clickSound.play(AssetLoader.VOLUME);
                game.setScreen(new StoryLineScreen(game,gameWidth,gameHeight));
                // TODO Set to tutorial screen
                //  gsm.set(new StoryScreen(game, gsm));
            }
        });
        buttonChooseCharacter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
             //   AssetLoader.clickSound.play(AssetLoader.VOLUME);
                game.setScreen(new YourCharacterScreen(game,gameWidth,gameHeight,1));
                // TODO Set to tutorial screen
                //  gsm.set(new StoryScreen(game, gsm));
            }
        });

        muteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AssetLoader.muteSFX();
                muteButton.remove();
                stage.addActor(unmuteButton);
            }

        });

        unmuteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AssetLoader.unmuteSFX();
                AssetLoader.clickSound.play(AssetLoader.VOLUME);
                unmuteButton.remove();
                stage.addActor(muteButton);
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
        batch.dispose();
    }

}
