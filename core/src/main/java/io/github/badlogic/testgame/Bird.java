package io.github.badlogic.testgame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.Serializable;

public class Bird extends GameObject implements Savable{
    public enum BirdType {
        RED("redbird.png", 0.6f, 0.6f, 0.8f,400),
        YELLOW("yellowbird.jpg", 0.6f, 0.6f, 0.8f,600),
        BLACK("blackbird.png", 0.9f, 0.9f, 0.8f,800);

        private final String texturePath;
        private final float width;
        private final float height;
        private final int damage;
        private final float density;

        BirdType(String texturePath, float width, float height,float density, int damage) {
            this.texturePath = texturePath;
            this.width = width;
            this.height = height;
            this.density=density;
            this.damage = damage;
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

        public float getDensity() {
            return density;
        }

        public int getPoints() {
            return damage;
        }
    }

    private final BirdType type;

    public Bird(World world, float x, float y,BirdType type) {
        super(world, type.getTexturePath());
        this.type = type;

        setSize(type.getWidth(), type.getHeight());
        setInitialPosition(x, y);

        System.out.println("Creating bird body at position (" + x + ", " + y + ")");
        this.body = createBody(world, x, y);

        if (this.body == null) {
            throw new IllegalStateException("Failed to create Body for bird.");
        }
    }
    public BirdType getType(){
        return type;
    }

    @Override
    protected Body createBody(World world) {
        return null;
    }

    private Body createBody(World world, float x, float y) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x, y);

        Body body = world.createBody(bdef);

        CircleShape shape = new CircleShape();
        shape.setRadius(getWidth() / 2);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = type.getDensity();
        fdef.friction = 0.9f;
        fdef.restitution = 0.0f;

        body.createFixture(fdef);
        shape.dispose();

        body.setUserData(this);
        return body;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public Object getState() {
        return new BirdState(this);
    }

    @Override
    public void restoreState(Object state, World world) {
        if (state instanceof BirdState) {
            BirdState birdState = (BirdState) state;
            this.getBody().setTransform(birdState.x, birdState.y, 0);
            this.getBody().setLinearVelocity(birdState.velocityX, birdState.velocityY);
        }
    }

}
