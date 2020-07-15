package com.dots.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.dots.entity.PointT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BoardView extends View{
    //dots
    private final int ORIGINAL_POING = 100;
    private final int DOTS_COUNT = 6;
    private final int DOTS_SIZE = 80;
    private final int DOTS_SPACE = 80;


    private ArrayList<ArrayList<Integer>> board;
    private GameMode gameMode;
    private int moves;
    private boolean gameStatus;
    private int scores;
    private boolean removable;

    private int[][] clickableAreas;
    private Paint mPaint;

    //the orientation of remove dots
    private boolean mHorizontal;
    private boolean mVertical;

    //variables for the indicators
    private int startRow;
    private int startColum;
    private int endRow;
    private int endColum;
    private boolean lineVisible;
    private boolean firstDotVisible;

    public BoardView(Context context) {
        super(context);
        init(null);
    }

    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

//    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(attrs);
//    }

    private void init(@Nullable AttributeSet set) {
        clickableAreas = new int[DOTS_COUNT * DOTS_COUNT][2];
        mPaint = new Paint();
        board = new ArrayList<ArrayList<Integer>>();
        moves = 0;
        gameStatus = true;
        scores = 0;
        for (int i = 0; i < DOTS_COUNT; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int j = 0; j < DOTS_COUNT; j++) {
                list.add(j, getRandomColor());
            }
            board.add(list);
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        if(firstDotVisible){
            drawFirstDotIndicator(canvas);
        }
        if(lineVisible){
            drawLineIndicator(canvas);
            drawDotIndicator(canvas);
        }
        for(int i = 0; i < DOTS_COUNT; i++){
            for(int j = 0; j < DOTS_COUNT; j++){
                float cx = ORIGINAL_POING + DOTS_SIZE/2 + (DOTS_SIZE + DOTS_SPACE) * j;
                float cy = ORIGINAL_POING + DOTS_SIZE/2 + (DOTS_SIZE + DOTS_SPACE) * i;
                mPaint.setColor(getColor(i, j));
                canvas.drawCircle(cx, cy, DOTS_SIZE/2, mPaint);
                clickableAreas[i * DOTS_COUNT + j][0] = (int) cx;
                clickableAreas[i * DOTS_COUNT + j][1] = (int) cy;
            }
        }
    }

    public boolean getGameStatus(){
        return this.gameStatus;
    }

    public void setGameMode(GameMode gameMode){
        this.gameMode = gameMode;
    }

    public boolean remove(PointT startP, PointT endP) {
        if (inSideOfView(startP) && inSideOfView(endP)) {
            if(validateConnective(startP, endP)){
                int color = markColor(startP, endP);
                int removeSize = 0;
                for(int i = 0; i < DOTS_COUNT; i++){
                    for(int j = 0; j < DOTS_COUNT; j++){
                        if(board.get(i).get(j) == Color.GRAY){
                            removeSize++;
                        }
                    }
                    board.get(i).removeAll(Arrays.asList(Color.GRAY));
                }
                moves++;
                this.gameStatus = gameMode.getGameStatus(removeSize, color, moves);
                scores += removeSize;
                return true;
            }
        }
//        return removable;
        return false;
//        return true;
    }

    public void updateView() {
        lineVisible = false;
        firstDotVisible = false;
        for(int i = 0; i < DOTS_COUNT; i++){
            for(int j = board.get(i).size(); j < DOTS_COUNT; j++){
                board.get(i).add(j, getRandomColor());
            }
        }
        invalidate();
    }

    //draw lines and dots when the dots are valid to remove
    public void drawIndicator(PointT startP, PointT endP){
        startRow = getIndex(startP.getY());
        startColum = getIndex(startP.getX());
        endRow = getIndex(endP.getY());
        endColum = getIndex(endP.getX());
        if(startRow == endRow && startColum == endColum){
            firstDotVisible = true;
            invalidate();
        }else{
            firstDotVisible = false;
        }
        if(validateConnective(startP, endP)) {
            lineVisible = true;
            invalidate();
        }else{
            lineVisible = false;
        }
    }

    public void setIndicatorVisible() {
        this.lineVisible = false;
        this.firstDotVisible = false;
    }

    //transfer the coordinate to the index of row or column
    private int getIndex(int number) {
        if (number >= ORIGINAL_POING && number < ORIGINAL_POING + DOTS_SIZE + DOTS_SPACE) {
            return 0;
        } else if (number >= ORIGINAL_POING + DOTS_SIZE + DOTS_SPACE && number < ORIGINAL_POING + (DOTS_SIZE + DOTS_SPACE) * 2) {
            return 1;
        } else if (number >= ORIGINAL_POING + (DOTS_SIZE + DOTS_SPACE) * 2 && number < ORIGINAL_POING + (DOTS_SIZE + DOTS_SPACE) * 3) {
            return 2;
        } else if (number >= ORIGINAL_POING + (DOTS_SIZE + DOTS_SPACE) * 3 && number < ORIGINAL_POING + (DOTS_SIZE + DOTS_SPACE) * 4) {
            return 3;
        } else if (number >= ORIGINAL_POING + (DOTS_SIZE + DOTS_SPACE) * 4 && number < ORIGINAL_POING + (DOTS_SIZE + DOTS_SPACE) * 5) {
            return 4;
        } else return 5;
    }

    //determines the touch dots are valid to remove when they are in the sam row or column and their colors are the same
    private boolean validateConnective(PointT startP, PointT endP) {
        int r, c;
        c = getIndex(startP.getX());
        r = getIndex(startP.getY());
        if (isSameLine(startP, endP)){
            if (mVertical) {
                int colum = getIndex(startP.getX());
                int row1 = Math.min(getIndex(startP.getY()), getIndex(endP.getY()));
                int row2 = Math.max(getIndex(startP.getY()), getIndex(endP.getY()));
                for (int row = row1; row <= row2; row++) {
                    if (getColor(row, colum) != getColor(r, c)) {
                        return false;
                    }
                }
            }
            //if the dots are in the horizontal direction
            else if (mHorizontal) {
                int row = getIndex(startP.getY());
                int colum1 = Math.min(getIndex(startP.getX()), getIndex(endP.getX()));
                int colum2 = Math.max(getIndex(startP.getX()), getIndex(endP.getX()));
                for (int colum = colum1; colum <= colum2; colum++) {
                    if (getColor(row, colum) != getColor(r, c)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    //mark the valid touch dots as a specific color
    private int markColor(PointT startP, PointT endP){
        int color = 0;
        //if the dots in the horizontal direction
        if(mHorizontal){
            int row = getIndex(startP.getY());
            int colum1 = Math.min(getIndex(startP.getX()), getIndex(endP.getX()));
            int colum2 = Math.max(getIndex(startP.getX()), getIndex(endP.getX()));
            for(int colum = colum1; colum <= colum2; colum++){
                color = getColor(row, colum);
                this.board.get(colum).set(DOTS_COUNT - 1 - row, Color.GRAY);
            }
        }
        //if the dots are in the vertical direction
        else if(mVertical){
            int colum = getIndex(startP.getX());
            int row1 = Math.min(getIndex(startP.getY()), getIndex(endP.getY()));
            int row2 = Math.max(getIndex(startP.getY()), getIndex(endP.getY()));
            for(int row = row1; row <= row2; row++){
                color = getColor(row, colum);
                this.board.get(colum).set(DOTS_COUNT - 1 - row, Color.GRAY);
            }
        }
        return color;
    }

    private int getColor(int i, int j) {
        return board.get(j).get(DOTS_COUNT - 1 - i);
    }

    //if the touched dot is inside of the view area
    private boolean inSideOfView(PointT point){
        int max = ORIGINAL_POING + DOTS_COUNT * DOTS_SIZE + (DOTS_COUNT - 1) * DOTS_SPACE;
        return (point.getX() > 0 && point.getX() <= max && point.getY() > 0 && point.getY() <= max);
    }

    //determines if the dots between the two points are in the same row or column
    private boolean isSameLine(PointT startP, PointT endP){
        mHorizontal = false;
        mVertical = false;
        int startRow = getIndex(startP.getY());
        int endRow = getIndex(endP.getY());
        int startColum = getIndex(startP.getX());
        int endColum = getIndex(endP.getX());
        if(startRow == endRow && !(startColum == endColum)) mHorizontal = true;
        if(startColum == endColum && !(startRow == endRow)) mVertical = true;
        return mHorizontal || mVertical;
     }

    public String getRC(PointT startP, PointT endP){
        return String.format("r1 : %d, c1 : %d;   r2  : %d, c2  : %d" , getIndex(startP.getY()), getIndex(startP.getX()), getIndex(endP.getY()), getIndex(endP.getX()));
//        return String.valueOf(validateConnective(startP, endP));
    }

    //the result of each remove action
    public String getMessage(){
        return this.gameMode.message(this.moves);

//        return String.format("Moves: %d     Scores: %s" , moves, scores);
    }

    public String getEndMessage(){
        return this.gameMode.endMessage();
    }

    //the down point and up point for the touch event
    public String getTest(PointT startP, PointT endP){
        return String.format("the start point is(%d, %d),the end point is(%d, %d)", startP.getX(), startP.getY(), endP.getX(), endP.getY());

    }


    //draw first indicated dots when touch the valid dots
    private void drawFirstDotIndicator(Canvas canvas) {
        mPaint.setColor(Color.rgb(255,255,0));
        if(startColum ==endColum && startRow == endRow){
            canvas.drawCircle(clickableAreas[startRow * DOTS_COUNT + startColum][0], clickableAreas[startRow * DOTS_COUNT + startColum][1], DOTS_SIZE/2 + 20, mPaint);
        }
    }


    //draw indicated dots when touch the valid dots
    private void drawDotIndicator(Canvas canvas) {
        mPaint.setColor(Color.rgb(255,255,0));
        if(startRow == endRow){
            for(int j = startColum; j < endColum + 1; j++){
                canvas.drawCircle(clickableAreas[startRow * DOTS_COUNT + j][0], clickableAreas[startRow * DOTS_COUNT + j][1], DOTS_SIZE/2 + 20, mPaint);
            }
        }
        if(startColum == endColum){
            for(int i = startRow; i < endRow + 1; i++){
                canvas.drawCircle(clickableAreas[i * DOTS_COUNT + startColum][0], clickableAreas[i * DOTS_COUNT + startColum][1], DOTS_SIZE/2 + 20, mPaint);
            }
        }
    }

    //draw the lines for the touch dots if they are validate connect
    private void drawLineIndicator(Canvas canvas){
        mPaint.setColor(Color.rgb(255,255,0));
        int top = 0, bottom = 0, left = 0, right = 0;
        if(mHorizontal){
            top = clickableAreas[startRow * DOTS_COUNT][1] - 30;
            bottom = clickableAreas[startRow * DOTS_COUNT][1] + 30;
            left = clickableAreas[startColum][0];
            right = clickableAreas[endColum][0];

        }
        if(mVertical){
            top = clickableAreas[startRow * DOTS_COUNT][1];
            bottom = clickableAreas[endRow * DOTS_COUNT][1];
            left = clickableAreas[startColum][0] - 30;
            right = clickableAreas[startColum][0] + 30;
        }
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    //generate random colors
    private int getRandomColor(){
        int x = new Random().nextInt(5);
        if(x == 1) return Color.rgb(255, 26, 26);//red
        if(x == 2 ) return Color.rgb(0, 179, 60);//green
        if(x == 3) return Color.rgb(255, 0, 255);//purple
        if(x == 4) return Color.rgb(0, 255, 255);//sky blue
        else return Color.rgb(0, 0, 255);//blue
    }
}