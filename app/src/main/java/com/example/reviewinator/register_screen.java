package com.example.reviewinator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class register_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
    }

    public void sendToMain(View v){
        Intent i = new Intent();
        i.putExtra("message", "message sent from another activity");
        setResult(RESULT_OK, i);
        finish();
    }
}
