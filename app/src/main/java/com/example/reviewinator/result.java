package com.example.reviewinator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class result extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setContentView(R.layout.activity_result);
        toolbar = (Toolbar) findViewById(R.id.toolbarForBack);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();

        TextView txtview = (TextView) findViewById(R.id.textView);
        txtview.setMovementMethod(new ScrollingMovementMethod());

        StringBuilder stringBuilder = new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra(MainActivity.EXTRA_TEXT));
            stringBuilder.append("Overall Rating: "+jsonObject.getString("overall_rating")+"\n\n\n\n");
            JSONArray array = jsonObject.getJSONArray("reviews");
            for (int i=0;i<array.length();i++) {

                stringBuilder.append(array.getJSONObject(i).getString("author")+"\n\n");
                stringBuilder.append("Rating: " + array.getJSONObject(i).getString("rating")+"\n");
                stringBuilder.append("Review: "+array.getJSONObject(i).getString("description")+"\n\n\n");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtview.setText(stringBuilder.toString());

    }


}
