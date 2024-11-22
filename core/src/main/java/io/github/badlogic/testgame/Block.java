//package io.github.badlogic.testgame;
//
//import com.badlogic.gdx.physics.box2d.*;
//
//public class Block extends GameObject {
//
//    public Block(World world, float x, float y, float width, float height, float rotation,int choice) {
//        super(world, getTexturePath(choice));
//        setSize(width, height);
//        setInitialPosition(x, y);
//        setRotation(rotation);
//    }
//
//    private static String getTexturePath(int choice) {
//        switch (choice) {
//            case 1: return "verticalwood.jpg";
//            case 2: return "horizwood.jpg";
//            case 3: return "blackbird.png";
//            default: return "verticalwood.jpg"; // default to red bird if choice is out of range
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
//}


//package io.github.badlogic.testgame;
//import com.badlogic.gdx.physics.box2d.*;
//
//public class Block extends GameObject {
//
//    public Block(World world, float x, float y, float width, float height, float rotation,int choice) {
//        super(world, getTexturePath(choice));
//        setSize(width, height);
//        if (world == null) {
//            throw new IllegalArgumentException("World cannot be null.");
//        }
//
//        // Log: Attempt to create the body
//        System.out.println("Creating block body at position (" + x + ", " + y + ")");
//
//        this.body = createBody(world, x, y, width, height);
//        if (this.body == null) {
//            throw new IllegalStateException("Failed to create Body for block.");
//        }
//        setInitialPosition(x, y);
//        //setRotation(rotation);
//    }
//
//    private static String getTexturePath(int choice) {
//        switch (choice) {
//            case 1: return "verticalwood.jpg";
//            case 2: return "horizwood.jpg";
//            case 3: return "blackbird.png";
//            default: return "verticalwood.jpg"; // default to red bird if choice is out of range
//        }
//    }
//
//    @Override
//    protected Body createBody(World world) { // If this is required by GameObject, ensure it's defined
//        // This function can be replaced or removed if not used directly
//        return null;
//    }
//
//    protected Body createBody(World world, float x, float y, float width, float height) {
//        // Define the body definition
//        BodyDef bdef = new BodyDef();
//        bdef.type = BodyDef.BodyType.DynamicBody; // Dynamic body for moving blocks, or StaticBody for stationary blocks
//        bdef.position.set(x, y);
//
//        // Create the body in the world
//        Body body = world.createBody(bdef);
//        if (body == null) {
//            System.err.println("ErrorBlock: Failed to create body in Box2D.");
//            return null;
//        }
//
//        // Define the shape as a rectangle
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(width / 2, height / 2); // Box2D uses half-width and half-height
//
//        // Define the fixture
//        FixtureDef fdef = new FixtureDef();
//        fdef.shape = shape;
//        fdef.density = 1f;  // Adjust as needed for weight
//        fdef.friction = 0.5f; // Optional, adds resistance when sliding
//        fdef.restitution = 0.3f; // Optional, adds bounce
//
//        try {
//            body.createFixture(fdef);
//        } catch (Exception e) {
//            System.err.println("Error creating fixture: " + e.getMessage());
//            return null;
//        } finally {
//            shape.dispose(); // Always dispose of shapes after use
//        }
//
//        // Log success
//        System.out.println("Body created successfully for Block at position: " + body.getPosition());
//
//        return body;
//    }
//
//}


package io.github.badlogic.testgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;

public class Block extends GameObject {
    private ShapeRenderer shapeRenderer;

    public Block(World world, float x, float y, float width, float height, float rotation, int choice) {
        super(world, null); // No texture, handled as a filled rectangle
        this.shapeRenderer = new ShapeRenderer();
        setSize(width, height);
        setInitialPosition(x, y);

        this.body = createBody(world, x, y, width, height);
        if (this.body == null) {
            throw new IllegalStateException("Failed to create Body for Block.");
        }
    }

    protected Body createBody(World world, float x, float y, float width, float height) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody; // Keep blocks dynamic if they can move
        bdef.position.set(x, y);

        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1f; // Ensure realistic mass
        fdef.friction = 0.5f; // Moderate friction
        fdef.restitution = 0.1f; // Reduce bounce for stability

        body.createFixture(fdef);
        shape.dispose();

        return body;
    }


    @Override
    public void draw(SpriteBatch batch) {
        // End the batch to avoid conflicts
        batch.end();

        // Use ShapeRenderer for the block
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BROWN); // Block color

        // Synchronize position and rotation with physics body
        float x = body.getPosition().x - getWidth() / 2;
        float y = body.getPosition().y - getHeight() / 2;
        float originX = getWidth() / 2;
        float originY = getHeight() / 2;

        shapeRenderer.rect(
            x, y,             // Bottom-left corner of the rectangle
            originX, originY, // Origin for rotation
            getWidth(), getHeight(), // Width and height
            1f, 1f,           // Scale factors (1 = no scaling)
            (float) Math.toDegrees(body.getAngle()) // Rotation angle in degrees
        );

        shapeRenderer.end();

        // Restart the batch for subsequent drawing
        batch.begin();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    @Override
    protected Body createBody(World world) {
        return null;
    }
}
