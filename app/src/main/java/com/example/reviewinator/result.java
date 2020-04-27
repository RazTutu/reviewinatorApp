package com.example.reviewinator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

public class result extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        toolbar = (Toolbar) findViewById(R.id.toolbarForBack);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String searchResult = intent.getStringExtra(MainActivity.EXTRA_TEXT);

        TextView txtview = (TextView) findViewById(R.id.textView);
        txtview.setText(searchResult);

    }
}
