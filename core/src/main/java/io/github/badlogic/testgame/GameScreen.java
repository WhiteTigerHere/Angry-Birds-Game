package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import static com.badlogic.gdx.Gdx.files;



public class GameScreen implements Screen {

    private final Game game;
    private SpriteBatch batch;
    private Texture birdTexture;  // Example bird image
    private Texture pigTexture;   // Example pig image
    private Sound birdSound;
    private Stage stage;
    private Skin skin;

    public GameScreen(Game game) {
        this.game = game;  // Use LibGDX's Game class to manage screens
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        birdTexture = new Texture(Gdx.files.internal("red.png")); // Example bird texture
        pigTexture = new Texture(Gdx.files.internal("pig1.png"));   // Example pig texture

        //birdSound = Gdx.audio.newSound(Gdx.files.internal("bird_sound.wav")); // Example sound
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // Draw elements
        batch.begin();
        batch.draw(birdTexture, 100, 100);  // Drawing the bird at (100, 100)
        batch.draw(pigTexture, 400, 100);   // Drawing the pig at (400, 100)
        batch.end();

        // Act and draw the stage (this renders UI)
        stage.act(delta);
        stage.draw(); // draw the stage (renders UI components like the button)

        // Example of playing a sound when the bird "flies"
        if (Gdx.input.justTouched()) {
            birdSound.play();
        }
    }

    @Override
    public void resize(int width, int height) {
        // Handle resize logic if needed
    }

    @Override
    public void pause() {
        // Handle game pause
    }

    @Override
    public void resume() {
        // Handle game resume
    }

    @Override
    public void hide() {
        // Clean up when screen is hidden
    }

    @Override
    public void dispose() {
        batch.dispose();
        birdTexture.dispose();
        pigTexture.dispose();
        birdSound.dispose();
    }
}
