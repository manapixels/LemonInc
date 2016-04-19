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
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lemoninc.nimbusrun.NimbusRun;
import com.lemoninc.nimbusrun.Sprites.GameMap;
import com.lemoninc.nimbusrun.Sprites.Player;

import java.util.Map;

public class HUD extends Group implements Disposable,ApplicationListener,Screen{
    TextureAtlas atlas;
    public Stage stage;
    private Viewport viewport;
    public Integer worldTimer;
    private Label nameLabel;
    //private float timecount;
    Label countdownLabel;
    Label timelabel;
    Label CharacterLabel;
    private Camera camera;

    private ShapeRenderer shapeRenderer;
    private float progress;
    private Map<Integer, Player> players;
    String PlayerCharacter;
//    ProgressBar.ProgressBarStyle barStyle;
//    TextureRegionDrawable textureBar;
    float timecount;
    public int count;
    Label dialoglabel;
    com.badlogic.gdx.scenes.scene2d.ui.Dialog dialogstart;
    private com.badlogic.gdx.scenes.scene2d.ui.Skin skin;
    private float powerup;
    int nopowerups;
    private float powerupdistance;
    float playerLocalX,worldLength;
    Map<Integer, GameMap.DummyPlayer> dummyPlayers;
    GameMap gameMap;

    int characternumber,position;
    Window scorewindow,timewindow,powerupwindow,charinfowindow;
    Label positionboardlabel, GlobalState, Poweruplabel, PowerUpsLeft,Powertype,yourposition;
    TextureRegionDrawable windowbackground;

    public HUD(SpriteBatch sb, String playername, GameMap gameMap,int characternumber){
//        this.characternumber=characternumber;

        this.gameMap=gameMap;

        //playerLocalX =gameMap.bgStartX;
        //worldLength = 18*gameMap.getGameport().getWorldWidth();

        Gdx.app.log("world length",String.valueOf(worldLength));
        powerupdistance=worldLength/3;


        worldTimer = 1;
//        worldTimer = 150;



        timecount=0;

        camera=new PerspectiveCamera();
        viewport=new FillViewport(NimbusRun.V_WIDTH,NimbusRun.V_HEIGHT,new OrthographicCamera());
        shapeRenderer=new ShapeRenderer();

        progress=1f;
        powerup=0f;
        count=3;
        nopowerups=3;
        position=1;
        windowbackground=new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("blackbg2.png"))));

        BitmapFont font=new BitmapFont(Gdx.files.internal("Fonts/font20.fnt"));
        font.getData().setScale(0.85f, 0.85f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        stage=new Stage(viewport,sb);
        stage.clear();

        dialoglabel=new Label(String.format("%01d", count),new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/Baskek45.fnt")), Color.YELLOW));
        skin=new com.badlogic.gdx.scenes.scene2d.ui.Skin(Gdx.files.internal("data/uiskin.json"));
        dialogstart=new com.badlogic.gdx.scenes.scene2d.ui.Dialog("",skin){
            @Override
            public float getPrefWidth(){
                // force dialog width
                // return Gdx.graphics.getWidth() / 2;
                return NimbusRun.V_WIDTH*0.3f;
            }
            @Override
            public float getPrefHeight() {
                // force dialog height
                // return Gdx.graphics.getWidth() / 2;
                return NimbusRun.V_HEIGHT*0.3f;
            }
        };

        scorewindow=new Window("",skin){
            @Override
            public float getPrefWidth(){
                // force dialog width
                // return Gdx.graphics.getWidth() / 2;
                return NimbusRun.V_WIDTH*0.25f;
            }
            @Override
            public float getPrefHeight() {
                // force dialog height
                // return Gdx.graphics.getWidth() / 2;
                return NimbusRun.V_HEIGHT*0.22f;
            }
        };

        timewindow=new Window("",skin) {
            @Override
            public float getPrefWidth() {
                // force dialog width
                // return Gdx.graphics.getWidth() / 2;
                return NimbusRun.V_WIDTH * 0.25f;
            }

            @Override
            public float getPrefHeight() {
                // force dialog height
                // return Gdx.graphics.getWidth() / 2;
                return NimbusRun.V_HEIGHT * 0.22f;
            }
        };

        stage.addActor(dialogstart);
        dialogstart.background(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("blackbg2.png")))));
        dialogstart.setPosition(0, 0, Align.center);
        dialogstart.show(stage);
        timecount=0;
        dialogstart.text(dialoglabel);
        stage.addActor(dialogstart);
        Table table = new Table();
        table.top();
        table.setHeight(viewport.getScreenHeight());
        table.setWidth(viewport.getScreenWidth());
        table.setFillParent(true);

        // 1. LAUGHING BUDDHA
        // 2. SHESHNAH WITH KRISHNA
        // 3. NINE-TAILED FOX
        // 4. KAPPA
        // 5. PONTIANAK
        // 6. MADAME WHITE SNAKE


        timelabel=new Label("TIME",new Label.LabelStyle(font,Color.WHITE)); //BLUE "#44a4c5"
        CharacterLabel=new Label("PLAYER",new Label.LabelStyle(font, Color.YELLOW));   //
        countdownLabel=new Label(String.format("%03d",worldTimer),new Label.LabelStyle(font, Color.YELLOW));
        nameLabel=new Label(playername,new Label.LabelStyle(font, Color.BLACK));
