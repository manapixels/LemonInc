package com.lemoninc.nimbusrun.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lemoninc.nimbusrun.NimbusRun;


/**
 * Created by Nikki on 10/4/2016.
 */
public class YourCharacterScreen implements Screen{
    private SpriteBatch batcher;
    private Sprite sprite;
    private NimbusRun game;
    private float gameWidth;
    private float gameHeight;
    private Viewport viewport;
    private Camera camera;
    private int playernumber;
    Texture player;
    Texture texture;
    Texture background;
    private ShapeRenderer shapeRenderer;
    private long startTime;

    @Override
    public void show() {
        background= new Texture("Plain-Brown-Worship-Background.png");
        sprite = new Sprite(background);
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    //    sprite.setColor(1, 1, 1, 0);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sprite.setPosition(0, 0);
        batcher = new SpriteBatch();
        shapeRenderer=new ShapeRenderer();
        startTime = TimeUtils.millis();

    }
    public YourCharacterScreen(NimbusRun game, float gameWidth, float gameHeight, int playernumber) {
        this.playernumber=playernumber;
        this.game = game;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;

        camera=new PerspectiveCamera();
        viewport=new FitViewport(gameWidth,gameHeight,camera);
        // 1. LAUGHING BUDDHA
        // 2. SHESHNAH WITH KRISHNA
        // 3. NINE-TAILED FOX
        // 4. KAPPA
        // 5. PONTIANAK
        // 6. MADAME WHITE SNAKE

        if(playernumber==1){
            TextureAtlas atlas=new TextureAtlas("spritesheets/LBspritesheet.atlas");
            player=atlas.findRegion("LaughingBuddha Animation0001").getTexture();
            texture=player;

        }
       else if(playernumber==2){
            TextureAtlas atlas=new TextureAtlas("spritesheets/SKspritesheet.atlas");
            player=atlas.findRegion("Sheshnag&Krishna Animation0001").getTexture();
            texture=player;

        }
        else if(playernumber==3){
            TextureAtlas atlas=new TextureAtlas("spritesheets/FXspritesheet.atlas");
            player=atlas.findRegion("Foxy Animation0001").getTexture();
            texture=player;
        }
        else if (playernumber==4){
            TextureAtlas atlas=new TextureAtlas("spritesheets/KPspritesheet.atlas");
            player=atlas.findRegion("Kappa Animation0001").getTexture();
            texture=player;
        }
        else if(playernumber==5){
            TextureAtlas atlas=new TextureAtlas("spritesheets/PTspritesheet.atlas");
            player=atlas.findRegion("Pontianak Animation0001").getTexture();
            texture=player;
        }
        else if(playernumber==6){
            TextureAtlas atlas=new TextureAtlas("spritesheets/MWSspritesheet.atlas");
            player=atlas.findRegion("MadameWhiteSnake Animation0001").getTexture();
            texture=player;
        }
        else{
            TextureAtlas atlas1=new TextureAtlas("spritesheets/FXspritesheet.atlas");
            player=atlas1.findRegion("Foxy Animation0001").getTexture();
            texture=player;
        }

    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //batcher.setProjectionMatrix(camera.combined);


        batcher.begin();
        sprite.draw(batcher);
        batcher.draw(texture, gameWidth / 2, gameHeight / 2, texture.getWidth(), texture.getHeight());
//        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.rect(75, 100, 300, 300);

        batcher.end();
        shapeRenderer.end();
       // if (TimeUtils.millis()>(startTime+5000)) game.setScreen(new WaitScreen(game,playernumber));


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {

    }

}
