package com.mad41.tripreminder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProviders;

import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.mad41.tripreminder.constants.Constants;
import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.trip_ui.AddNoteAdapter;
import com.mad41.tripreminder.trip_ui.NoteAdapter;
import com.mad41.tripreminder.room_database.view_model.TripViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddTripFragments extends Fragment {

    public static final String TAG = "room";
    public final static String START = "START";
    public final static String END = "END";
    public static final String ID = "ID";
    //Radio
    Switch repeat_switch;
    RadioGroup radioGroup;
    RadioButton radio_btn_day;
    RadioButton radio_btn_month;
    RadioButton radio_btn_week;

    RadioButton radio_btn_year;
    //Notes
    ArrayList<String> myNotes;
    RecyclerView recyclerViewNote;
    RelativeLayout relativeLayoutNote;
    AddNoteAdapter addNoteAdapter;
    NoteAdapter noteAdapter;
    RecyclerView.LayoutManager layoutManager;
    ImageButton btn_add_note;
    ImageButton btn_date_round;
    ImageButton btn_time_round;
    TextView txt_date_round;
    TextView txt_time_round;
    Switch roundSwitch;
    EditText txt_place;
    TextView txt_date;
    TextView txt_time;
    CircleImageView btnDate;
    CircleImageView btnTime;
    int t1Hour, t1Minuite;
    private int mYear, mMonth, mDay;
    private MyRoomDataBase dataBaseInstance;

    int t1Hour2, t1Minuite2;
    private int mYear2, mMonth2, mDay2;


    private int id;

    //Return Date and Time
    String round_date;
    String round_time;
    int AUTOCOMPLETE_REQUEST_CODE_START = 1;
    int AUTOCOMPLETE_REQUEST_CODE2_END = 2;
    EditText txt_start;
    EditText txt_end;
    Button btn_place;
    Context context;
    ArrayList<Trip> arrayList;
    // private int id;
    private int updatedID;
    private Communicator communicatorListener;
    private TripViewModel tripViewModel;


    public AddTripFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        communicatorListener = (Communicator) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_trip_fragments, container, false);
        Places.initialize(getContext().getApplicationContext(), "AIzaSyA7dH75J8SZ0-GkeHqHANbflPhdpbfU5yI");

        //radio buttons
        radioGroup = view.findViewById(R.id.radio_group);
        radio_btn_day = view.findViewById(R.id.radio_btn_day);
        radio_btn_month = view.findViewById(R.id.radio_btn_week);
        radio_btn_week = view.findViewById(R.id.radio_btn_week);
        repeat_switch = view.findViewById(R.id.repeat_switch);

        txt_date_round = view.findViewById(R.id.txt_date_round);
        txt_time_round = view.findViewById(R.id.txt_time_round);
        btn_date_round = view.findViewById(R.id.btn_date_round);
        btn_time_round = view.findViewById(R.id.btn_time_round);

        //Notes
        btn_add_note = view.findViewById(R.id.btn_add_note);
        recyclerViewNote = view.findViewById(R.id.recyclerNote);
        relativeLayoutNote = view.findViewById(R.id.relativeLayout);
        myNotes = new ArrayList<String>();
        layoutManager = new LinearLayoutManager(context);
        addNoteAdapter = new AddNoteAdapter(context, myNotes);
        //  noteAdapter = new NoteAdapter(context, myNotes);
        recyclerViewNote.setAdapter(addNoteAdapter);
        recyclerViewNote.setLayoutManager(layoutManager);

        btn_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myNotes.add("");
                addNoteAdapter.notifyDataSetChanged();
            }
        });


        txt_date = (TextView) view.findViewById(R.id.txt_date);
        txt_time = (TextView) view.findViewById(R.id.txt_time);
        txt_place = (EditText) view.findViewById(R.id.txt_place);

        txt_start = (EditText) view.findViewById(R.id.txt_startPlace);
        txt_end = (EditText) view.findViewById(R.id.txt_endPlace);

        btnDate = (CircleImageView) view.findViewById(R.id.btn_date);
        btnTime = (CircleImageView) view.findViewById(R.id.btn_time);
        btn_place = (Button) view.findViewById(R.id.btn_addTrip);

        roundSwitch = (Switch) view.findViewById(R.id.round_switch);
        dataBaseInstance = MyRoomDataBase.getUserDataBaseInstance(getContext().getApplicationContext());

        tripViewModel = ViewModelProviders.of(requireActivity()).get(TripViewModel.class);
        Bundle bundle = getArguments();

        if (bundle != null) {
            btn_place.setText("update Trip");
            Trip trip = bundle.getParcelable("trip");
            updatedID = trip.getId();
            txt_date.setText(trip.getDate());
            txt_time.setText(trip.getTime());
            txt_place.setText(trip.getName());
            txt_start.setText(trip.getStartLoacation());
            txt_end.setText(trip.getStartLoacation());

            ArrayList<String> Notes = trip.getNotes();
            for(String note :Notes){
                btn_add_note.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myNotes.add(note);
                        addNoteAdapter.notifyDataSetChanged();
                    }
                });
                btn_add_note.performClick();
            }

            String time = txt_time.getText().toString();
            String day = txt_date.getText().toString();
            Log.i("room","string time: '"+time+"'");
            Log.i("room","string time: '"+day+"'");

            String[] timee = time.split(":");
            t1Hour = Integer.parseInt(timee[0]);
            t1Minuite = Integer.parseInt(timee[1]);
            String amPm = timee[2];
            if(amPm.equals("PM")){t1Hour=t1Hour+12;}
