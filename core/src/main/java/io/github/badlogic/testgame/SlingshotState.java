package io.github.badlogic.testgame;

import java.util.ArrayList;
import java.util.List;

public class SlingshotState {
    public float x;
    public float y;
    public int loadedBirdIndex;
    public List<BirdState> queuedBirds;

    public SlingshotState(Slingshot slingshot) {
        this.x = slingshot.getPosition().x;
        this.y = slingshot.getPosition().y;
        this.loadedBirdIndex = slingshot.getLoadedBirdIndex();
        this.queuedBirds = new ArrayList<>();

        // Store states of birds in queue
        for (Bird bird : slingshot.getBirdQueue()) {
            queuedBirds.add(new BirdState(bird));
        }
    }
}
