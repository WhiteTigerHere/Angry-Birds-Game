package io.github.badlogic.testgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;

public class Level1 extends Level {
    public Level1(String mapFilename, GameScreen gameScreen) {
        super(mapFilename, gameScreen);
    }

    @Override
    public void create(World world) {
        // Set positions and dimensions based on the grid in the image
        createGround(world);

        OrthographicCamera camera = gameScreen.getCamera();

        // Pig
        gameObjects.add(new Pig(world, 720f / GameScreen.PPM, 360f / GameScreen.PPM, Pig.PigType.CLASSIC));

        // Blocks
        gameObjects.add(new Block(world, 638f / GameScreen.PPM, 210f / GameScreen.PPM, 30f / GameScreen.PPM, 150f / GameScreen.PPM, Block.MaterialType.WOOD));
        gameObjects.add(new Block(world, 802f / GameScreen.PPM, 210f / GameScreen.PPM, 30f / GameScreen.PPM, 150f / GameScreen.PPM, Block.MaterialType.WOOD));
        gameObjects.add(new Block(world, 720f / GameScreen.PPM, 301f / GameScreen.PPM, 220f / GameScreen.PPM, 30f / GameScreen.PPM, Block.MaterialType.WOOD));

        //String levelfile= gameScreen.getLevelFileName();

        Slingshot slingshot = new Slingshot(world, 135.33f/ GameScreen.PPM, 315.33f / GameScreen.PPM, 50 / GameScreen.PPM, 100 / GameScreen.PPM, camera,gameScreen);
        gameScreen.setSlingshot(slingshot);
        gameObjects.add(slingshot);

        Bird bird1 = new Bird(world, 135.33f / GameScreen.PPM, 315.33f / GameScreen.PPM, Bird.BirdType.RED);
        Bird bird2 = new Bird(world, 92.33f / GameScreen.PPM, 160.00f / GameScreen.PPM, Bird.BirdType.RED);
        Bird bird3 = new Bird(world, 39.00f / GameScreen.PPM, 160.00f / GameScreen.PPM, Bird.BirdType.RED);

        slingshot.loadBird(bird1);
        slingshot.loadBird(bird2);
        slingshot.loadBird(bird3);

        gameObjects.add(bird1);
        gameObjects.add(bird2);
        gameObjects.add(bird3);


    }
}
