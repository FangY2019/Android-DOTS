package com.dots;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dots.entity.PointT;
import com.dots.view.BoardView;
import com.dots.view.GameEndless;
import com.dots.view.GameLevels;
import com.dots.view.GameMoves;
import com.dots.view.GameTimed;

public class BoardActivity extends AppCompatActivity{
    private BoardView mBoardView;
    private String gameMode;
    private TextView tvShow;
    private PointT startP;
    private PointT endP;
    private PointT tempP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
//        gameMode = "";

        //get the dots view
        mBoardView = (BoardView) findViewById(R.id.dotsView);
//        mBoardView.setGameMode(new GameMoves());
        setGameMode();

        //get the text view
        tvShow = findViewById(R.id.tv_condition);
        initUI();
    }

    private void initUI(){
        startP = new PointT();
        endP = new PointT();
        tempP = new PointT();

        //show the initial game condition
        tvShow.setText(mBoardView.getMessage());
        //update the game condition when touch the board
        mBoardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(mBoardView.getGameStatus()){
                    //set the first touch point as the start point
                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                        startP.setX((int) motionEvent.getX());
                        startP.setY((int) motionEvent.getY());
                    }
                    //set the end point when finishing touch
                    else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                        endP.setX((int) motionEvent.getX());
                        endP.setY((int) motionEvent.getY());
                        //update the dots colors
                        if(mBoardView.remove(startP, endP)){
                            mBoardView.updateView();
                            //update the scores
                            tvShow.setText(mBoardView.getMessage());
                            mBoardView.setIndicatorVisible();
                        }
                    }
                    tempP.setX((int) motionEvent.getX());
                    tempP.setY((int) motionEvent.getY());
                    //draw line and dot indicator when touch the valid dots
                    mBoardView.drawIndicator(startP, tempP);
                    return true;
                }
                //if the game status is false, go to result activity and set the touch listener as false
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ResultActivity.class);
                intent.putExtra("endMessage", mBoardView.getEndMessage());

                //send the game mode to result activity
                intent.putExtra("gameMode", gameMode);
                startActivity(intent);
                return false;
            }
        });

        findViewById(R.id.btn_more_games).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ModeActivity.class);
                startActivity(intent);
            }
        });
    }

    //get game mode from Mode activity or result activity
    private void setGameMode(){
        Intent intent = getIntent();
        gameMode = intent.getStringExtra("gameMode");
        if(gameMode.equals("moves")){
            mBoardView.setGameMode(new GameMoves());
        }else if(gameMode.equals("endless")){
            mBoardView.setGameMode(new GameEndless());
        }else if(gameMode.equals("timed")){
            mBoardView.setGameMode(new GameTimed());
        }else if(gameMode.equals("levels")){
            mBoardView.setGameMode(new GameLevels());
        }
    }
}
