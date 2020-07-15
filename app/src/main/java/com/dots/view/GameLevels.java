package com.dots.view;

import android.graphics.Color;

import com.dots.entity.Constants;

public class GameLevels implements GameMode{
    private int maxMove;
    private final int color1;
    private final int color2;
    private int scores1;
    private int scores2;
    protected int timeLeft;
    private static double startTime;

    public GameLevels() {
        maxMove = Constants.OBJECT_LEVEL_MOVE;
        timeLeft = Constants.OBJECT_LEVEL_TIMED;
        startTime = System.currentTimeMillis();
        color1 = Color.rgb(255, 26, 26);//red
        color2 = Color.rgb(0, 0, 255);//blue
        scores1 = 0;
        scores2 = 0;
    }

    @Override
    public boolean getGameStatus(int score, int color, int moves) {
        if (color == color1) {
            scores1 += score;
        } else if (color == color2) {
            scores2 += score;
        }
        long now = System.currentTimeMillis();
        timeLeft = timeLeft -(int)((now - startTime) /1000.0);
        return timeLeft > 0 && (scores1 < Constants.LEVEL1_SCORE1 || scores2 < Constants.LEVEL1_SCORE2);
    }

    @Override
    public String message(int moves) {
        int time;
        if(timeLeft >= 0)
            time = timeLeft;
        else
            time = 0;
        String str1 = String.format("      Time:%d s \n", time);
        String str2 = String.format("    Red      Blue\n");
        String str3 = String.format("    %d/%d      %d/%d", scores1, Constants.LEVEL1_SCORE1, scores2, Constants.LEVEL1_SCORE2);
        return str1 + str2 + str3;
    }

    @Override
    public String endMessage() {
        return String.format("Your Score: %d", scores1 + scores2);
    }
}
