package com.mad41.tripreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mad41.tripreminder.Login_form;
import com.mad41.tripreminder.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread splash = new Thread()
        {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2*1000);
                    Intent i = new Intent(getApplicationContext(), Login_form.class);
                    startActivity(i);
                    //remove Activity
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splash.start();
    }
}