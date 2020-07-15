package com.dots.view;

public class GameEndless implements GameMode{
    private int scores;

    public GameEndless() {
        scores = 0;
    }

    @Override
    public boolean getGameStatus(int score, int color, int moves) {
        scores += score;
        return true;
    }

    @Override
    public String message(int moves) {
        return String.format("Move: %d     Scores: %s" , moves, scores);
    }

    @Override
    public String endMessage() {
        return String.format("Your Score: %d", scores);
    }

}
