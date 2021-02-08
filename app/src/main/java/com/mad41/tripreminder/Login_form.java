package com.mad41.tripreminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.room_database.view_model.TripViewModel;

import java.util.ArrayList;


public class Login_form extends AppCompatActivity implements View.OnClickListener {
    public static final String PREFS_NAME = "PreFile";
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = "EmailPassword";
    TextView forgetPassword;
    private TripViewModel tripViewModel;
    GoogleSignInClient googleSignInClient;
    private int RC_SIGN_IN = 1;
    EditText Email , Password ;
    Button Login , Registeration;
    SignInButton signInButton;
    ArrayList<User_Data> TotalUserData;
    String UserID;

    public static Handler fireBaseReadHandler;
    public static Thread readFireBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        Email = findViewById(R.id.EmailTxt);
        Password = findViewById(R.id.PasswordTxt);
        Registeration = findViewById(R.id.RegisterationButton);
        forgetPassword = findViewById(R.id.forgetPassword);
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
                    Toast.makeText(getApplicationContext(), "You don't have data", Toast.LENGTH_SHORT).show();
                }else {

                    for(int i=0;i<TotalUserData.size();i++){
                        User_Data data=TotalUserData.get(i);
                        ArrayList<String> Notes = new ArrayList<>();
                        if( !data.getNotes().isEmpty()){
                            String[] arrOfStr = data.getNotes().split("##%");
                            for (String a : arrOfStr)
                                Notes.add(a);
                        }
                        Trip trip =new Trip(data.getTripName(), data.getStart(), data.getEnd(),data.getTime(),data.getDate()

                                , Notes , Integer.parseInt(data.getStatus()) ,true,0);

                        tripViewModel.insert(trip);
                        System.out.println("message"+ " from fireeeee"+ data.getNotes().isEmpty());
                    }

                    System.out.println("the result after thread :  " + TotalUserData.size() + "");
                    // System.out.println("the first note of first element :  " + TotalUserData.get(1).getNotes().get(2) + "");
                }
                writeInSharedPreference();

            }
        };

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
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signWithGoogle();
            }
        });


        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(Login_form.this, "on success", Toast.LENGTH_SHORT).show();
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
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Login_form.this, "Please, Enter your email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Login_form.this, "Please, Enter your password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<6){
                    Toast.makeText(Login_form.this, "Password should be more than 5 characters", Toast.LENGTH_SHORT).show();
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login_form.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    UserID = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
                                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                                    intent.putExtra("userID",UserID );
                                    startActivity(intent);
                                    Toast.makeText(Login_form.this, "Login complete.", Toast.LENGTH_SHORT).show();
                                    readFireBase.start();
                                } else {
                                    Toast.makeText(Login_form.this, "Login Failed.", Toast.LENGTH_SHORT).show();

                                }


                            }
                        });

            }
        });

        Registeration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Registration_Form.class));
            }
        });



    }
    private void signWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    private void handleFacebookToken(AccessToken token){
        Toast.makeText(this, "handleFacebookToken", Toast.LENGTH_SHORT).show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Toast.makeText(Login_form.this, "task is Successful", Toast.LENGTH_SHORT).show();
                    readFireBase.start();
                    UserID = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                    intent.putExtra("userID",UserID );
                    startActivity(intent);
                }else{
                    Toast.makeText(Login_form.this, "failed", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
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


                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                    intent.putExtra("userID",UserID );
                    startActivity(intent);



                }
                else
                {
                    Toast.makeText(Login_form.this, "Failed to Login", Toast.LENGTH_SHORT).show();


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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgetPassword:
                startActivity(new Intent(Login_form.this,ResetPassword.class));
                break;
        }
    }
}