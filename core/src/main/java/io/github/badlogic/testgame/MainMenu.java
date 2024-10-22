package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.Color;
import static com.badlogic.gdx.Gdx.files;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import java.util.ArrayList;

public class MainMenu implements Screen {
    private final Core game;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;
    private TextField playerNameField;
    //private BitmapFont font;  // To store the scalable font
    private Label nameLabel;  // Reference for your label
    private FreeTypeFontGenerator fontGenerator;
    private TextButton musicButton;


    public MainMenu(Core game) {
        this.game = game;
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
        backgroundTexture = new Texture(files.internal("commonbg.jpg")); // Load background image

        // Load the skin for UI elements
        skin = new Skin(files.internal("uiskin.json"));

        // Generate a font based on screen size
        int fontSize = Math.max(20, Gdx.graphics.getWidth() / 40); // Adjust font size based on screen width
        BitmapFont font = generateFont(fontSize);

        // Apply the font to the skin
        skin.getFont("default-font").getData().setScale(fontSize / 20.0f); // Adjust font scale
        skin.add("custom-font", font, BitmapFont.class);

        // Create a stage and set it as the input processor
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // Create a table for layout
        Table mainTable = new Table();
        mainTable.setFillParent(true); // Make the table fill the parent (stage)
        stage.addActor(mainTable);


        // Set up label style with generated font
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor=Color.NAVY;

        // Label for the player's name input
        Label nameLabel = new Label("Enter Player Name:", labelStyle);
        //nameLabel.setColor(Color.BLACK);
        mainTable.add(nameLabel).colspan(3).padTop(50); // Add a label for the name input
        mainTable.row();  // Move to the next row for the text field

        // Add the player name TextField
        playerNameField = new TextField("", skin);  // TextField for player name input
        playerNameField.setMessageText("Player");  // Placeholder text
        // Set the alignment of the text (including placeholder) to center
        //playerNameField.setAlignment(Align.center);

        mainTable.add(playerNameField).colspan(3).width(300).height(50).padTop(30).padBottom(40).center();  // Set the size and padding for the TextField
        mainTable.row();  // Move to the next row for the buttons

        // Add an invisible cell to push the button down
        //mainTable.add().expandX().expandY(); // Adjust the height value to control how far down the button will be
        mainTable.row(); // Move to the next row of the table

        // Music button on top left
//        Table topLeftTable = new Table();
//        stage.addActor(topLeftTable);  // Directly add to stage instead of mainTable
//        topLeftTable.setFillParent(true);  // This ensures it takes the full screen space
//        topLeftTable.top().left().pad(50); // Align to top-left with padding
//
//        // Create the music button and ensure skin is applied
//        musicButton = new TextButton(GameSettings.getInstance().isMusicEnabled() ? "Music: On" : "Music: Off", skin);
//        musicButton.invalidateHierarchy();  // Force layout update right after creation
//        topLeftTable.add(musicButton).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable));
//
//        // Add listener for toggling music
//        musicButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                boolean newState = !GameSettings.getInstance().isMusicEnabled();
//                GameSettings.getInstance().setMusicEnabled(newState);
//                MusicManager.getInstance().updateMusicState();
//
//                // Update the button's label and invalidate layout to force redraw
//                musicButton.setText(newState ? "Music: On" : "Music: Off");
//                musicButton.invalidate();  // Force the button to refresh its layout
//            }
//        });

        // Create the music button with correct initial state
        musicButton = new TextButton(GameSettings.getInstance().isMusicEnabled() ? "Music: On" : "Music: Off", skin);

        // Set explicit size for the button
        musicButton.setSize(150, 50);  // Set the button size directly instead of percentWidth

        // Set the button position manually if needed (optional)
        musicButton.setPosition(50, Gdx.graphics.getHeight() - 100);  // Adjust position for top-left placement

        // Add listener to toggle music state when clicked
        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Toggle the music state
                boolean newState = !GameSettings.getInstance().isMusicEnabled();
                GameSettings.getInstance().setMusicEnabled(newState);
                MusicManager.getInstance().updateMusicState(); // Update music manager state

                // Update the button label
                musicButton.setText(newState ? "Music: On" : "Music: Off");
            }
        });

        // Add the button directly to the stage (bypassing any table layout issues)
        stage.addActor(musicButton);

        // Create a table for the buttons at the bottom
        Table buttonTable = new Table();
        mainTable.add(buttonTable).colspan(3).padTop(20);
        //mainTable.row().padTop(20);

        TextButton NewGameButton = new TextButton("New Game", skin);
        buttonTable.add(NewGameButton).colspan(3).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padRight(50);

        // Add a click listener to the new screen button
        NewGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
        buttonTable.add(SavedGameButton).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padLeft(50);        //buttonTable.row();

        // Add a click listener to the new screen button
        SavedGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to the game screen
                game.setScreen(new SavedGame(game, playerNameField.getName()));
            }
        });

        // Add the exit button
        TextButton ExitButton = new TextButton("Exit", skin);
        buttonTable.add(ExitButton).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padLeft(50);        //buttonTable.row();

        // Add a click listener to the exit button
        ExitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Close the application
                Gdx.app.exit();
            }
        });

        mainTable.row().padTop(20);
        // Add button table to the main table and center it at the bottom
        //mainTable.add(buttonTable).padBottom(50).center().expandX().bottom();

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


//        // Update the label's font
//        nameLabel.setStyle(new Label.LabelStyle(font, Color.BLACK));
//
//
//        // Resize the playerNameField width and height based on the screen dimensions
//        playerNameField.getStyle().font = font;  // Apply the resized font to the text field

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
