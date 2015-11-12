package com.zemosolabs.storede;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zemosolabs.zetarget.sdk.ZeTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SplashActivity extends Activity implements View.OnClickListener {
    static final String GCM_SENDER_ID ="914500168484";
    static final String ZETARGET_API_KEY = "ZemosoL";
    private static final String MY_URL = "http://api.zetarget.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZeTarget.setZeTargetURL(MY_URL);
        ZeTarget.initializeWithContextAndKey(getApplicationContext(), ZETARGET_API_KEY, GCM_SENDER_ID);
        setContentView(R.layout.activity_splash);
        SharedPreferences demoData = getSharedPreferences("DemoData",MODE_PRIVATE);
        Button logInButton = (Button) findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("AppData", MODE_PRIVATE);
        if(!sharedPreferences.contains("gender")) {
            if (Math.random() >= 0.5) {
                sharedPreferences.edit().putString("gender","male").apply();
            }else{
                sharedPreferences.edit().putString("gender","female").apply();
            }
        }
        if(!sharedPreferences.contains("dob")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int year = (int)Math.ceil(Math.random()*50)+1950;
            int month = (int)Math.ceil(Math.random()*12);
            int day = (int)Math.ceil(Math.random())*28;
            editor.putString("dob", year + "" + String.format("%02d", month) + String.format("%02d", day));
            editor.apply();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.log_in_button:

                Intent intent = new Intent(this,ShopActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context ctx) {
        super.attachBaseContext(ZeTarget.attachBaseContext(ctx,this));
    }

}
