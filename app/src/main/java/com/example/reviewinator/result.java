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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class result extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

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

        ArrayList<ReviewItem> reviewItems = new ArrayList<>();


        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra(MainActivity.EXTRA_TEXT));
            //poate o sa adaugam un overall_rating on top of all reviews
            /*if(jsonObject.getString("overall_rating").equals("")){
              overall_rating.setText("not available");
            }else{
                overall_rating.setText(jsonObject.getString("overall_rating"));
            }
            */
               JSONArray array = jsonObject.getJSONArray("reviews");
               for (int i = 0; i < array.length(); i++) {
                   String author = array.getJSONObject(i).getString("author");
                   String rating = "Rating: "+array.getJSONObject(i).getString("rating");
                   String review ="Review: " + array.getJSONObject(i).getString("description");

                   reviewItems.add(new ReviewItem(author,rating,review));
               }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ReviewAdapter(reviewItems);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }


}
