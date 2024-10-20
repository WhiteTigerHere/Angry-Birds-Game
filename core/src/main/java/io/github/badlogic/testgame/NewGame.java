package io.github.badlogic.testgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
//kk
import static com.badlogic.gdx.Gdx.files;

public class NewGame implements Screen{
    private final Core game;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;
    private final String playerName;

    public NewGame(Core game, String playerName) {
        this.game = game;
        this.playerName = playerName;
    }

    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("bkg.png")); // Load background image

        // Load the skin for UI elements
        skin = new Skin(files.internal("uiskin.json"));

        // Create a stage and set it as the input processor
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // Create a table for layout
        Table mainTable = new Table();
        mainTable.setFillParent(true); // Make the table fill the parent (stage)
        stage.addActor(mainTable);

        // Add an invisible cell to push the button down
        mainTable.add().expandY().height(400); // Adjust the height value to control how far down the button will be
        mainTable.row(); // Move to the next row of the table

        // Add the new screen button
        TextButton NewGameButton = new TextButton("New Game", skin);
        mainTable.add(NewGameButton).width(200).height(80).padBottom(20).padLeft(50).expandX().left(); // Center the button horizontally

        // Add a click listener to the new screen button
        NewGameButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Transition to the game screen
                game.setScreen(new NewGame(game,"Player"));
            }
        });

        // Add the new screen button
        TextButton SavedGameButton = new TextButton("Saved Game", skin);
        mainTable.add(SavedGameButton).width(200).height(80).padBottom(20).padRight(50).expandX().right();// Center the button horizontally

        // Add a click listener to the new screen button
        SavedGameButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Transition to the game screen
                game.setScreen(new SavedGame(game));
            }
        });

        // Set the table position at the bottom
        mainTable.bottom().padBottom(50); // Position the buttons at the bottom with padding from the screen bottom
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
        // Handle screen resizing
        game.batch.getProjectionMatrix().setToOrtho2D(0,0,width,height);
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