//            String[] datee = day.split(" ");
            String[] datee = day.split("-");
            mDay=Integer.parseInt(datee[0]);
            mMonth=Integer.parseInt(datee[1])-1;
            mYear=Integer.parseInt(datee[2]);
            Log.i("room"," time returned "+mYear+" "+mMonth+" "+mDay+" "+t1Hour+" "+t1Minuite);
//            Log.i("room"," time returned "+'"'+datee[0]+'"'+"  "+'"'+datee[1]+'"'+"  "+'"'+datee[2]+'"');

            addNoteAdapter.setNotes(trip.getNotes());
        }


        btn_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("room"," time before check "+mYear+" "+mMonth+" "+mDay+" "+t1Hour+" "+t1Minuite);
                long alarmTime = getAlarmTime(mYear,mMonth,mDay,t1Hour,t1Minuite);
                long alarmTimeRound=0;
                int reapeated = checkRepeated();
                Log.i("room"," repeated "+reapeated);
                //checking trip time
//                if (alarmTime > 0) {
                    //checking time for round trip
                    if(roundSwitch.isChecked()){
                        alarmTimeRound=getAlarmTime(mYear2,mMonth2,mDay2,t1Hour2,t1Minuite2);
                        if(alarmTimeRound<alarmTime){
                            Toast.makeText(getContext(), "Please enter a valid coming back date & time", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    //adding or editing trip
                    ArrayList<String> strlist = new ArrayList<>();
                    strlist.add("mmmm");
                    Trip myTrip = new Trip(txt_place.getText().toString(), txt_start.getText().toString(), txt_end.getText().toString(),
                            txt_time.getText().toString(), txt_date.getText().toString(), myNotes, Constants.TRIP_UPCOMING, true, true);
                    Log.i("room","switch state "+roundSwitch.isChecked());
                    if (getArguments() == null) {
                        id = (int) tripViewModel.insert(myTrip);
                    } else {
                        myTrip.setId(updatedID);
                        tripViewModel.update(myTrip);
                        id = updatedID;
                    }
                    Log.i("room", "id is: " + id);

                    communicatorListener.setAlarm(alarmTime,id,txt_end.getText().toString(),reapeated,myTrip);

                    //adding or editing round trip
                    if(roundSwitch.isChecked()){
                            Trip myTripRound = new Trip(txt_place.getText().toString(), txt_end.getText().toString(), txt_start.getText().toString(),
                                    txt_time_round.getText().toString(), txt_date_round.getText().toString(), myNotes, Constants.TRIP_UPCOMING, true, true);
                            if (getArguments() == null) {
                                id = (int) tripViewModel.insert(myTripRound);
                            } else {
                                myTripRound.setId(updatedID);
                                tripViewModel.update(myTripRound);
                                id = updatedID;
                            }
                            Log.i("room", "id is: " + id);

                            communicatorListener.setAlarm(alarmTimeRound,id, txt_start.getText().toString(),reapeated,myTripRound);
                    }

                    myData();

//                } else {
//                    Toast.makeText(getContext(), "Please enter a valid date & time", Toast.LENGTH_SHORT).show();
//                }
                //print_Notes
                for (int i = 0; i < myNotes.size(); i++) {
                    Toast.makeText(getContext(), myNotes.get(i), Toast.LENGTH_LONG).show();
                }
            }
        });

        selectDate();
        selectTime();
        Start_trip();
        End_trip();
        setRound();


        return view;
    }

    private int checkRepeated() {
        int repeatCase = 0;
        if(repeat_switch.isChecked()){
            switch (radioGroup.getCheckedRadioButtonId()){
                case R.id.radio_btn_day:
                    repeatCase = 1;
                    break;
                case R.id.radio_btn_week:
                    repeatCase = 2;
                    break;
                case R.id.radio_btn_month:
                    repeatCase = 3;
                    break;
            }
        }
        return repeatCase;
    }

    void setRound() {
        roundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    //openDialog();
                    btn_date_round.setVisibility(View.VISIBLE);
                    btn_time_round.setVisibility(View.VISIBLE);
                    txt_date_round.setVisibility(View.VISIBLE);
                    txt_time_round.setVisibility(View.VISIBLE);

                    btn_date_round.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get Current Date
                            final Calendar c = Calendar.getInstance();
                            mYear2 = c.get(Calendar.YEAR);
                            mMonth2 = c.get(Calendar.MONTH);
                            mDay2 = c.get(Calendar.DAY_OF_MONTH);


                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {
                                            mYear2 = year;
                                            mMonth2 = monthOfYear;
                                            mDay2 = dayOfMonth;
                                            txt_date_round.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                        }
                                    }, mYear2, mMonth2, mDay2);
                            datePickerDialog.show();

                        }
                    });

                    btn_time_round.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //initialize TimePicker Dialogue
                            TimePickerDialog timePickerDialog = new TimePickerDialog(
                                    getContext(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    //Intialize Hour and Minute
                                    t1Hour2 = hourOfDay;
                                    t1Minuite2 = minute;
                                    //initialize Calender
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(0, 0, 0, t1Hour2, t1Minuite2);

                                    txt_time_round.setText(DateFormat.format("hh:mm:aa", calendar));


                                }
                            }, 12, 0, false
                            );

                            //Displayed previous Selected time
                            timePickerDialog.updateTime(t1Hour2, t1Minuite2);
                            timePickerDialog.show();

                        }
                    });


                } else {

                    btn_date_round.setVisibility(View.GONE);
                    btn_time_round.setVisibility(View.GONE);
                    txt_date_round.setVisibility(View.GONE);
                    txt_time_round.setVisibility(View.GONE);
                }
            }
        });
    }


    private long getAlarmTime(int year, int month, int day, int hr, int minute) {
        Calendar calendar1 = Calendar.getInstance();
        long l = calendar1.getTimeInMillis();
        Log.i("room", " check calendar " + l);
        Calendar calendar = Calendar.getInstance();
        //alarm time
//        calendar.set(mYear, mMonth, mDay, t1Hour, t1Minuite,0);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
//        Log.i("room", " check calendar alarm date " + calendar.getTime());
//        calendar.set(Calendar.DAY_OF_MONTH, day+30);
        Log.i("room", " check calendar alarm date " + calendar.getTime());
        Log.i("room", "check real time"+SystemClock.elapsedRealtime());
        Log.i("room"," time before check "+year+" "+month+" "+mDay+" "+hr+" "+minute);
        long alarmTime = calendar.getTimeInMillis() - l;
//        Calendar c3 = Calendar.getInstance();
//        Log.i("room", " check new calendar " + c3.getTime());

        Log.i("room", " check " + alarmTime);
        return alarmTime;
    }

    void selectDate() {
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txt_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                                txt_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
    }

    void selectTime() {
        btnTime.setOnClickListener(new View.OnClickListener() {
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

                        txt_time.setText(DateFormat.format("hh:mm:aa", calendar));


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
                        .build(getContext().getApplicationContext());
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
                        .build(getContext().getApplicationContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE2_END);
            }
        });
    }

    void myData() {
        communicatorListener.returnToOnGoingActivity();
    }


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        context = activity;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_START) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                txt_start.setText(place.getAddress());
                Toast.makeText(getContext(), place.getLatLng() + "", Toast.LENGTH_LONG).show();

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;

        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE2_END) {

            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                txt_end.setText(place.getAddress());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                // Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void getArrayList(ArrayList<Trip> arrayList) {
        this.arrayList = arrayList;
    }

    public interface Communicator {
        void setAlarm(long alarmTime, int id, String end, int repeatInterval,Trip trip);

        void returnToOnGoingActivity();
    }


}
