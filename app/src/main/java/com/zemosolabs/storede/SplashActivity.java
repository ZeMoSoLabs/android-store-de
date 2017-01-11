package com.zemosolabs.storede;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zemosolabs.inapptextsdk.InAppText;


public class SplashActivity extends Activity implements View.OnClickListener {
    static final String GCM_SENDER_ID ="914500168484";
    static final String ZETARGET_API_KEY = "ZemosoL";
    private static final String MY_URL = "http://api.zetarget.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ZeTarget.setZeTargetURL(MY_URL);
//        ZeTarget.initializeWithContextAndKey(getApplicationContext(), ZETARGET_API_KEY, GCM_SENDER_ID);
//        ZeTarget.disableDebugging();
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
    protected void onResume() {
        InAppText.enableDebugging();
        InAppText.enableAudio();
        InAppText.initializeInAppText(this);
        super.onResume();
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

//    @Override
//    protected void attachBaseContext(Context ctx) {
//        super.attachBaseContext(ZeTarget.attachBaseContext(ctx,this));
//    }

}
