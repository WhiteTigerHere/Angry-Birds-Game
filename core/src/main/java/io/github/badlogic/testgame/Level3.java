package io.github.badlogic.testgame;

import com.badlogic.gdx.physics.box2d.World;

public class Level3 extends Level {
    public Level3(String mapFilename) {
        super(mapFilename);
    }
    public void create(World world) {
        // Create a pig at a different position with a different size
        Pig pig = new Pig(world, 78, 94, 10, 10);
        gameObjects.add(pig);

        // Create a bird at a different position with a different size
        Bird bird = new Bird(world, 20, 20, 3, 3);
        gameObjects.add(bird);

        // Create a block at a different position with a different size and rotation
        Block block = new Block(world, 50, 70, 15, 15, 30);
        gameObjects.add(block);
    }
}
