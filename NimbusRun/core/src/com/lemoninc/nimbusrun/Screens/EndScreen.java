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
import com.badlogic.gdx.audio.Music;
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
import com.lemoninc.nimbusrun.Sprites.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EndScreen implements Screen{

    private NimbusRun game;
    private GameMap gameMap;
    private OrthographicCamera gamecam;
    private Viewport gameport;
    private Map<Integer, Player> players;
    private List<Integer> rankings;
    private int numPlayers;

    private SpriteBatch batch, winner, second, third, last;
    Music music;
    Boolean playmusic;
    private TextButton.TextButtonStyle style;
    private TextButton Continue;
    private Stage stage;

    private List<Sprite> dummySprites;

    public EndScreen(NimbusRun game, Boolean playmusic, Map<Integer, Player> players, List<Integer> rankings){
        this.playmusic=playmusic;
        this.game = game;
        gamecam = new OrthographicCamera();
        gameport = new FitViewport(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM, gamecam);

        music=Gdx.audio.newMusic(Gdx.files.internal("Sounds/puppetry_comedy.mp3"));
        music.setVolume(0.5f);                 // sets the volume to half the maximum volume
        music.setLooping(true);
        if(playmusic){
            music.play();
        }

        stage= new Stage(new ExtendViewport(game.V_WIDTH, game.V_HEIGHT));
        this.players = players;
        this.rankings = rankings;

        //Log.info(players.size() + " size");

        batch = new SpriteBatch();

        style = new TextButton.TextButtonStyle();  //can customize
        style.font = new BitmapFont(Gdx.files.internal("Fonts/crimesFont48Black.fnt"));
        style.font.setColor(Color.RED);
        style.font.getData().setScale(0.65f, 0.65f);
        style.up= new TextureRegionDrawable(new TextureRegion(new Texture("5_EndScreen/button_up.png")));
        style.down= new TextureRegionDrawable(new TextureRegion(new Texture("5_EndScreen/button_down1.png")));

        Continue = new TextButton("Click to Return", style);
        Continue.setSize(250, 75);
        //Continue.setPosition(game.V_WIDTH/game.PPM*0.8f, game.V_HEIGHT/game.PPM*0.8f, Align.bottomLeft);
        Continue.setPosition(game.V_WIDTH / game.PPM * 0.8f, game.V_HEIGHT / game.PPM * 0.8f, Align.bottomLeft);
        stage.addActor(Continue);

        /*
        *  remove code below after removal of dummyPlayers
         */
        dummySprites = new ArrayList<Sprite>();
        dummySprites.add(new Sprite(new Texture(Gdx.files.internal("5_EndScreen/btn_kappa.png"))));
        dummySprites.add(new Sprite(new Texture(Gdx.files.internal("5_EndScreen/btn_madame.png"))));
        dummySprites.add(new Sprite(new Texture(Gdx.files.internal("5_EndScreen/btn_kappa.png"))));
        dummySprites.get(0).setSize(150, 150);
        dummySprites.get(1).setSize(150, 150);
        dummySprites.get(2).setSize(150, 150);

        int i=1;

        float positionX = Gdx.graphics.getWidth() / 4;
        Log.info("Hello" + positionX);
        int numPlayers = dummySprites.size();
        for (Sprite sprite : dummySprites) {
            int ranking = i++;

            // formula for setting X positions based on rankings
            // ((numPlayers - ranking + 1)/(numPlayers+1))
            Log.info("numplayers: "+ numPlayers + " " + ranking + " " + Gdx.graphics.getWidth());
            sprite.setPosition(((gameport.getWorldWidth() * ((numPlayers - ranking + 1f)/(numPlayers+1f)))),gameport.getWorldHeight()/2);
            // positionX += (Gdx.graphics.getWidth() * ((numPlayers - ranking + 1)/(numPlayers+1)));
            Log.info("pos " + (Gdx.graphics.getWidth() * ((numPlayers - ranking + 1f)/(numPlayers+1f))));
        }

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
        //Log.info("hello" + dummySprites.size() + " " + rankings.size());

        for (Sprite sprite : dummySprites) {
            sprite.draw(batch);
        }

        /*
        Use the code below to replace the dummy player sprites
         */
//        int numPlayers = players.size();
//        for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
//            Player curPlayer = playerEntry.getValue();
//            int ranking = rankings.indexOf(playerEntry.getKey());
//            // formula for setting X positions based on rankings
//            curPlayer.setX(game.V_WIDTH/game.PPM * ((numPlayers - ranking + 1)/(numPlayers+1)));
//            Log.info("pos " + (game.V_WIDTH/game.PPM * ((numPlayers - ranking + 1)/(numPlayers+1))));
//            curPlayer.draw(batch);
//        }

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
    public void hide() {  dispose();  }

    public Viewport getGamePort(){
        return gameport;
    }

    @Override
    public void dispose() {
        //stage.dispose();
        music.dispose();
        //gameMap.getWorld().dispose();
        //stage.dispose();
//        for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
//            playerEntry.getValue().dispose();
//        }
        //this.players.clear();
    }
}
