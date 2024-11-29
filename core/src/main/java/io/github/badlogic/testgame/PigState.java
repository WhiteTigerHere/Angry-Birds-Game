package io.github.badlogic.testgame;

public class PigState {
    public float x, y;
    public String pigType;

    public PigState(Pig pig) {
        this.x = pig.getBody().getPosition().x;
        this.y = pig.getBody().getPosition().y;
        this.pigType = pig.getType().toString();
    }
}
