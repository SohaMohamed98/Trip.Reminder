package com.mad41.tripreminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad41.tripreminder.Firebase.checkConnectionToInternet;

public class Registration_Form extends AppCompatActivity {

    EditText firstName, email,password,confirmationPassword;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    Button Signup , Login ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration__form);
        firstName = (EditText)findViewById(R.id.userName);
        email = (EditText)findViewById(R.id.E_mail);
        password = (EditText)findViewById(R.id.passwordID);
        Signup = findViewById(R.id.signUp);
        Login = findViewById(R.id.Login);
        confirmationPassword = (EditText)findViewById(R.id.ConfirmPasswordID);
        progressBar = findViewById(R.id.signUPprogressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Main = new Intent(getApplicationContext(), Login_form.class);
                startActivity(Main);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void signUp(){
        String emailString = email.getText().toString();
        String passString = password.getText().toString();
        String confPassString = confirmationPassword.getText().toString();
        String nameString = firstName.getText().toString();
        if(TextUtils.isEmpty(nameString))
        {
            firstName.setError("Enter valid Name.");
            firstName.requestFocus();

        }
       else if(TextUtils.isEmpty(emailString) || ! Patterns.EMAIL_ADDRESS.matcher(emailString).matches())
        {
            email.setError("Enter Valid Email.");
            email.requestFocus();

        }
       else if(TextUtils.isEmpty(passString))
        {
            password.setError("Password is Required.");
            password.requestFocus();

        }
       else if(TextUtils.isEmpty(confPassString))
        {
            password.setError("confirm Password is Required.");
            password.requestFocus();

        }
        else if(passString.length() < 6)
        {
            password.setError("Password must be more 6 digits");
            password.requestFocus();

        }
       else if(!passString.equals(confPassString))
        {
            Toast.makeText(this, "Password not matching", Toast.LENGTH_SHORT).show();
            confirmationPassword.setError("Password must be more 6 digits");
            confirmationPassword.requestFocus();

        }
       else if(passString.equals(confPassString) && !passString.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE );
            if(checkConnectionToInternet.isConnected(getApplicationContext())) {
                firebaseAuth.createUserWithEmailAndPassword(emailString, passString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ((task.isSuccessful())) {
                            Toast.makeText(Registration_Form.this, "Registeration Done", Toast.LENGTH_SHORT).show();
                            Login_form.readFireBase.start();
                            Intent Main = new Intent(getApplicationContext(), MainScreen.class);
                            startActivity(Main);
                            firebaseAuth = FirebaseAuth.getInstance();
                            writeInSharedPreference();

                        } else {
                            Toast.makeText(Registration_Form.this, "Faild !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE );
                        }

                    }
                });
            }else{
                Toast.makeText(Registration_Form.this, "Please, check your internet connection " , Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE );
            }
        }

    }

    public void writeInSharedPreference(){
        SharedPreferences writr = getSharedPreferences(Login_form.PREFS_NAME , Context.MODE_PRIVATE);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        SharedPreferences.Editor editor = writr.edit();
        editor.putString("UserName",user.getDisplayName());
        editor.putString("Email",user.getEmail());
        editor.commit();
        System.out.println("user name is : "+user.getDisplayName());
        System.out.println("email is : "+user.getEmail());
    }
}