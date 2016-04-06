package com.mygdx.taptap3.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.taptap3.Screens.PlayScreen;
import com.mygdx.taptap3.TapTap3;

public class Player extends Sprite {
    public World world;
    public Body b2body;
    public enum State { DOUBLEJUMPING, JUMPING, DEFAULT, FORWARD, BACK, DEAD }
    public State currentState;
    public State previousState;

    private Sprite img;
    private PlayScreen screen;
    private final float CHARACTER_SIZE;


    Vector2 previousPosition;

    public Player() {
        CHARACTER_SIZE = 100 / TapTap3.PPM;
    }

    public Player(PlayScreen screen, String fileName, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.DEFAULT;
        previousState = State.DEFAULT;
        CHARACTER_SIZE = 100 / TapTap3.PPM;

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

        img = new Sprite(new Texture(fileName));
        img.setSize(CHARACTER_SIZE * 1.25f, CHARACTER_SIZE * 1.25f);
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
    public void draw(Batch batch){
        img.draw(batch);
    }

    public float getX(){
        return b2body.getPosition().x;
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x, b2body.getPosition().y);
        img.setPosition(b2body.getPosition().x - CHARACTER_SIZE / 2 * 1.25f, b2body.getPosition().y - CHARACTER_SIZE / 2 * 1.25f);
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
}