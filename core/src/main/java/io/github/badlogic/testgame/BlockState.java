package io.github.badlogic.testgame;

public class BlockState {
    public float x, y;
    public float width, height;
    public String materialType;

    public BlockState(Block block) {
        this.x = block.getBody().getPosition().x;
        this.y = block.getBody().getPosition().y;
        this.width = block.getWidth();
        this.height = block.getHeight();
        this.materialType = block.getMaterialType().toString();
    }
}
