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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SavedGame implements Screen {
    private final Core game;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;
    private String playerName;
    private ImageButton[] themeButtons;
    private int selectedTheme = -1;
    private Label[] themeLabels;

    public SavedGame(Core game, String playerName) {
        this.game = game;
        this.playerName = playerName;
    }
//    public void loadGame(String filePath) {
//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
//            GameState gameState = (GameState) ois.readObject();
//
//            this.levelNumber = gameState.levelNumber;
//            this.theme = gameState.theme;
//            this.score = gameState.score;
//
//            birds.clear();
//            for (Bird.BirdData birdData : gameState.birds) {
//                birds.add(Bird.fromData(world, birdData));
//            }
//
//            pigs.clear();
//            for (Pig.PigData pigData : gameState.pigs) {
//                pigs.add(Pig.fromData(world, pigData));
//            }
//
//            blocks.clear();
//            for (Block.BlockData blockData : gameState.blocks) {
//                blocks.add(Block.fromData(world, blockData));
//            }
//
//            System.out.println("Game loaded successfully.");
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void show() {
        // load background image
        backgroundTexture = new Texture(Gdx.files.internal("commonbg.jpg"));

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
        mainTable.center();
        stage.addActor(mainTable);

        // set up label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // add title label
        Label titleLabel = new Label("Choose theme and then press Continue Game", labelStyle);
        mainTable.add(titleLabel).colspan(3).pad(20);
        mainTable.row();

        // set up theme buttons, labels
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
            buttonTable.add(themeButton).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.25f, mainTable)).center();
            buttonTable.row();

            Label themeLabel = new Label(themeNames[i], labelStyle);
            themeLabels[i] = themeLabel;
            buttonTable.add(themeLabel).padTop(10).center();

            mainTable.add(buttonTable).pad(10);
        }

        mainTable.row().padTop(20);

        // create button table
        Table buttonTable = new Table();
        mainTable.add(buttonTable).colspan(3).center().padTop(20);

        // add continue button
        TextButton continueButton = new TextButton("Continue", skin);
        buttonTable.add(continueButton).colspan(3).width(Value.percentWidth(0.25f, mainTable)).height(Value.percentWidth(0.10f, mainTable)).padRight(100);

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedTheme != -1) {
                    game.setScreen(new MainMenu(game));   // later code here for next screen to take input of theme
                }
            }
        });

        // add main menu button
        TextButton homeButton = new TextButton("Main Menu", skin);
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
        // clear screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // draw background
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // update and draw the stage (ui)
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
