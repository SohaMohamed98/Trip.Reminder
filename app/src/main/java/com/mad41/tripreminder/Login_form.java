package com.mad41.tripreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.mad41.tripreminder.trip_ui.UpcomingActivity;

import java.util.Arrays;
import java.util.List;


public class Login_form extends AppCompatActivity {
    private static int AUTH_REQUEST_CODE = 7123;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private List<AuthUI.IdpConfig> provider;
    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_form);
        init();
    }
    private void init() {
        provider = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()

        );
        firebaseAuth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    //soha
                  //  startActivity(new Intent(getApplicationContext(), UpcomingActivity.class));
                    //Moataz
                    startActivity(new Intent(getApplicationContext(),MainScreen.class));
                    finish();
                    Toast.makeText(Login_form.this, "you already login before", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setLogo(R.drawable.notification_icone)
                          //  .setTheme(R.style.LoginTheme)
                            .setAvailableProviders(provider)
                            .build(),AUTH_REQUEST_CODE);
                }
            }
        };
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTH_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(requestCode == RESULT_OK ) {
                Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainScreen.class));
                finish();
            }
            else {
               finish();
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(listener != null) firebaseAuth.removeAuthStateListener(listener);

    }


}