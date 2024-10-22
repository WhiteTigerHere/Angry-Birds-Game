package io.github.badlogic.testgame;

public class GameSettings {
    private static GameSettings instance;
    private boolean musicEnabled = true;

    private GameSettings() {}

    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void setMusicEnabled(boolean enabled) {
        musicEnabled = enabled;
    }
}
