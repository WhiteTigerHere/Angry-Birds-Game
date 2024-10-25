package io.github.badlogic.testgame;

import com.badlogic.gdx.physics.box2d.*;

public class Pig extends GameObject {
    public Pig(World world, float x, float y, float width, float height) {
        super(world, "pig.jpg");
        setSize(width, height);
        setInitialPosition(x, y);
    }

    @Override
    protected Body createBody(World world) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() + getWidth()/2, getY() + getHeight()/2);
        bdef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(bdef);

        CircleShape shape = new CircleShape();
        shape.setRadius(getWidth() / 2);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
       // fdef.density = 1f;  // Add some density
        body.createFixture(fdef);
        shape.dispose();

        return body;
    }
}
