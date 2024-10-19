package io.github.badlogic.testgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Core extends Game {
    private Texture bkgtexture;

    @Override
    public void create() {

        setScreen(new FirstScreen(this));
        //bkgtexture = new Texture("libgdxlogo.png");
    }
}
