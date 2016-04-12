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

import com.badlogic.gdx.Gdx;
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
import com.lemoninc.nimbusrun.NimbusRun;

public class Player extends Sprite {
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

    public Player() {
        CHARACTER_SIZE = 150 / NimbusRun.PPM;
    }

    /**
     * TODO: this constructor should only be created by client?
     * @param gameMap
     * @param img
     * @param x
     * @param y
     */
    public Player(GameMap gameMap, TextureAtlas img, float x, float y) {

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
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(x, y);
        fdef.shape = shape;
        b2body.createFixture(fdef); //Player is a circle
        shape.dispose();

//        switch(whichCharacter){
//            // 1. LAUGHING BUDDHA
//            // 2. SHESHNAH WITH KRISHNA
//            // 3. NINE-TAILED FOX
//            // 4. KAPPA
//            // 5. PONTIANAK
//            // 6. MADAME WHITE SNAKE
//            case 1: img = new TextureAtlas(Gdx.files.internal("spritesheets/LBspritesheet.atlas")); break;
//            case 2: img = new TextureAtlas(Gdx.files.internal("spritesheets/SKspritesheet.atlas")); break;
//            case 3: img = new TextureAtlas(Gdx.files.internal("spritesheets/FXspritesheet.atlas")); break;
//            case 4: img = new TextureAtlas(Gdx.files.internal("spritesheets/KPspritesheet.atlas")); break;
//            case 5: img = new TextureAtlas(Gdx.files.internal("spritesheets/PTspritesheet.atlas")); break;
//            case 6: img = new TextureAtlas(Gdx.files.internal("spritesheets/MWSspritesheet.atlas")); break;
//            default: img = new TextureAtlas(Gdx.files.internal("spritesheets/PTspritesheet.atlas")); break;
//        }

        this.img = img;

        anim = new Animation(1f/40f, img.getRegions());
        //img = new Sprite(new Texture(fileName));
        //img.setSize(CHARACTER_SIZE * 1.25f, CHARACTER_SIZE * 1.25f);

        previousPosition = new Vector2(this.getX(), this.getY()); //TODO: is this correct?
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
     * checks if the box2d body of Player has moved or not
     * @return
     */
    public boolean hasMoved() {
        if (previousPosition.x != this.getX() || previousPosition.y != getY()) {
            previousPosition.x = this.getX();
            previousPosition.y = this.getY();
            return true;
        }
        return false;
    }

    public void update(float delta){
        this.setPosition(getX(), getY());
        this.setRegion(anim.getKeyFrame(stateTime, true));
        //img.setPosition(b2body.getPosition().x - CHARACTER_SIZE / 2 * 1.25f, b2body.getPosition().y - CHARACTER_SIZE / 2 * 1.25f);
    }

    public void jump() {
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
    }

    public void speed() {
        if (b2body.getLinearVelocity().x <= 4) {
            b2body.applyLinearImpulse(new Vector2(1.25f, 0), b2body.getWorldCenter(), true);
        }
    }
    public void slow() {
        if (b2body.getLinearVelocity().x >= -4) {
            b2body.applyLinearImpulse(new Vector2(-1.25f, 0), b2body.getWorldCenter(), true);
        }
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
}