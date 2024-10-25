package io.github.badlogic.testgame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.*;

public class Bird extends Sprite {
    public World world;
    public Body body;

    public Bird(World world){
        super(new Texture("bird.jpeg"));
        this.world=world;
        BodyDef bdef =new BodyDef();
        bdef.position.set(152,269);
        bdef.type=BodyDef.BodyType.DynamicBody;
        body=world.createBody(bdef);
        FixtureDef fdef=new FixtureDef();
        CircleShape shape=new CircleShape();
        shape.setRadius(5);

        fdef.shape=shape;
        body.createFixture(fdef);
        this.setSize(50,50);
        this.setPosition(64,64);
    }
    public void update(){
        setPosition(body.getPosition().x, body.getPosition().y);
    }

}
