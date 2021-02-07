package com.mad41.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.mad41.tripreminder.constants.Constants;
import com.mad41.tripreminder.Firebase.WriteHandler;

import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;


import com.mad41.tripreminder.room_database.view_model.TripViewModel;
import com.mad41.tripreminder.trip_ui.NoteReviewDialogue;
import com.mad41.tripreminder.trip_ui.TripModel;


import java.util.ArrayList;
import java.util.List;


public class MainScreen extends AppCompatActivity implements AddTripFragments.Communicator,
        OnGoingFrag.onGoingCommunicator {

    public static Context context;
    AddTripFragments fragment;
    List<Trip> trips;
    String name, start, end, date, time;
    ArrayList<String> notes;
    String dateDialogue;
    String timeDialogue;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private OnGoingFrag frag1;
    private HistoryFragment frag2;
    private FragmentManager mgr;
    private FragmentTransaction trns;
    private NavigationView drawerMenu;
    private TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        frag2 = new HistoryFragment();
        fragment = new AddTripFragments();
        notes = new ArrayList<String>();

        for (int i = 0; i < notes.size(); i++) {
            Toast.makeText(getApplication().getBaseContext(), notes.get(i), Toast.LENGTH_SHORT).show();
        }

        context = this;
        //we need the toolbar and drawer to show the menu button
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        //this will show the menu button
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawerMenu = findViewById(R.id.drawerMenu);
        setListener();

        if (savedInstanceState == null) {
            mgr = getSupportFragmentManager();
            trns = mgr.beginTransaction();
            frag1 = new OnGoingFrag();
            trns.replace(R.id.dynamicFrag, frag1);
            trns.commit();
            drawerMenu.setCheckedItem(R.id.btnOngoing);
        }
        tripViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(TripViewModel.class);


    }


    @Override
    public void startAddTripFragment(Bundle bundle) {
        mgr = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mgr.beginTransaction();

        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.dynamicFrag, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void setListener() {
        drawerMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.btnOngoing:
                        //if I used mgr from above it will crash if I rotated and then changed fragment, and if I used trns it will crash anyway because it's outside the listener
                        getSupportFragmentManager().beginTransaction().replace(R.id.dynamicFrag, new OnGoingFrag()).commit();
                        break;
                    case R.id.btnHistory:
                        getSupportFragmentManager().beginTransaction().replace(R.id.dynamicFrag, frag2).commit();
                        break;
                    case R.id.btnLanguage:
                        //LiveData<List<Trip>> trips= MyRoomDataBase.getUserDataBaseInstance(getApplicationContext()).tripDao().getAllTrips();
                        tripViewModel.getAllNotes().observe(MainScreen.this, new Observer<List<Trip>>() {
                            @Override
                            public void onChanged(List<Trip> trips) {
                                System.out.println(trips.get(0));
                                WriteHandler.WriteInfireBase(trips);
                            }
                        });

                        Toast.makeText(MainScreen.this, "show language dialog", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnExit:
                        logOut();
                        break;

                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
        builder.setMessage("Sure you want to log out?").setCancelable(false).setTitle("Log out")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //log out
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Login_form.class));
                        LoginManager.getInstance().logOut();
                        //clear database
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MyRoomDataBase myRoomDataBase = MyRoomDataBase.getUserDataBaseInstance(MainScreen.this);
                                myRoomDataBase.tripDao().deleteAllTrips();
                            }
                        }).start();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        //check if the drawer is open then the back button close the drawer first and not exit the activity directly
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (fragment.isVisible() || frag2.isVisible()) {
                returnToOnGoingActivity();
                //mgr.popBackStack();

            } else {
                super.onBackPressed();
                finishAffinity();
            }
        }
    }

    @Override
    public void respon(long alarmTime, int id, String start, String end, int tripBack, int repeatInterval) {
        //one trip
        if (tripBack == 0 && repeatInterval == 0) {
            setAlarm(alarmTime, id, end, false, 0);
            //two trips
        } else if (tripBack != 0 && repeatInterval == 0) {
            setAlarm(alarmTime, id, end, false, 0);
            setAlarm(alarmTime + tripBack, id + 1, start, false, 0);
            //one trip repeated
        } else if (tripBack == 0 && repeatInterval != 0) {
            setAlarm(alarmTime, id, end, true, repeatInterval);
            //two trips repeated
        } else {
            setAlarm(alarmTime, id, end, true, repeatInterval);
            setAlarm(alarmTime + tripBack, id + 1, start, true, repeatInterval);
        }
    }

    private void setAlarm(long alarmTime, int id, String end, boolean repeated, long interval) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent notifyIntent = new Intent(this, TransparentActivity.class);

            notifyIntent.putExtra(Constants.END, end);
            Log.i("room", "id sent " + id);
            notifyIntent.putExtra(Constants.ID, id);
            notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (repeated) {
                alarmManager.setRepeating(id, SystemClock.elapsedRealtime() + alarmTime, interval, notifyPendingIntent);
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + alarmTime, notifyPendingIntent);
            }
            Log.i("alram what is this ", SystemClock.elapsedRealtime() + "");
        }
    }

    @Override
    public void returnToOnGoingActivity() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        frag1 = new OnGoingFrag();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dynamicFrag, frag1);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}