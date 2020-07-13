package com.dots;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ModeActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        initUI();
    }

    private void initUI() {
        findViewById(R.id.btn_timed).setOnClickListener(this);
        findViewById(R.id.btn_moves).setOnClickListener(this);
        findViewById(R.id.btn_endless).setOnClickListener(this);
        findViewById(R.id.btn_levels).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), BoardActivity.class);
        startActivity(intent);
    }
}
