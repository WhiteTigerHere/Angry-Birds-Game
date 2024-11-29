package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import java.util.LinkedList;
import java.util.Queue;

public class Slingshot extends GameObject {
    private Bird loadedBird;
    private Queue<Bird> birdQueue = new LinkedList<>();
    private boolean isDragging = false;
    private final Vector2 startPosition;
    private final Camera camera;
    private final float maxDragDistance = 100 / GameScreen.PPM;
    private ShapeRenderer shapeRenderer;
    private World world;
    private GameScreen gameScreen;

    public Slingshot(World world, float x, float y, float width, float height, Camera camera,GameScreen gameScreen) {
        super(world, "slingshot.png");
        this.world = world;
        setSize(width, height);
        setInitialPosition(x, y);
        this.startPosition = new Vector2(x, y);
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();
        this.gameScreen = gameScreen;
    }

    public Vector2 getPosition() {
        return startPosition;
    }

    public Queue<Bird> getBirdQueue() {
        return new LinkedList<>(birdQueue);
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
            loadedBird.setPosition(startPosition.x, startPosition.y); // Positioning bird at slingshot's start position
            loadedBird.getBody().setActive(true);
            System.out.println("Loaded bird: " + loadedBird);
        }
    }


    private void launchBird() {
        if (loadedBird == null) {
            System.out.println("No bird loaded to launch.");
            return;
        }

        System.out.println("Launching bird: " + loadedBird);
        Vector2 launchVector = startPosition.cpy().sub(loadedBird.getPosition()).scl(5f);
        loadedBird.getBody().applyLinearImpulse(launchVector, loadedBird.getBody().getWorldCenter(), true);
        loadedBird.setLaunched(true);
        loadedBird = null;

        Timer.schedule(new Timer.Task() { // Delay loading the next bird
            @Override
            public void run() {
                if (!birdQueue.isEmpty()) {
                    setLoadedBird(birdQueue.poll());
                }else{
                    // Check the score and required points if all birds are used
                    checkGameOverCondition();
                }
            }
        }, 1); // 1-second delay to avoid overlap
    }


    public int getLoadedBirdIndex() {
        int index = 0;
        for (Bird bird : birdQueue) {
            if (bird == loadedBird) {
                return index;
            }
            index++;
        }
        return -1; // If no bird is loaded
    }

    public void setLoadedBirdIndex(int index) {
        if (index >= 0 && index < birdQueue.size()) {
            // Create a temporary queue to store birds up to the index
            Queue<Bird> tempQueue = new LinkedList<>();
            int i = 0;

            // Iterate over the birdQueue to find the bird at the given index
            for (Bird bird : birdQueue) {
                if (i == index) {
                    // Set the found bird as the loaded bird
                    setLoadedBird(bird);
                } else {
                    tempQueue.add(bird);
                }
                i++;
            }
            // Restore the remaining birds back to the queue
            birdQueue = tempQueue;
        } else {
            System.out.println("Invalid index, no bird set.");
        }
    }

    private void checkGameOverCondition() {
        // Determine required points for the level
        char levelNo = gameScreen.getLevelFileName().charAt(5);
        int levelNum = Character.getNumericValue(levelNo);
        int levelPointsRequired = 0;

        switch (levelNum) {
            case 1:
                levelPointsRequired = 2000;
                break;
            case 2:
                levelPointsRequired = 6000;
                break;
            case 3:
                levelPointsRequired = 12000;
                break;
        }
        // If score is less than required points, transition to LostLevel
        if (gameScreen.getScore() < levelPointsRequired) {
            //String levelfile= "level"+levelNo+(selectedTheme+1)+".tmx
            Gdx.app.postRunnable(() -> {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        gameScreen.getGame().setScreen(new LostLevel(gameScreen.getGame(),gameScreen.getLevelFileName())); // Transition to LostLevel screen
                    }
                }, 3f); // 3-second delay
            });
        }
    }

    private boolean isInsideSlingArea(Vector2 touchPos) {
        float distance = touchPos.dst(startPosition);
        return distance <= maxDragDistance;
    }

    public void update(float delta) {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos3D);
            Vector2 touchPos = new Vector2(touchPos3D.x, touchPos3D.y);

            if (!isDragging && loadedBird != null && isInsideSlingArea(touchPos)) {
                isDragging = true;
            }

            if (isDragging && loadedBird != null) {
                float distance = touchPos.dst(startPosition);
                if (distance > maxDragDistance) {
                    Vector2 direction = touchPos.sub(startPosition).nor();
                    touchPos.set(startPosition.x + direction.x * maxDragDistance,
                        startPosition.y + direction.y * maxDragDistance);
                }
                loadedBird.setPosition(touchPos.x, touchPos.y);
                loadedBird.getBody().setTransform(touchPos.x, touchPos.y, 0);  // Ensure body matches visual for dragging
            }
        } else if (isDragging) {
            if (loadedBird != null) {
                launchBird();
            }
            isDragging = false;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
    }
}

