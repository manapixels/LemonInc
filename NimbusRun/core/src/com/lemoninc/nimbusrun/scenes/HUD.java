package com.lemoninc.nimbusrun.scenes;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lemoninc.nimbusrun.NimbusRun;
import com.lemoninc.nimbusrun.Sprites.GameMap;


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
    //private float timecount;
    Label countdownLabel;
    Label timelabel;
    Label CharacterLabel;
    private Camera camera;
    String Player;
    private ShapeRenderer shapeRenderer;
    private float progress;
    String PlayerCharacter;
//    ProgressBar.ProgressBarStyle barStyle;
//    TextureRegionDrawable textureBar;
    float timecount;
    int count;
    Label dialoglabel;
    com.badlogic.gdx.scenes.scene2d.ui.Dialog dialogstart;
    private com.badlogic.gdx.scenes.scene2d.ui.Skin skin;
    private float powerup;
    int nopowerups;
    private float powerupdistance;
    float playerLocalX,worldLength;
    GameMap gameMap;

    public  HUD(SpriteBatch sb, String playernumber, GameMap gameMap){

        this.Player=playernumber;
        this.gameMap=gameMap;
        //playerLocalX =gameMap.bgStartX;
        //worldLength = 18*gameMap.getGameport().getWorldWidth();
        Gdx.app.log("worled length",String.valueOf(worldLength));
        powerupdistance=worldLength/3;
        worldTimer = 1;

        timecount=0;
        camera=new PerspectiveCamera();
        viewport=new FillViewport(NimbusRun.V_WIDTH,NimbusRun.V_HEIGHT,new OrthographicCamera());
        shapeRenderer=new ShapeRenderer();
        progress=1f;
        powerup=0f;
        count=4;
        nopowerups=3;


        stage=new Stage(viewport,sb);
        stage.clear();

        dialoglabel=new Label(String.format("%01d", count),new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/Baskek45.fnt")), Color.CYAN));
        skin=new com.badlogic.gdx.scenes.scene2d.ui.Skin(Gdx.files.internal("data/uiskin.json"));
        dialogstart=new com.badlogic.gdx.scenes.scene2d.ui.Dialog("",skin){
            @Override
            public float getPrefWidth(){
                // force dialog width
                // return Gdx.graphics.getWidth() / 2;
                return 600f;
            }
            @Override
            public float getPrefHeight() {
                // force dialog height
                // return Gdx.graphics.getWidth() / 2;
                return 300f;
            }
        };

        stage.addActor(dialogstart);
        dialogstart.background(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Dialogbox.png")))));
        dialogstart.setPosition(0, 0, Align.center);
        dialogstart.show(stage);
        timecount=0;
        count=4;
        dialogstart.text(dialoglabel);
        stage.addActor(dialogstart);
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
        show();
        timecount += delta;
        if(count>0) {
            stage.draw();

            if (timecount >= 1) {
                count--;
                dialoglabel.setText(String.format("%01d", count));
                dialogstart.text(dialoglabel);
                timecount = 0;
            }
        }
        //progress=(60-worldTimer)/60;
        else {
            dialogstart.remove();
//            if(Gdx.input.justTouched()) {
//               playerLocalX = gameMap.playerLocal.getX();
//            }
            if (timecount >= 1&&worldTimer>0) {
                worldTimer--;
                countdownLabel.setText(String.format("%03d", worldTimer));
                timecount = 0;
            }
        }

        progress= MathUtils.lerp(progress,(worldTimer/60),0.001f);
//        powerup=playerLocalX/powerupdistance;
//        Gdx.app.log("Powerdistance",String.valueOf(powerupdistance));
//       if(playerLocalX%powerupdistance==0){
//           nopowerups--;
//           powerup=1f;
//       }
       /*if player used power up, onclicklistener at Playscreen/gamemap,
        command the power up has been used,powerup=0f)

        /*
        distancetravelledratio=(playerlocal.getx/worldwidth);
        # of power up= 6;
        distance for power up=worldwidth/6;
        powerup= getx/powerupdistance
        if(getx%powerupdistance==0)
        #powerup--

        if(powerup)
        powerup=MathUtils.lerp(powerup,1f,0.01f);
        */

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
        shapeRenderer.rect(Gdx.graphics.getWidth()/2-90, Gdx.graphics.getHeight()-80, 220, 40);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(Gdx.graphics.getWidth()/2-90,  Gdx.graphics.getHeight()-80, this.progress* 220, 40);
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
//       skin.dispose();
        atlas.dispose();


    }
}
