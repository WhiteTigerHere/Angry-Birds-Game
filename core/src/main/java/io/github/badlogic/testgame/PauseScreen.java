package io.github.badlogic.testgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PauseScreen implements Screen {
    private final Core game;
    private GameScreen gameScreen;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;
    private ImageButton[] themeButtons;
    private int selectedTheme = -1;
    private Label[] themeLabels;
    private TextButton musicButton;

    public PauseScreen(Core game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;  // assign the current game screen
    }

    @Override
    public void show() {
        // load background image
        backgroundTexture = new Texture(Gdx.files.internal("paused2.png"));

        skin=game.skin;

        // font based on screen size
        int fontSize = Math.max(20, Gdx.graphics.getWidth() / 40);
        BitmapFont font = game.generateFont(fontSize);

        // apply font to skin
        skin.getFont("default-font").getData().setScale(fontSize / 20.0f);
        skin.add("custom-font", font, BitmapFont.class);

        // set up stage and input processor
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // create main table
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // set up label styles
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = font;
        titleLabelStyle.fontColor = Color.BLUE;

        Label.LabelStyle themeLabelStyle = new Label.LabelStyle();
        themeLabelStyle.font = font;
        themeLabelStyle.fontColor = Color.WHITE;

        // create button table
        Table buttonTable = new Table();
        mainTable.add(buttonTable).colspan(3).center().padTop(20);

        // add restart game button
        TextButton restartButton = new TextButton("Restart Level", skin);
        buttonTable.add(restartButton).colspan(3).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).padRight(50);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game,gameScreen.getLevelFileName()));
            }
        });

        // add music toggle button
        musicButton = new TextButton(GameSettings.getInstance().isMusicEnabled() ? "Music: On" : "Music: Off", skin);
        buttonTable.add(musicButton).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).padLeft(50);

        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean newState = !GameSettings.getInstance().isMusicEnabled();
                GameSettings.getInstance().setMusicEnabled(newState);
                MusicManager.getInstance().updateMusicState();
                musicButton.setText(newState ? "Music: On" : "Music: Off");
            }
        });

        // add main menu button
        TextButton homeButton = new TextButton("Main Menu", skin);
        buttonTable.add(homeButton).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).padLeft(60);

        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game)); // return to main menu
            }
        });

        mainTable.row().padTop(40);

        // add resume button
        TextButton resumeButton = new TextButton("Resume", skin);
        mainTable.add(resumeButton).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padTop(50);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resumeGame();
            }
        });

        // add save game button
        TextButton saveGameButton = new TextButton("Save Game", skin);
        mainTable.add(saveGameButton).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padLeft(50).padTop(50);

        saveGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //gameScreen.saveGame(gameScreen.getWorld(), gameScreen.getLevel.getslingshot(),gamesavegame.dat");
                gameScreen.saveCurrentGame();
                game.setScreen(new MainMenu(game));
                //resumeGame();
                //serialization
            }
        });

        mainTable.row().padTop(20);
    }

    // method to resume game
    private void resumeGame() {
        gameScreen.setInputProcessor(); // reset input processor for game screen
        game.setScreen(gameScreen);
    }

    @Override
    public void render(float delta) {
        // clear screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // draw background
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // update and draw stage (ui)
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // update proj matrix and viewport
        game.batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
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
        game.batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
        skin.dispose();
        for (ImageButton themeButton : themeButtons) {
            if (themeButton != null) {
                ((TextureRegionDrawable) themeButton.getStyle().imageUp).getRegion().getTexture().dispose();
            }
        }
    }
}
