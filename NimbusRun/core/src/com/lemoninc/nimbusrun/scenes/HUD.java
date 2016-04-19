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
import com.lemoninc.nimbusrun.Screens.PlayScreen;
import com.lemoninc.nimbusrun.Sprites.GameMap;
import com.lemoninc.nimbusrun.Sprites.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Nikki on 8/4/2016.
 */
public class HUD extends Group implements Disposable,ApplicationListener,Screen{
    TextureAtlas atlas;
    public Stage stage;
    private Viewport viewport;
    public Integer worldTimer;
    private Label playername;
    //private float timecount;
    Label countdownLabel;
    Label timelabel;
    Label CharacterLabel;
    private Camera camera;
    String player;
    private ShapeRenderer shapeRenderer;
    private float progress;
    private Map<Integer, Player> players;
    String PlayerCharacter;
    float timecount;
    public int count;
    Label dialoglabel;
    com.badlogic.gdx.scenes.scene2d.ui.Dialog dialogstart;
    private com.badlogic.gdx.scenes.scene2d.ui.Skin skin;
    private float powerup;
    int nopowerups;
    private float powerupdistance;
    float playerLocalX,worldLength;
    GameMap gameMap;
    ArrayList positioninfo;

    int characternumber,position;
    Window scorewindow,timewindow;
    Label positionboardlabel, GlobalState, Poweruplabel, PowerUpsLeft,Powertype,yourposition;
    String playernamestring,playerpowerstring,Globalinfo;
    TextureRegionDrawable windowbackground;

    private PlayScreen playScreen;

    public  HUD(PlayScreen playScreen, SpriteBatch sb, String playernumber, GameMap gameMap,int characternumber){
        this.playScreen = playScreen;
        this.characternumber=characternumber;
        this.player=playernumber;
        this.gameMap=gameMap;
        worldLength = 300f;
        Gdx.app.log("world length",String.valueOf(worldLength));
        powerupdistance=worldLength/4;
        Gdx.app.log("power distance",String.valueOf(powerupdistance));
        worldTimer = 150;



        timecount=0;
        camera=new PerspectiveCamera();
        viewport=new FillViewport(NimbusRun.V_WIDTH,NimbusRun.V_HEIGHT,new OrthographicCamera());
        shapeRenderer=new ShapeRenderer();
        powerup=0f;
        count=4;
        nopowerups=0;
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
                return NimbusRun.V_WIDTH*0.3f;
            }
            @Override
            public float getPrefHeight() {
                return NimbusRun.V_HEIGHT*0.3f;
            }
        };

        scorewindow=new Window("",skin){
            @Override
            public float getPrefWidth(){
                return NimbusRun.V_WIDTH*0.25f;
            }
            @Override
            public float getPrefHeight() {
                return NimbusRun.V_HEIGHT*0.22f;
            }
        };

        timewindow=new Window("",skin) {
            @Override
            public float getPrefWidth() {
                return NimbusRun.V_WIDTH * 0.25f;
            }

            @Override
            public float getPrefHeight() {
                return NimbusRun.V_HEIGHT * 0.22f;
            }
        };

        stage.addActor(dialogstart);
        dialogstart.background(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("blackbg2.png")))));
        dialogstart.setPosition(0, 0, Align.center);
        dialogstart.show(stage);
        timecount=0;
        count=4;
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

        if(characternumber==1){
            playernamestring="LAUGHING BUDDHA";
            playerpowerstring="STUN";
        }
        else if(characternumber==2){
            playernamestring="SHESHNAH WITH KRISHNA";
            playerpowerstring="FLASH LIGHT";
        }
        else if(characternumber==3){
            playernamestring="NINE-TAILED FOX";
            playerpowerstring="SUCKS BACK";
        }
        else if(characternumber==4){
            playernamestring=" KAPPA";
            playerpowerstring="FREEZE";
        }
        else if(characternumber==5){
            playernamestring="PONTIANAK";
            playerpowerstring="DARKEN";
        }
        else if(characternumber==6){
            playernamestring="MADAME WHITE SNAKE";
            playerpowerstring="JUMP STOP";
        }
        else {
            playernamestring="LAUGHING BUDDHA";
            playerpowerstring="STUN";
        }


        timelabel=new Label("TIME",new Label.LabelStyle(font,Color.WHITE)); //BLUE "#44a4c5"
        CharacterLabel=new Label("PLAYER",new Label.LabelStyle(font, Color.YELLOW));   //
        countdownLabel=new Label(String.format("%03d",worldTimer),new Label.LabelStyle(font, Color.YELLOW));
        playername=new Label(playernamestring,new Label.LabelStyle(font, Color.BLACK));
        Powertype=new Label("POWER: "+playerpowerstring,new Label.LabelStyle(font, Color.RED)); //DARK BLUE
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
        table.row();
        table.add(playername).expand().align(Align.bottom);
        table.add();
        table.add(Poweruplabel).expand().align(Align.bottom);
        table.row();
        table.add(Powertype).expandX().align(Align.top);
        table.add();
        table.add(PowerUpsLeft).expandX().align(Align.top);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);



    }
