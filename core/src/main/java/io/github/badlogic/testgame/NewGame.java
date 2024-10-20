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


//    @Override
//    public void show() {
//        backgroundTexture = new Texture(Gdx.files.internal("commonbg.jpg")); // Load background image
//
//        skin = new Skin(files.internal("uiskin.json"));
//
//        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
//        Gdx.input.setInputProcessor(stage);
//
//        // Create a table for layout
//        Table mainTable = new Table();
//        mainTable.setFillParent(true); // Make the table fill the parent (stage)
//        stage.addActor(mainTable);
//
//        Label titleLabel = new Label("Choose theme and then press Start Game", skin);
//        mainTable.add(titleLabel).colspan(3).pad(20);
//        mainTable.row();
//
//        themeButtons = new ImageButton[3];
//        themeLabels = new Label[3];
//        String[] themeNames = {"Classic", "Beach", "Halloween"};
//
//        for (int i = 0; i < 3; i++) {
//            final int index = i;
//            String imagePath = "theme" + (i + 1) + ".jpg";
//            Texture themeTexture = new Texture(Gdx.files.internal(imagePath));
//            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
//            style.up = new TextureRegionDrawable(new TextureRegion(themeTexture));
//            style.checked = new TextureRegionDrawable(new TextureRegion(themeTexture));
//            ImageButton themeButton = new ImageButton(style);
//            themeButtons[i] = themeButton;
//
//            themeButton.addListener(new ClickListener() {
//                @Override
//                public void clicked(InputEvent event, float x, float y) {
//                    selectTheme(index);
//                }
//            });
//
//            // Add button and label dynamically sized relative to screen
//            Table buttonTable = new Table();
//            buttonTable.add(themeButton).width(Value.percentWidth(0.25f, mainTable))  // 25% of parent width
//                .height(Value.percentWidth(0.25f, mainTable)); // Adjust height similarly
//            buttonTable.row();
//
//            Label themeLabel = new Label(themeNames[i], skin);
//            themeLabels[i] = themeLabel;
//            buttonTable.add(themeLabel).padTop(10);
//
//            mainTable.add(buttonTable).pad(10);
//        }
//
//        mainTable.row();
//
//        // Add the Start Game button
//        TextButton startButton = new TextButton("Start Game", skin);
//        mainTable.add(startButton).colspan(3).width(Value.percentWidth(0.25f, mainTable))  // 25% of parent width
//            .height(Value.percentWidth(0.10f, mainTable))  // Adjust height relative to parent
//            .pad(20);
//
//        startButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                if (selectedTheme != -1) {
//                    System.out.println("Starting game with theme: " + selectedTheme);
//                    game.setScreen(new MainMenu(game)); // Start MainMenu or any screen based on logic
//                }
//            }
//        });
//    }
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

    Label.LabelStyle labelStyle = new Label.LabelStyle();
    labelStyle.font = font;
    //labelStyle.fontColor = Color.BLACK;

    Label titleLabel = new Label("Choose theme and then press Start Game", labelStyle); // Use custom font
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

        Label themeLabel = new Label(themeNames[i], labelStyle); // Use the custom label style with larger font
        themeLabels[i] = themeLabel;
        buttonTable.add(themeLabel).padTop(10);

        mainTable.add(buttonTable).pad(10);
    }

    mainTable.row();

    TextButton startButton = new TextButton("Start Game", skin);
    mainTable.add(startButton).colspan(3).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).pad(20);

    startButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (selectedTheme != -1) {
                game.setScreen(new MainMenu(game));   // for next screen take input of themeeeeeeeeeee
            }
        }
    });
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
