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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.lemoninc.nimbusrun.NimbusRun;

import java.util.HashMap;
import java.util.Map;


public class Player extends Sprite implements InputProcessor {
    public World world;
    public Body b2body;
    public Boolean attackactivated=true;

    public enum State {DOUBLEJUMPING, JUMPING, DEFAULT}

    public State currentState;
    public State previousState;
    public boolean isLocal;

    private GameMap gameMap;

    private TextureAtlas img;
    private Animation anim;
    private final float CHARACTER_SIZE;
    private float stateTime;

    private int id;
    private String name;
    public int character;
    private BitmapFont font;

    private boolean stunned, poisoned, reversed, terrored, flashed, confused, devMode, finished;
    private float stunTime, poisonTime, reverseTime, terrorTime, flashTime, confuseTime;

    private final float JUMPFORCE = 8f;
    private final float MOVEFORCE = 2.5f;
    private final float MOVESPEEDCAP = 8;
    private float factor = 1;

    private float screenWidth = Gdx.graphics.getWidth();
    private Sound jumpSound;
    private Sound buddhaSound, snakeSound, kappaSound, pontianakSound, gumihoSound, krishnaSound;



    Vector2 previousPosition;

    //  Sound attacksound,jumpsound;
    private Map<Integer, TouchInfo> touches;

    /**
     * @param gameMap
     * @param img
     * @param x
     * @param y
     */
    public Player(GameMap gameMap, TextureAtlas img, float x, float y, boolean isLocal, int character) { //TODO: character parameter to differentiate the Player? (switch-case for attack())

        this.gameMap = gameMap;
        this.world = gameMap.getWorld();
        this.isLocal = isLocal;
        this.character = character;
        currentState = State.DEFAULT;
        previousState = State.DEFAULT;
        CHARACTER_SIZE = 220 / NimbusRun.PPM;
        stateTime = 0f;

        //debuff variables
        stunTime = 0f;
        stunned = false;
        poisonTime = 0f;
        poisoned = false;
        reverseTime = 0f;
        reversed = false;
        terrorTime = 0f;
        terrored = false;
        flashed = false;
        flashTime = 0f;
        confused = false;
        confuseTime = 0f;
        devMode = false;
        finished = false;

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

        anim = new Animation(1f / 40f, img.getRegions());

        //only for playerLocal
        if (isLocal) {

            //create music

            jumpSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/swoosh.wav"));
            buddhaSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Buddha.mp3"));
            snakeSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Snake.mp3"));
            pontianakSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Pontianak.mp3"));
            gumihoSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Gumiho.mp3"));
            kappaSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Kappa.mp3"));
            krishnaSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Krishna.mp3"));

//            Gdx.input.setInputProcessor(this);

            touches = new HashMap<Integer, TouchInfo>();

            for (int i = 0; i < 2; i++) {
                touches.put(i, new TouchInfo());
            }
        }
    }

    class TouchInfo {
        float touchX;
        float touchY;
        boolean touched;

        TouchInfo() {
            touchX = 0;
            touchY = 0;
            touched = false;
        }
    }

    public State getState() {
        if (b2body.getLinearVelocity().y != 0) {
            if (previousState == State.JUMPING) {
                return State.DOUBLEJUMPING;
            } else {
                return State.JUMPING;
            }
        } else
            return State.DEFAULT;
    }

    public boolean isStunned() {
        return stunned;
    }

    public boolean isPoisoned() {
        return poisoned;
    }

    public boolean isReversed() {
        return reversed;
    }

    public boolean isBlackHoled() {
        return terrored;
    }

    public boolean isFlashed() {
        return flashed;
    }

