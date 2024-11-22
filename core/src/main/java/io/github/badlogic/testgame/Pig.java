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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;

public class Pig extends GameObject {
    public boolean markedForRemoval=false;
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

        // Associate this Pig object with the body
        body.setUserData(this);

        System.out.println("Body created successfully for Pig at position: " + body.getPosition());
        return body;
    }

    public void burst() {
        System.out.println("Pig burst!");

        // Create burst effect
        createBurstEffect(body.getPosition().x, body.getPosition().y);

        // Schedule removal after a delay
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                markedForRemoval = true;
                System.out.println("Pig marked for removal after 0.5 seconds.");
            }
        }, 0.2f); // Delay: 0.2 seconds
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void markForRemoval() {
        this.markedForRemoval = true;
    }

    public Body getBody() {
        return this.body;
    }


    //    private void createBurstEffect(float x, float y) {
//        // Example: Show a single "burst" image
//        Sprite burstSprite = new Sprite(new Texture("burst.png"));
//        burstSprite.setSize(1, 1); // Adjust size as needed
//        burstSprite.setPosition(x - 0.5f, y - 0.5f); // Center it
//
//        // Add the burst sprite to a temporary list for rendering
//        burstEffects.add(burstSprite);
//
//        // Schedule removal after a short delay
//        Timer.schedule(new Timer.Task() {
//            @Override
//            public void run() {
//                burstEffects.remove(burstSprite);
//            }
//        }, 0.5f); // Show for 0.5 seconds
//    }
    private Sprite burstSprite = null;

    private void createBurstEffect(float x, float y) {
        if (burstSprite == null) {
            burstSprite = new Sprite(new Texture("burst.png"));
            burstSprite.setSize(1, 1); // Adjust size as needed
        }
        burstSprite.setPosition(x - 0.5f, y - 0.5f); // Center it

        // Ensure rendering during the burst lifespan
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                burstSprite = null; // Clear the effect after displaying
            }
        }, 1f); // Effect lasts the same as removal delay
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch); // Draw pig if texture is available
        if (burstSprite != null) {
            burstSprite.draw(batch); // Draw burst effect
        }
    }


}

