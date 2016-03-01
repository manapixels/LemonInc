package com.taptap.Sprites;

/**
 * Created by Wei Sheng on 1/3/2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taptap.MyGdxGame;

public class Buttons extends Actor {
    Viewport viewport;
    Stage stage;
    OrthographicCamera cam;
    boolean leftPressed, rightPressed;

    public Buttons(){
        cam = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
        stage = new Stage(viewport, MyGdxGame.batch);
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        Image leftBtn = new Image(new Texture("leftFoot.png"));
        leftBtn.setSize(100, 100);
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
        rightBtn.setSize(100, 100);
        rightBtn.setPosition(700, 0);
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
