package com.example.reviewinator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class History extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);

        toolbar = (Toolbar) findViewById(R.id.toolbarBack);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reviews");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView textView = (TextView) findViewById(R.id.reviewHistory);
        textView.setMovementMethod(new ScrollingMovementMethod());

        //get  the username  from  main activity
        Intent intent = getIntent();

        String nickname = intent.getStringExtra("nickname");
        //store  the  reviews inside reviews string when server sends the response
        final String[] reviewsArray = new String[1];

        //tell the server  that you want reviews as response for the nickname string
        //we  send the nickname string and expect a json with reviews as response
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(History.this);
            //String URL = "http://reviewinatorserver.chickenkiller.com:6969/user/login";
            String URL = "http://192.168.0.50:6960/test";
            //String URL = "http://10.0.2.2:6969/test";
            //String URL = "https://reviewnator-api.herokuapp.com/api/v1/airports";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nickname", nickname);

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    reviewsArray[0] = response;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = new String(response.data);
                    }
                    assert response != null;
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

            };
            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 20000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 20000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            requestQueue.add(stringRequest);

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        //now the response is stored inside reviews variable. make it a json object
        String reviews = reviewsArray[0];
        reviews = "{\"reviews\":[{\"date\":\"Aug 27, 2011\",\"author\":\"Alison Van Arsdel\",\"rating\":4,\"description\":\"My teacher for this class never assigned readings or homework from the book. I wish he would have. The book was definitely way more involved that what he thought we should know for the class, but I don\'t mind learning extra.\"},{\"date\":\"Oct 18, 2014\",\"author\":\"Ebad\",\"rating\":\"Not available\",\"description\":\"awesome\"},{\"date\":\"Feb 18, 2015\",\"author\":\"Chantharamanee Prat\",\"rating\":5,\"description\":\"Not available\"},{\"date\":\"Jul 09, 2017\",\"author\":\"Sakthi Sundaram\",\"rating\":1,\"description\":\"Not available\"},{\"date\":\"Sep 26, 2013\",\"author\":\"Fahad Kh\",\"rating\":5,\"description\":\"Not available\"},{\"date\":\"Jul 03, 2016\",\"author\":\"Dgfggffgf\",\"rating\":5,\"description\":\"Not available\"},{\"date\":\"Feb 28, 2019\",\"author\":\"Hasham Khan\",\"rating\":1,\"description\":\"Not available\"}],\"overall_rating\":\"3.50\"}";



        StringBuilder strBuilder = new StringBuilder();
        try {
            JSONObject jsonReviews = new JSONObject(reviews);


            JSONArray array = jsonReviews.getJSONArray("reviews");
            for (int i = 0; i < array.length(); i++) {

                //strBuilder.append("ISBN: " + array.getJSONObject(i).getString("isbn") + "\n\n");
                strBuilder.append("Author: " + array.getJSONObject(i).getString("author") + "\n\n");
                strBuilder.append("Rating: " + array.getJSONObject(i).getString("rating") + "\n");
                strBuilder.append("Review: " + array.getJSONObject(i).getString("description") + "\n\n\n");
                strBuilder.append("Date: " + array.getJSONObject(i).getString("date") + "\n");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        textView.setText(strBuilder.toString());

    }
}

/*you will have to use something  like this to parse reviews inside JSONObject to get them but I don't know how*/
//JSONObject jsonReviews = new JSONObject(reviews);

//now use string builder and JSONObject(i).getString() to get  the reviews stored in the jsonReviews
//Look how result.java is doing this kind of task. You should copy what  they did there.  It's  a similar task from now