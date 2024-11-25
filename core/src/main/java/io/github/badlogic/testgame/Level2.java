package io.github.badlogic.testgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;

public class Level2 extends Level {
    public Level2(String mapFilename, GameScreen gameScreen) {
        super(mapFilename, gameScreen);
    }

    @Override
    public void create(World world) {
        createGround(world);
        OrthographicCamera camera = gameScreen.getCamera();

        // Pig
        gameObjects.add(new Pig(world, 720f / GameScreen.PPM, 272f / GameScreen.PPM, Pig.PigType.KING));
        gameObjects.add(new Pig(world, 720f / GameScreen.PPM, 430f / GameScreen.PPM, Pig.PigType.CLASSIC));
        // Blocks
        gameObjects.add(new Block(world, 638f / GameScreen.PPM, 165f / GameScreen.PPM, 30f / GameScreen.PPM, 60f / GameScreen.PPM, Block.MaterialType.WOOD));
        gameObjects.add(new Block(world, 802f / GameScreen.PPM, 165f / GameScreen.PPM, 30f / GameScreen.PPM, 60f / GameScreen.PPM, Block.MaterialType.WOOD));
        gameObjects.add(new Block(world, 720f / GameScreen.PPM, 206f / GameScreen.PPM, 220f / GameScreen.PPM, 20f / GameScreen.PPM, Block.MaterialType.ICE));
        gameObjects.add(new Block(world, 638f / GameScreen.PPM, 292f / GameScreen.PPM, 30f / GameScreen.PPM, 150f / GameScreen.PPM, Block.MaterialType.WOOD));
        gameObjects.add(new Block(world, 802f / GameScreen.PPM, 292f / GameScreen.PPM, 30f / GameScreen.PPM, 150f / GameScreen.PPM, Block.MaterialType.WOOD));
        gameObjects.add(new Block(world, 720f / GameScreen.PPM, 378f / GameScreen.PPM, 220f / GameScreen.PPM, 20f / GameScreen.PPM, Block.MaterialType.ICE));

        Slingshot slingshot = new Slingshot(world, 135.33f/ GameScreen.PPM, 315.33f / GameScreen.PPM, 50 / GameScreen.PPM, 100 / GameScreen.PPM, camera,gameScreen);
        gameObjects.add(slingshot);

        Bird bird1 = new Bird(world, 135.33f / GameScreen.PPM, 315.33f / GameScreen.PPM, 70 / GameScreen.PPM, 80 / GameScreen.PPM, 1);
        Bird bird2 = new Bird(world, 92.33f / GameScreen.PPM, 160.00f / GameScreen.PPM, 70 / GameScreen.PPM, 80 / GameScreen.PPM, 1);
        Bird bird3 = new Bird(world, 39.00f / GameScreen.PPM, 160.00f / GameScreen.PPM, 70 / GameScreen.PPM, 80 / GameScreen.PPM, 2);

        slingshot.loadBird(bird1);
        slingshot.loadBird(bird2);
        slingshot.loadBird(bird3);

        gameObjects.add(bird1);
        gameObjects.add(bird2);
        gameObjects.add(bird3);
    }
}
