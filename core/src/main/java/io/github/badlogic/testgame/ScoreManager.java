package io.github.badlogic.testgame;

public class ScoreManager {
    private static ScoreManager instance;
    private int score;

    // Private constructor to prevent instantiation
    private ScoreManager() {
        score = 0;  // Initialize score to 0
    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    // Add points to the current score
    public void addScore(int points) {
        score += points;
    }

    // Get the current score
    public int getScore() {
        return score;
    }
}

