package com.mad41.tripreminder.trip_ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.mad41.tripreminder.R;
import com.mad41.tripreminder.constants.Constants;
import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddTrip extends AppCompatActivity {

    public static final String TAG = "room";
    EditText txt_place;
    TextView txtDate;
    TextView txtTtime;
    CircleImageView btnDate;
    CircleImageView btnTime;
    int t1Hour, t1Minuite;
    private int mYear, mMonth, mDay;
    private MyRoomDataBase dataBaseInstance;

    public final static String START = "START";
    public final static String END = "END";

    int AUTOCOMPLETE_REQUEST_CODE_START = 1;
    int AUTOCOMPLETE_REQUEST_CODE2_END = 2;
    EditText txt_start;
    EditText txt_end;
    Button btn_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        Places.initialize(getApplication().getBaseContext(), "AIzaSyA7dH75J8SZ0-GkeHqHANbflPhdpbfU5yI");

        txtDate = (TextView) findViewById(R.id.txt_date);
        txtTtime = (TextView) findViewById(R.id.txt_time);
        txt_place = (EditText) findViewById(R.id.txt_place);

        txt_start = (EditText) findViewById(R.id.txt_startPlace);
        txt_end = (EditText) findViewById(R.id.txt_endPlace);

        btnDate = (CircleImageView) findViewById(R.id.btn_date);
        btnTime = (CircleImageView) findViewById(R.id.btn_time);
        btn_place = (Button) findViewById(R.id.btn_addTrip);
        dataBaseInstance = MyRoomDataBase.getUserDataBaseInstance(this);

        btn_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trip myTrip = new Trip(txt_place.getText().toString(), txt_start.getText().toString(), txt_end.getText().toString(),
                        txtTtime.getText().toString(), txtDate.getText().toString(), Constants.TRIP_UPCOMING, true, true);
                new Thread() {
                    @Override
                    public void run() {
                        dataBaseInstance.tripDao().insertTrip(myTrip);
                        printTrip();
                    }
                }.start();

                finish();
                setAlram();
            }
        });

        selectDate();
        selectTime();
        End_trip();
        Start_trip();
    }

    void selectDate(){
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTrip.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText("Date: "+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                                txtDate.setText("Date: " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
    }

    void selectTime(){
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //initialize TimePicker Dialogue
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddTrip.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Intialize Hour and Minute
                        t1Hour = hourOfDay;
                        t1Minuite = minute;
                        //initialize Calender
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, t1Hour, t1Minuite);

                        txtTtime.setText("Time:" + DateFormat.format("hh:mm:aa", calendar));


                    }
                }, 12, 0, false
                );

                //Displayed previous Selected time
                timePickerDialog.updateTime(t1Hour, t1Minuite);
                timePickerDialog.show();
            }
        });
    }

    private void Start_trip() {

        txt_start.setFocusable(false);
        txt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields1 = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields1) //FullScreen
                        .build(getApplication().getBaseContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_START);
            }
        });
    }

    private void End_trip() {
        txt_end.setFocusable(false);
        txt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields1 = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.ID, Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields1) //FullScreen
                        .build(getApplication().getBaseContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE2_END);
            }
        });
    }

    private void printTrip() {

        new Thread() {
            @Override
            public void run() {
                ArrayList<Trip> trips = (ArrayList<Trip>) dataBaseInstance.tripDao().getUpcomingTrips();
              //  Log.i(TAG, "" + trips.get(3));
                Intent intentToCard=new Intent(AddTrip.this, UpcomingActivity.class);

                for(int i = 0; i < trips.size(); i++){
                    intentToCard.putExtra("name", trips.get(i).getName());
                    intentToCard.putExtra("date", trips.get(i).getDate().toString());
                    intentToCard.putExtra("time", trips.get(i).getTime().toString());
                    intentToCard.putExtra("start", trips.get(i).getStartLoacation());
                    intentToCard.putExtra("end", trips.get(i).getEndLoacation());
                    intentToCard.putExtra("status",String.valueOf(trips.get(i).getStatus()));

                }
                setResult(RESULT_OK,intentToCard);
                finish();


            }
        }.start();




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_START) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                txt_start.setText(place.getAddress());
                Toast.makeText(AddTrip.this,place.getLatLng()+"",Toast.LENGTH_LONG).show();

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;

        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE2_END) {

            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                txt_end.setText(place.getAddress());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                // Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setAlram() {
        long alarmTime = getAlarmTime();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent notifyIntent = new Intent(this, TransparentActivity.class);
            notifyIntent.putExtra(AddTrip.START,txt_start.getText().toString());
            notifyIntent.putExtra(END,txt_end.getText().toString());

            PendingIntent notifyPendingIntent = PendingIntent.getActivity(this,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+alarmTime,notifyPendingIntent);
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+5000,notifyPendingIntent);
            Log.i("alram what is this ",SystemClock.elapsedRealtime()+"");
        }
    }

    private long getAlarmTime() {

        long alarmTime = 0;
        Calendar calendar = Calendar.getInstance();
        //alarm time
        calendar.set(mYear, mMonth,  mDay, t1Hour, t1Minuite);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        String date = calendar.getTime().toString();
        //current time
        Calendar c = Calendar.getInstance();
        String datee = simpleDateFormat.format(c.getTime());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");
            LocalDateTime localDate = LocalDateTime.parse(date, formatter);
            alarmTime = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
            localDate = LocalDateTime.parse(datee, formatter);
            alarmTime = alarmTime - localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
            Log.i(TAG, "alarm " + alarmTime);
        }else{
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            try {
                Date mDate = simpleDateFormat1.parse(date);
                alarmTime = mDate.getTime();
                alarmTime = alarmTime - simpleDateFormat1.parse(datee).getTime();
                Log.i(TAG, "alarm " + alarmTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return alarmTime;
    }
}