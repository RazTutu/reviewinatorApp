package com.example.reviewinator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.transform.Result;


public class result extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    EditText ISBN;
    Button send_ISBN;
    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("resulttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt");
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

             JSONObject jsonObject = new JSONObject(intent.getStringExtra("raspuns"));
            nickname=intent.getStringExtra("nume");
            //poate o sa adaugam un overall_rating on top of all reviews
            /*if(jsonObject.getString("overall_rating").equals("")){
              overall_rating.setText("not available");
            }else{
                overall_rating.setText(jsonObject.getString("overall_rating"));
            }

            */  JSONObject jsonBodys = new JSONObject();
           // jsonObject.put("mesajEroare", );
            if(jsonObject.has("mesajEroare") )
            {

               setContentView(R.layout.isbn_for_error);


                ISBN=findViewById(R.id.isbn_input);
                send_ISBN=findViewById(R.id.send_isbn);

                 TextWatcher send_textWatcher=new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String input =ISBN.getText().toString().trim();
                    send_ISBN.setEnabled(!input.isEmpty());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
               ISBN.addTextChangedListener(send_textWatcher);

            }
            else {

                JSONArray array = jsonObject.getJSONArray("reviews");
                for (int i = 0; i < array.length(); i++) {
                    String author = array.getJSONObject(i).getString("author");
                    String rating = "Rating: " + array.getJSONObject(i).getString("rating");
                    String review = "Review: " + array.getJSONObject(i).getString("description");

                    reviewItems.add(new ReviewItem(author, rating, review));
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(this);
                    adapter = new ReviewAdapter(reviewItems);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    public void start(View view) {

        String input = ISBN.getText().toString().trim();
        System.out.println("BAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        RequestQueue requestQueue = Volley.newRequestQueue(result.this);
        try {
            //String URL = "http://reviewinatorserver.chickenkiller.com:6969/user/login";
            // String URL = "http://192.168.0.2:6969/test";
           // String URL = "http://10.0.2.2:6969/test";
            String URL = "https://reviewnator-server.herokuapp.com/test";
            //String URL = "https://reviewnator-api.herokuapp.com/api/v1/airports";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("encoding", "");
            jsonBody.put("isbn", input);
            jsonBody.put("nickname", nickname);
            System.out.println(jsonBody);
            System.out.println("am printat requestul in rezult");

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    showResult(response);
                    finish();
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

        }  catch(
                JSONException e)

        {
            e.printStackTrace();
        }

    }
    private void showResult(String a ) {
        Intent intent = new Intent(this, com.example.reviewinator.result.class);
        intent.putExtra("raspuns", a);
        intent.putExtra("nume",  nickname);
        startActivity(intent);
    }
}
