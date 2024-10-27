package io.github.badlogic.testgame;

import com.badlogic.gdx.physics.box2d.World;

public class Level1 extends Level {
    public Level1(String mapFilename) {

        super(mapFilename);
    }

    @Override
    public void create(World world) {
        // Set positions and dimensions based on the grid in the image
        // Birds
        createGround(world);
        gameObjects.add(new Bird(world, 135.33f / GameScreen.PPM, 315.33f / GameScreen.PPM, 70 / GameScreen.PPM, 80 / GameScreen.PPM, 1));
        gameObjects.add(new Bird(world, 92.33f / GameScreen.PPM, 160.00f / GameScreen.PPM, 70 / GameScreen.PPM, 80 / GameScreen.PPM, 1));
        gameObjects.add(new Bird(world, 39.00f / GameScreen.PPM, 160.00f / GameScreen.PPM, 70 / GameScreen.PPM, 80 / GameScreen.PPM, 1));

        // Pig
        gameObjects.add(new Pig(world, 725.33f / GameScreen.PPM, 372.33f / GameScreen.PPM, 104 / GameScreen.PPM, 82 / GameScreen.PPM));

        // Blocks
        gameObjects.add(new Block(world, 640.00f / GameScreen.PPM, 237.33f / GameScreen.PPM, 33.67f / GameScreen.PPM, 194.67f / GameScreen.PPM, 0, 1));
        gameObjects.add(new Block(world, 801.33f / GameScreen.PPM, 237.33f / GameScreen.PPM, 33.67f / GameScreen.PPM, 194.67f / GameScreen.PPM, 0, 1));
        gameObjects.add(new Block(world, 720.33f / GameScreen.PPM, 320.67f / GameScreen.PPM, 190.67f / GameScreen.PPM, 32.67f / GameScreen.PPM, 0, 2));
    }
}
