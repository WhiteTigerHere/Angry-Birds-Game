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
    private int score = 0; // Keeps track of the player's score

    public GameScreen(Core game, String levelFileName) {
        this.game = game;

        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load(levelFileName);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        float mapWidth = map.getProperties().get("width", Integer.class) * 32;
        float mapHeight = map.getProperties().get("height", Integer.class) * 32;

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(mapWidth, mapHeight, gameCam);

        gameCam.position.set(mapWidth / 2, mapHeight / 2, 0);

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        setupUI();
        Gdx.input.setInputProcessor(stage);
    }
    public void setInputProcessor() {
        Gdx.input.setInputProcessor(stage);
    }

    private void setupUI() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        Texture pauseTexture = new Texture(Gdx.files.internal("pausebutton.png"));
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseTexture)));

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PauseScreen(game, GameScreen.this));
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        scoreLabel = new Label("Score: " + score, labelStyle);

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
        // Update the camera and render the map
        gameCam.update();
        mapRenderer.setView(gameCam);

        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1); // Set the background to black
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // Render the map
        mapRenderer.render();

        // Draw the stage (UI)
        stage.act();
        stage.draw();
        //score will be incresed here
        //scoreLabel.setText("Score: " + (int) score);
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        stage.getViewport().update(width, height, true); // Ensure the UI resizes properly
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
        mapRenderer.dispose();
        map.dispose();
        stage.dispose();
    }
}
