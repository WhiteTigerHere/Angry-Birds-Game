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

public class CompletedGame implements Screen {
    private final Core game;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;
    private ImageButton[] themeButtons;
    private int selectedTheme = -1;
    private Label[] themeLabels;

    public CompletedGame(Core game) {
        this.game = game;
        //this.playerName = playerName;
    }

    @Override
    public void show() {
        // load background image
        backgroundTexture = new Texture(Gdx.files.internal("gamecomplete.png"));

        skin=game.skin;

        // generate a font based on screen size
        int fontSize = Math.max(20, Gdx.graphics.getWidth() / 40);
        BitmapFont font = game.generateFont(fontSize);

        // apply the font to the skin
        skin.getFont("default-font").getData().setScale(fontSize / 20.0f);
        skin.add("custom-font", font, BitmapFont.class);

        // set up the stage with a viewport
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // create main table
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // create home button
        TextButton homeButton = new TextButton("Main Menu", skin);
        mainTable.add(homeButton).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).expandX().bottom().padBottom(120).padRight(70);

        mainTable.setFillParent(true);
        mainTable.bottom();

        // add listener to home button
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
        // update projection matrix and viewport
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
