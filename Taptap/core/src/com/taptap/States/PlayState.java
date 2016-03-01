package com.taptap.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.taptap.MyGdxGame;
import com.taptap.Sprites.Buttons;
import com.taptap.Sprites.Player;

/**
 * Created by Wei Sheng on 28/2/2016.
 */
public class PlayState extends State {

    private Texture bg;
    private Texture ground, ground1;
    private Vector2 groundPos1, groundPos2, ground1Pos1, ground1Pos2;
    private static final int GROUND_Y_OFFSET = -20;
    private static final int PLAYER_OFFSET = 50;
    private Player player, player1;
    private Buttons buttons;
    private boolean leftLeg, rightLeg;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        player = new Player(-10,40);
        player1 = new Player(-10,40 + PLAYER_OFFSET);
        cam.setToOrtho(false, MyGdxGame.WIDTH / 2, MyGdxGame.HEIGHT / 2);
        bg = new Texture("bg1.png");
        ground = new Texture("ground.png");
        ground1 = new Texture("ground.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);
        ground1Pos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUND_Y_OFFSET + PLAYER_OFFSET);
        ground1Pos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET + PLAYER_OFFSET);
        buttons = new Buttons();
        leftLeg = false;
        rightLeg = false;
    }
    @Override
    protected void handleInput() {
        if (buttons.isLeftPressed()) {
            if (!leftLeg){
                player.runFaster();
                leftLeg = true;
                rightLeg = false;
            }
        }
        if (buttons.isRightPressed()) {
            if (!rightLeg){
                player.runFaster();
                leftLeg = false;
                rightLeg = true;
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        player.update(dt);
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        sb.draw(ground1, ground1Pos1.x, ground1Pos1.y);
        sb.draw(ground1, ground1Pos2.x, ground1Pos2.y);
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        sb.draw(player.getPlayer(), player.getPosition().x, player.getPosition().y, player.getWidth() / 4, player.getHeight() / 4);
        sb.draw(player1.getPlayer(), player1.getPosition().x, player1.getPosition().y, player1.getWidth() / 4, player1.getHeight() / 4);
        sb.end();
        buttons.draw();
    }

    @Override
    public void dispose() {
        bg.dispose();
        ground.dispose();
        player.dispose();
        System.out.println("Play state disposed");
    }
}
