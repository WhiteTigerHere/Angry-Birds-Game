package io.github.badlogic.testgame;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    public List<BirdState> birds = new ArrayList<>();
    public List<PigState> pigs = new ArrayList<>();
    public List<BlockState> blocks = new ArrayList<>();
    public SlingshotState slingshot;
    public String levelFileName;
    public int score;
    public int deadCount;

    public GameState(String levelFileName, int score, int deadCount) {
        this.levelFileName = levelFileName;
        this.score = score;
        this.deadCount = deadCount;
    }
}
