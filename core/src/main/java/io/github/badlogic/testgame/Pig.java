package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;

public class Pig extends GameObject {
    public boolean markedForRemoval=false;
   // private final GameScreen game1;

    public enum PigType {
        CLASSIC("classicpig.png", 0.8f, 0.8f,2000), // Smaller circle
        KING("king.png", 1f, 1f,4000),          // Larger circle
        CORPORAL("corporal.jpg", 0.9f, 0.9f,6000);  // Medium circle

        private final String texturePath;
        private final float width;
        private final float height;
        private final int points;

        PigType(String texturePath, float width, float height, int points) {
            this.texturePath = texturePath;
            this.width = width;
            this.height = height;
            this.points = points;
        }

        public String getTexturePath() {
            return texturePath;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        public int getPoints() {
            return points;
        }
    }

    private final PigType type;
    public Pig(World world, float x, float y, PigType type) {
        super(world, type.getTexturePath());
        this.type = type;

        setSize(type.getWidth(), type.getHeight());
        setInitialPosition(x, y);

        System.out.println("Creating pig body at position (" + x + ", " + y + ")");
        this.body = createBody(world, x, y);

        if (this.body == null) {
            throw new IllegalStateException("Failed to create Body for pig.");
        }
    }

    public PigType getType() {
        return type;
    }

    private Body createBody(World world, float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x, y); // Use x, y passed as arguments

        Body body = world.createBody(bdef);

        CircleShape shape = new CircleShape();
        shape.setRadius(getWidth() / 2); // Radius matches the width

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1f; // Adjust density
        fdef.friction = 0.9f; // Moderate friction
        fdef.restitution = 0.2f; // Some bounce

        body.createFixture(fdef);
        shape.dispose();

        body.setUserData(this);
        return body;
    }

    public void burst() {
        System.out.println("Pig burst!");

        // Create burst effect
        createBurstEffect(body.getPosition().x, body.getPosition().y);

        // Add score based on pig type using ScoreManager
//        int points = type.getPoints();
//        ScoreManager.getInstance().addScore(points); // Update score using the ScoreManager
//
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

    public void update() {
        if (body != null) {
            // Get screen dimensions in world units
            float screenWidth = Gdx.graphics.getWidth() / GameScreen.PPM; // PPM = Pixels per meter
            float screenHeight = Gdx.graphics.getHeight() / GameScreen.PPM;

            float x = body.getPosition().x;
            float y = body.getPosition().y;

            // Check if the pig is out of bounds
            if (x < 0 || x > screenWidth || y < 0 || y > screenHeight) {
                if (!markedForRemoval) {
                    System.out.println("Pig is out of screen, marking for removal.");
                    burst(); // Trigger bursting effect and handle score
                }
            }
        }
    }


    @Override
    protected Body createBody(World world) {
        return null;
    }

    @Override
    public void draw(SpriteBatch batch) {
        update();
        super.draw(batch); // Draw pig if texture is available
        if (burstSprite != null) {
            burstSprite.draw(batch); // Draw burst effect
        }
    }


}

