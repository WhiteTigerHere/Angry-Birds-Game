package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static com.badlogic.gdx.Gdx.files;

public class FirstScreen implements Screen {
    private final Core game;
    //private SpriteBatch batch;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;

    public FirstScreen(Core game) {
        this.game = game;
    }

    @Override
    public void show() {
        //batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("newscreen.jpg")); // Load background image

        // Load the skin for UI elements
        skin = new Skin(files.internal("uiskin.json"));

        // Create a stage and set it as the input processor
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
//kk
        // Create a table for layout
        Table mainTable = new Table();
        mainTable.setFillParent(true); // Make the table fill the parent (stage)
        stage.addActor(mainTable);

        // Add an invisible cell to push the button down
        mainTable.add().expandY().height(400); // Adjust the height value to control how far down the button will be
        mainTable.row(); // Move to the next row of the table

        // Add the Play button
        TextButton playButton = new TextButton("Play", skin);
        mainTable.add(playButton).width(200).height(80).padBottom(150).center(); // Center the button horizontally

//        // Add a click listener to the play button
//        playButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
//            @Override
//            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
//                // Transition to the game screen
//                game.setScreen(new GameScreen(game));
//            }
//        });
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to the MenuScreen
                game.setScreen(new MainMenu(game));
            }
        });
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
