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

import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class localHistoryScreen extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView1,recyclerView2,recyclerView3;
    private RecyclerView.Adapter adapter1, adapter2, adapter3;
    private RecyclerView.LayoutManager layoutManager1,layoutManager2,layoutManager3;
    private TextView emptyView, lastReview, cachedReview1, cachedReview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setContentView(R.layout.activity_local_history_screen);
        toolbar = (Toolbar) findViewById(R.id.local_history_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Your last 3 books are here:");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView3 = findViewById(R.id.recyclerView3);

        emptyView = findViewById(R.id.empty_view);
        lastReview = findViewById(R.id.last_review);
        cachedReview1 = findViewById(R.id.more_cached_reviews1);
        cachedReview2 = findViewById(R.id.more_cached_reviews2);


        Intent intent = getIntent();
        ArrayList<ArrayList<ReviewItem>>reviewItems = new ArrayList<ArrayList<ReviewItem>>(4);
        String[] string_results = new String[3];
        string_results[0]= Objects.requireNonNull(getIntent().getExtras()).getString("Value");
        string_results[1]= Objects.requireNonNull(getIntent().getExtras()).getString("Value1");
        string_results[2]= Objects.requireNonNull(getIntent().getExtras()).getString("Value2");
        System.out.println(string_results[0]+"\n");
        System.out.println(string_results[1]+"\n");
        System.out.println(string_results[2]+"\n");



        for(int ii=0;ii<3;ii++)
        {
            try {

                JSONObject jsonObject = new JSONObject(string_results[ii]);
                //poate o sa adaugam un overall_rating on top of all reviews
            /*if(jsonObject.getString("overall_rating").equals("")){
              overall_rating.setText("not available");
            }else{
                overall_rating.setText(jsonObject.getString("overall_rating"));
            }
            */
                ArrayList<ReviewItem> a1 = new ArrayList<ReviewItem>();
                JSONArray array = jsonObject.getJSONArray("reviews");
                for (int i = 0; i < array.length(); i++) {
                    String author = array.getJSONObject(i).getString("author");
                    String rating = "Rating: "+array.getJSONObject(i).getString("rating");
                    String review ="Review: " + array.getJSONObject(i).getString("description");

                    a1.add(new ReviewItem(author,rating,review));
                }
                reviewItems.add(a1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println(reviewItems.size());

        recyclerView1.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        recyclerView3.setHasFixedSize(true);

        layoutManager1 = new LinearLayoutManager(this);
        layoutManager2 = new LinearLayoutManager(this);
        layoutManager3 = new LinearLayoutManager(this);

        if(reviewItems.size()>=1)adapter1 = new ReviewAdapter(reviewItems.get(0));
        if(reviewItems.size()>=2)adapter2 = new ReviewAdapter(reviewItems.get(1));
        if(reviewItems.size()>=3)adapter3 = new ReviewAdapter(reviewItems.get(2));

        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(adapter1);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setAdapter(adapter2);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setAdapter(adapter3);


        lastReview.setVisibility(View.VISIBLE);
        recyclerView1.setVisibility(View.VISIBLE);
        cachedReview1.setVisibility(View.VISIBLE);
        recyclerView2.setVisibility(View.VISIBLE);
        cachedReview2.setVisibility(View.VISIBLE);
        recyclerView3.setVisibility(View.VISIBLE);

        if(reviewItems.size()==2) {
            cachedReview2.setVisibility(View.GONE);
            recyclerView3.setVisibility(View.GONE);
        }
        if(reviewItems.size()==1){
            cachedReview1.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.GONE);
            cachedReview2.setVisibility(View.GONE);
            recyclerView3.setVisibility(View.GONE);
        }
        if(reviewItems.size()==0){
            emptyView.setVisibility(View.VISIBLE);
            lastReview.setVisibility(View.GONE);
            recyclerView1.setVisibility(View.GONE);
            cachedReview1.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.GONE);
            cachedReview2.setVisibility(View.GONE);
            recyclerView3.setVisibility(View.GONE);
        }
    }

    public void  backToMain(View v){
        finish();
    }
}
