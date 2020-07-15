package com.dots.view;

import com.dots.entity.Constants;

public class GameTimed implements GameMode{
    protected int timeLeft;
    private int scores;
    private static double startTime;

    public GameTimed() {
        timeLeft = Constants.OBJECT_TIMED;
        startTime = System.currentTimeMillis();
    }

    @Override
    public boolean getGameStatus(int score, int color, int moves) {
        scores += score;
        long now = System.currentTimeMillis();
        timeLeft = timeLeft -(int)((now - startTime) /1000.0);
        return timeLeft >0;
    }

    @Override
    public String message(int moves) {
        int time;
        if(timeLeft >= 0)
            time = timeLeft;
        else
            time = 0;
        return String.format("Time left: %d s    Scores: %s" , time, scores);
    }

    @Override
    public String endMessage() {
        return String.format("Your Score: %d", scores);
    }
}
