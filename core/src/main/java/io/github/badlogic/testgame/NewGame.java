package io.github.badlogic.testgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

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

    @Override
    public void show() {
        // load background texture
        backgroundTexture = new Texture(Gdx.files.internal("commonbg.jpg"));

        // load skin
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

        // add title label
        Label titleLabel = new Label("Choose theme and then press Start Game", titleLabelStyle);
        mainTable.add(titleLabel).colspan(3).pad(20);
        mainTable.row();

        // set up theme buttons and labels
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

            Label themeLabel = new Label(themeNames[i], themeLabelStyle);
            themeLabels[i] = themeLabel;
            buttonTable.add(themeLabel).padTop(10);

            mainTable.add(buttonTable).pad(10);
        }

        mainTable.row().padTop(20);

        // create button table
        Table buttonTable = new Table();
        mainTable.add(buttonTable).colspan(3).center().padTop(20);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.up = skin.newDrawable("default-round", Color.CYAN); // Button background (default state)
        textButtonStyle.over = skin.newDrawable("default-round", Color.CORAL); // Hover effect
        textButtonStyle.down = skin.newDrawable("default-round", Color.CORAL); // Clicked effect
        textButtonStyle.font = skin.getFont("default-font");

        // add start game button
        TextButton startButton = new TextButton("Start Game", textButtonStyle);
        buttonTable.add(startButton).colspan(3).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padRight(100);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedTheme != -1) {
                    //String levelfile= "level1"+(selectedTheme+1)+".tmx";
                    String levelfile= "level1"+(selectedTheme+1)+".tmx";

                    //String levelfile="level11.tmx";
                    game.setScreen(new GameScreen(game,levelfile));

                }
            }
        });

        // add main menu button
        TextButton homeButton = new TextButton("Main Menu", textButtonStyle);
        buttonTable.add(homeButton).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padLeft(100);

        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game)); // return to main menu
            }
        });

        mainTable.row().padTop(20);
    }

    // method to handle theme selection
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
        // clear the screen
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
