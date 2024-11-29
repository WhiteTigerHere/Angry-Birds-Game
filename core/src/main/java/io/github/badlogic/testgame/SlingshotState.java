package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class SlingshotState {
    public float x, y;
    public int loadedBirdIndex;

    public SlingshotState(Slingshot slingshot) {

        if (slingshot == null || slingshot.getPosition() == null) {
            Gdx.app.error("SlingshotState", "Slingshot or its position is null. Using default values.");
            this.x = 135.33f / GameScreen.PPM; // Default position
            this.y = 315.33f / GameScreen.PPM;
            this.loadedBirdIndex = -1; // Default index, indicating no bird is loaded
        } else {
            this.x = slingshot.getPosition().x;
            this.y = slingshot.getPosition().y;
            this.loadedBirdIndex = slingshot.getLoadedBirdIndex();
        }
    }
}
