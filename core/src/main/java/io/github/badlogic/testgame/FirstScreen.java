package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class FirstScreen implements Screen {
    private final Core game;
    private Texture backgroundTexture;
    private Stage stage;
    private Skin skin;

    public FirstScreen(Core game) {
        this.game = game;
    }

    @Override
    public void show() {
        // load background image
        backgroundTexture = new Texture(Gdx.files.internal("newscreen.jpg"));
        skin=game.skin;
        MusicManager.getInstance().playMusic();

        // create a stage and set it as the input processor
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // create a table for layout
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // add an invisible cell to push the button down
        mainTable.add().expandY().height(400);
        mainTable.row();

        // add the play button
//        TextButton playButton = new TextButton("Play", skin);
//        mainTable.add(playButton).width(200).height(80).padBottom(150).center();
//
//        // add click listener to play button
//        playButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                // transition to the menuscreen
//                game.setScreen(new MainMenu(game));
//            }
//        });

        // Load your texture for the image button (make sure the path is correct)
        Texture buttonTexture = new Texture("playbutton.png");
        TextureRegion buttonRegion = new TextureRegion(buttonTexture);
        Drawable buttonDrawable = new TextureRegionDrawable(buttonRegion);

        // Create the ImageButton using the drawable
        ImageButton playButton = new ImageButton(buttonDrawable);
        // Customize the button to fill the screen proportionally
        mainTable.add(playButton).width(Gdx.graphics.getWidth() * 0.95f) // Set the width to 50% of screen width
            .height(Gdx.graphics.getHeight() * 0.95f) // Set the height to 10% of screen height
            .padBottom(40).center();  // Add padding and center it

        // Add click listener to the button
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to the main menu screen
                game.setScreen(new MainMenu(game));
            }
        });
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
        // handle screen resizing
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
        // dispose of resources
        game.batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
        skin.dispose();
    }
}
