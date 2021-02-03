package com.mad41.tripreminder.Firebase;

import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.mad41.tripreminder.Login_form;

import java.util.ArrayList;

public class ReadHandler implements Runnable{

    private DatabaseReference mDatabase;
    ArrayList<User_Data> returnedData;
    @Override
    public void run() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        returnedData = new ArrayList<>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Message Msg = Message.obtain();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DataSnapshot dataSnapshot = ds.child("TripsInfo");
                    User_Data user = dataSnapshot.getValue(User_Data.class);
                    System.out.println(user.getTripName());
                    DataSnapshot DSnapshot = ds.child("Notes");
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                    };
                    ArrayList<String> TripNotes = DSnapshot.getValue(t);
                    user.setNotes(TripNotes);
                    Log.i("user","notes value"+user.getNotes().get(1));
                    returnedData.add(user);
                }
                System.out.println("the result inside thread :  "+ returnedData.size()+"");
                Msg.obj = returnedData;
                Login_form.fireBaseReadHandler.sendMessage(Msg);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}
