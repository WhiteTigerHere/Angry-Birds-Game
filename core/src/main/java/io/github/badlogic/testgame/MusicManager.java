package io.github.badlogic.testgame;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Gdx;

public class MusicManager {
    private static MusicManager instance;
    private Music backgroundMusic;

    private MusicManager() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("angry_birds.mp3"));
        backgroundMusic.setLooping(true);
    }

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public void playMusic() {
        if (GameSettings.getInstance().isMusicEnabled() && !backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    public void stopMusic() {
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
    }

    public void updateMusicState() {
        if (GameSettings.getInstance().isMusicEnabled()) {
            playMusic();
        } else {
            stopMusic();
        }
    }

    public void dispose() {
        backgroundMusic.dispose();
    }
}
