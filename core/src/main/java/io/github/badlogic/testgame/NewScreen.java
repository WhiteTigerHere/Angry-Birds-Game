package io.github.badlogic.testgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Game;

public class NewScreen implements Screen {
    private final Game game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private Rectangle playButton;
    private Music backgroundMusic;
    private Sound clickSound;

    public NewScreen(Game game) {
        this.game = game;  // Use LibGDX's built-in Game class
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("newscreen.jpg")); // Load background image
        //font = new BitmapFont(Gdx.files.internal("font.fnt")); // Load your font
        shapeRenderer = new ShapeRenderer();

        // Create rectangles for buttons
        playButton = new Rectangle(200, 50, 200, 60); // Start Button
        //exitButton = new Rectangle(300, 300, 200, 60);  // Exit Button

        // Load music and sound effects
        //backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("menu_music.m4a"));
        //backgroundMusic.setLooping(true);
        //backgroundMusic.play();

        //clickSound = Gdx.audio.newSound(Gdx.files.internal("click.m4a"));
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // Draw elements
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Draw background
        //font.draw(batch, "Start Game", startButton.x + 20, startButton.y + 40);
        //font.draw(batch, "Exit", exitButton.x + 20, exitButton.y + 40);
        batch.end();

        // Optional: draw button shapes (just for visualization, can be removed)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(playButton.x, playButton.y, playButton.width, playButton.height);
        //shapeRenderer.rect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);
        shapeRenderer.end();

        // Handle input for button clicks
        if (Gdx.input.isTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y; // Adjust to LibGDX coordinate system

            if (playButton.contains(touchPos)) {
                //clickSound.play();
                // Transition to game screen (replace with your actual GameScreen)
                game.setScreen(new GameScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Handle resizing
        batch.getProjectionMatrix().setToOrtho2D(0,0,width,height);
    }

    @Override
    public void pause() {
        // Handle pause
    }

    @Override
    public void resume() {
        // Handle resume
    }

    @Override
    public void hide() {
        // Handle screen hide
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        font.dispose();
        shapeRenderer.dispose();
        backgroundMusic.dispose();
        clickSound.dispose();
    }
}
