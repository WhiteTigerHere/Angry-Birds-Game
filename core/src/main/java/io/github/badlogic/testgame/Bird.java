//package io.github.badlogic.testgame;
//
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.*;
//
//public class Bird extends GameObject {
//    public Bird(World world, float x, float y, float width, float height, int choice) {
//        super(world, getTexturePath(choice));
//        setSize(width, height);
//        setInitialPosition(x, y);
//
//
//    }
//
//    private static String getTexturePath(int choice) {
//        switch (choice) {
//            case 1: return "redbird.png";
//            case 2: return "yellowbird.webp";
//            case 3: return "blackbird.png";
//            default: return "redbird.png"; // default to red bird if choice is out of range
//        }
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
//
//    public Body getBody() {
//        return body;
//    }
//
//    public Vector2 getPosition() {
//        return body.getPosition();
//    }
//}


package io.github.badlogic.testgame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Bird extends GameObject {
    public Bird(World world, float x, float y, float width, float height, int choice) {
        super(world, getTexturePath(choice));
        setSize(width, height);
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null.");
        }

        // Log: Attempt to create the body
        System.out.println("Creating Bird body at position (" + x + ", " + y + ")");

        this.body = createBody(world, x, y, width, height);
        if (this.body == null) {
            throw new IllegalStateException("Failed to create Body for Bird.");
        }

        setInitialPosition(x, y);  // Ensure position is set after body creation
    }

    private static String getTexturePath(int choice) {
        switch (choice) {
            case 1: return "redbird.png";
            case 2: return "yellowbird.webp";
            case 3: return "blackbird.png";
            default: return "redbird.png"; // default to red bird if choice is out of range
        }
    }

    @Override
    protected Body createBody(World world) { // If this is required by GameObject, ensure it's defined
        // This function can be replaced or removed if not used directly
        return null;
    }

    private Body createBody(World world, float x, float y, float width, float height) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x, y);

        Body body = world.createBody(bdef);
        if (body == null) {
            System.err.println("ErrorBird: Failed to create body in Box2D.");
            return null;
        }

        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1.0f;

        try {
            body.createFixture(fdef);
        } catch (Exception e) {
            System.err.println("Error creating fixture: " + e.getMessage());
            return null;
        } finally {
            shape.dispose();
        }

        // Log: Body creation success
        System.out.println("Body created successfully for Bird at position: " + body.getPosition());

        return body;
    }

    public void setPosition(float x, float y) {
        if (this.body != null) {
            body.setTransform(x, y, body.getAngle());
        } else {
            throw new IllegalStateException("Body is not initialized.");
        }
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }
}

