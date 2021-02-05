package com.mad41.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import com.mad41.tripreminder.room_database.trip.Trip;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity implements AddTripFragments.Communicator , OnGoingFrag.onGoingCommunicator{
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private OnGoingFrag frag1;
    private HistoryFragment frag2;
    private FragmentManager mgr;
    private FragmentTransaction trns;
    private NavigationView drawerMenu;
    AddTripFragments fragment;


    String name, start, end, date, time;


    @Override
    public void saveArrayList(ArrayList<Trip> arr) {
        fragment.getArrayList(arr);
    }

    @Override
    public void startAddTripFragment() {
        fragment = new AddTripFragments();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.HNotes, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //we need the toolbar and drawer to show the menu button
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        //this will show the menu button
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawerMenu = findViewById(R.id.drawerMenu);
        setListener();

        if(savedInstanceState==null){
            mgr = getSupportFragmentManager();
            trns = mgr.beginTransaction();
            frag1 = new OnGoingFrag();
            trns.replace(R.id.HNotes,frag1);
            trns.commit();
            drawerMenu.setCheckedItem(R.id.btnOngoing);
        }


    }


    private void setListener() {
        drawerMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.btnOngoing:
                        //if I used mgr from above it will crash if I rotated and then changed fragment, and if I used trns it will crash anyway because it's outside the listener
                        getSupportFragmentManager().beginTransaction().replace(R.id.HNotes,new OnGoingFrag()).commit();
                        break;
                    case R.id.btnHistory:
                        getSupportFragmentManager().beginTransaction().replace(R.id.HNotes,new HistoryFragment()).commit();
                        break;
                    case R.id.btnLanguage:
                        Toast.makeText(MainScreen.this, "show language dialog", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnExit: {
                        Toast.makeText(MainScreen.this, "show logout dialog", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        startActivity(new Intent(getApplicationContext(),Login_form.class));
                        finish();
                        break;
                    }
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        //check if the drawer is open then the back button close the drawer first and not exit the activity directly
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
            finishAffinity();
        }
    }

    @Override
    public void respon(long alarmTime, int id, String end) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent notifyIntent = new Intent(this, TransparentActivity.class);

            notifyIntent.putExtra(AddTripFragments.END,end);
            Log.i("room","id sent "+id);
            notifyIntent.putExtra(AddTripFragments.ID,id);
            notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(this,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+alarmTime,notifyPendingIntent);
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+5000,notifyPendingIntent);
            Log.i("alram what is this ",SystemClock.elapsedRealtime()+"");
        }

    }

    @Override
    public void sendArrayListToRecycleView(ArrayList<Trip> arrayList2) {
        frag1.getArrayList(arrayList2);
    }

    @Override
    public void returnToOnGoingActivity() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        frag1 = new OnGoingFrag();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.HNotes, frag1);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}