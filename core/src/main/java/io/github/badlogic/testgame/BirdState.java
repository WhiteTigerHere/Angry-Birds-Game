package io.github.badlogic.testgame;

public class BirdState {
    public float x, y;
    public float velocityX, velocityY;
    public String birdType;

    public BirdState(Bird bird) {
        this.x = bird.getBody().getPosition().x;
        this.y = bird.getBody().getPosition().y;
        this.velocityX = bird.getBody().getLinearVelocity().x;
        this.velocityY = bird.getBody().getLinearVelocity().y;
        this.birdType = bird.getType().toString();
    }
}
