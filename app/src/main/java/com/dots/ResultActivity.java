package com.dots;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initUI();
    }
    private void initUI(){
        //get game ending message from board activity and show it in the result activity
        String message = getIntent().getStringExtra("endMessage");
        TextView tvGameResult = findViewById(R.id.tv_game_result);
        tvGameResult.setText(message);

        //receive the game mode information from board activity
        final String gameMode = getIntent().getStringExtra("gameMode");

        //set action for play again button
        findViewById(R.id.btn_play_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), BoardActivity.class);
                //send the game mode information to board activity
                intent.putExtra("gameMode", gameMode);
                startActivity(intent);
            }
        });
    }

}
