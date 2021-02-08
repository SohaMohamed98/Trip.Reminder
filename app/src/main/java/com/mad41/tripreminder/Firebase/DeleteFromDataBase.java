package com.mad41.tripreminder.Firebase;

import android.os.Message;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad41.tripreminder.Login_form;
import com.mad41.tripreminder.MainScreen;

public class DeleteFromDataBase implements Runnable{
   // public static String userId;

    @Override
    public void run() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        Message Msg = Message.obtain();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                   mDatabase.removeValue();


        Msg.obj = "done";
        MainScreen.fireBaseDeleteHandler.sendMessage(Msg);
    }
}
