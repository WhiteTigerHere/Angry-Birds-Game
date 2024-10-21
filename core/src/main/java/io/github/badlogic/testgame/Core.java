package io.github.badlogic.testgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//kk
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Core extends Game {
    private Texture bkgtexture;
    public SpriteBatch batch;

    @Override
    public void create() {
        batch=new SpriteBatch();
        //setScreen(new FirstScreen(this));
        //setScreen(new PauseScreen(this));
        //setScreen(new CompletedGame(this));
        //setScreen(new LostLevel(this));
        setScreen(new LevelWin(this,"Red",7000));
        //setScreen(new MenuScreen(this));
        //bkgtexture = new Texture("libgdxlogo.png");
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }


    @Override
    public void render(){
        super.render();
    }
}
