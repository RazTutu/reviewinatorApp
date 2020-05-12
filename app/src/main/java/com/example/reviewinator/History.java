package com.example.reviewinator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Intent intent = getIntent();
        String newString;
        TextView txtview = findViewById(R.id.historyTextView);
        txtview.setMovementMethod(new ScrollingMovementMethod());

        Bundle extras = getIntent().getExtras();

        StringBuilder stringBuilder = new StringBuilder();
        try {
//
//            System.out.println("-----------------/////////////////////////////----------------------\n");
//            System.out.println(String.valueOf(extras.getInt("reviews")));
//            System.out.println("-----------------/////////////////////////////----------------------\n");

            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("reviews");
            }

            JSONObject jsonObject = new JSONObject(newString);
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
