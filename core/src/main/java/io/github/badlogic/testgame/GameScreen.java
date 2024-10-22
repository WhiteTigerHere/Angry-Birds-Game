package io.github.badlogic.testgame;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

public class GameScreen implements Screen {
    private final Core game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Stage stage;
    private Label scoreLabel;
    private int score = 0; // keeps track of the player's score

    public GameScreen(Core game, String levelFileName) {
        this.game = game;

        // load and set up the tiled map
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load(levelFileName);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // calculate map dimensions
        float mapWidth = map.getProperties().get("width", Integer.class) * 32;
        float mapHeight = map.getProperties().get("height", Integer.class) * 32;

        // set up camera and viewport
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(mapWidth, mapHeight, gameCam);

        gameCam.position.set(mapWidth / 2, mapHeight / 2, 0);

        // set up stage for ui elements
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        setupUI();
        Gdx.input.setInputProcessor(stage);
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
        // update the camera and render the map
        gameCam.update();
        mapRenderer.setView(gameCam);

        // clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // render the map
        mapRenderer.render();

        // draw the stage (ui)
        stage.act();
        stage.draw();
    }

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
    }
}