//        Powertype=new Label("POWER: "+playerpowerstring,new Label.LabelStyle(font, Color.RED)); //DARK BLUE
        positionboardlabel=new Label("POSITION",new Label.LabelStyle(font, Color.WHITE));
        yourposition=new Label(String.format("%01d",position),new Label.LabelStyle(font, Color.YELLOW));
        GlobalState=new Label("World has been:",new Label.LabelStyle(font, Color.YELLOW));
        Poweruplabel=new Label("POWER-UPs",new Label.LabelStyle(font, Color.BLACK));
        PowerUpsLeft=new Label(String.format("%01d",nopowerups),new Label.LabelStyle(font, Color.RED)); //DARK BLUE #0681ab


        scorewindow.setBackground(windowbackground);
        scorewindow.pad(0.001f);
        scorewindow.setResizeBorder(1);
        scorewindow.setPosition(0, 0, Align.topLeft);
        scorewindow.add(positionboardlabel).expandX().align(Align.top);
        scorewindow.row();
        scorewindow.add(yourposition).expandX().align(Align.center);

        timewindow.setBackground(windowbackground);
        timewindow.pad(0.001f);
        timewindow.setResizeBorder(1);
        timewindow.setPosition(0, 0, Align.topRight);
        timewindow.add(timelabel).expandX().align(Align.top);
        timewindow.row();
        timewindow.add(countdownLabel).expandX().align(Align.center);
        timewindow.row();


        table.add(scorewindow).expandX().align(Align.center).padTop(5f);
        table.add(GlobalState).expandX().align(Align.center).padTop(5f);
        table.add(timewindow).expandX().align(Align.center).padTop(5f);
//        table.row();
//        table.add(charinfowindow).expand();
//        table.add();
//        table.add(powerupwindow).expand();
 //       table.layout();
        table.row();
        table.add(nameLabel).expand().align(Align.bottom);
        table.add();
        table.add(Poweruplabel).expand().align(Align.bottom);
        table.row();
        table.add(Powertype).expandX().align(Align.top);
        table.add();
        table.add(PowerUpsLeft).expandX().align(Align.top);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

    }
    public void update(float delta) {
        show();
        timecount += delta;

        if(gameMap.getGameMapReadyForHUD()){
            //Log.info("boom boom");
            Map <Integer, Player> players = gameMap.getPlayers();
            //Log.info("how many players: "+ players.size());
            for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
                Player curPlayer = playerEntry.getValue();
                //Log.info("hiXpos: " + curPlayer.getX());
            }
        }

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
//                playerLocalX = gameMap.getPlayers().get(0).getX();
                //gameMap.getDummyById(1).x;
//            playerLocalX=1f;
              //  Gdx.app.log("playerposition", String.valueOf(playerLocalX));
            if (timecount >= 1&&worldTimer>0) {
                worldTimer--;
                countdownLabel.setText(String.format("%03d", worldTimer));
                timecount = 0;
            }
        }

        progress= MathUtils.lerp(progress,(worldTimer/150),0.001f);
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
        shapeRenderer.setColor(Color.YELLOW); //DarkBlure"#0681ab"
        shapeRenderer.rect(Gdx.graphics.getWidth()/2-90, Gdx.graphics.getHeight()*0.1f, 220, 40);
        shapeRenderer.setColor(Color.RED); //DarkRed "#ab3c57"
        shapeRenderer.rect(Gdx.graphics.getWidth()/2-90,  Gdx.graphics.getHeight()*0.1f, this.progress* 220, 40);
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
