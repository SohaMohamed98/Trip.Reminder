package com.mad41.tripreminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    TextView txt_place;
    TextView txtDate;
    TextView txtTtime;
    CircleImageView btnDate;
    CircleImageView btnTime;
    int t1Hour, t1Minuite, t2Hour, t2Minuite;
    private int mYear, mMonth, mDay, mHour, mMinute;


    int AUTOCOMPLETE_REQUEST_CODE = 1;
    int AUTOCOMPLETE_REQUEST_CODE2 = 2;
    EditText txt_start;
    EditText txt_end;
    Button btn_place;

    AutocompleteSupportFragment autocompleteFragmentStart;
    AutocompleteSupportFragment autocompleteFragmentEnd;
    String TAG = "lifeCycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Places.initialize(getApplication().getBaseContext(), "AIzaSyA7dH75J8SZ0-GkeHqHANbflPhdpbfU5yI");



        txtDate = (TextView) findViewById(R.id.txt_date);
        txtTtime = (TextView) findViewById(R.id.txt_time);
        txt_place = (TextView) findViewById(R.id.txt_place);

        txt_start=(EditText)findViewById(R.id.txt_startPlace) ;
        txt_end=(EditText)findViewById(R.id.txt_endPlace);

        btnDate = (CircleImageView) findViewById(R.id.btn_date);
        btnTime = (CircleImageView) findViewById(R.id.btn_time);
        btn_place=(Button) findViewById(R.id.btn_addTrip) ;


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
        End_trip();
        Start_trip();



    }



    private void Start_trip() {
        txt_start.setFocusable(false);
        txt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields1= Arrays.asList(Place.Field.ID,Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields1) //FullScreen
                        .build(getApplication().getBaseContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
    }

    private void End_trip() {
        txt_end.setFocusable(false);
        txt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields1= Arrays.asList(Place.Field.ID,Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields1) //FullScreen
                        .build(getApplication().getBaseContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                txt_start.setText(place.getAddress());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;

        }else if(requestCode == AUTOCOMPLETE_REQUEST_CODE2) {

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



}