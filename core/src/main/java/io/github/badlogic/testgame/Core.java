package io.github.badlogic.testgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.Gdx.files;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Core extends Game {
    private Texture bkgtexture;
    Skin skin;
    private OrthographicCamera gamecam;
    private Viewport gameport;
    public SpriteBatch batch;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load the skin for ui elements
        skin = new Skin(files.internal("uiskin.json"));

        setScreen(new FirstScreen(this));
        MusicManager.getInstance().playMusic();
        //setScreen(new PauseScreen(this));
        //setScreen(new CompletedGame(this));
        //setScreen(new LostLevel(this));
        //setScreen(new LevelWin(this,"Red",7000));
    }

    // method to generate custom bitmap font
    BitmapFont generateFont(int baseFontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ARIAL.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = baseFontSize; // to set base font size dynamically
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    @Override
    public void dispose() {
        // dispose of resources when done
        batch.dispose();
        MusicManager.getInstance().dispose();
        super.dispose();
    }

    @Override
    public void render() {
        // call superclass render method to ensure game loop runs correctly
        super.render();

    }
}
