package com.mad41.tripreminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    TextView txtDate;
    TextView txtTtime;
    CircleImageView btnDate;
    CircleImageView btnTime;
    int t1Hour, t1Minuite, t2Hour, t2Minuite;
    private int mYear, mMonth, mDay, mHour, mMinute;



    AutocompleteSupportFragment autocompleteFragmentStart;
    AutocompleteSupportFragment autocompleteFragmentEnd;
    String TAG = "lifeCycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Places.initialize(getApplication().getBaseContext(), "AIzaSyA7dH75J8SZ0-GkeHqHANbflPhdpbfU5yI");

        autocomplete_fragmentStart();
        autocomplete_fragmentEnd();
        txtDate = (TextView) findViewById(R.id.txt_date);
        txtTtime = (TextView) findViewById(R.id.txt_time);

        btnDate = (CircleImageView) findViewById(R.id.btn_date);
        btnTime = (CircleImageView) findViewById(R.id.btn_time);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText("Date: " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

btnTime.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

      //initialize TimePicker Dialogue
               TimePickerDialog timePickerDialog= new TimePickerDialog(
                         HomeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                     @Override
                     public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                         //Intialize Hour and Minute
                         t1Hour= hourOfDay;
                         t1Minuite=minute;
                         //initialize Calender
                         Calendar calendar=Calendar.getInstance();
                         calendar.set(0,0,0,t1Hour,t1Minuite);

                         txtTtime.setText("Time: " + DateFormat.format("hh:mm:aa",calendar));


                     }
                 },12,0,false
                 );

                 //Displayed previous Selected time
                 timePickerDialog.updateTime(t1Hour,t1Minuite);
                 timePickerDialog.show();
             }
         });


    }


    public void autocomplete_fragmentStart() {
        autocompleteFragmentStart = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_start);

        autocompleteFragmentStart.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragmentStart.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


    public void autocomplete_fragmentEnd() {
        autocompleteFragmentEnd = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_end);

        autocompleteFragmentEnd.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragmentEnd.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

}