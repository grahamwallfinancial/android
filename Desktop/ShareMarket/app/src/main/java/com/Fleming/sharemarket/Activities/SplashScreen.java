package com.Fleming.sharemarket.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.toshiba.sharemarket.R;

public class SplashScreen extends AppCompatActivity {
    private final int REQUEST_CODE_IME = 20;


    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler=new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // your code to start second activity. Will wait for 3 seconds before calling this method


                if (Build.VERSION.SDK_INT < 23) {
                    //.................get/_user_phone_imei number....................//
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();


                } else {

                    if (checkPermission()) {
                        //................hit the api................//
                        Log.e("seema", "dsgg");
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_LOGS}, REQUEST_CODE_IME);
                    }
                }



            }
        }, 1000);

    }


    private boolean checkPermission() {

        if ( checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED&& checkSelfPermission(Manifest.permission.READ_LOGS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_IME && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
//        if (requestCode == REQUEST_CODE_IME && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.e("seema", "1111");

            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);
            finish();

        }
    }
}
