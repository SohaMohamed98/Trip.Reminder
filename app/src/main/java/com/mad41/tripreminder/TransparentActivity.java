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
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.mad41.tripreminder.constants.Constants;
import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.room_database.view_model.TripViewModel;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransparentActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private Intent incomingIntent;
    private static final String CHANNEL_ID = "SERVICE_CHANNEL_ID";
    private MyRoomDataBase myRoomDataBase;
    private String start;
    private String end;
    private int tripId;
    private MediaPlayer mMediaPlayer;
    private int repeated;
    private long interval;
    CardView cardView;
    private TripViewModel tripViewModel;
    private int t1Hour, t1Minuite;
    private int mYear, mMonth, mDay;
    private String time, date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        incomingIntent = getIntent();
        myRoomDataBase = MyRoomDataBase.getUserDataBaseInstance(this);

        tripViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(TripViewModel.class);
//        Trip finishedTrip = (Trip) Parcels.unwrap(incomingIntent.getParcelableExtra(Constants.TRIP));

//        Log.i("room","Triped received safely "+finishedTrip.getId()+" "+finishedTrip.getName());
        end = incomingIntent.getStringExtra(Constants.END);
        tripId = incomingIntent.getIntExtra(Constants.ID, 0);
        repeated = incomingIntent.getIntExtra(Constants.REPEATED,0);

        if(repeated!=0){
            ArrayList<String> strlist = new ArrayList<>();
            strlist.add("mmmm");
            Trip trip = new Trip("fake trip","fake trip","fake trip","12:00:AM","7-2-2021",strlist,2,false,false);
            String name = trip.getName();
            String start = trip.getStartLoacation();
            String end = trip.getEndLoacation();
            time = trip.getTime();
            date = trip.getDate();
            ArrayList<String> strlis2 = trip.getNotes();
            Trip realTrip = new Trip(name,start,end,time,date,strlis2,2,false,false);

            addNextTrip(repeated,realTrip);
        }
        Log.i("location", incomingIntent.getAction() + "");
        Log.i("room", "incoming id " + tripId);
        showAlert();

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.sound1);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.start();
    }



    private void addNextTrip(int repeated,Trip newTrip) {
        long alarmTime;
        String[] timee = time.split(":");
        t1Hour = Integer.parseInt(timee[0]);
        t1Minuite = Integer.parseInt(timee[1]);
        String amPm = timee[2];
        if(amPm.equals("PM")){t1Hour=t1Hour+12;}
//            String[] datee = day.split(" ");
        String[] datee = date.split("-");
        mDay=Integer.parseInt(datee[0]);
        mMonth=Integer.parseInt(datee[1])-1;
        mYear=Integer.parseInt(datee[2]);

        Calendar calendar = Calendar.getInstance();
        long l = calendar.getTimeInMillis();

        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.HOUR_OF_DAY, t1Hour);
        calendar.set(Calendar.MINUTE, t1Minuite);
        calendar.set(Calendar.SECOND, 0);

        switch (repeated){
            case 1:
                calendar.set(Calendar.MONTH, mMonth);
                calendar.set(Calendar.DAY_OF_MONTH, mDay+1);
                break;
            case 2:
                calendar.set(Calendar.MONTH, mMonth);
                calendar.set(Calendar.DAY_OF_MONTH, mDay+7);
                break;
            case 3:
                calendar.set(Calendar.MONTH, mMonth+1);
                calendar.set(Calendar.DAY_OF_MONTH, mDay);
                break;
        }
        Log.i("room", "alarm date " + calendar.getTime());
        alarmTime = calendar.getTimeInMillis() - l;
        int id;
        id = (int) tripViewModel.insert(newTrip);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent notifyIntent = new Intent(this, TransparentActivity.class);
            notifyIntent.putExtra(Constants.END, end);
            Log.i("room", "repeated id " + id);
            notifyIntent.putExtra(Constants.ID, id);
            notifyIntent.putExtra(Constants.REPEATED, repeated);

            notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + alarmTime, notifyPendingIntent);
            Log.i("room", " system realtime "+SystemClock.elapsedRealtime());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();
    }

    private void showAlert() {
        builder = new AlertDialog.Builder(this);
        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to start your trip now?").setCancelable(false).setTitle("Reminder")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startTrip();
//                        if(repeated){
//                            updateDate(interval);
//                        }
                        finish();
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

//    private void updateDate(long interval) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                myRoomDataBase.tripDao().updateStatus(tripId, Constants.TRIP_UPCOMING);
//                //update date
//            }
//        }).start();
//    }

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