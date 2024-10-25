package io.github.badlogic.testgame;

import com.badlogic.gdx.physics.box2d.World;

public class Level1 extends Level {
    public Level1(String mapFilename) {
        super(mapFilename);
    }

    @Override
    public void create(World world) {
        float worldWidth = getMap().getProperties().get("width", Integer.class) * getMap().getProperties().get("tilewidth", Integer.class) / GameScreen.PPM;
        float worldHeight = getMap().getProperties().get("height", Integer.class) * getMap().getProperties().get("tileheight", Integer.class) / GameScreen.PPM;

        float objectY = worldHeight / 2;  // Place objects at the middle height of the world

        gameObjects.add(new Pig(world, worldWidth / 2, objectY, 1f, 1f));
        gameObjects.add(new Bird(world, worldWidth / 3, objectY, 1f, 1f));
        gameObjects.add(new Block(world, 2 * worldWidth / 3, objectY, 1f, 1f, 0));
    }
}
