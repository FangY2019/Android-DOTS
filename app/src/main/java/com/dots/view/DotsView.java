package com.dots.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.dots.entity.PointT;

import java.util.ArrayList;
import java.util.Random;

public class DotsView extends View{
    //dots
    private final int DOTS_COUNT = 6;
    private final int DOTS_SIZE = 100;
    private final int DOTS_SPACE = 50;


    private ArrayList<ArrayList<Integer>> board;
    private int moves;
    private int scores;
    private boolean removable;


    private int[][] clickableAreas;
    private Paint mPaint;

    //line
    private final int LINE_WIDTH = 2;
    private boolean mDotClickEnable;
    private boolean mIndicatorDragEnable;
    private boolean mTouchEnable;



    //if call the animator
    private boolean havePressAniming = false;

    public interface OnDotClickListener{
        void onDotClickChange(View v, float[][] clickableAreas);
    }


    public DotsView(Context context) {
        super(context);
        init(null);
    }

    public DotsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DotsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

//    public DotsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(attrs);
//    }

    private void init(@Nullable AttributeSet set){
        clickableAreas = new int[DOTS_COUNT * DOTS_COUNT][2];
        mPaint = new Paint();
        board = new ArrayList<ArrayList<Integer>>();
        moves = 6;
        scores = 0;
        for(int i = 0; i < DOTS_COUNT; i++){
            ArrayList<Integer> list= new ArrayList<Integer>();
            for(int j = 0; j < DOTS_COUNT; j++){
                list.add(j, getRandomColor());
            }
            board.add(list);
        }
    }

    public void swapColor(){
        board.get(0).set(0, Color.GRAY);
        board.get(0).set(1, Color.GRAY);
        board.get(0).set(2, Color.GRAY);
        invalidate();
        moves--;
        scores += 2;

//        postInvalidate();
    }

    public boolean isRemovable(PointT startP, PointT endP){
        if(!(inSideOfView(startP) && inSideOfView(endP))){
            removable = false;
        }
        else if(!isSameLine(startP, endP)){
            removable = false;
        }
        else removable = true;
        return removable;
    }

    public void updateView(){
        if(removable){
            board.get(0).set(0, Color.GRAY);
            board.get(0).set(1, Color.GRAY);
            board.get(0).set(2, Color.GRAY);
            invalidate();
            moves--;
            scores += 2;
        }
    }

    //if the touch point is inside of the view area
    private boolean inSideOfView(PointT point){
        int max = DOTS_COUNT* DOTS_SIZE + (DOTS_COUNT - 1) * DOTS_SPACE;
        return (point.getX() > 0 && point.getX() <= max &&  point.getY() > 0 && point.getY() <= max);
    }

    //if two points in the horizontal direction or vertical direction
    private boolean isSameLine(PointT startP, PointT endP){
        return (Math.abs(startP.getX() - endP.getX()) < DOTS_SIZE/2 || Math.abs(startP.getY() - endP.getY()) < DOTS_SIZE/2);
    }


    //the result of each remove action
    public String getMessage(){
        return String.format("Moves: %d     Scores: %s" , moves, scores);
    }

    //the down point and up point for the touch event
    public String getTest(PointT startP, PointT endP){
        return String.format("the start point is(%d, %d),the end point is(%d, %d)", startP.getX(), startP.getY(), endP.getX(), endP.getY());
    }

    public String getTest(int x1,int y1,int x2, int y2){
        return String.format("the start point is(%d, %d),the end point is(%d, %d)", x1, y1, x2, y2);
    }

    @Override
    protected void onDraw(Canvas canvas){
        for(int i = 0; i < DOTS_COUNT; i++){
            for(int j = 0; j < DOTS_COUNT; j++){
                float cx = DOTS_SIZE/2 + (DOTS_SIZE + DOTS_SPACE) * j;
                float cy = DOTS_SIZE/2 + (DOTS_SIZE + DOTS_SPACE) * i;
                mPaint.setColor(board.get(i).get(j));
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