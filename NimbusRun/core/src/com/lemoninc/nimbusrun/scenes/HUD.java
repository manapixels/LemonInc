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
import com.esotericsoftware.minlog.Log;
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

    private ShapeRenderer shapeRenderer;
    private float progress;
    String PlayerCharacter;
    float timecount;
    public int count_initial;
    Label dialoglabel;
    com.badlogic.gdx.scenes.scene2d.ui.Dialog dialogstart;
    private com.badlogic.gdx.scenes.scene2d.ui.Skin skin;


    float playerLocalX,worldLength;
    GameMap gameMap;

    int characternumber,position;
    Window scorewindow,timewindow;
    Label positionboardlabel, GlobalState, Poweruplabel, PowerUpsLeft,Powertype,yourposition;
    TextureRegionDrawable windowbackground;

    /**
     * Parameters are passed into HUD at PlayScreen after GameMap is created, initialised.
     * @param sb
     * @param playername
     * @param gameMap client's gameMap
     * @param characternumber
     */
    public HUD(SpriteBatch sb, String playername, GameMap gameMap,int characternumber){

        this.gameMap=gameMap;
        Gdx.app.log("world length",String.valueOf(worldLength));
        worldTimer = 150;

        timecount=0;

        viewport=new FillViewport(NimbusRun.V_WIDTH,NimbusRun.V_HEIGHT,new OrthographicCamera());
        shapeRenderer=new ShapeRenderer();
        progress=1f;
        count_initial=4;

        position=1;
        windowbackground=new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("blackbg2.png"))));

        BitmapFont font=new BitmapFont(Gdx.files.internal("Fonts/font20.fnt"));
        font.getData().setScale(0.85f, 0.85f);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        stage=new Stage(viewport,sb);
        stage.clear();

        dialoglabel=new Label(String.format("%01d", count_initial),new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Fonts/Baskek45.fnt")), Color.YELLOW));
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
        count_initial=4;
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
        CharacterLabel=new Label("PLAYER",new Label.LabelStyle(font, Color.YELLOW));
        countdownLabel=new Label(String.format("%03d",worldTimer),new Label.LabelStyle(font, Color.YELLOW));
        nameLabel=new Label(playername,new Label.LabelStyle(font, Color.BLACK));
        positionboardlabel=new Label("POSITION",new Label.LabelStyle(font, Color.WHITE));
        yourposition=new Label(String.format("%01d",position),new Label.LabelStyle(font, Color.YELLOW));
        GlobalState=new Label("World has been:",new Label.LabelStyle(font, Color.YELLOW));
        Poweruplabel=new Label("POWER-UPs",new Label.LabelStyle(font, Color.BLACK));
        PowerUpsLeft=new Label(String.format("%01d",gameMap.noPowerUps),new Label.LabelStyle(font, Color.RED)); //DARK BLUE #0681ab

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

        //initial count down
        if(count_initial>0) {
            stage.draw();

            if (timecount >= 1) {
                count_initial--;
                dialoglabel.setText(String.format("%01d", count_initial));
                dialogstart.text(dialoglabel);
                timecount = 0;
            }
        }
        //progress=(60-worldTimer)/60;
        else {
            dialogstart.remove();
            if (timecount >= 1&&worldTimer>0) {
                worldTimer--;
                countdownLabel.setText(String.format("%03d", worldTimer));
                timecount = 0;
            }
        }

        PowerUpsLeft.setText(String.format("%01d", gameMap.noPowerUps)); //client's gamemap's powerups

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
