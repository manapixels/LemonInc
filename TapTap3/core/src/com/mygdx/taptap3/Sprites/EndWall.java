package com.mygdx.taptap3.Sprites;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.taptap3.Screens.PlayScreen;
import com.mygdx.taptap3.TapTap3;

public class EndWall {
    public World world;
    public Body b2body;
    private PlayScreen screen;

    public EndWall(PlayScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();

        BodyDef bdef = new BodyDef();
        bdef.position.set(screen.getGamePort().getWorldWidth()*3.5f, 0);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(100 / TapTap3.PPM, screen.getGamePort().getWorldHeight());

        fdef.shape = shape;
        b2body.createFixture(fdef);
        shape.dispose();
    }
}
