package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
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

    public Level(String mapFilename) {

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

    public static Level createLevel(String levelFileName, World world) {
        Gdx.app.log("Level", "Creating level with fileName: " + levelFileName);
        Level level;

        switch (levelFileName.charAt(levelFileName.length() - 5)) {
            case '1':
                level = new Level1(levelFileName);
                break;
            case '2':
                level = new Level2(levelFileName);
                break;
            case '3':
                level = new Level3(levelFileName);
                break;
            default:
                Gdx.app.error("Level", "Invalid level filename: " + levelFileName);
                throw new IllegalArgumentException("Invalid level: " + levelFileName);
        }

        // Move the ground creation code here
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //ground
        for(MapObject object: level.map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth()/2, rect.getY() + rect.getHeight()/2);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2, rect.getHeight()/2);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        level.create(world);
        return level;
    }

    protected void createGround(World world) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(getMap().getProperties().get("width", Integer.class) * 0.5f / PPM, 0.5f); // Center of the ground

        Body body = world.createBody(bdef);

        shape.setAsBox(getMap().getProperties().get("width", Integer.class) * 0.5f / PPM, 0.5f); // Half width of the map, 0.5 meters high
        fdef.shape = shape;
        body.createFixture(fdef);
        shape.dispose();
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
