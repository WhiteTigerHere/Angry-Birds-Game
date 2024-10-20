package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.Color;
import static com.badlogic.gdx.Gdx.files;

public class MainMenu implements Screen {
    private final Core game;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;
    private TextField playerNameField;

    public MainMenu(Core game) {
        this.game = game;
    }




    @Override
    public void show() {
        backgroundTexture = new Texture(Gdx.files.internal("commonbg.jpg")); // Load background image

        // Load the skin for UI elements
        skin = new Skin(files.internal("uiskin.json"));

        // Create a stage and set it as the input processor
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // Create a table for layout
        Table mainTable = new Table();
        mainTable.setFillParent(true); // Make the table fill the parent (stage)
        stage.addActor(mainTable);

        // Label for the player's name input
        Label nameLabel = new Label("Enter PLayer Name:", skin);
        nameLabel.setColor(Color.BLUE);
        mainTable.add(nameLabel).padBottom(10).padTop(50).expandX().expandY().center(); // Add a label for the name input
        mainTable.row();  // Move to the next row for the text field

        // Add the player name TextField
        playerNameField = new TextField("", skin);  // TextField for player name input
        playerNameField.setMessageText("Player");  // Placeholder text
        mainTable.add(playerNameField).width(300).height(50).padBottom(20).expandX().expandY().center();  // Set the size and padding for the TextField
        mainTable.row();  // Move to the next row for the buttons


        // Add an invisible cell to push the button down
        mainTable.add().expandY(); // Adjust the height value to control how far down the button will be
        mainTable.row(); // Move to the next row of the table

        // Add the new screen button
        TextButton NewGameButton = new TextButton("New Game", skin);
        mainTable.add(NewGameButton).width(200).height(80).padBottom(20).padLeft(50).expandX().left(); // Center the button horizontally

        // Add a click listener to the new screen button
        NewGameButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                String playerName = playerNameField.getText(); // Retrieve the player name
                if (playerName.isEmpty()) {
                    playerName = "Player"; // Default name if no input is given
                }
                // Transition to the game screen with the player's name
                game.setScreen(new NewGame(game,playerName));
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
