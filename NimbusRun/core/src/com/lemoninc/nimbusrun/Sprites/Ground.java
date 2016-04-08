package com.lemoninc.nimbusrun.Sprites;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lemoninc.nimbusrun.Screens.PlayScreen;

public class Ground {
    public World world;
    public Body b2body;
    private PlayScreen screen;

    public Ground(PlayScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();

        BodyDef bdef = new BodyDef();
        bdef.position.set(0, 0);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(screen.getGamePort().getWorldWidth()*4, screen.getGamePort().getWorldHeight()/20);

        fdef.shape = shape;
        b2body.createFixture(fdef);
        shape.dispose();
    }
}
