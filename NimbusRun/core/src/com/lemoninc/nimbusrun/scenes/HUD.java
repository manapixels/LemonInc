package com.lemoninc.nimbusrun.scenes;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lemoninc.nimbusrun.TapTap3;


/**
 * Created by Nikki on 8/4/2016.
 */
public class HUD extends Group implements Disposable,ApplicationListener,Screen{
    TextureAtlas atlas;
//    private Skin skin;
    public Stage stage;
    private Viewport viewport;
    public Integer worldTimer;
    private Label playername;
    private float timecount;
    Label countdownLabel;
    Label timelabel;
    Label CharacterLabel;
    private Camera camera;
    String Player;
    private ShapeRenderer shapeRenderer;
    private float progress;
//    ProgressBar.ProgressBarStyle barStyle;
//    TextureRegionDrawable textureBar;



    public  HUD(SpriteBatch sb, String playernumber){
        this.Player=playernumber;
        worldTimer = 60;
        timecount=0;
        camera=new PerspectiveCamera();
        viewport=new FillViewport(TapTap3.V_WIDTH,TapTap3.V_HEIGHT,new OrthographicCamera());
        shapeRenderer=new ShapeRenderer();
        progress=1f;

        stage=new Stage(viewport,sb);
        stage.clear();


        Table table = new Table();

        table.top();
        table.setFillParent(true);
        // 1. LAUGHING BUDDHA
        // 2. SHESHNAH WITH KRISHNA
        // 3. NINE-TAILED FOX
        // 4. KAPPA
        // 5. PONTIANAK
        // 6. MADAME WHITE SNAKE

        if(Player.equals("Player1")){
            playername=new Label("LAUGHING BUDDHA",new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/font20.fnt")), Color.CYAN));
        }
        else if(Player.equals("Player2")){
            playername=new Label("SHESHNAH WITH KRISHNA",new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/font20.fnt")), Color.CYAN));
        }
        else if(Player.equals("Player3")){
            playername=new Label("NINE-TAILED FOX",new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/font20.fnt")), Color.CYAN));
        }
        else if(Player.equals("Player4")){
            playername=new Label(" KAPPA",new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/font20.fnt")), Color.CYAN));
        }
        else if(Player.equals("Player5")){
            playername=new Label("PONTIANAK",new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/font20.fnt")), Color.CYAN));
        }
        else if(Player.equals("Player6")){
            playername=new Label("MADAME WHITE SNAKE",new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/font20.fnt")), Color.CYAN));

        }
        else {
            playername=new Label("POTIANAK",new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/font20.fnt")), Color.CYAN));
        }


        timelabel=new Label("TIME",new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/font.fnt")), Color.GOLDENROD));
        CharacterLabel=new Label("PLAYER",new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/font.fnt")), Color.GOLDENROD));
        countdownLabel=new Label(String.format("%03d",worldTimer),new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/font20.fnt")), Color.CYAN));


        table.add(CharacterLabel).expandX().padTop(20f);

        table.add(timelabel).expandX().padTop(20f);
        table.row();
        table.add(playername);
        table.add(countdownLabel);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

    }
    public void update(float delta) {

        timecount += delta;

        //progress=(60-worldTimer)/60;
        if (timecount >= 1&&worldTimer>0) {
            worldTimer--;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timecount = 0;
        }
        progress= MathUtils.lerp(progress,(worldTimer/60),0.001f);
        //condition to update

    }

    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(230, 425, 220, 30);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(230, 425, this.progress * 220, 30);
        shapeRenderer.end();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
       // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);




    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }


    @Override
    public void dispose() {
        stage.dispose();
//        skin.dispose();
        atlas.dispose();

    }
}
