package io.github.badlogic.testgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Core extends Game {
    private Texture bkgtexture;
    private OrthographicCamera gamecam;
    private Viewport gameport; // Initialized later
    public SpriteBatch batch;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    @Override
    public void create() {
        // Initialize the SpriteBatch

        batch = new SpriteBatch();
        setScreen(new FirstScreen(this));
        MusicManager.getInstance().playMusic();
        //setScreen(new PauseScreen(this));
        //setScreen(new CompletedGame(this));
        //setScreen(new LostLevel(this));
        //setScreen(new LevelWin(this,"Red",7000));
        // Load and render the tiled map

        //mapLoader = new TmxMapLoader();
        //map = mapLoader.load("level12.tmx");
        //renderer = new OrthogonalTiledMapRenderer(map);

        // Initialize the game camera
//        gamecam = new OrthographicCamera();
//
//        // Initialize the viewport (FitViewport keeps the aspect ratio, adjusting if necessary)
//        gameport = new FitViewport(800, 600, gamecam);
//
//        // Set the camera position to the center of the screen
//        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);
    }

    @Override
    public void dispose() {
        // Dispose of the SpriteBatch and map renderer when done
        batch.dispose();
        MusicManager.getInstance().dispose();
        //renderer.dispose();
        super.dispose();
    }

    @Override
    public void render() {
        // Call the superclass render method to ensure the game loop runs correctly
        super.render();

        // Set the projection matrix for the renderer
       // renderer.setView(gamecam);

        // Render the tiled map
        //renderer.render();
    }
}
