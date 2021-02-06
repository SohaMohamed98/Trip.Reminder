package com.mad41.tripreminder.trip_ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;


import com.mad41.tripreminder.AddTripFragments;
import com.mad41.tripreminder.OnGoingFrag;
import com.mad41.tripreminder.R;

import java.util.Calendar;

public class RoundTripDialogue extends AppCompatDialogFragment {

    TextView txt_date_dialogue;
    TextView txt_time_dialogue;
    ImageButton btn_date_dialogue;
    ImageButton btn_time_dialogue;


    String date, time;

    private int t1Hour, t1Minuite;
    private int mYear, mMonth, mDay;

    String place, start, end, ftime, fdate;


    public RoundTripDialogue() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_round_trip_dialogue, null);

        if (getArguments() != null) {
            place = getArguments().getString("savePlace");
            start = getArguments().getString("saveStart");
            end = getArguments().getString("saveEnd");
            ftime = getArguments().getString("saveTime");
            fdate = getArguments().getString("saveDate");


        }


        txt_date_dialogue = view.findViewById(R.id.txt_date_dialogue);
        txt_time_dialogue = view.findViewById(R.id.txt_time_dialogue);

        btn_date_dialogue = (ImageButton) view.findViewById(R.id.img_date_dialogue);
        btn_time_dialogue = (ImageButton) view.findViewById(R.id.img_time_dialogue);

        setDate();
        setTime();

        builder.setView(view)
                .setTitle("Round")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        date = txt_date_dialogue.getText().toString();
                        time = txt_date_dialogue.getText().toString();
                   /*     Bundle bundle = new Bundle();
                        bundle.putString("return_date", date);
                        bundle.putString("return_time", time);
                        bundle.putString("replace", place);
                        bundle.putString("restart",start);
                        bundle.putString("reend", end);
                        bundle.putString("redate", fdate);
                        bundle.putString("retime", ftime);
                        AddTripFragments addTripFragments = new AddTripFragments();
                        addTripFragments.setArguments(bundle);
                        FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                        FragmentTransaction  transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.dynamicFrag, addTripFragments,"frag");
                        transaction.commit();*/

                    }
                });

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("myDate", date);
        outState.putString("myTime", time);
    }

    void setDate() {
        btn_date_dialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                                txt_date_dialogue.setText("Date: " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

    }

    void setTime() {
        btn_time_dialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize TimePicker Dialogue
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Intialize Hour and Minute
                        t1Hour = hourOfDay;
                        t1Minuite = minute;
                        //initialize Calender
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0, 0, 0, t1Hour, t1Minuite);

                        txt_time_dialogue.setText("Time:" + DateFormat.format("hh:mm:aa", calendar));


                    }
                }, 12, 0, false
                );

                //Displayed previous Selected time
                timePickerDialog.updateTime(t1Hour, t1Minuite);
                timePickerDialog.show();

            }
        });
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        // listener = (DialogListener) childFragment;
    }

}