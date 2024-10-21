
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
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class LevelWin implements Screen {
    private final Core game;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;
    private final String playerName;
    private final int levelScore; // Store the score of the level

    public LevelWin(Core game, String playerName, int levelScore) {
        this.game = game;
        this.playerName = playerName;
        this.levelScore = levelScore; // Initialize the score from memory
    }

    private BitmapFont generateFont(int baseFontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ARIAL.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = baseFontSize;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("winbg.png")); // Set the background image

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Generate font based on screen size
        int fontSize = Math.max(20, Gdx.graphics.getWidth() / 100);
        BitmapFont font = generateFont(fontSize);
        skin.getFont("default-font").getData().setScale(fontSize / 20.0f);
        skin.add("custom-font", font, BitmapFont.class);

        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.OLIVE;

        // Title Label
        Label congratsLabel = new Label("Congratulations!", labelStyle);
        mainTable.add(congratsLabel).padBottom(20);
        mainTable.row();

        // Display the player's score
        Label scoreLabel = new Label("Your Score: " + levelScore, labelStyle);
        mainTable.add(scoreLabel).padBottom(20);
        mainTable.row();

        // Table for buttons
        Table buttonTable = new Table();
        mainTable.add(buttonTable).padTop(20);

        // Next level button
        TextButton nextLevelButton = new TextButton("Next Level", skin);
        buttonTable.add(nextLevelButton).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).padRight(50);
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game)); // Implement the next level screen logic
            }
        });

        // Main menu button
        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        buttonTable.add(mainMenuButton).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).padLeft(50);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game)); // Return to the main menu
            }
        });

        mainTable.row().padTop(20); // Add some space below buttons if needed
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
    }
}
