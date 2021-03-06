package com.mad41.tripreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mad41.tripreminder.Firebase.ReadHandler;
import com.mad41.tripreminder.Firebase.User_Data;
import com.mad41.tripreminder.constants.Constants;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.Firebase.checkConnectionToInternet;
import com.mad41.tripreminder.room_database.view_model.TripViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Login_form extends AppCompatActivity {
    public static final String PREFS_NAME = "PreFile";
    public static Handler fireBaseReadHandler;
    public static Thread readFireBase;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = "EmailPassword";
    private int RC_SIGN_IN = 1;
    private TripViewModel tripViewModel;
    GoogleSignInClient googleSignInClient;
    EditText Email , Password ;
    Button Login , Registeration , forgetPassword;
    SignInButton signInButton;
    ProgressBar progress_bar;
    ArrayList<User_Data> TotalUserData;
    String UserID;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!= null){
                    UserID = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                    intent.putExtra("userID",UserID );
                    startActivity(intent);
                }
            }
        };
        setContentView(R.layout.activity_login_form);
        Email = findViewById(R.id.EmailTxt);
        Password = findViewById(R.id.PasswordTxt);
        Registeration = findViewById(R.id.RegisterationButton);
        forgetPassword = findViewById(R.id.forgetPassword);
        progress_bar = findViewById(R.id.progressBar);
        Login = findViewById(R.id.LoginButton);
        loginButton = findViewById(R.id.login_button);
        signInButton = (SignInButton)findViewById(R.id.googleBtn);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        tripViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(TripViewModel.class);
        readFireBase = new Thread(new ReadHandler());

        fireBaseReadHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                TotalUserData = (ArrayList<User_Data>) msg.obj;
                if(TotalUserData.size() == 0){
                }else {
                    for(int i=0;i<TotalUserData.size();i++){
                        User_Data data=TotalUserData.get(i);
                        ArrayList<String> Notes = new ArrayList<>();
                        if( !data.getNotes().isEmpty()){
                            String[] arrOfStr = data.getNotes().split("##%");
                            for (int k = 1 ; k <arrOfStr.length; k++)
                                Notes.add(arrOfStr[k]);
                        }
                        Trip trip =new Trip(data.getTripName(), data.getStart(), data.getEnd(),data.getTime(),data.getDate()
                                , Notes , Integer.parseInt(data.getStatus()) ,true,0);
                        tripViewModel.insert(trip);
                    }
                }
                writeInSharedPreference();
                setLoginAlarms();

            }
        };


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConnectionToInternet.isConnected(getApplicationContext())) {
                    signWithGoogle();
                }else{
                    Toast.makeText(Login_form.this, "please check your internet connection ", Toast.LENGTH_SHORT).show();

                }
            }
        });


        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {}
            @Override
            public void onError(FacebookException error) {}

        });


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                mAuth.signOut();
            }
        };


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Email.setError("Enter Valid Email.");
                    Email.requestFocus();
                }
                else if (TextUtils.isEmpty(password)){
                    Password.setError("Password is Required.");
                    Password.requestFocus();
                }
                else if(password.length()<6){
                    Password.setError("Password should be more than 5 characters.");
                    Password.requestFocus();
                }
               else if(checkConnectionToInternet.isConnected(getApplicationContext()))
                   {
                       progress_bar.setVisibility(View.VISIBLE);
                       mAuth.signInWithEmailAndPassword(email, password)
                           .addOnCompleteListener(Login_form.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                                        intent.putExtra("userID", UserID);
                                        startActivity(intent);
                                        Toast.makeText(Login_form.this, "Login complete.", Toast.LENGTH_SHORT).show();
                                        readFireBase.start();
                                       writeUserStatus("true",UserID);
                                    } else {
                                        Toast.makeText(Login_form.this, "Please check your data or create account.", Toast.LENGTH_SHORT).show();
                                        progress_bar.setVisibility(View.INVISIBLE);

                                    }
                                }
                            });
                }
               else{
                    Toast.makeText(Login_form.this, "please check your internet connection", Toast.LENGTH_LONG).show();

                }
            }
        });

        Registeration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Registration_Form.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_form.this,ResetPassword.class));
            }
        });

    }
    private void signWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    private void handleFacebookToken(AccessToken token){
//        Toast.makeText(this, "handleFacebookToken", Toast.LENGTH_SHORT).show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                   // Toast.makeText(Login_form.this, "task is Successful", Toast.LENGTH_SHORT).show();
                    readFireBase.start();
                    writeUserStatus("true", UserID);
                    UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                    intent.putExtra("userID",UserID );
                    startActivity(intent);
                }else{
                    Toast.makeText(Login_form.this, "please check your internet connection", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
           // Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(account);

        } catch (ApiException e) {
            Toast.makeText(this, "Signed In Failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Login_form.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                    UserID = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
                    readFireBase.start();
                    writeUserStatus("true", UserID);
                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                    intent.putExtra("userID",UserID );
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(Login_form.this, "please check your internet connection ", Toast.LENGTH_LONG).show();


                }
            }
        });

    }
    public void writeInSharedPreference(){
        SharedPreferences writr = getSharedPreferences(PREFS_NAME , Context.MODE_PRIVATE);
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences.Editor editor = writr.edit();
        editor.putString("UserName",user.getDisplayName());
        editor.putString("Email",user.getEmail());
        editor.commit();
        System.out.println("user name is : "+user.getDisplayName());
        System.out.println("email is : "+user.getEmail());
    }
    public void writeUserStatus(String value, String UserId){
        SharedPreferences writr = getSharedPreferences("userAuth" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = writr.edit();
        editor.putString("userMode",value);
        editor.putString("userId",UserId);
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
    public void setLoginAlarms(){
        List<Trip> comingTrips = tripViewModel.getAllTripsForFireBase();
        for(Trip trip:comingTrips){
            if(trip.getStatus()==2) {
                Calendar calendar = Calendar.getInstance();
                long alarmTime, now;
                String comingTime = trip.getTime();
                String comingDate = trip.getDate();
                int comingId = trip.getId();

                String[] datee = comingDate.split("-");
                int mDay = Integer.parseInt(datee[0]);
                int mMonth = Integer.parseInt(datee[1]) - 1;
                int mYear = Integer.parseInt(datee[2]);
                String[] timee = comingTime.split(":");
                int t1Hour = Integer.parseInt(timee[0]);
                int t1Minuite = Integer.parseInt(timee[1]);

                calendar.set(Calendar.SECOND, 0);
                now = calendar.getTimeInMillis();
                calendar.set(mYear, mMonth, mDay, t1Hour, t1Minuite);
                alarmTime = calendar.getTimeInMillis() - now;
                if (alarmTime > 0) {
                    setAlarm(alarmTime, comingId);
                } else {
                    tripViewModel.updateStatus(comingId, Constants.TRIP_MISSED);
                }
            }
        }
    }

    public  void setAlarm(long alarmTime, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent notifyIntent = new Intent(this, TransparentActivity.class);
            Log.i("room", "id sent " + id);
            notifyIntent.putExtra(Constants.ID, id);

            notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, id, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + alarmTime, notifyPendingIntent);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + alarmTime, notifyPendingIntent);
            Log.i("alram what is this ", SystemClock.elapsedRealtime() + "");
        }
    }

}