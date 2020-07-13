package com.dots;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dots.entity.PointT;
import com.dots.view.DotsView;

public class BoardActivity extends AppCompatActivity{
    private DotsView mDotsView;
    private TextView tvShow;
    private TextView tvStartEnd;
    private TextView tvTemp;

//    private int x1, x2, y1, y2;

    private PointT startP;
    private PointT endP;
    private PointT tempP;

    private boolean removeable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        startP = new PointT();
        endP = new PointT();
        tempP = new PointT();

        //get the dots view
        mDotsView = (DotsView) findViewById(R.id.dotsView);

        //get the text view
        tvShow = findViewById(R.id.tv_condition);
        tvStartEnd = findViewById(R.id.tv_start_end_point);
        tvTemp = findViewById(R.id.tv_temp_points);

        //show the initial game condition
        tvShow.setText(mDotsView.getMessage());

        //show the position
        tvStartEnd.setText(mDotsView.getTest(new PointT(0, 0), new PointT(0, 0)));
        tvTemp.setText(mDotsView.getTest(new PointT(0, 0), new PointT(0, 0)));

//        findViewById(R.id.btn_swap_color).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mDotsView.swapColor();
//                tvShow.setText(mDotsView.getMessage());
//            }
//        });

        mDotsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //set the first touch point as the start point
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
//                    x1 = (int) motionEvent.getX();
//                    y1 = (int) motionEvent.getY();
//                    startP.setX(x1);
//                    startP.setY(y1);
                    startP.setX((int) motionEvent.getX());
                    startP.setY((int) motionEvent.getY());
                }
                //set the end point when finishing touch
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
//                    x2 = (int) motionEvent.getX();
//                    y2 = (int) motionEvent.getY();
//                    endP.setX(x2);
//                    endP.setY(y2);
                    endP.setX((int) motionEvent.getX());
                    endP.setY((int) motionEvent.getY());
                    //update the dots colors
                    if(mDotsView.isRemovable(startP, endP)){
                        mDotsView.updateView();
                        //update the scores
                        tvShow.setText(mDotsView.getMessage());
                        tvStartEnd.setText(mDotsView.getTest(startP, endP));

                    }
                }
                tempP.setX((int) motionEvent.getX());
                tempP.setY((int) motionEvent.getY());
                tvTemp.setText(mDotsView.getRC(startP, tempP));
                return true;
            }

        });
    }


}
