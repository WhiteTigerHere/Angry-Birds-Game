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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.Color;
import static com.badlogic.gdx.Gdx.files;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import java.util.ArrayList;


public class MainMenu implements Screen {
    private final Core game;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;
    private TextField playerNameField;
    private BitmapFont font;  // To store the scalable font
    private Label nameLabel;  // Reference for your label
    private FreeTypeFontGenerator fontGenerator;


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

        // Create a FreeTypeFontGenerator to load a custom font
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ARIAL.TTF")); // Provide the path to your TTF font file

        // Generate a font with a default size
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 20;  // Initial size, will be adjusted later
        font = fontGenerator.generateFont(fontParameter);

        // Set up label style with generated font
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // Create a table for layout
        Table mainTable = new Table();
        mainTable.setFillParent(true); // Make the table fill the parent (stage)
        stage.addActor(mainTable);

        // Label for the player's name input
        nameLabel = new Label("Enter Player Name:", labelStyle);
        nameLabel.setColor(Color.BLACK);
        mainTable.add(nameLabel).padBottom(10).padTop(100).center(); // Add a label for the name input
        mainTable.row();  // Move to the next row for the text field

        // Add the player name TextField
        playerNameField = new TextField("", skin);  // TextField for player name input
        playerNameField.setMessageText("Player");  // Placeholder text
        // Set the alignment of the text (including placeholder) to center
        //playerNameField.setAlignment(Align.center);

        mainTable.add(playerNameField).width(300).height(50).padBottom(40).center();  // Set the size and padding for the TextField
        mainTable.row();  // Move to the next row for the buttons


        // Add an invisible cell to push the button down
        mainTable.add().expandY(); // Adjust the height value to control how far down the button will be
        mainTable.row(); // Move to the next row of the table

        // Create a table for the buttons at the bottom
        Table buttonTable = new Table();

        TextButton NewGameButton = new TextButton("New Game", skin);
        buttonTable.add(NewGameButton).expand().fill().width(200).height(80).padRight(25); // Center the button horizontally
        //buttonTable.row();

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
        buttonTable.add(SavedGameButton).expand().fill().width(200).height(80).padRight(25).padLeft(25);// Center the button horizontally
        //buttonTable.row();

        // Add a click listener to the new screen button
        SavedGameButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Transition to the game screen
                game.setScreen(new SavedGame(game, playerNameField.getName()));
            }
        });

        // Add the exit button
        TextButton ExitButton = new TextButton("Exit", skin);
        buttonTable.add(ExitButton).expand().fill().width(200).height(80).padLeft(25); // Add padding and size for the exit button
        //buttonTable.row();

        // Add a click listener to the exit button
        ExitButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Close the application
                Gdx.app.exit();
            }
        });


        // Add button table to the main table and center it at the bottom
        mainTable.add(buttonTable).padBottom(50).center().expandX().bottom();

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

        // Dynamically adjust the font size based on the window width
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = Math.max(20, width / 30); // Adjust the font size based on the window width
        font.dispose();  // Dispose the old font
        font = fontGenerator.generateFont(fontParameter); // Generate a new font with the adjusted size

        // Update the label's font
        nameLabel.setStyle(new Label.LabelStyle(font, Color.BLACK));


        // Resize the playerNameField width and height based on the screen dimensions
        playerNameField.getStyle().font = font;  // Apply the resized font to the text field

        // Resize the playerNameField width and height based on the screen dimensions
        playerNameField.setWidth(width * 0.4f);  // Set the width to 40% of the screen width
        playerNameField.setHeight(height * 0.08f); // Set the height to 8% of the screen height

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
