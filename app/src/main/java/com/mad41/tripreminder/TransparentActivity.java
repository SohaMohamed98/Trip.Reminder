package com.mad41.tripreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class TransparentActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    Intent incomingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        incomingIntent = getIntent();

        String start = incomingIntent.getStringExtra(HomeActivity.START);
        String end = incomingIntent.getStringExtra(HomeActivity.END);
        Log.i("location",incomingIntent.getAction()+"");
        Log.i("location",start);
        Log.i("location",end);


        builder = new AlertDialog.Builder(this);
        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to start your trip now?").setCancelable(false).setTitle("Reminder")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Create a Uri from an intent string. Use the result to create an Intent.
                        Uri openMaps = Uri.parse("http://maps.google.com/maps?daddr="+Uri.encode(end)+" &dirflg=d");
                        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, openMaps);
                        // Make the Intent explicit by setting the Google Maps package
                        mapIntent.setPackage("com.google.android.apps.maps");
                        // check if there's at least one app can open that intent
                        if(mapIntent.resolveActivity(getPackageManager()) != null){
                            // Attempt to start an activity that can handle the Intent
                            startActivity(mapIntent);
                        }else{
                            Toast.makeText(TransparentActivity.this, "There's an issue opening google maps", Toast.LENGTH_SHORT).show();
                        }

                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        finish();
                    }
                }).setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.show();

    }
}