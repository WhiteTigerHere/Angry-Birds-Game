package io.github.badlogic.testgame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import static com.badlogic.gdx.Gdx.files;

public class NewGame implements Screen {
    private final Core game;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;
    private final String playerName;
    private ImageButton[] themeButtons;
    private int selectedTheme = -1;
    private Label[] themeLabels;

    public NewGame(Core game, String playerName) {
        this.game = game;
        this.playerName = playerName;
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
    backgroundTexture = new Texture(Gdx.files.internal("commonbg.jpg")); // Load background image

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

//    Label.LabelStyle labelStyle = new Label.LabelStyle();
//    labelStyle.font = font;
    //labelStyle.fontColor = Color.BLACK;

    // Set up label style for the title
    Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
    titleLabelStyle.font = font;
    titleLabelStyle.fontColor = Color.BLUE; // Set font color of title label to blue

    // Set up label style for theme names (keeping them white)
    Label.LabelStyle themeLabelStyle = new Label.LabelStyle();
    themeLabelStyle.font = font;
    themeLabelStyle.fontColor = Color.WHITE; // Keep theme names white

    Label titleLabel = new Label("Choose theme and then press Start Game", titleLabelStyle); // Use custom font
    mainTable.add(titleLabel).colspan(3).pad(20);
    mainTable.row();

    themeButtons = new ImageButton[3];
    themeLabels = new Label[3];
    String[] themeNames = {"Classic", "Beach", "Halloween"};

    for (int i = 0; i < 3; i++) {
        final int index = i;
        String imagePath = "theme" + (i + 1) + ".jpg";
        Texture themeTexture = new Texture(Gdx.files.internal(imagePath));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(themeTexture));
        style.checked = new TextureRegionDrawable(new TextureRegion(themeTexture));
        ImageButton themeButton = new ImageButton(style);
        themeButtons[i] = themeButton;

        themeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectTheme(index);
            }
        });

        Table buttonTable = new Table();
        buttonTable.add(themeButton).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.25f, mainTable));
        buttonTable.row();

        Label themeLabel = new Label(themeNames[i], themeLabelStyle); // Use the custom label style with larger font
        themeLabels[i] = themeLabel;
        buttonTable.add(themeLabel).padTop(10);

        mainTable.add(buttonTable).pad(10);
    }

    mainTable.row().padTop(20);

    // Add a new row for buttons and proper alignment
    Table buttonTable = new Table();
    mainTable.add(buttonTable).colspan(3).center().padTop(20);

    TextButton startButton = new TextButton("Start Game", skin);
    buttonTable.add(startButton).colspan(3).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padRight(100);

    startButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (selectedTheme != -1) {
                game.setScreen(new MainMenu(game));   // for next screen take input of themeeeeeeeeeee
            }
        }
    });

    // Home button
    TextButton homeButton = new TextButton("Home Page", skin);
    buttonTable.add(homeButton).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padLeft(100);

    homeButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            game.setScreen(new MainMenu(game)); // Return to main menu
        }
    });

    mainTable.row().padTop(20); // Add space before any future content (if needed)
}
    private void selectTheme(int index) {
        selectedTheme = index;
        for (int i = 0; i < themeButtons.length; i++) {
            themeButtons[i].setChecked(i == index);
            if (i == index) {
                themeLabels[i].setColor(Color.YELLOW);
            } else {
                themeLabels[i].setColor(Color.WHITE);
            }
        }
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
