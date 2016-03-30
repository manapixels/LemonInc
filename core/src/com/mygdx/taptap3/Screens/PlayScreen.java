package com.mygdx.taptap3.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.taptap3.Sprites.Ceiling;
import com.mygdx.taptap3.Sprites.EndWall;
import com.mygdx.taptap3.Sprites.Ground;
import com.mygdx.taptap3.Sprites.Player;
import com.mygdx.taptap3.Sprites.StartWall;
import com.mygdx.taptap3.TapTap3;

public class PlayScreen implements Screen {

    private TapTap3 game;
    private OrthographicCamera gamecam;
    private Viewport gameport;

    private SpriteBatch batch;
    private Sprite background;

    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player1, player2, player3, player4;
    private Ground ground;
    private Ceiling ceiling;
    private StartWall startWall;
    private EndWall endWall;

    public PlayScreen(TapTap3 game){
        this.game = game;
        gamecam = new OrthographicCamera();
        gameport = new FitViewport(game.V_WIDTH / game.PPM, game.V_HEIGHT / game.PPM, gamecam);

        batch = new SpriteBatch();
        background = new Sprite(new Texture("TapTap_BGseamless_long.png"));
        background.setPosition(-gameport.getWorldWidth(), 0);
        background.setSize(background.getWidth() / game.PPM, background.getHeight() / game.PPM);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        player1 = new Player(this, "LaughingBuddha.png", 32, 200);
        player2 = new Player(this, "Foxy.png", 150, 200);
        player3 = new Player(this, "Sheshnag_Krishna.png", -150, 200);
        player4 = new Player(this, "Madam White Snake.png", 250, 200);
        ground = new Ground(this);
        ceiling = new Ceiling(this);
        startWall = new StartWall(this);
        endWall = new EndWall(this);
    }

    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player1.jump();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            player1.speed();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            player1.slow();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            gameOver();
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(gamecam.combined);
        batch.begin();
//        draw(TextureRegion region, float x, float y, float width, float height)
//        batch.draw(background,0,0, gameport.getScreenWidth(), gameport.getScreenHeight());
//        batch.draw(background, gamecam.position.x - (gamecam.viewportWidth / 2), 0);
        background.draw(batch);
        player1.draw(batch);
        player1.draw(batch);
        player2.draw(batch);
        player3.draw(batch);
        player4.draw(batch);

        b2dr.render(world, gamecam.combined);

        batch.end();

    }

    public void update(float dt) {
        handleInput();
        player1.update(dt);
        if (player1.b2body.getPosition().y <= 0){
            gameOver();
        }
        player2.update(dt);
        player3.update(dt);
        player4.update(dt);
        world.step(1 / 60f, 6, 2);
        gamecam.position.set(player1.getX(), gamecam.viewportHeight / 2, 0);
        gamecam.update();
    }

    public void gameOver(){
        game.setScreen(new EndScreen(game));
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

    public World getWorld(){
        return world;
    }
    public Viewport getGamePort(){
        return gameport;
    }

    @Override
    public void dispose() {
        world.dispose();
        background.getTexture().dispose();
    }
}
