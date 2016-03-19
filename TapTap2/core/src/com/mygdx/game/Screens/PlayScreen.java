package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Scenes.TapTapController;

/**
 * Created by Wei Sheng on 10/3/2016.
 */
public class PlayScreen implements Screen {
    // Reference to our Game, used to set Screens
    private MyGdxGame game;
    // Basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    // Our HUD
    private Hud hud;
    // Box2D variables
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body player1, player2;
    // Our buttons
    private TapTapController buttons;
    private boolean leftLeg, rightLeg;


    public PlayScreen(MyGdxGame game) {
        this.game = game;
        // create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();
        // create a FitViewport to maintain virtual aspect ratio
        gamePort = new FitViewport(MyGdxGame.V_WIDTH / MyGdxGame.PPM, MyGdxGame.V_HEIGHT / MyGdxGame.PPM, gamecam);
        // create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);
        // initially set our gamecam to center correctly
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        // create our box2d world, setting gravity in negative y direction, and allow bodies to sleep
        world = new World(new Vector2(0, -5), true);
        // allows for debug lines of our box2d world
        debugRenderer = new Box2DDebugRenderer();

        createGround();
        createPlayer1();
//        createPlatform();
        createPlayer2();
        createEndBlock();

        // create our game controller buttons
        buttons = new TapTapController(game.batch);
        // buttons logic
        leftLeg = false;
        rightLeg = false;
    }

    protected void handleInput() {
        if (buttons.isLeftPressed()) {
            if (!leftLeg){
                player1.applyLinearImpulse(new Vector2(1.5f, 0), player1.getWorldCenter(), true);
                leftLeg = true;
                rightLeg = false;
            }
        }
        if (buttons.isRightPressed()) {
            if (!rightLeg){
                player1.applyLinearImpulse(new Vector2(0.5f, 0), player1.getWorldCenter(), true);
                leftLeg = false;
                rightLeg = true;
            }
        }
    }

    public void update(float dt) {
        // handle user input first
        handleInput();
        //takes 1 step in the physics simulation (60 times per sec)
        world.step(1 / 60f, 6, 2);
        // update our hud with dt
        hud.update(dt);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        // update our gamecam with correct coordinates after changes
        gamecam.update();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // separate update logic from render
        update(delta);
        // clear the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(world, gamecam.combined);
        hud.stage.draw();
        buttons.draw();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public void createGround() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(gamePort.getWorldWidth()/2, 0);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body ground = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(gamePort.getWorldWidth(), 30/MyGdxGame.PPM);

        fdef.shape = shape;
        ground.createFixture(fdef);
        shape.dispose();
    }
    public void createPlatform() {
        BodyDef bdef3 = new BodyDef();
        bdef3.type = BodyDef.BodyType.StaticBody;
        bdef3.position.set(0, 0);
        Body platform = world.createBody(bdef3);

        FixtureDef fdef3 = new FixtureDef();
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(0, gamePort.getWorldHeight()/2, gamePort.getWorldWidth(), gamePort.getWorldHeight()/2);

        fdef3.shape = edgeShape;
        platform.createFixture(fdef3);
        edgeShape.dispose();
    }
    public void createPlayer1(){
        BodyDef bdef1 = new BodyDef();
        bdef1.position.set(gamePort.getWorldWidth()/7, gamePort.getWorldHeight()/5);
        bdef1.type = BodyDef.BodyType.DynamicBody;
        player1 = world.createBody(bdef1);

        FixtureDef fdef1 = new FixtureDef();
        PolygonShape shape1 = new PolygonShape();
        shape1.setAsBox(gamePort.getWorldHeight()/20, gamePort.getWorldHeight()/20);

        fdef1.shape = shape1;
        player1.createFixture(fdef1);
        shape1.dispose();
    }
    public void createPlayer2(){
        BodyDef bdef2 = new BodyDef();
        bdef2.position.set(gamePort.getWorldWidth()/7, gamePort.getWorldHeight()/2+gamePort.getWorldHeight()/10);
        bdef2.type = BodyDef.BodyType.DynamicBody;
        player2 = world.createBody(bdef2);

        FixtureDef fdef2 = new FixtureDef();
        CircleShape shape2 = new CircleShape();
        shape2.setRadius(gamePort.getWorldHeight()/20);

        fdef2.shape = shape2;
        player2.createFixture(fdef2);
        shape2.dispose();
    }

    public void createEndBlock(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(gamePort.getWorldWidth()*15/16, 0);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body ground = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(gamePort.getWorldWidth()/12, gamePort.getWorldHeight());

        fdef.shape = shape;
        ground.createFixture(fdef);
        shape.dispose();
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
        world.dispose();
        hud.dispose();
    }
}