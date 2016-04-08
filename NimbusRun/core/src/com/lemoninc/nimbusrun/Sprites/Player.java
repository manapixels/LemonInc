package com.lemoninc.nimbusrun.Sprites;

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
import com.lemoninc.nimbusrun.Screens.PlayScreen;
import com.lemoninc.nimbusrun.TapTap3;

public class Player extends Sprite {
    public World world;
    public Body b2body;
    public enum State { DOUBLEJUMPING, JUMPING, DEFAULT, FORWARD, BACK, DEAD }
    public State currentState;
    public State previousState;

    private TextureAtlas img;
    private Animation anim;
    private PlayScreen screen;
    private final float CHARACTER_SIZE;
    private float stateTime;


    Vector2 previousPosition;

    public Player() {
        CHARACTER_SIZE = 150 / TapTap3.PPM;
    }

    public Player(PlayScreen screen, int whichCharacter, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.DEFAULT;
        previousState = State.DEFAULT;
        CHARACTER_SIZE = 170 / TapTap3.PPM;
        stateTime = 0f;

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / TapTap3.PPM, y / TapTap3.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(CHARACTER_SIZE / 2);
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(x, y);
        fdef.shape = shape;
        b2body.createFixture(fdef);
        shape.dispose();

        switch(whichCharacter){
            // 1. LAUGHING BUDDHA
            // 2. SHESHNAH WITH KRISHNA
            // 3. NINE-TAILED FOX
            // 4. KAPPA
            // 5. PONTIANAK
            // 6. MADAME WHITE SNAKE
            case 1: img = new TextureAtlas(Gdx.files.internal("spritesheets/LBspritesheet.atlas")); break;
            case 2: img = new TextureAtlas(Gdx.files.internal("spritesheets/SKspritesheet.atlas")); break;
            case 3: img = new TextureAtlas(Gdx.files.internal("spritesheets/FXspritesheet.atlas")); break;
            case 4: img = new TextureAtlas(Gdx.files.internal("spritesheets/KPspritesheet.atlas")); break;
            case 5: img = new TextureAtlas(Gdx.files.internal("spritesheets/PTspritesheet.atlas")); break;
            case 6: img = new TextureAtlas(Gdx.files.internal("spritesheets/MWSspritesheet.atlas")); break;
            default: img = new TextureAtlas(Gdx.files.internal("spritesheets/PTspritesheet.atlas")); break;
        }

        anim = new Animation(1f/40f, img.getRegions());
        //img = new Sprite(new Texture(fileName));
        //img.setSize(CHARACTER_SIZE * 1.25f, CHARACTER_SIZE * 1.25f);
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
    public void draw(SpriteBatch batch){
        stateTime += Gdx.graphics.getDeltaTime();
        batch.draw(anim.getKeyFrame(stateTime, true), getX()-CHARACTER_SIZE/2, getY()-CHARACTER_SIZE/2, CHARACTER_SIZE, CHARACTER_SIZE);
        //img.draw(batch);
    }

    public float getX(){
        return b2body.getPosition().x;
    }

    public void update(float dt){
        this.setPosition(b2body.getPosition().x, b2body.getPosition().y);
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
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
        else {
            currentState = State.JUMPING;
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
    }

    public void speed() {
//        b2body.setLinearVelocity(new Vector2(3f, 0));
//    }
        if (b2body.getLinearVelocity().x <= 3) {
            b2body.applyLinearImpulse(new Vector2(1f, 0), b2body.getWorldCenter(), true);
        }
    }
    public void slow() {
//        b2body.setLinearVelocity(new Vector2(-3f, 0));
        if (b2body.getLinearVelocity().x >= -3) {
            b2body.applyLinearImpulse(new Vector2(-1f, 0), b2body.getWorldCenter(), true);
        }
    }

    public TextureAtlas getTxtAtlas(){ return img;}
}