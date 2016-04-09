package com.lemoninc.nimbusrun.Sprites;

/*********************************
 * FILENAME : StartWall.java
 * DESCRIPTION :
 * PUBLIC FUNCTIONS :
 *       none
 * NOTES :
 * LAST UPDATED: 8/4/2016 09:00
 *
 * ********************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lemoninc.nimbusrun.Screens.PlayScreen;
import com.lemoninc.nimbusrun.NimbusRun;

public class StartWall {
    public World world;
    public Body b2body;
    private PlayScreen screen;
    private GameMap gameMap;

    public StartWall(GameMap gameMap) {
        this.gameMap = gameMap;
        this.world = gameMap.getWorld();

        BodyDef bdef = new BodyDef();
        bdef.position.set(-Gdx.graphics.getWidth() / 2, 0);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(100 / NimbusRun.PPM, Gdx.graphics.getHeight());

        fdef.shape = shape;
        b2body.createFixture(fdef);
        shape.dispose();
    }
}
