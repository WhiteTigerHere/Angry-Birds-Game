package io.github.badlogic.testgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Core extends Game {
    private Texture bkgtexture;
    public SpriteBatch batch;

    @Override
    public void create() {
        batch=new SpriteBatch();
        setScreen(new FirstScreen(this));
        //setScreen(new MainMenu(this));
        //bkgtexture = new Texture("libgdxlogo.png");
    }

    @Override
    public void render(){
        super.render();
    }
}
