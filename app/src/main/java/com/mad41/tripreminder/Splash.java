package com.mad41.tripreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
                    SharedPreferences Read = getSharedPreferences("userAuth" , Context.MODE_PRIVATE);
                    String user_node = Read.getString("userMode","false");
                    String user_id = Read.getString("userId","false");
                    if(user_node.equals("false")) {
                        Intent i = new Intent(getApplicationContext(), Login_form.class);
                        startActivity(i);
                        //remove Activity
                        finish();
                    }else{
                        Intent Main = new Intent(getApplicationContext(), MainScreen.class);
                        Main.putExtra("userID",user_id);
                        startActivity(Main);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splash.start();
    }
}