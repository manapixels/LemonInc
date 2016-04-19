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
 * LAST UPDATED: 19/4/2016 20:07
 *
 * ********************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.esotericsoftware.minlog.Log;
import com.lemoninc.nimbusrun.NimbusRun;
import com.lemoninc.nimbusrun.Sprites.GameMap;

import java.util.ArrayList;
import java.util.List;

public class EndScreen implements Screen{

    private NimbusRun game;
    private GameMap gameMap;
    private OrthographicCamera gamecam;
    private Viewport gameport;
    private List<Integer> rankings;
    private int numPlayers;
    private List<Integer> playerTypes;
    private List<Texture> spritesTXT;
    private List<Sprite> sprites;

    private SpriteBatch batch, winner, second, third, last;
    Music music;
    Boolean playmusic;
    private Sound gongSound;
    private TextButton.TextButtonStyle style;
    private TextButton Continue;
    private Stage stage;

    public EndScreen(NimbusRun game, Boolean playmusic){
        this.playmusic = playmusic;
        this.game = game;

        gamecam = new OrthographicCamera();
        gameport = new FitViewport(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM, gamecam);
        gongSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/chineseGong.mp3"));
//        music=Gdx.audio.newMusic(Gdx.files.internal("Sounds/puppetry_comedy.mp3"));
//        music.setVolume(0.5f);                 // sets the volume to half the maximum volume
//        music.setLooping(true);
//        if(playmusic){
//            music.play();
//        }

        stage= new Stage(new ExtendViewport(game.V_WIDTH, game.V_HEIGHT));

        this.rankings = rankings;

        batch = new SpriteBatch();

        style = new TextButton.TextButtonStyle();  //can customize
        style.font = new BitmapFont(Gdx.files.internal("Fonts/crimesFont48Black.fnt"));
        style.font.setColor(Color.RED);
        style.font.getData().setScale(0.65f, 0.65f);
        style.up= new TextureRegionDrawable(new TextureRegion(new Texture("5_EndScreen/button_up.png")));
        style.down= new TextureRegionDrawable(new TextureRegion(new Texture("5_EndScreen/button_down1.png")));

        Continue = new TextButton("Click to Return", style);
        Continue.setSize(250, 75);
        Continue.setPosition(game.V_WIDTH / game.PPM * 0.8f, game.V_HEIGHT / game.PPM * 0.8f, Align.bottomLeft);
        stage.addActor(Continue);


        /* Waiting for Nikki's ranking arraylist and playerType (kappa? etc.) arraylist */
        playerTypes = new ArrayList<Integer>();
        playerTypes.add(0); playerTypes.add(0); playerTypes.add(1); // Laughing Buddha, Foxy, then Kappa
        rankings = new ArrayList<Integer>();
        rankings.add(1); rankings.add(2); rankings.add(3);
        // player 2 is rank 1, player 1 is rank 2, player 3 is rank 3
        // so Foxy is most right, Kappa is most left

        numPlayers = playerTypes.size();
        gongSound.play();
        initChar();


    }

    @Override
    public void render(float delta) {
//        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        show();
}

    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
    }

    @Override
    public void show() {

        //batch.setProjectionMatrix(gamecam.combined);
        batch.begin();

//        Log.info("width: " + gameport.getWorldWidth() + " " + gameport.getScreenWidth() + " " + gameport.getWorldHeight() + " " + gameport.getScreenHeight());

        for (int i=0; i<rankings.size(); i++) {
            sprites.get(rankings.get(i)-1).setPosition(Gdx.graphics.getWidth() * (numPlayers - (i+1) + 1f)/(numPlayers+1f), Gdx.graphics.getHeight()/2);
            sprites.get(rankings.get(i)-1).draw(batch);
        }

        batch.end();
        Gdx.input.setInputProcessor(stage);

        stage.act();
        stage.draw();
        Continue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SplashScreen(game, gamecam.viewportWidth, gamecam.viewportHeight));
            }
        });
    }

    @Override
    public void pause() {    }

    @Override
    public void resume() {    }

    @Override
    public void hide() {
        dispose();
    }

    public Viewport getGamePort(){
        return gameport;
    }

    @Override
    public void dispose() {
        music.dispose();
        for (Texture txt: spritesTXT) { txt.dispose(); }
        //stage.dispose();
    }

    public void initChar() {

        spritesTXT = new ArrayList<Texture>();

        spritesTXT.add(new Texture("5_EndScreen/LaughingBuddha.png"));
        spritesTXT.add(new Texture("5_EndScreen/Krishna.png"));
        spritesTXT.add(new Texture("5_EndScreen/Foxy.png"));
        spritesTXT.add(new Texture("5_EndScreen/Kappa.png"));
        spritesTXT.add(new Texture("5_EndScreen/Pontianak.png"));
        spritesTXT.add(new Texture("5_EndScreen/WhiteSnake.png"));

        sprites = new ArrayList<Sprite>();
        for (int i=0; i<numPlayers; i++) {
            Sprite sprite;
            switch(playerTypes.get(i)){
                // 1. LAUGHING BUDDHA
                // 2. SHESHNAH WITH KRISHNA
                // 3. NINE-TAILED FOX
                // 4. KAPPA
                // 5. PONTIANAK
                // 6. MADAME WHITE SNAKE
                case 0: sprite = new Sprite(spritesTXT.get(0)); break;
                case 1: sprite = new Sprite(spritesTXT.get(1)); break;
                case 2: sprite = new Sprite(spritesTXT.get(2)); break;
                case 3: sprite = new Sprite(spritesTXT.get(3)); break;
                case 4: sprite = new Sprite(spritesTXT.get(4)); break;
                case 5: sprite = new Sprite(spritesTXT.get(5)); break;
                default: sprite = new Sprite(spritesTXT.get(0)); break;
            }
            sprite.setSize(200, 200);
            sprites.add(sprite);
        }

    }
}
