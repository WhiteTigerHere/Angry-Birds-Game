package io.github.badlogic.testgame;

public class SlingshotState {
    public float x, y;
    public int loadedBirdIndex;

    public SlingshotState(Slingshot slingshot) {
        this.x = slingshot.getPosition().x;
        this.y = slingshot.getPosition().y;
        this.loadedBirdIndex = slingshot.getLoadedBirdIndex();
    }
}
