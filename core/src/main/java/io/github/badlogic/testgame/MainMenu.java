package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.graphics.Color;
import static com.badlogic.gdx.Gdx.files;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen {
    private final Core game;
    private GameScreen gameScreen;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;
    private TextField playerNameField;
    private Label nameLabel;
    private FreeTypeFontGenerator fontGenerator;
    private TextButton musicButton;

    public MainMenu(Core game) {
        this.game = game;

    }

    @Override
    public void show() {
        // load background image
        backgroundTexture = new Texture(files.internal("commonbg.jpg"));

        // load the skin
        skin = game.skin;

        // font based on screen size
        int fontSize = Math.max(20, Gdx.graphics.getWidth() / 40);
        BitmapFont font = game.generateFont(fontSize);

        // apply font to skin
        skin.getFont("default-font").getData().setScale(fontSize / 20.0f);
        skin.add("custom-font", font, BitmapFont.class);

        // create a stage and set it as the input processor
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.up = skin.newDrawable("default-round", Color.CYAN); // Button background (default state)
        textButtonStyle.over = skin.newDrawable("default-round", Color.CORAL); // Hover effect
        textButtonStyle.down = skin.newDrawable("default-round", Color.CORAL); // Clicked effect
        textButtonStyle.font = skin.getFont("default-font");

        // create a table for layout
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // set up label style with generated font
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.NAVY;

        // create music toggle button
        musicButton = new TextButton(GameSettings.getInstance().isMusicEnabled() ? "Music: On" : "Music: Off", textButtonStyle);
        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean newState = !GameSettings.getInstance().isMusicEnabled();
                GameSettings.getInstance().setMusicEnabled(newState);
                MusicManager.getInstance().updateMusicState();
                musicButton.setText(newState ? "Music: On" : "Music: Off");
            }
        });

        // add music button to the top-left corner
        mainTable.add(musicButton).top().left().padTop(10).padLeft(10).expandX();
        mainTable.row();

        // add label for player name input
        Label nameLabel = new Label("Enter Player Name:", labelStyle);
        mainTable.add(nameLabel).padTop(50);
        mainTable.row();

        // add player name text field
        playerNameField = new TextField("", skin);
        playerNameField.setMessageText("Player");
        mainTable.add(playerNameField).width(300).height(50).padTop(30).padBottom(40);
        mainTable.row();

        // create table for buttons at the bottom
        Table buttonTable = new Table();
        buttonTable.top().center().padTop(20);

        //add new game button
        TextButton NewGameButton = new TextButton("New Game", textButtonStyle);
        buttonTable.add(NewGameButton).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padRight(20);
        NewGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String playerName = playerNameField.getText().isEmpty() ? "Player" : playerNameField.getText();
                game.setScreen(new NewGame(game, playerName));
            }
        });

        // add saved game button
        TextButton SavedGameButton = new TextButton("Saved Game", textButtonStyle);
        buttonTable.add(SavedGameButton).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padRight(20);
        SavedGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // First, create a new GameScreen instance (initializing gameScreen properly)
               // gameScreen = new GameScreen(game, null);  // Initialize with null or use a default level for fallback
// MainMenu SavedGameButton listener
                gameScreen = new GameScreen(game, "level11.tmx"); // Use default path if needed
                // Load the game state from the save file
                String saveFilePath = "savegame.json"; // Path to your save file
                GameState gameState = gameScreen.loadGame(saveFilePath);

                if (gameState != null && gameState.levelFileName != null) {
                    // If game state is valid and level file name is available, create GameScreen with the levelFileName
                    gameScreen = new GameScreen(game, gameState.levelFileName);
                    gameScreen.loadSavedGame();  // Load the saved game state
                } else {
                    Gdx.app.error("MainMenu", "Failed to load saved game or level file is missing.");
                }
            }
        });


        // add exit button
        TextButton ExitButton = new TextButton("Exit", textButtonStyle);
        buttonTable.add(ExitButton).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable));
        ExitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        mainTable.add(buttonTable).expandY().bottom().padBottom(20);

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
        // handle screen resizing
        game.batch.getProjectionMatrix().setToOrtho2D(0,0,width,height);
        stage.getViewport().update(width, height, true);
        playerNameField.setWidth(width * 0.4f);
        playerNameField.setHeight(height * 0.08f);
        stage.getViewport().update(width, height, true);

        // update viewport
        stage.getViewport().update(width, height, true);

        // adjust player name field size
        float fieldWidth = Math.min(width * 0.4f, 300);
        float fieldHeight = Math.min(height * 0.08f, 50);
        playerNameField.setSize(fieldWidth, fieldHeight);

        Table mainTable = (Table) stage.getRoot().getChildren().get(0);
        mainTable.invalidate();
        mainTable.setFillParent(true);
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
    }
}
