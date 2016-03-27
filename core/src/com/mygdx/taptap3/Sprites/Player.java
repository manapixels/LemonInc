package com.mygdx.taptap3.Sprites;


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
    public enum State { DOUBLEJUMPING, JUMPING, STANDING, RUNNING, DEAD }
    public State currentState;
    public State previousState;

    private PlayScreen screen;

    public Player(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;

        BodyDef bdef = new BodyDef();
        bdef.position.set(x / TapTap3.PPM, y / TapTap3.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50 / TapTap3.PPM);
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(x, y);
        fdef.shape = shape;
        b2body.createFixture(fdef);
        shape.dispose();
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
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public float getX(){
        return b2body.getPosition().x;
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x, b2body.getPosition().y);
    }

    public void jump() {
        if (currentState == State.DOUBLEJUMPING){
            currentState = getState();
        }
        else if (currentState == State.JUMPING){
            previousState = State.JUMPING;
            currentState = State.DOUBLEJUMPING;
            b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);
        }
        else {
            currentState = State.JUMPING;
            b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);
        }
    }

    public void speed() {
        if (b2body.getLinearVelocity().x <= 4 && (getState() != State.JUMPING) && (getState() != State.DOUBLEJUMPING)) {
            b2body.applyLinearImpulse(new Vector2(2f, 0), b2body.getWorldCenter(), true);
        }
    }

    public void slow() {
        if (b2body.getLinearVelocity().x >= -4 && (getState() != State.JUMPING) && (getState() != State.DOUBLEJUMPING)) {
            b2body.applyLinearImpulse(new Vector2(-2f, 0), b2body.getWorldCenter(), true);
        }
    }
}