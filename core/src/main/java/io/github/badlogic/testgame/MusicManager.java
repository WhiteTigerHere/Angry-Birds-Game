package io.github.badlogic.testgame;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Gdx;

public class MusicManager {
    private static MusicManager instance;
    private Music backgroundMusic;

    // private constructor to prevent direct instantiation
    private MusicManager() {
        // load background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("angry_birds.mp3"));
        backgroundMusic.setLooping(true);
    }

    // only one pattern to ensure only one instance
    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    // start playing music if enabled
    public void playMusic() {
        if (GameSettings.getInstance().isMusicEnabled() && !backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    // stop music if currently playing
    public void stopMusic() {
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
    }

    // update music state
    public void updateMusicState() {
        if (GameSettings.getInstance().isMusicEnabled()) {
            playMusic();
        } else {
            stopMusic();
        }
    }

    // disposal
    public void dispose() {
        backgroundMusic.dispose();
    }
}
