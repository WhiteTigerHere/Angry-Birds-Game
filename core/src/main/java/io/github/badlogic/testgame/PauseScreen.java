package io.github.badlogic.testgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

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
        this.gameScreen = gameScreen;  // Assign the current game screen
    }

    private BitmapFont generateFont(int baseFontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ARIAL.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = baseFontSize; // Set base font size dynamically
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }


    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("paused2.png")); // Load background image

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Generate a font based on screen size
        int fontSize = Math.max(20, Gdx.graphics.getWidth() / 40); // Adjust font size based on screen width
        BitmapFont font = generateFont(fontSize);

        // Apply the font to the skin
        skin.getFont("default-font").getData().setScale(fontSize / 20.0f); // Adjust font scale
        skin.add("custom-font", font, BitmapFont.class);

        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);


        // Set up label style for the title
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = font;
        titleLabelStyle.fontColor = Color.BLUE; // Set font color of title label to blue

        // Set up label style for theme names (keeping them white)
        Label.LabelStyle themeLabelStyle = new Label.LabelStyle();
        themeLabelStyle.font = font;
        themeLabelStyle.fontColor = Color.WHITE; // Keep theme names white


        // Add a new row for buttons and proper alignment
        Table buttonTable = new Table();
        mainTable.add(buttonTable).colspan(3).center().padTop(20);

        TextButton restartButton = new TextButton("Restart Game", skin);
        buttonTable.add(restartButton).colspan(3).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).padRight(50);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.setScreen(new GameScreen(game, ")); // Replace with your actual level file name
                resumeGame();
            }
        });


//        musicButton = new TextButton("Music Off", skin);
       // buttonTable.add(musicButton).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).padLeft(50);

//        musicButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                //switch off music
//            }
//        });
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

        // Home button
        TextButton homeButton = new TextButton("Main Menu", skin);
        buttonTable.add(homeButton).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).padLeft(50);

        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game)); // Return to main menu
            }
        });

        mainTable.row().padTop(20);

        // resume button
        TextButton resumeButton = new TextButton("Resume", skin);
        mainTable.add(resumeButton).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padLeft(100).padTop(50);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resumeGame();
            }
        });


        mainTable.row().padTop(20); // Add space before any future content (if needed)
        Gdx.input.setInputProcessor(stage);
    }

    private void resumeGame() {
        gameScreen.setInputProcessor(); // Reset the input processor for the game screen
        game.setScreen(gameScreen);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // Draw the background
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // Update and draw the stage (UI)
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
