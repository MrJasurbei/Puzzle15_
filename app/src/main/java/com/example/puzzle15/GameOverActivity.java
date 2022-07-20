package com.example.puzzle15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {
    private Button restartButton, exitButton;
    private TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        textInfo = findViewById(R.id.textResult);
        restartButton = findViewById(R.id.btnRestart);
        exitButton = findViewById(R.id.btnExit);
        String player = getIntent().getStringExtra("player");
        int moves = getIntent().getIntExtra("moves", 0);
        textInfo.setText(player + "! You won the game with " +moves +" moves");

        restartButton.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            finish();
        });
        exitButton.setOnClickListener(v->{
            finish();
        });
    }
}