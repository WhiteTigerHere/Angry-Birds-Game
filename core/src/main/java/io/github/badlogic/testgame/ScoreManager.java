package io.github.badlogic.testgame;

public class ScoreManager {
    private static ScoreManager instance;
    private int score;

    // Private constructor to prevent instantiation
    private ScoreManager() {
        score = 0;  // Initialize score to 0
    }

    // Get the singleton instance of ScoreManager
    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    // Add points to the current score
    public void addScore(int points) {
        score += points;
        //System.out.println("Score updated! Current score: " + score);
    }

    // Get the current score
    public int getScore() {
        return score;
    }
}

