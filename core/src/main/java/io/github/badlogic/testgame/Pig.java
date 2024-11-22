//package io.github.badlogic.testgame;
//
//import com.badlogic.gdx.physics.box2d.*;
//
//public class Pig extends GameObject {
//    public Pig(World world, float x, float y, float width, float height) {
//        super(world, "classicpig.png");
//        setSize(width, height);
//        setInitialPosition(x, y);
//    }
//
//    @Override
//    protected Body createBody(World world) {
//        BodyDef bdef = new BodyDef();
//        bdef.position.set(getX() + getWidth()/2, getY() + getHeight()/2);
//        bdef.type = BodyDef.BodyType.DynamicBody;
//
//        Body body = world.createBody(bdef);
//
//        CircleShape shape = new CircleShape();
//        shape.setRadius(getWidth() / 2);
//
//        FixtureDef fdef = new FixtureDef();
//        fdef.shape = shape;
//       // fdef.density = 1f;  // Add some density
//        body.createFixture(fdef);
//        shape.dispose();
//
//        return body;
//    }
//}

package io.github.badlogic.testgame;

import com.badlogic.gdx.physics.box2d.*;

public class Pig extends GameObject {
    public Pig(World world, float x, float y, float width, float height) {
        super(world, "classicpig.png");
        setSize(width, height);

        if (world == null) {
            throw new IllegalArgumentException("World cannot be null.");
        }

        System.out.println("Creating pig body at position (" + x + ", " + y + ")");
        this.body = createBody(world, x, y, width, height);

        if (this.body == null) {
            throw new IllegalStateException("Failed to create Body for pig.");
        }

        setInitialPosition(x, y);
    }

    @Override
    protected Body createBody(World world) {
        return null;
    }

    protected Body createBody(World world, float x, float y, float width, float height) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x, y);
        bdef.fixedRotation = false; // Allow rotation for realistic reactions

        Body body = world.createBody(bdef);

        CircleShape shape = new CircleShape();
        shape.setRadius(getWidth() / 3);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1f;  // Adjust density
        fdef.friction = 0.5f; // Moderate friction to prevent sticking
        fdef.restitution = 0.2f; // Some bounce for realism

        try {
            body.createFixture(fdef);
        } catch (Exception e) {
            System.err.println("Error creating fixture: " + e.getMessage());
            return null;
        } finally {
            shape.dispose();
        }

        // Optional: Lower damping for dynamic responses
        body.setLinearDamping(0.1f); // Light damping for smoother motion
        body.setAngularDamping(0.1f); // Allow slight spinning

        System.out.println("Body created successfully for Pig at position: " + body.getPosition());
        return body;
    }
}

