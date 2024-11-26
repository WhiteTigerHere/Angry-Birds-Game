package io.github.badlogic.testgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;

public class Block extends GameObject {

    private ShapeRenderer shapeRenderer;
    private MaterialType materialType; // Store material type for the block

    public enum MaterialType {
        WOOD(Color.BROWN, 1f, 0.9f, 0f),      // Default values for wood
        ICE(new Color(0.7f, 0.8f, 0.9f, 0.7f), 0.5f, 0.1f, 0f), // Transparent sky-blue
        CEMENT(Color.GRAY, 2f, 0.9f, 0f);    // Gray color, higher density

        public final Color color;
        public final float density;
        public final float friction;
        public final float restitution;

        MaterialType(Color color, float density, float friction, float restitution) {
            this.color = color;
            this.density = density;
            this.friction = friction;
            this.restitution = restitution;
        }
    }

    public Block(World world, float x, float y, float width, float height, MaterialType materialType) {
        super(world, null); // No texture, handled as a filled rectangle
        this.shapeRenderer = new ShapeRenderer();
        this.materialType = materialType;
        setSize(width, height);
        setInitialPosition(x, y);

        this.body = createBody(world, x, y, width, height, materialType);
        if (this.body == null) {
            throw new IllegalStateException("Failed to create Body for Block.");
        }
    }

    protected Body createBody(World world, float x, float y, float width, float height, MaterialType materialType) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody; // Blocks are dynamic by default
        bdef.position.set(x, y);

        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = materialType.density;
        fdef.friction = materialType.friction;
        fdef.restitution = materialType.restitution;

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
        shapeRenderer.setColor(materialType.color); // Use material-specific color

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
        body.setUserData(this);

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

