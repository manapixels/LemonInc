package com.lemoninc.nimbusrun.Sprites;

/*********************************
 * FILENAME : Starship.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       boolean    hasMoved()
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

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