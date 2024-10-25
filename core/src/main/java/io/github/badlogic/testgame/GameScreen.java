package io.github.badlogic.testgame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

public class GameScreen implements Screen {
    private final Core game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private World world;
    private Box2DDebugRenderer b2rend;
    private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
    private Level level;
    private Stage stage;
    private Label scoreLabel;
    private int score = 0; // keeps track of the player's score
    public static final float PPM = 100;

    public GameScreen(Core game, String levelFileName) {
        this.game = game;

        // Load the map first
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load(levelFileName);

        // Get map properties
        int mapWidth = map.getProperties().get("width", Integer.class);
        int mapHeight = map.getProperties().get("height", Integer.class);
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);

        // Calculate world dimensions in meters
        float worldWidth = mapWidth * tileWidth / PPM;
        float worldHeight = mapHeight * tileHeight / PPM;

        // Set up camera and viewport
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(worldWidth, worldHeight, gameCam);
        gameCam.position.set(worldWidth / 2, worldHeight / 2, 0);
        gameCam.update();

        // Create Box2D world
        world = new World(new Vector2(0, 0), true);
        Gdx.app.log("GameScreen", "Gravity: " + world.getGravity());
        b2rend = new Box2DDebugRenderer();

        // Create level
        level = Level.createLevel(levelFileName, world);
        gameObjects = level.getGameObjects();

        // Set up map renderer
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

        // set up stage for ui elements
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        setupUI();
        Gdx.input.setInputProcessor(stage);

        Gdx.app.log("GameScreen", "Level created, map: " + (level.getMap() != null ? "loaded" : "null"));
    }

    public void setInputProcessor() {
        Gdx.input.setInputProcessor(stage);
    }

    private void setupUI() {
        Skin skin=game.skin;

        // create pause button
        Texture pauseTexture = new Texture(Gdx.files.internal("pausebutton.png"));
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseTexture)));

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PauseScreen(game, GameScreen.this));
            }
        });

        // set up score label
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        scoreLabel = new Label("Score: " + score, labelStyle);

        // set up table for ui layout
        Table table = new Table();
        table.top().left();
        table.setFillParent(true);

        table.add(pauseButton).size(50, 50).padTop(10).padLeft(10);

        table.add().expandX();
        table.add(scoreLabel).right().padTop(10).padRight(10);

        stage.addActor(table);
    }


    @Override
    public void show() {
        setInputProcessor();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameCam.update();

        mapRenderer.setView(gameCam);
        mapRenderer.render();

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        for (GameObject gameObject : gameObjects) {
            gameObject.update(game.batch);
        }
        game.batch.end();

         world.step(1/60f, 6, 2);
         b2rend.render(world, gameCam.combined);

        stage.act(delta);
        stage.draw();
    }

//    @Override
//    public void render(float delta) {
//        //gameCam.update();
//
//        Gdx.gl.glClearColor(0,0,0,1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        // render the map
//        mapRenderer.setView(gameCam);
//        mapRenderer.render();
//
//        // tell the spritebatch to do the draw
//        game.batch.setProjectionMatrix(gameCam.combined);
//
//        game.batch.begin();
//        for (GameObject gameObject : gameObjects) {
//            gameObject.update(game.batch);
//        }
//        game.batch.end();
//
//        b2rend.render(world, gameCam.combined);
//
//        // update and draw stage (UI)
//        stage.act(delta);
//        stage.draw();
//    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        // dispose of resources
        mapRenderer.dispose();
        map.dispose();
        stage.dispose();
        b2rend.dispose();
        for (GameObject gameObject : gameObjects) {
            gameObject.getTexture().dispose();
        }
    }


}
