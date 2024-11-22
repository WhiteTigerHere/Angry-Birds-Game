package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

import static io.github.badlogic.testgame.GameScreen.PPM;

public class Level {

    protected static TiledMap map;
    protected ArrayList<GameObject> gameObjects;
    protected GameScreen gameScreen;

    public Level(String mapFilename,GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        if (map == null) {
            try {
                map = new TmxMapLoader().load(mapFilename);
                if (map == null) {
                    Gdx.app.error("Level", "Failed to load map: " + mapFilename);
                    throw new RuntimeException("Failed to load map: " + mapFilename);
                }
                Gdx.app.log("Level", "Successfully loaded map: " + mapFilename);
            } catch (Exception e) {
                Gdx.app.error("Level", "Error loading map: " + mapFilename, e);
                throw new RuntimeException("Error loading map: " + mapFilename, e);
            }
        }
        gameObjects = new ArrayList<>();
    }

    public void create(World world) {
        //subclasses override this method
    }

    public static Level createLevel(String levelFileName, World world, GameScreen gameScreen) {
        Gdx.app.log("Level", "Creating level with fileName: " + levelFileName);
        Level level;

        switch (levelFileName.charAt(levelFileName.length() - 6)) {
            case '1':
                level = new Level1(levelFileName, gameScreen);
                break;
            case '2':
                level = new Level2(levelFileName, gameScreen);
                break;
            case '3':
                level = new Level3(levelFileName, gameScreen);
                break;
            default:
                Gdx.app.error("Level", "Invalid level filename: " + levelFileName);
                throw new IllegalArgumentException("Invalid level: " + levelFileName);
        }

        level.create(world);
        return level;
    }

//    protected void createGround(World world) {
//        MapLayer groundLayer = getMap().getLayers().get("ground");
//
//        for (MapObject object : groundLayer.getObjects()) {
//            if (object instanceof RectangleMapObject) {
//                Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//                BodyDef bdef = new BodyDef();
//                bdef.type = BodyDef.BodyType.StaticBody;
//                bdef.position.set((rect.x + rect.width / 2) / PPM, (rect.y + rect.height / 2) / PPM);
//
//                Body body = world.createBody(bdef);
//
//                PolygonShape shape = new PolygonShape();
//                shape.setAsBox(rect.width / 2 / PPM, rect.height / 2 / PPM);
//
//                FixtureDef fdef = new FixtureDef();
//                fdef.shape = shape;
//                body.createFixture(fdef);
//
//                shape.dispose();
//            }
//        }
//    }
protected void createGround(World world) {
    // Get the ground layer from the map
    MapLayer groundLayer = getMap().getLayers().get("ground");

    for (MapObject object : groundLayer.getObjects()) {
        if (object instanceof RectangleMapObject) {
            // Get the rectangle from the map object
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            // Define the body definition
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.x + rect.width / 2) / PPM, (rect.y + rect.height / 2) / PPM);

            // Create the static body in the world
            Body body = world.createBody(bdef);

            // Define the shape as a box
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rect.width / 2 / PPM, rect.height / 2 / PPM);

            // Define the fixture with friction
            FixtureDef fdef = new FixtureDef();
            fdef.shape = shape;
            fdef.friction = 0.8f; // Add friction (range: 0.0 to 1.0, higher = more friction)

            // Create the fixture on the body
            body.createFixture(fdef);

            // Dispose of the shape to free resources
            shape.dispose();
        }
    }
}


    public TiledMap getMap() {
        return this.map;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    // Also dispose of the map and gameObjects when the level is done. You can continually add to this method as you add more resources to your level.
    public void dispose() {
        map.dispose();
        for (GameObject obj : gameObjects) {
            obj.getTexture().dispose();
        }
    }

}
