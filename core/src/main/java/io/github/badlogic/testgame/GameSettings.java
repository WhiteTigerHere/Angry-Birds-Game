package io.github.badlogic.testgame;

public class GameSettings {
    private static GameSettings instance;
    private boolean musicEnabled = true;

    private GameSettings() {// private constructor to prevent direct instantiation

    }

    public static GameSettings getInstance() {  // only one pattern to ensure only one instance of GameSettings
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    // getter for music status
    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    // setter for music status
    public void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
    }
}