    public boolean isConfused() {
        return confused;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public boolean isFinished() {
        return finished;
    }

    public float getStunTime() {
        return stunTime;
    }

    public float getPoisonTime() {
        return poisonTime;
    }

    public float getBlackHoleTime() {
        return terrorTime;
    }

    public float getReverseTime() {
        return reverseTime;
    }

    public float getFlashTime() {
        return flashTime;
    }

    public float getConfuseTime() {
        return confuseTime;
    }

    public void render(SpriteBatch spriteBatch) {
        this.draw(spriteBatch);
    }

    public void draw(SpriteBatch batch) {
        if (b2body != null) {
            stateTime += Gdx.graphics.getDeltaTime();
            batch.draw(anim.getKeyFrame(stateTime, true), getX() - CHARACTER_SIZE / 2, getY() - CHARACTER_SIZE / 2, CHARACTER_SIZE, CHARACTER_SIZE);
        }
    }

    public float getX() {
        return b2body.getPosition().x;
    }

    public float getY() {
        return b2body.getPosition().y;
    }

    /**
     * Player's movement
     *
     * @return
     */
    public boolean handleInput() {

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            if (Gdx.input.justTouched()) {
//                System.out.println("Points are: X=" + Gdx.input.getX() + "Y=" + Gdx.input.getY());
                int x=Gdx.input.getX();
                int y=Gdx.input.getY();
                if(x > screenWidth / 2){
                    if (isConfused()){
                        return this.jump();
                    } else {
                        Gdx.app.log("GDX Player", "Moving Right");
                        return this.moveRight();
                    }
                } else {
                    if (isConfused()) {
                        return this.moveRight();
                    } else {
                        return this.jump();
                    }
                }
            }
            if (touches.get(0).touched && touches.get(1).touched) {
                if (touches.get(0).touchX < (screenWidth / 2) && touches.get(1).touchX > (screenWidth / 2)) {
                    if (mayAttack()) {
                        attackSoundPlay(character);
                        attack();
                    }

                } else if (touches.get(1).touchX < (screenWidth / 2) && touches.get(0).touchX > (screenWidth / 2)) {
                    if (mayAttack()) {
                        attackSoundPlay(character);
                        attack();
                    }
                }
            }
        } else {
            //for Desktop
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                if (isConfused()) {
                    return this.moveRight();
                } else {
                    return this.jump();
                }
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
                if (isConfused()) {
                    return this.jump();
                } else {
                    return this.moveRight();
                }
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))    //testing purposes only
                return this.moveLeft(1);
            if (Gdx.input.isKeyJustPressed(Input.Keys.A))       //testing purposes only
                return this.stun();
            if (Gdx.input.isKeyJustPressed(Input.Keys.S))       //testing purposes only
                return this.poison();
            if (Gdx.input.isKeyJustPressed(Input.Keys.D))       //testing purposes only
                return this.reverse();
            if (Gdx.input.isKeyJustPressed(Input.Keys.F))       //testing purposes only
                return this.terror();
            if (Gdx.input.isKeyJustPressed(Input.Keys.G))       //testing purposes only
                return this.flash();
            if (Gdx.input.isKeyJustPressed(Input.Keys.H))       //testing purposes only
                return this.confuse();
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {  //testing purposes only {
                devMode = true;
                return true;
            }
        }
        return false;
    }

    public void attackSoundPlay(int character) {
        if (character == 1)
            buddhaSound.play();
        if (character == 2)
            krishnaSound.play();
        if (character == 3)
            gumihoSound.play();
        if (character == 4)
            kappaSound.play();
        if (character == 5)
            pontianakSound.play();
        if (character == 6)
            snakeSound.play();
    }

    public void update(float delta) {
        if (gameMap.getHud().count_initial > 0){
            stunned = true;
            stunTime = 1f;
        }
        else {
            recover(1f);
        }
    }

    public boolean recover(float delta) {
        if (b2body != null || gameMap.getGameport() != null) {
            if (this.getX() >= gameMap.getGameport().getWorldWidth() * 18.5f) {
                finished = true;
            }
        }
        if (isStunned()) {
            stunTime -= delta;
            if (stunTime <= 0)
                stunned = false;
        }
        if (isPoisoned()) {
            poisonTime -= delta;
            if (poisonTime <= 0)
                poisoned = false;
        }
        if (isReversed()) {
            reverseTime -= delta;
            if (reverseTime <= 0)
                reversed = false;
        }
        if (isBlackHoled()) {
            if (b2body.getLinearVelocity().x >= -MOVESPEEDCAP * 2f)
                b2body.applyLinearImpulse(new Vector2(-MOVEFORCE, 0), b2body.getWorldCenter(), true);
            terrorTime -= delta;
            if (terrorTime <= 0)
                terrored = false;
        }
        if (isFlashed()) {
            flashTime -= delta;
            if (flashTime <= 0)
                flashed = false;
        }
        if (isConfused()) {
            confuseTime -= delta;
            if (confuseTime <= 0)
                confused = false;
        }
        return true;
    }

    public boolean mayAttack() {
        //TODO:check if player can attack



        if (gameMap.noPowerUps > 0) {
            gameMap.noPowerUps--;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean attack() {
        Gdx.app.log("GDX Player", "Player " +id+" Attacked");
        Network.PlayerAttack msgPlayerAttack = new Network.PlayerAttack(id, character);

//        if (character )
        gameMap.playerAttacked(msgPlayerAttack);

        //decrement number of powerups
//        gameMap.getHud().noPowerUps--;

        if (isLocal) {
            //attack sound
            gameMap.clientSendMessage(msgPlayerAttack);
        }
        return true;
    }

    public boolean stun() {
        stunned = true;
        stunTime = 150f;
        b2body.setLinearVelocity(new Vector2(0, 0));
        return true;
    }

    public boolean poison() {
        poisoned = true;
        poisonTime = 300f;
        return true;
    }

    public boolean reverse() {
        reversed = true;
        reverseTime = 250f;
        return true;
    }

    public boolean terror() {
        if (!isFinished()){
            terrored = true;
            b2body.setLinearVelocity(0, 0);
            terrorTime = 75f;
        }
        return true;
    }

    public boolean flash() {
        flashed = true;
        flashTime = 250f;
        return true;
    }

    public boolean confuse() {
        confused = true;
        confuseTime = 500f;
        return true;
    }

    public boolean jump() {
        factor = checkCondition();
        if (currentState == State.DOUBLEJUMPING) {
            currentState = getState();
        } else if (currentState == State.JUMPING) {
            previousState = State.JUMPING;
            currentState = State.DOUBLEJUMPING;
            b2body.applyLinearImpulse(new Vector2(0, JUMPFORCE * checkCondition()), b2body.getWorldCenter(), true);
            jumpSound.play();
        } else {
            currentState = State.JUMPING;
            b2body.applyLinearImpulse(new Vector2(0, JUMPFORCE * checkCondition()), b2body.getWorldCenter(), true);
            jumpSound.play();
        }
        return false;
    }

    public boolean moveRight() {
        factor = checkCondition();
        if (isReversed()) {
            moveLeft(factor);
        } else {
            if (b2body.getLinearVelocity().x <= (MOVESPEEDCAP * factor)) {
                b2body.applyLinearImpulse(new Vector2(MOVEFORCE * factor, 0), b2body.getWorldCenter(), true);
            }
        }
        return true;
    }

    public boolean moveLeft(float factor) {     //NOTE: NEVER CALLED BY PLAYER INPUT, ONLY CALLED WHEN REVERSED
        if (!isFinished()) {
            if (b2body.getLinearVelocity().x >= -MOVESPEEDCAP * factor) {
                b2body.applyLinearImpulse(new Vector2(-MOVEFORCE * factor, 0), b2body.getWorldCenter(), true);
            }
        }
        return true;
    }

    private float checkCondition() {
        factor = 1;
        if (isStunned() || isFinished()) {
            factor = factor * 0f;
        }
        if (isPoisoned()) {
            factor = factor * 0.5f;
        }
        if (isDevMode()) {
            factor = factor * 2.5f;
        }
        return factor;
    }

    /**
     * Get the Player's body linear Velocity wrapped in MovementState
     *
     * @return
     */
    public Network.MovementState getMovementState() {
        return new Network.MovementState(id, b2body.getPosition(), b2body.getLinearVelocity());
    }

    /**
     * Set the player's linear velocity according to the received MovementState Packet
     *
     * @param msg
     */
    public synchronized void setMovementState(Network.MovementState msg) {
        Gdx.app.log("GDX Player", "set Movement State");

        b2body.setLinearVelocity(msg.linearVelocity);
        b2body.setTransform(msg.position, 0f); //this is outside the world.step call
    }

    public TextureAtlas getTxtAtlas() {
        return img;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
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
//        Gdx.app.log("GDX Player touchDown", "touchDOwn called");

        if(pointer < 2){
//            Gdx.app.log("GDX Player touchDown", "touchDOwn called pointer < 2");
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

    public void dispose() {
        if (isLocal) {
            jumpSound.dispose();
            krishnaSound.dispose();
            buddhaSound.dispose();
            pontianakSound.dispose();
            snakeSound.dispose();
            gumihoSound.dispose();
            kappaSound.dispose();
        }
        img.dispose();
    }
}