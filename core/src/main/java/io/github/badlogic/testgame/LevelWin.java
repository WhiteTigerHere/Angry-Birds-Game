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
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class LevelWin implements Screen {
    private final Core game;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;
    private final String playerName;
    private final int levelScore; // to store score of the level
    private int levelnum;
    private int levelthemenum;

    public LevelWin(Core game, String playerName, int levelScore, int levelnum, int levelthemenum) {
        this.game = game;
        this.playerName = playerName;
        this.levelScore = levelScore; //to initialize score from memory
        this.levelnum = levelnum;
        this.levelthemenum = levelthemenum;
    }

    @Override
    public void show() {
        // load background image
        backgroundTexture = new Texture(Gdx.files.internal("winbg.png"));

        skin=game.skin;

        // generate font based on screen size
        int fontSize = Math.max(20, Gdx.graphics.getWidth() / 100);
        BitmapFont font = game.generateFont(fontSize);
        skin.getFont("default-font").getData().setScale(fontSize / 20.0f);
        skin.add("custom-font", font, BitmapFont.class);

        // set up stage and input processor
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // create main table
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // set up label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.OLIVE;

        // add congrats label
        Label congratsLabel = new Label("Congratulations!", labelStyle);
        mainTable.add(congratsLabel).padBottom(20);
        mainTable.row();

        // add score label
        Label scoreLabel = new Label("Your Score: " + levelScore, labelStyle);
        mainTable.add(scoreLabel).padBottom(20);
        mainTable.row();

        // create button table
        Table buttonTable = new Table();
        mainTable.add(buttonTable).padTop(20);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.up = skin.newDrawable("default-round", Color.CYAN); // Button background (default state)
        textButtonStyle.over = skin.newDrawable("default-round", Color.CORAL); // Hover effect
        textButtonStyle.down = skin.newDrawable("default-round", Color.CORAL); // Clicked effect
        textButtonStyle.font = skin.getFont("default-font");

        // add next level button
        TextButton nextLevelButton = new TextButton("Next Level", textButtonStyle);
        buttonTable.add(nextLevelButton).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).padRight(50);
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //game.setScreen(new MainMenu(game));
                if(levelnum==3){
                    game.setScreen(new CompletedGame(game));
                }
                else if (levelthemenum != -1) {
                    //String levelfile= "level1"+(selectedTheme+1)+".tmx";
                    String levelfile= "level"+(levelnum+1)+(levelthemenum)+".tmx";

                    //String levelfile="level11.tmx";
                    game.setScreen(new GameScreen(game,levelfile));

                }
            }
        });

        // add main menu button
        TextButton mainMenuButton = new TextButton("Main Menu", textButtonStyle);
        buttonTable.add(mainMenuButton).width(Value.percentWidth(0.15f, mainTable)).height(Value.percentWidth(0.07f, mainTable)).padLeft(50);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game)); // returns to mainmenu
            }
        });

        mainTable.row().padTop(20);
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
    }
}
