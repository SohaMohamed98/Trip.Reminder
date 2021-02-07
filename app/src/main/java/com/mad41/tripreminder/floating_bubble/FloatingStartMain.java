package com.mad41.tripreminder.floating_bubble;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FloatingStartMain extends AppCompatActivity {
    Button showBubble;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_floating_start_main);
        //showBubble=findViewById(R.id.btn_start_floating);



  showBubble.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startBubble();
    }
});
    }

    private void startBubble(){
        //Check if the application has draw over other apps permission or not?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            Toast.makeText(this,"Hello Button cliced", Toast.LENGTH_LONG).show();
             initializeView();
        }
    }

    private void initializeView() {
                startService(new Intent(this, FloatingViewService.class));
                finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,"Draw over other app permission not available. Closing the application",Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
