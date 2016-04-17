package com.lemoninc.nimbusrun.Sprites;

/*********************************
 * FILENAME : Player.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       State          getState()
 *       void           render(float delta)
 *       void           draw(SpriteBatch batch)
 *       float          getX()
 *       float          getY()
 *       public boolean hasMoved()
 *       void           update(float delta)
 *       void           jump()
 *       void           speed()
 *       void           slow()
 *       TextureAtlas   getTxtAtlas()
 *       public void setId(int id)
 *       public void setName(String name)
 *       public String getName()
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.lemoninc.nimbusrun.Networking.Network;
import com.lemoninc.nimbusrun.Screens.PlayScreen;
import com.lemoninc.nimbusrun.NimbusRun;

import java.util.HashMap;
import java.util.Map;

public class Player extends Sprite implements InputProcessor{
    public World world;
    public Body b2body;
    public enum State { DOUBLEJUMPING, JUMPING, DEFAULT, FORWARD, BACK, DEAD }
    public State currentState;
    public State previousState;

    private TextureAtlas img;
    private Animation anim;
    private final float CHARACTER_SIZE;
    private float stateTime;

    private int id;
    private String name;


    Vector2 previousPosition;

    private Map<Integer,TouchInfo> touches;

    /**
     * TODO: this constructor should only be created by client?
     * @param gameMap
     * @param img
     * @param x
     * @param y
     */
    public Player(GameMap gameMap, TextureAtlas img, float x, float y, boolean isLocal) {

        this.world = gameMap.getWorld();
        currentState = State.DEFAULT;
        previousState = State.DEFAULT;
        CHARACTER_SIZE = 170 / NimbusRun.PPM;
        stateTime = 0f;

        //create a dynamic bodydef
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / NimbusRun.PPM, y / NimbusRun.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef); //Body of Player

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(CHARACTER_SIZE / 2);

        fdef.shape = shape;
        b2body.createFixture(fdef); //Player is a circle
        shape.dispose();

        this.img = img;

        anim = new Animation(1f/40f, img.getRegions());

        //only for playerLocal
        if (isLocal) {
            Gdx.input.setInputProcessor(this);

            touches = new HashMap<Integer,TouchInfo>();

            for(int i = 0; i < 2; i++){
                touches.put(i, new TouchInfo());
            }
        }
    }

    class TouchInfo {
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y != 0){
            if (previousState == State.JUMPING){
                return State.DOUBLEJUMPING;
            }
            else {
                return State.JUMPING;
            }
        }
        else
            return State.DEFAULT;
    }

    public void render(SpriteBatch spriteBatch) {
        this.draw(spriteBatch);
    }

    public void draw(SpriteBatch batch) {
        if (b2body != null) {
            stateTime += Gdx.graphics.getDeltaTime();
            batch.draw(anim.getKeyFrame(stateTime, true), getX() - CHARACTER_SIZE / 2, getY() - CHARACTER_SIZE / 2, CHARACTER_SIZE, CHARACTER_SIZE);
        }

        //img.draw(batch);
    }

    public float getX(){
        return b2body.getPosition().x;
    }
    public float getY(){
        return b2body.getPosition().y;
    }

    /**
     * Player's movement
     * @return
     */
    public boolean handleInput(){

        if(Gdx.app.getType() == Application.ApplicationType.Android){
            if(Gdx.input.justTouched()) {
//                System.out.println("Points are: X=" + Gdx.input.getX() + "Y=" + Gdx.input.getY());
                int x=Gdx.input.getX();
                int y=Gdx.input.getY();
                if(x>NimbusRun.V_WIDTH/2){
                    return this.speed();
                }
                else{
                    return this.jump();
                }
            }
            if(touches.get(0).touched&&touches.get(1).touched){
                if(touches.get(0).touchX<(NimbusRun.V_WIDTH/2)&&touches.get(1).touchX>(NimbusRun.V_WIDTH-(NimbusRun.V_WIDTH/2))){
                    // TODO: Implement method for attack
                    //player1.attack;
                }
                else if(touches.get(1).touchX<(NimbusRun.V_WIDTH/2)&&touches.get(0).touchX>(NimbusRun.V_WIDTH-(NimbusRun.V_WIDTH/2))) {
                    //TODO: Implement method for attack
                    //player1.attack
                }
            }
        }
        else {
            //for Desktop
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                return this.jump();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                return this.speed();
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                return this.slow();
        }


        return false;
    }

    public void update(float delta){

        //player.checkDebuff()
        //if this is true, player.recover(delta)
        //inside the player class, implement the recover mechanism

        //TODO: recovery mechanism for weisheng
    }

    public boolean jump() {
        if (currentState == State.DOUBLEJUMPING){
            currentState = getState();
        }
        else if (currentState == State.JUMPING){
            previousState = State.JUMPING;
            currentState = State.DOUBLEJUMPING;
            b2body.applyLinearImpulse(new Vector2(0, 6f), b2body.getWorldCenter(), true);
        }
        else {
            currentState = State.JUMPING;
            b2body.applyLinearImpulse(new Vector2(0, 6f), b2body.getWorldCenter(), true);
        }
        return true;
    }


    public boolean speed() {
        if (b2body.getLinearVelocity().x <= 3) {
            b2body.applyLinearImpulse(new Vector2(1f, 0), b2body.getWorldCenter(), true);
        }
        return true;
    }

    public boolean slow() {
        if (b2body.getLinearVelocity().x >= -3) {
            b2body.applyLinearImpulse(new Vector2(-1f, 0), b2body.getWorldCenter(), true);
        }
        return true;
    }

    /**
     * Get the Player's body linear Velocity wrapped in MovementState
     * @return
     */
    public Network.MovementState getMovementState() {
        return new Network.MovementState(id, b2body.getPosition(), b2body.getLinearVelocity());
    }

    /**
     * Set the player's linear velocity according to the received MovementState Packet
     * @param msg
     */
    public synchronized void setMovementState(Network.MovementState msg) {
        b2body.setLinearVelocity(msg.linearVelocity);
        b2body.setTransform(msg.position, 0f); //this is outside the world.step call
//        System.out.println("Changed player's x is "+msg.position.x+" y is "+msg.position.y);
    }

    public TextureAtlas getTxtAtlas(){ return img;}

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

        @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 2){
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 2){
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}