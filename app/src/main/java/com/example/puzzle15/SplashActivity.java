package com.example.puzzle15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    ProgressBar progressBar;
    TextView textView;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progress);
        textView = findViewById(R.id.textV);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getProgress()<100){
                    progressBar.setProgress(progress);
                    progress++;
                    handler.postDelayed(this, 30);
                    if(progress==30)
                        textView.setText("Data is retrieving");
                    if (progress==80)
                        textView.setText("Almost done");

                }
                else{
                    textView.setText("Loading finished");
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }, 1000);
                }

            }
        }, 30);
    }

}