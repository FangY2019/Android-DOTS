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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BoardView extends View {
    //dots
    private final int DOTS_COUNT = 6;
    private final int DOTS_SIZE = 100;
    private final int DOTS_SPACE = 50;


    private ArrayList<ArrayList<Integer>> board;
    private int moves;
    private boolean status;

    private int scores;
    private boolean removable;


    private int[][] clickableAreas;
    private Paint mPaint;

    //the orientation of remove dots
    private boolean mHorizontal;
    private boolean mVertical;

    //line
    private final int LINE_WIDTH = 2;
    private boolean mDotClickEnable;
    private boolean mIndicatorDragEnable;
    private boolean mTouchEnable;





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
        scores = 0;
        for (int i = 0; i < DOTS_COUNT; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            for (int j = 0; j < DOTS_COUNT; j++) {
                list.add(j, getRandomColor());
            }
            board.add(list);
        }
    }

    private int getColor(int i, int j) {
        return board.get(j).get(DOTS_COUNT - 1 - i);
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
                scores += removeSize;
                return true;
            }
        }
//        return removable;
        return false;
//        return true;
    }

    public void updateView() {
        for(int i = 0; i < DOTS_COUNT; i++){
            for(int j = board.get(i).size(); j < DOTS_COUNT; j++){
                board.get(i).add(j, getRandomColor());
            }
        }
        invalidate();
    }

    private int getIndex(int number) {
        if (number >= 0 && number < 150) {
            return 0;
        } else if (number >= 150 && number < 300) {
            return 1;
        } else if (number >= 300 && number < 450) {
            return 2;
        } else if (number >= 450 && number < 600) {
            return 3;
        } else if (number >= 600 && number < 750) {
            return 4;
        } else return 5;
    }

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

    //if the touch point is inside of the view area
    private boolean inSideOfView(PointT point){
        int max = DOTS_COUNT * DOTS_SIZE + (DOTS_COUNT - 1) * DOTS_SPACE;
        return (point.getX() > 0 && point.getX() <= max && point.getY() > 0 && point.getY() <= max);
    }

    //if two points in the horizontal direction or vertical direction
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
        return String.format("Moves: %d     Scores: %s" , moves, scores);
    }

    //the down point and up point for the touch event
    public String getTest(PointT startP, PointT endP){
        return String.format("the start point is(%d, %d),the end point is(%d, %d)", startP.getX(), startP.getY(), endP.getX(), endP.getY());

    }

//
//    public String getTest(int x1,int y1,int x2, int y2){
//        return String.format("the start point is(%d, %d),the end point is(%d, %d)", x1, y1, x2, y2);
//    }

    @Override
    protected void onDraw(Canvas canvas){
        for(int i = 0; i < DOTS_COUNT; i++){
            for(int j = 0; j < DOTS_COUNT; j++){
                float cx = DOTS_SIZE/2 + (DOTS_SIZE + DOTS_SPACE) * j;
                float cy = DOTS_SIZE/2 + (DOTS_SIZE + DOTS_SPACE) * i;
                mPaint.setColor(getColor(i, j));
                canvas.drawCircle(cx, cy, DOTS_SIZE/2, mPaint);
                clickableAreas[i * DOTS_COUNT + j][0] = (int) cx;
                clickableAreas[i * DOTS_COUNT + j][1] = (int) cy;
            }
        }
    }


    private int getRandomColor(){
        int x = new Random().nextInt(4);
        if(x == 1) return Color.RED;
        if(x == 2 ) return Color.GREEN;
        if(x == 3) return Color.YELLOW;
        else return Color.BLUE;
    }
}