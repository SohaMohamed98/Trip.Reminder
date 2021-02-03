package com.mad41.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainScreen extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private OnGoingFrag frag1;
    private HistoryFragment frag2;
    private FragmentManager mgr;
    private FragmentTransaction trns;
    private NavigationView drawerMenu;

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
            trns.replace(R.id.dynamicFrag,frag1);
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.dynamicFrag,new OnGoingFrag()).commit();
                        break;
                    case R.id.btnHistory:
                        getSupportFragmentManager().beginTransaction().replace(R.id.dynamicFrag,new HistoryFragment()).commit();
                        break;
                    case R.id.btnLanguage:
                        Toast.makeText(MainScreen.this, "show language dialog", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnExit:
                        Toast.makeText(MainScreen.this, "show logout dialog", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),Login_form.class));
                        break;
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
        }
    }
}