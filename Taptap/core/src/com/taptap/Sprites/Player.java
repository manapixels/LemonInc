package com.taptap.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import javax.swing.plaf.synth.SynthOptionPaneUI;

/**
 * Created by Wei Sheng on 28/2/2016.
 */
public class Player {
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Texture texture;

    public Player(int x, int y){
        texture = new Texture("pusheen.png");
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void update(float dt){
        if (position.x >= 300){
            velocity.x = 0;
        } else {
            position.add(velocity.x, 0, 0);
        }
        bounds.setPosition(position.x, position.y);
    }

    public void runFaster(){
        velocity.x = velocity.x + (float)0.15;
    }

    public Texture getPlayer(){ return texture; }

    public int getWidth(){ return texture.getWidth(); }

    public int getHeight(){ return texture.getHeight(); }

    public Vector3 getPosition() { return position; }

    public void dispose(){ texture.dispose(); }
}
