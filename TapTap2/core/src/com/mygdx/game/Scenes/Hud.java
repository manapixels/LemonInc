package com.mygdx.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

/**
 * Created by Wei Sheng on 10/3/2016.
 */
public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private int worldTimer;
    private float timeCount;

    BitmapFont fontName;
    Label timeLabel;
    Label instrucLabel;
    Label gameLabel;

    public Hud(SpriteBatch sb) {
        worldTimer = 100;
        timeCount = 0;

        viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table tbl = new Table();
        tbl.top();
        tbl.setFillParent(true);

        fontName = new BitmapFont();
        timeLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(fontName, Color.WHITE));
        instrucLabel = new Label("First to reach\n the end wins!", new Label.LabelStyle(fontName, Color.WHITE));
        gameLabel = new Label("TAP TAP", new Label.LabelStyle(fontName, Color.WHITE));

        tbl.add(gameLabel).expandX();
        tbl.add(instrucLabel).expandX();
        tbl.add(timeLabel).expandX();

        stage.addActor(tbl);
    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            worldTimer--;
            timeLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
}
