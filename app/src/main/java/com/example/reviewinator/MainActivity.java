package com.example.reviewinator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.os.Environment;
import android.provider.MediaStore;

import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import android.widget.Button;

import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.text.StringEscapeUtils;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int CAMERA_REQUEST_CODE = 102;
    private static final int CAMERA_PERM_CODE = 101;
    public static final int GALLERY_REQUEST_CODE = 105;
    public static final String EXTRA_TEXT = "com.example.reviewinator.EXTRA_TEXT";
    public String registerResponse = "";
    public String nickname = "";
    Menu myMenu;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Button button, btnGallery;
    String currentPhotoPath;
    String given_respose = "";
    String given_respose1 = "";
    String given_respose2 = "";
    String filename = "saveFile1.txt";

    public void readFile() throws FileNotFoundException {
        File file = new File(this.getFilesDir(), filename);
        FileInputStream fis = this.openFileInput(filename);
        //boolean deleted = file.delete();
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                if(given_respose.equals("")) given_respose = line;
                else if(given_respose1.equals("")) given_respose1 = line;
                else if (given_respose2.equals(""))given_respose2 = line;
                else{
                    given_respose2 = given_respose1;
                    given_respose1 = given_respose;
                    given_respose = line;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        }
    }

    @SuppressLint({"RestrictedApi", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        button = findViewById(R.id.button);
        btnGallery = findViewById(R.id.btnGallery);
        try {
            readFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    public void open_register_activity(MenuItem item){
        //Toast.makeText(this, "Hello World", Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(this, register_screen.class);
        //intent.putExtra(EXTRA_TEXT, "Gone to register screen.");
        //startActivity(intent);
        //Activity registerActiviy = new registerAction();
        startActivityForResult(new Intent(getApplicationContext(),   register_screen.class), 999);
    }

    public void open_login_activity(MenuItem item){
        startActivityForResult(new Intent(getApplicationContext(), login_screen.class), 1000);
    }

    public void logoutClicked(MenuItem item){


        /*
        Menu menu = navigationView.getMenu();

        //invalidateOptionsMenu();
        MenuItem login_item = menu.findItem(R.id.loginButton);
        login_item.setVisible(true);


        MenuItem register_item = menu.findItem(R.id.registerButton);
        register_item.setVisible(true);

        MenuItem local_history_item = menu.findItem(R.id.local_history);
        local_history_item.setVisible(true);

        MenuItem logout_item = menu.findItem(R.id.logout_item);
        logout_item.setVisible(false);


        Toast.makeText(this, "Logout  pressed.", Toast.LENGTH_SHORT).show();

         */
        //do a force restart

        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finishAffinity();
        nickname = "";
    }


    private void showResult(String a) {
        Intent intent = new Intent(this, com.example.reviewinator.result.class);
        intent.putExtra(EXTRA_TEXT, a);
        startActivity(intent);
    }

    private void postRequest(String encodedImage) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

            //String URL = "http://reviewinatorserver.chickenkiller.com:6969/test";
            String URL = "http://192.168.43.229:200/test";
            //String URL = "http://10.0.2.2:6969/test";

            //          String URL = "https://reviewnator-api.herokuapp.com/api/v1/airports";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("encoding", encodedImage);
            jsonBody.put("nickname", nickname);

            //jsonBody.put("country", "MAXUT");
            //jsonBody.put("city", "DELENI");
            //jsonBody.put("plainCapacity", "69");
            final String requestBody = jsonBody.toString();
            System.out.println(requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.i("VOLLEY", response.toString());
                    showResult(StringEscapeUtils.unescapeHtml4(response));
                    if(given_respose.equals("")) given_respose = response;
                    else if(given_respose1.equals("")) given_respose1 = response;
                    else if (given_respose2.equals(""))given_respose2 = response;
                    else{
                        given_respose2 = given_respose1;
                        given_respose1 = given_respose;
                        given_respose = response;
                    }
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
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use Camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // mod nou SI BUN
    public String encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        String encodedImage = Base64.encodeToString(byteFormat, Base64.CRLF | Base64.NO_WRAP);
        return encodedImage;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                assert bitmap != null;
                String encodedString = encodeBitmap(bitmap);

                byte[] decodeString = Base64.decode(encodedString, Base64.DEFAULT);
                Bitmap decoded = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);

                postRequest(encodedString);
                Toast.makeText(MainActivity.this, "POST Request successful din camera", Toast.LENGTH_SHORT).show();

            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri filePath = data.getData();
                Bitmap bitmap = null;
                try {
                    InputStream inputStream = getContentResolver().openInputStream(filePath);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(filePath);
                Log.d("GALLERY", "onActivityResult: Gallery Image Uri: " + imageFileName);
                String encodedString = encodeBitmap(bitmap);

                postRequest(encodedString);
                Toast.makeText(MainActivity.this, "POST Request successful din galerie", Toast.LENGTH_SHORT).show();
            }
        }


        switch(requestCode) {
            //user back to main from register
            case (999) : {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    String returnValue = data.getStringExtra("some_key");
                    Toast.makeText(this, data.getStringExtra("message"), Toast.LENGTH_LONG).show();
                    //String response = data.getStringExtra("serverCode");
                    registerResponse = data.getStringExtra("serverCode");
                    nickname = data.getStringExtra("nickname");
                    System.out.println("nickname is " + nickname);
                    if(registerResponse.equals("1")){
                        System.out.println(registerResponse);
                        //hide register menu item
                        //MenuItem item = findViewById(R.id.registerButton); //code crashes the app
                        //item.setVisible(false); //code crashes the app
                        Menu menu =navigationView.getMenu();
                        MenuItem register_item = menu.findItem(R.id.registerButton);
                        register_item.setVisible(false);

                        MenuItem login_button = menu.findItem(R.id.loginButton);
                        login_button.setVisible(false);

                        MenuItem local_history = menu.findItem(R.id.local_history);
                        local_history.setVisible(false);

                        //uncomment after history implemented. Make sure that history visibility is  false in menu_item.xml
                        //MenuItem history = menu.findItem(R.id.history);
                        //history.setVisible(true);

                        MenuItem logout = menu.findItem(R.id.logout_item);
                        logout.setVisible(true);
                    }
                }
                break;
            }
            //user back to main from login
            case (1000) : {
                System.out.println("back to main");
                if(resultCode == Activity.RESULT_OK){
                    registerResponse = data.getStringExtra("serverCode");
                    nickname = data.getStringExtra("nickname");
                    if(registerResponse.equals("1")){
                        Menu menu = navigationView.getMenu();
                        MenuItem register_item = menu.findItem(R.id.registerButton);
                        register_item.setVisible(false);

                        MenuItem login_button = menu.findItem(R.id.loginButton);
                        login_button.setVisible(false);

                        MenuItem local_history = menu.findItem(R.id.local_history);
                        local_history.setVisible(false);

                        //uncomment after history implemented. Make sure that history visibility is  false in menu_item.xml
                        //MenuItem history = menu.findItem(R.id.history);
                        //history.setVisible(true);

                        MenuItem logout = menu.findItem(R.id.logout_item);
                        logout.setVisible(true);

                        Toast.makeText(this, "Logged in", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            }
        }
    }




    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.loginButton:
                Toast.makeText(MainActivity.this, "Login Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.registerButton:
                Toast.makeText(MainActivity.this, "Register selected", Toast.LENGTH_SHORT).show();
                System.out.println("Text");
                break;
            case R.id.history:
                Toast.makeText(MainActivity.this, "History selected", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, History.class);
                startActivity(i);
                break;
        }
        return false;
    }



    public void local_history_activity(MenuItem item){
        Intent intent = new Intent(this, localHistoryScreen.class);
        intent.putExtra("Value", given_respose);
        intent.putExtra("Value1", given_respose1);
        intent.putExtra("Value2", given_respose2);
        System.out.println("----------------------------------------------------------------------------------------------");
        startActivity(intent);
        //finish();
    }

    public void local_history(MenuItem item){
        Toast.makeText(MainActivity.this, "History selected", Toast.LENGTH_LONG).show();
        System.out.println("Something");
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
    }

    protected void onPause() {
        super.onPause();
        try
        {
            OutputStreamWriter fout= new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
            fout.append(given_respose);
            fout.append("\n");
            fout.append(given_respose1);
            fout.append("\n");
            fout.append(given_respose2);
            fout.append("\n");
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