//    public ArrayList<Float> playersXPositions;
//    public ArrayList<Float> sortedXPositions;
//    public ArrayList<Integer> sortedPlayers;



    static <K,V extends Comparable<? super V>>
    List<Map.Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

        List<Map.Entry<K,V>> sortedEntries = new ArrayList<Map.Entry<K,V>>(map.entrySet());

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );
        return sortedEntries;
    }


    Map<Integer,Float> XandID = new HashMap<Integer,Float>();

    public void update(float delta) {
        show();
        timecount += delta;

        List<Integer> positions = new ArrayList<Integer>();

        if(gameMap.getGameMapReadyForHUD()) {

            Map<Integer, Player> players = gameMap.getPlayers();

            for (Map.Entry<Integer, Player> playerEntry : players.entrySet()) {
                Player curPlayer = playerEntry.getValue();
                XandID.put(curPlayer.getId(), curPlayer.getX());
            }
            int i=1;
            List<Map.Entry<Integer,Float>> sortedlist =entriesSortedByValues(XandID);
            for (Map.Entry<Integer, Float> entry : sortedlist){
                positions.add(entry.getKey());
                Gdx.app.log("Local Id", String.valueOf(gameMap.playerLocal.getId()));
                if(entry.getKey().equals(gameMap.playerLocal.getId())) {
                    Gdx.app.log("Here", "I am here");
                    position = i;

                    Gdx.app.log("Index value:",String.valueOf(position));
                }
                i++;
            }

            playScreen.setRankings(positions);

            yourposition.setText(String.format("%01d", position));
            Gdx.app.log("Pos: ",String.valueOf(position));
            Gdx.app.log("Hi",String.valueOf(entriesSortedByValues(XandID)));
//            for (Map.Entry<Integer, Float> entry : XandID.entrySet()) {
//               Gdx.app.log("Playerinfo: ", "PLayer POS : " + entry.getValue()
//                       + "  : PlayerID" + entry.getKey());
//            }
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
        else {
            dialogstart.remove();
            if (timecount >= 1&&worldTimer>0) {
                worldTimer--;
                countdownLabel.setText(String.format("%03d", worldTimer));
                timecount = 0;
            }
        }
       if (nopowerups == 0) {
           if(playerLocalX/powerupdistance>=1) {
               powerupdistance=powerupdistance+75f;
               nopowerups=1;
               powerup = 1f;
               PowerUpsLeft.setText(String.format("%01d", nopowerups));
           }
           else{
               powerup=playerLocalX/powerupdistance;
               nopowerups=0;
               PowerUpsLeft.setText(String.format("%01d", nopowerups));
           }
       }
        else{
           powerup=1f;
           nopowerups=1;
           PowerUpsLeft.setText(String.format("%01d", nopowerups));

       }
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
        shapeRenderer.rect(Gdx.graphics.getWidth()/2-90,  Gdx.graphics.getHeight()*0.1f, this.powerup* 220, 40);
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
