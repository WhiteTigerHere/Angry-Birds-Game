package io.github.badlogic.testgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class LostLevel implements Screen {
    private final Core game;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;
    private ImageButton[] themeButtons;
    private int selectedTheme = -1;
    private Label[] themeLabels;
    private String levelfilename;

    public LostLevel(Core game,String levelfilename) {
        this.levelfilename=levelfilename;
        this.game = game;
    }

    @Override
    public void show() {
        // load background image
        backgroundTexture = new Texture(Gdx.files.internal("levellost.png"));

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

        // create button table
        Table buttonTable = new Table();
        mainTable.add(buttonTable).colspan(3).center().padTop(20);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.up = skin.newDrawable("default-round", Color.CYAN); // Button background (default state)
        textButtonStyle.over = skin.newDrawable("default-round", Color.CORAL); // Hover effect
        textButtonStyle.down = skin.newDrawable("default-round", Color.CORAL); // Clicked effect
        textButtonStyle.font = skin.getFont("default-font");

        // add restart level button
        TextButton restartButton = new TextButton("Restart Level", textButtonStyle);
        buttonTable.add(restartButton).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).padLeft(30).padTop(20);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game,levelfilename)); // Replace with the appropriate screen class for the level
                //dispose();
            }
        });

//        musicButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                // will code later
//            }
//        });

        mainTable.row().padTop(20);

        // add main menu button
        TextButton mainmenuButton = new TextButton("Main Menu", textButtonStyle);
        mainTable.add(mainmenuButton).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).padLeft(30).padTop(60);

        mainmenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game)); // returns to main menu
            }
        });

        mainTable.row().padTop(20);
    }

    // to handle theme selection
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

        // draw the background
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // update and draw the stage (ui)
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // update projec matrix and viewport
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
        // dispose of resources
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
