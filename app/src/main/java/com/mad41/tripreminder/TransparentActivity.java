package com.mad41.tripreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mad41.tripreminder.constants.Constants;

import com.mad41.tripreminder.floating_bubble.FloatingViewService;
import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.room_database.view_model.TripViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransparentActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private Intent incomingIntent;
    private static final String CHANNEL_ID = "SERVICE_CHANNEL_ID";
    private String end;
    private int tripId;
    private MediaPlayer mMediaPlayer;
    private int repeated;
    private TripViewModel tripViewModel;
    private String time, date;
    private Trip trip;
    private Calendar calendar;
    private String comingPage;
    private boolean comingBoolean;
    private int mDay, mMonth, mYear, t1Hour, t1Minuite;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String preferenceId, preferenceBoolean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);

        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
//        preferenceBoolean = sharedPreferences.getString(Constants.preferenceBoolean,"false");
        calendar = Calendar.getInstance();
        Log.i("room", "alarm date before switch " + calendar.getTime());

        incomingIntent = getIntent();
        comingBoolean = true;
        tripViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(TripViewModel.class);
        tripId = incomingIntent.getIntExtra(Constants.ID, 0);

        if(tripId==0){
//            if(preferenceBoolean.equals("false")){

//            }else{
                preferenceId = sharedPreferences.getString(Constants.preferenceId,"0");
                tripId = Integer.parseInt(preferenceId);
                trip = tripViewModel.getTripById(tripId);
                end = trip.getEndLoacation();
                repeated = trip.isRound();
//            }
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer = MediaPlayer.create(this, R.raw.sound1);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.start();
            showAlert();
        }else{
            editor.putString(Constants.preferenceId,tripId+"");
//            editor.putString(Constants.preferenceBoolean,"true");
            editor.commit();
            trip = tripViewModel.getTripById(tripId);
            end = trip.getEndLoacation();
            repeated = trip.isRound();
            Log.i("room"," "+incomingIntent);

            comingPage = incomingIntent.getStringExtra(Constants.START);
            Log.i("room"," "+incomingIntent.getStringExtra(Constants.START));
            if(comingPage.equals(Constants.START)){
                comingBoolean=false;
            }
            Log.i("room", "incoming id " + tripId);
            Log.i("room", "incoming repeated " + repeated);

            if(comingBoolean){
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer = MediaPlayer.create(this, R.raw.sound1);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.start();
                showAlert();
            }else{
                startBubble();
                startTrip();
                finish();
            }
        }

    }

    private void addNextTrip() {
        long alarmTime = setDate();
        String name = trip.getName();
        String start = trip.getStartLoacation();
        ArrayList<String> strlist = trip.getNotes();
        Trip realTrip = new Trip(name,start,end,time,date,strlist,2,false,repeated);
        int id;
        id = (int) tripViewModel.insert(realTrip);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent notifyIntent = new Intent(this, TransparentActivity.class);
            Log.i("room", "repeated id " + id);
            notifyIntent.putExtra(Constants.ID, id);
            notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + alarmTime, notifyPendingIntent);
            Log.i("room", " system realtime "+SystemClock.elapsedRealtime());
        }
    }

    private long setDate() {
        String[] timee = time.split(":");
        t1Hour = Integer.parseInt(timee[0]);
        t1Minuite = Integer.parseInt(timee[1]);
        String[] datee = date.split("-");
        mDay=Integer.parseInt(datee[0]);
        mMonth=Integer.parseInt(datee[1])-1;
        mYear=Integer.parseInt(datee[2]);
        Log.i("room", "yea month day " + mYear+" "+mMonth+" "+mDay);

        long alarmTime, now;
        calendar.set(Calendar.SECOND, 0);
        Log.i("room", "alarm date before switch " + calendar.getTime());
        now = calendar.getTimeInMillis();
        switch (repeated){
            case 1:
                Log.i("room","case 1 : "+Calendar.DAY_OF_YEAR);
//                calendar.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH+1);
                calendar.set(mYear,mMonth,mDay+1,t1Hour,t1Minuite);
                break;
            case 2:
                Log.i("room","day of year : "+Calendar.DAY_OF_YEAR);
//                calendar.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH+7);
                calendar.set(mYear,mMonth,mDay+7,t1Hour,t1Minuite);
                break;
            case 3:
                Log.i("room","case 3 : "+Calendar.DAY_OF_YEAR);
//                calendar.set(Calendar.MONTH, Calendar.MONTH);//don't need to add 1 because second one take index from 0
                calendar.set(mYear,mMonth+1,mDay,t1Hour,t1Minuite);
                break;
        }
        Log.i("room", "alarm date from transparent activity " + calendar.getTime());
        alarmTime = calendar.getTimeInMillis() - now;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //get date string that will be added to the new trip in database and card
            String format = "dd-MM-YYYY";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date today = calendar.getTime();
            date = dateFormat.format(today);
            Log.i("room","today is : "+date);
        }
        return alarmTime;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();
    }

    private void showAlert() {
        builder = new AlertDialog.Builder(this);
        //Setting message manually and performing action on button click
        builder.setMessage(R.string.startTripNow).setCancelable(false).setTitle(R.string.reminder)
                .setPositiveButton(R.string.startTrip, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startTrip();
                        startBubble();
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancelTrip, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        tripViewModel.updateStatus(tripId,Constants.TRIP_CANCELED);
                        editor.putString(Constants.preferenceBoolean,"false");
                        editor.commit();

                        if(repeated!=0){
                            time = trip.getTime();
                            date = trip.getDate();
                            addNextTrip();
                            Log.i("room", "date from trip " + date);
                        }
                        finish();
                    }
                }).setNeutralButton(R.string.snooze, new DialogInterface.OnClickListener() {
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
        editor.putString(Constants.preferenceBoolean,"false");
        editor.commit();

        if(repeated!=0){
            time = trip.getTime();
            date = trip.getDate();
            addNextTrip();
            Log.i("room", "date from trip " + date);
        }
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri openMaps = Uri.parse("http://maps.google.com/maps?daddr=" + Uri.encode(end) + " &dirflg=d");
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, openMaps);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        // check if there's at least one app can open that intent
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            // Attempt to start an activity that can handle the Intent
            tripViewModel.updateStatus(tripId,Constants.TRIP_DONE);
            startActivity(mapIntent);
        } else {
            Toast.makeText(TransparentActivity.this, "There's an issue opening google maps", Toast.LENGTH_SHORT).show();
        }
    }

    private void startBubble(){
        //Check if the application has draw over other apps permission or not?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
//            Toast.makeText(this,"Hello Button cliced", Toast.LENGTH_LONG).show();
            initializeView();
        }
    }

    private void initializeView() {
       // Toast.makeText(this,"before service started", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this, FloatingViewService.class);
        intent.putExtra("TRIP_ID",tripId);
        Toast.makeText(this,"Trans: "+tripId, Toast.LENGTH_LONG).show();
        startService(intent);
       // Toast.makeText(this,"after service started", Toast.LENGTH_LONG).show();
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,"Draw over other app permission not available. Closing the application",Toast.LENGTH_SHORT).show();
            //    finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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