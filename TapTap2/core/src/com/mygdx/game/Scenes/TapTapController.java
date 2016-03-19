package com.mygdx.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;


/**
 * Created by Wei Sheng on 10/3/2016.
 */
public class TapTapController extends Stage {
    public Stage stage;
    private Viewport viewport;
    OrthographicCamera cam;
    boolean leftPressed, rightPressed;

    public TapTapController(SpriteBatch sb) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        Image leftBtn = new Image(new Texture("leftFoot.png"));
        leftBtn.setSize(viewport.getWorldHeight()/5, viewport.getWorldHeight()/5);
        leftBtn.setPosition(0, 0);
        leftBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });
        stage.addActor(leftBtn);

        Image rightBtn = new Image(new Texture("rightFoot.png"));
        rightBtn.setSize(viewport.getWorldHeight()/5, viewport.getWorldHeight()/5);
        rightBtn.setPosition(viewport.getWorldWidth() - viewport.getWorldHeight()/5, 0);
        rightBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });
        stage.addActor(rightBtn);
    }

    public void draw(){ stage.draw(); }
    public boolean isLeftPressed(){ return leftPressed; }
    public boolean isRightPressed(){ return rightPressed; }
}
