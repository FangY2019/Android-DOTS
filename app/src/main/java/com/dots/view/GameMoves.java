package com.dots.view;

import com.dots.entity.Constants;

public class GameMoves implements GameMode{
    private int Maxmove;
    private int scores;

    public GameMoves() {
        Maxmove = Constants.OBJECT_MOVE;
        scores = 0;
    }

    @Override
    public boolean getGameStatus(int score, int colour, int moves) {
        scores += score;
        return Maxmove - moves >0;
    }


    @Override
    public String message(int moves) {
        return String.format("Moves: %d     Scores: %s" , Maxmove-moves, scores);
    }


    @Override
    public String endMessage() {
        return String.format("SCORES: %d", scores);
    }
}
