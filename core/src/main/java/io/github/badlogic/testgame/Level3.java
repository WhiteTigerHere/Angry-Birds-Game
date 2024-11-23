package io.github.badlogic.testgame;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;

public class Level3 extends Level {
    public Level3(String mapFilename, GameScreen gameScreen) {
        super(mapFilename, gameScreen);
    }

    @Override
    public void create(World world) {
        createGround(world);
        OrthographicCamera camera = gameScreen.getCamera();

       //  Pig
        gameObjects.add(new Pig(world, 720f / GameScreen.PPM, 256f / GameScreen.PPM, Pig.PigType.CORPORAL));
        gameObjects.add(new Pig(world, 638f / GameScreen.PPM, 383f / GameScreen.PPM, Pig.PigType.KING));
        gameObjects.add(new Pig(world, 570f / GameScreen.PPM, 251f / GameScreen.PPM, Pig.PigType.CLASSIC));

        // Blocks
        gameObjects.add(new Block(world, 638f / GameScreen.PPM, 165f / GameScreen.PPM, 30f / GameScreen.PPM, 60f / GameScreen.PPM, Block.MaterialType.CEMENT));
        gameObjects.add(new Block(world, 802f / GameScreen.PPM, 165f / GameScreen.PPM, 30f / GameScreen.PPM, 60f / GameScreen.PPM, Block.MaterialType.CEMENT));
        gameObjects.add(new Block(world, 474f / GameScreen.PPM, 165f / GameScreen.PPM, 30f / GameScreen.PPM, 60f / GameScreen.PPM, Block.MaterialType.CEMENT));

        gameObjects.add(new Block(world, 638f / GameScreen.PPM, 206f / GameScreen.PPM, 360f / GameScreen.PPM, 20f / GameScreen.PPM, Block.MaterialType.ICE));
       // gameObjects.add(new Block(world, 638f / GameScreen.PPM, 260f / GameScreen.PPM, 30f / GameScreen.PPM, 150f / GameScreen.PPM, Block.MaterialType.WOOD));
       // gameObjects.add(new Block(world, 798f / GameScreen.PPM, 260f / GameScreen.PPM, 30f / GameScreen.PPM, 150f / GameScreen.PPM, Block.MaterialType.WOOD));
        gameObjects.add(new Block(world, 638f / GameScreen.PPM, 317f / GameScreen.PPM, 200f / GameScreen.PPM, 20f / GameScreen.PPM, Block.MaterialType.CEMENT));
       // gameObjects.add(new Block(world, 478f / GameScreen.PPM, 260f / GameScreen.PPM, 30f / GameScreen.PPM, 150f / GameScreen.PPM, Block.MaterialType.WOOD));

        gameObjects.add(new Block(world, 638f / GameScreen.PPM, 231f / GameScreen.PPM, 50f / GameScreen.PPM, 50f / GameScreen.PPM, Block.MaterialType.WOOD));
        gameObjects.add(new Block(world, 638f / GameScreen.PPM, 282f / GameScreen.PPM, 50f / GameScreen.PPM, 50f / GameScreen.PPM, Block.MaterialType.WOOD));
       // gameObjects.add(new Block(world, 638f / GameScreen.PPM, 310f / GameScreen.PPM, 50f / GameScreen.PPM, 50f / GameScreen.PPM, Block.MaterialType.WOOD));

        gameObjects.add(new Block(world, 723f / GameScreen.PPM, 351f / GameScreen.PPM, 50f / GameScreen.PPM, 50f / GameScreen.PPM, Block.MaterialType.WOOD));
        //gameObjects.add(new Block(world, 723f / GameScreen.PPM, 430f / GameScreen.PPM, 50f / GameScreen.PPM, 50f / GameScreen.PPM, Block.MaterialType.CEMENT));

        gameObjects.add(new Block(world, 573f / GameScreen.PPM, 351f / GameScreen.PPM, 50f / GameScreen.PPM, 50f / GameScreen.PPM, Block.MaterialType.WOOD));
       // gameObjects.add(new Block(world, 573f / GameScreen.PPM, 430f / GameScreen.PPM, 50f / GameScreen.PPM, 50f / GameScreen.PPM, Block.MaterialType.CEMENT));

        Slingshot slingshot = new Slingshot(world, 135.33f/ GameScreen.PPM, 315.33f / GameScreen.PPM, 50 / GameScreen.PPM, 100 / GameScreen.PPM, camera);
        gameObjects.add(slingshot);

        Bird bird1 = new Bird(world, 135.33f / GameScreen.PPM, 315.33f / GameScreen.PPM, 70 / GameScreen.PPM, 80 / GameScreen.PPM, 1);
        Bird bird2 = new Bird(world, 92.33f / GameScreen.PPM, 160.00f / GameScreen.PPM, 70 / GameScreen.PPM, 80 / GameScreen.PPM, 2);
        Bird bird3 = new Bird(world, 39.00f / GameScreen.PPM, 160.00f / GameScreen.PPM, 70 / GameScreen.PPM, 80 / GameScreen.PPM, 3);

        slingshot.loadBird(bird1);
        slingshot.loadBird(bird2);
        slingshot.loadBird(bird3);

        gameObjects.add(bird1);
        gameObjects.add(bird2);
        gameObjects.add(bird3);
    }
}
