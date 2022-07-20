package com.example.puzzle15;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EasyGameActivity extends AppCompatActivity {
    private List<Integer> numbers = new ArrayList<>();
    private GridLayout gridContainer;
    private int x = 2;
    private int y = 2;

    private Button emptyButton, pauseButton, resumeButton;
    private TextView textPlayer, textMoves;
    private ImageView iconShuffle;
    private Chronometer chronometer;
    private LinearLayout pauseOverlay;
    int moves = 0;
    private long onBackPressedTime;
    private long timeWhenPaused = 0;
    private boolean isGameNotLaunched = true;
    private final int[][] btnTag = {{1, 2, 3}, {4, 5, 6,}, {7, 8, 9} };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_game);
        loadNumbers();
        initializeGame();
        generateNumbers();
        iconShuffle.setOnClickListener(v -> {
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            isGameNotLaunched = true;
            restartUI();
            moves = 0;
            updateMovementUI();
            generateNumbers();

        });
        pauseButton.setOnClickListener(v -> {
            timeWhenPaused = chronometer.getBase() - SystemClock.elapsedRealtime();
            chronometer.stop();
            pauseOverlay.setFocusable(true);
            pauseOverlay.setClickable(true);
            pauseOverlay.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.INVISIBLE);
        });
        resumeButton.setOnClickListener(v -> {
            chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
            chronometer.start();
            pauseOverlay.setVisibility(View.INVISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
        });
    }

    private void restartUI() {
        for (int i = 0; i < gridContainer.getChildCount(); i++) {
            gridContainer.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }

    private void initializeGame() {
        gridContainer = findViewById(R.id.grid_container);
        pauseOverlay = findViewById(R.id.pauseOverlay);
        textPlayer = findViewById(R.id.textPlayer);
        textMoves = findViewById(R.id.textMoves);
        iconShuffle = findViewById(R.id.iconShuffle);
        chronometer = findViewById(R.id.chronometer);
        pauseButton = findViewById(R.id.pauseBtn);
        resumeButton = findViewById(R.id.resumeBtn);


        textPlayer.setText(getIntent().getStringExtra("player"));
    }

    private void loadNumbers() {
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
    }

    private void generateNumbers() {
        do {
            Collections.shuffle(numbers);
        } while (!isSolvable(numbers));

        for (int i = 0; i < gridContainer.getChildCount(); i++) {
            if (numbers.get(i) == 9) {
                String tag = gridContainer.getChildAt(i).getTag().toString();
                x = tag.charAt(0) - '0';
                y = tag.charAt(1) - '0';
                emptyButton = (Button) gridContainer.getChildAt(i);
                emptyButton.setVisibility(View.INVISIBLE);
            } else {
                ((Button) gridContainer.getChildAt(i)).setText(String.valueOf(numbers.get(i)));
                ((Button) gridContainer.getChildAt(i)).setText(String.valueOf(numbers.get(i)));
                if(numbers.get(i) == i + 1) {
                    ((Button) gridContainer.getChildAt(i)).setBackground(getDrawable(R.drawable.true_btn_theme));
                } else {
                    ((Button) gridContainer.getChildAt(i)).setBackground(getDrawable(R.drawable.btn_theme));
                }
            }
        }
    }

    private boolean isSolvable(List<Integer> numbers) {
        int counter = 0;
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                if (numbers.get(i) > numbers.get(j)) {
                    counter++;
                }
            }
        }
        return counter % 2 == 0;
    }

    public void click(View view) {
        if (isGameNotLaunched) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            isGameNotLaunched = false;
        }
        Button clicked = (Button) view;
        String tag = view.getTag().toString();
        int clickedX = tag.charAt(0) - '0';
        int clickedY = tag.charAt(1) - '0';
        if (canMove(clickedX, clickedY)) {
            moves++;
            updateMovementUI();
            swap(clicked, clickedX, clickedY);
            if (isGameOver()) {
                Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
                intent.putExtra("player", textPlayer.getText().toString());
                intent.putExtra("moves", moves);
                startActivity(intent);
                finish();
            }

        }
    }

    private void swap(Button clicked, int clickedX, int clickedY) {
        String tag = emptyButton.getTag().toString();
        int emptyX = tag.charAt(0) - '0';
        int emptyY = tag.charAt(1) - '0';
        String text = clicked.getText().toString();
        clicked.setText("");
        clicked.setVisibility(View.INVISIBLE);
        emptyButton.setText(text);
        emptyButton.setVisibility(View.VISIBLE);
        if (Integer.parseInt(text) == btnTag[emptyX][emptyY]) {
            emptyButton.setBackground(getDrawable(R.drawable.true_btn_theme));
        } else {
            emptyButton.setBackground(getDrawable(R.drawable.btn_theme));
        }
        emptyButton = clicked;
        x = clickedX;
        y = clickedY;

    }

    private boolean canMove(int clickedX, int clickedY) {
        return Math.abs(clickedX - x) != 2 && Math.abs(clickedY - y) != 2 &&
                (Math.abs(clickedX + clickedY - (x + y)) == 1);
    }

    private void updateMovementUI() {
        textMoves.setText(String.valueOf(moves));
    }


    private boolean isGameOver() {
        int counter = 1;
        for (int i = 0; i < 8; i++) {
            Button checkerButton = (Button) gridContainer.getChildAt(i);
            String text = checkerButton.getText().toString();
            if (text.isEmpty()) return false;
            if (Integer.parseInt(checkerButton.getText().toString()) == counter)
                counter++;
        }
        return counter == 9;

    }

    @Override
    public void onBackPressed() {
        if (onBackPressedTime + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intent);
            super.onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Please press back again to return to the menu!", Toast.LENGTH_SHORT).show();
            onBackPressedTime = System.currentTimeMillis();
        }
    }
}