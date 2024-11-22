package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import java.util.LinkedList;
import java.util.Queue;

public class Slingshot extends GameObject {
    private Bird loadedBird;
    private final Queue<Bird> birdQueue = new LinkedList<>();
    private boolean isDragging = false;
    private final Vector2 startPosition;
    private final Camera camera;
    private final float maxDragDistance = 100 / GameScreen.PPM;

    public Slingshot(World world, float x, float y, float width, float height, Camera camera) {
        super(world, "slingshot.png");
        setSize(width, height);
        setInitialPosition(x, y);
        this.startPosition = new Vector2(x, y);
        this.camera = camera;
    }

    @Override
    protected Body createBody(World world) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2);
        bdef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        body.createFixture(fdef);
        shape.dispose();

        return body;
    }

    public void loadBird(Bird bird) {
        System.out.println("Loading bird: " + bird);
        bird.getBody().setActive(false); // Ensure queue birds are inactive
        birdQueue.add(bird);
        if (loadedBird == null) {
            setLoadedBird(birdQueue.poll()); // Loads the first bird upon initialization
        }
    }

    private void setLoadedBird(Bird bird) {
        if (bird != null) {
            loadedBird = bird;
            loadedBird.setPosition(startPosition.x, startPosition.y);
            loadedBird.getBody().setActive(true);
            System.out.println("Loaded bird: " + loadedBird);
        }
    }

    private void launchBird() {
        if (loadedBird != null) {
            System.out.println("Launching bird: " + loadedBird);
            Vector2 launchVector = startPosition.cpy().sub(loadedBird.getPosition()).scl(5f);
            loadedBird.getBody().applyLinearImpulse(launchVector, loadedBird.getBody().getWorldCenter(), true);
            loadedBird = null;

            Timer.schedule(new Timer.Task() { // Delay loading the next bird
                @Override
                public void run() {
                    if (!birdQueue.isEmpty()) {
                        setLoadedBird(birdQueue.poll());
                    }
                }
            }, 1); // 1-second delay to avoid overlap
        }
    }

    private boolean isInsideSlingArea(Vector2 touchPos) {
        float distance = touchPos.dst(startPosition);
        return distance <= maxDragDistance;
    }

public void update(float delta) {
    if (Gdx.input.isTouched() && loadedBird != null) {
        Vector3 touchPos3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos3D);
        Vector2 touchPos = new Vector2(touchPos3D.x, touchPos3D.y);

        if (!isDragging && isInsideSlingArea(touchPos)) {
            isDragging = true;
        }

        if (isDragging) {
            float distance = touchPos.dst(startPosition);
            if (distance > maxDragDistance) {
                Vector2 direction = touchPos.sub(startPosition).nor();
                touchPos.set(startPosition.x + direction.x * maxDragDistance,
                    startPosition.y + direction.y * maxDragDistance);
            }
            loadedBird.setPosition(touchPos.x, touchPos.y);
        }
    } else if (isDragging) {
        if (loadedBird != null) {
            launchBird();
        }
        isDragging = false;
    }
}
}
