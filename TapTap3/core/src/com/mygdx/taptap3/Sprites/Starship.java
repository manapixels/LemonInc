package com.mygdx.taptap3.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by kevin on 3/25/2016.
 */
public class Starship extends Sprite {
    Vector2 previousPosition;

    public Starship(Texture texture) {
        super(texture);
        previousPosition = new Vector2(getX(), getY()); //Sprite.getX()
    }

    public boolean hasMoved() {
        if (previousPosition.x != getX() || previousPosition.y != getY()) {
            previousPosition.x = getX();
            previousPosition.y = getY();
            return true;
        }
        return false;
    }
}