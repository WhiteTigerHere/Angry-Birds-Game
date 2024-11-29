

package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class GameObject extends Sprite {
    protected Body body;
    private static final float PPM = GameScreen.PPM;
    protected float initialY, initialX;
    private Texture texture;
    private Vector2 position;

    /**
     * Constructor for GameObject.
     * - Initializes `texture` if a valid path is given.
     * - For non-texture cases, pass `null` or handle in subclasses.
     */
    public GameObject(World world, String texturePath) {
        if (texturePath != null && !texturePath.isEmpty()) {
            try {
                this.texture = new Texture(Gdx.files.internal(texturePath));
                super.setTexture(this.texture);
                setSize(getWidth() / PPM, getHeight() / PPM);
            } catch (Exception e) {
                System.err.println("Texture loading failed for path: " + texturePath);
                e.printStackTrace(); // Outputs stack trace for debugging
            }
        }
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public void update(SpriteBatch batch) {
        if (texture != null && body != null) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            draw(batch);  // Draw texture-based object
        }
    }

    protected abstract Body createBody(World world);

    public void setInitialPosition(float x, float y) {
        this.initialX = x;
        this.initialY = y;
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }

    public Vector2 getPosition() {
        return position;
    }


    public void draw(SpriteBatch batch) {
        if (texture != null && body != null) {
            batch.draw(texture,
                body.getPosition().x - getWidth() / 2,
                body.getPosition().y - getHeight() / 2,
                getWidth(),
                getHeight());
        }
    }

    public Body getBody() {
        return body;
    }
}
