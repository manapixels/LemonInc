package com.lemoninc.nimbusrun.Sprites;

/*********************************
 * FILENAME : Ceiling.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       none
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lemoninc.nimbusrun.Screens.PlayScreen;

public class Ceiling {
    public World world;
    public Body b2body;
    private PlayScreen screen;

    public Ceiling(PlayScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();

        BodyDef bdef = new BodyDef();
        bdef.position.set(screen.getGamePort().getWorldWidth()/2, screen.getGamePort().getWorldHeight());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(screen.getGamePort().getWorldWidth()*4, 0);

        fdef.shape = shape;
        b2body.createFixture(fdef);
        shape.dispose();
    }
}
