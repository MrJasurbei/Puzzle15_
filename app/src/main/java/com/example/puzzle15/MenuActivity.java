package com.example.puzzle15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MenuActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        preferences = getSharedPreferences("app_user_name", MODE_PRIVATE);
        editor = preferences.edit();
        Button button = findViewById(R.id.btnPlay);

        editText = findViewById(R.id.editPlayer);
        String lastPlayer = preferences.getString("player", "");
        editText.setText(lastPlayer);

        Button button1 = findViewById(R.id.btnInfo);
        Button button2 = findViewById(R.id.btnPlay8);
        button1.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
            startActivity(intent);
        });

        button.setOnClickListener(v->{
            if (!editText.getText().toString().isEmpty()){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("player", editText.getText().toString());
                startActivity(intent);
                finish();
            }

        });
        button2.setOnClickListener(v -> {
            if (!editText.getText().toString().isEmpty()){
                Intent intent = new Intent(getApplicationContext(), EasyGameActivity.class);
                intent.putExtra("player", editText.getText().toString());
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onStop() {
        if (editText.getText().toString().length()>0){
            editor.putString("player", editText.getText().toString());
            editor.commit();
        }
        super.onStop();
    }
}