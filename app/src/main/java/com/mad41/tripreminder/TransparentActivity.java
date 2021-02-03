package com.mad41.tripreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mad41.tripreminder.constants.Constants;
import com.mad41.tripreminder.room_database.MyRoomDataBase;

public class TransparentActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private Intent incomingIntent;
    private static final String CHANNEL_ID = "SERVICE_CHANNEL_ID";
    private MyRoomDataBase myRoomDataBase;
    private String start;
    private String end;
    private int tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        incomingIntent = getIntent();
        myRoomDataBase = MyRoomDataBase.getUserDataBaseInstance(this);
        start = incomingIntent.getStringExtra(HomeActivity.START);
        end = incomingIntent.getStringExtra(HomeActivity.END);
        tripId = incomingIntent.getIntExtra(HomeActivity.ID, 0);
        Log.i("location", incomingIntent.getAction() + "");
        Log.i("location", start);
        Log.i("location", end);
        Log.i("room", "incoming id " + tripId);
        showAlert();
    }

    private void showAlert() {
        builder = new AlertDialog.Builder(this);
        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to start your trip now?").setCancelable(false).setTitle("Reminder")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startTrip();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                myRoomDataBase.tripDao().updateStatus(tripId, Constants.TRIP_CANCELED);
                            }
                        }).start();
                        finish();
                    }
                }).setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showNotification();
                finish();
            }
        });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.show();
    }

    private void startTrip() {
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri openMaps = Uri.parse("http://maps.google.com/maps?daddr=" + Uri.encode(end) + " &dirflg=d");
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, openMaps);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        // check if there's at least one app can open that intent
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent);
        } else {
            Toast.makeText(TransparentActivity.this, "There's an issue opening google maps", Toast.LENGTH_SHORT).show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                myRoomDataBase.tripDao().updateStatus(tripId, Constants.TRIP_DONE);
            }
        }).start();
        finish();

    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, TransparentActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Reminder", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("start your trip");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID).
                    setSmallIcon(R.drawable.notification_icone).
                    setContentTitle("Reminder").setAutoCancel(true).
                    setContentText("Start your trip").setOngoing(true).
                    setContentIntent(pendingIntent);
            Notification not = builder.build();
            notificationManager.notify(1,not);
        }
    }
}