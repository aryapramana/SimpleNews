package com.software.tempe.simplenews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread loading = new Thread() {
            public void run() {
                try {
                    sleep(2500);
                    Intent main = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(main);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };

        loading.start();
    }
}
