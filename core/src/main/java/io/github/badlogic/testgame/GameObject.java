package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class GameObject extends Sprite {
    protected Body body;
    private static final float PPM = GameScreen.PPM;
    protected float initialY,initialX;

    public GameObject(World world, String textureName) {
        super(new Texture(textureName));
        setSize(getWidth() / PPM, getHeight() / PPM);
    }

    public void update(SpriteBatch batch) {
        setPosition(initialX - getWidth()/2, initialY - getHeight()/2);
        draw(batch);
    }

    protected abstract Body createBody(World world);


    public Body getBody() {
        return body;
    }

    public void setInitialPosition(float x, float y) {
        this.initialX = x;
        this.initialY = y;
        setPosition(x - getWidth()/2, y - getHeight()/2);
    }
}
