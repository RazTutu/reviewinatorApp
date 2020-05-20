package com.example.reviewinator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class register_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
    }

    public void sendToMain(View v){
        EditText email = (EditText)findViewById(R.id.emailZone);
        String email_string = email.getText().toString();

        EditText first_name = (EditText)findViewById(R.id.first_name);
        String first_name_string = first_name.getText().toString();

        EditText last_name = (EditText)findViewById(R.id.last_name);
        String last_name_string = last_name.getText().toString();

        EditText nickname = (EditText)findViewById(R.id.nickname);
        String nickname_string = nickname.getText().toString();

        EditText password = (EditText)findViewById(R.id.password);
        String password_string = password.getText().toString();

        final String[] serverResponse = {""};

        try{
            RequestQueue requestQueue = Volley.newRequestQueue(register_screen.this);
            //String URL = "http://reviewinatorserver.chickenkiller.com:6969/user/signup";
            //String URL = "http://192.168.0.2:6969/test";
            String URL = "https://reviewnator-server-last.herokuapp.com/user/signup";

            //String URL = "http://192.168.0.2:6969/test";
            //String URL = "https://reviewnator-api.herokuapp.com/api/v1/airports";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email_string);
            jsonBody.put("name", first_name_string);
            jsonBody.put("lastName", last_name_string);
            jsonBody.put("nickname", nickname_string);
            jsonBody.put("password", password_string);

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.i("VOLLEY", response.toString());
                    //System.out.println(response.toString());
                    //response  needs to be extracted
                    System.out.println("server response is "+ response.toString());
                    Intent i = new Intent();
                    i.putExtra("message", "You are logged in!");
                    //response.toString is 1 if succes and 0 if false.
                   // i.putExtra("serverCode", response.toString());
                    i.putExtra("serverCode", "1");
                    i.putExtra("nickname", nickname_string);
                    setResult(RESULT_OK, i);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            })  {
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
        } catch(JSONException e) {
            e.printStackTrace();
        }




    }

    public void  backToMain(View v){
        finish();
    }
}
