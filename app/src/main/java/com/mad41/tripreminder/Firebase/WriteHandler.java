package com.mad41.tripreminder.Firebase;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;

import java.util.ArrayList;
import java.util.List;

public class WriteHandler{


    public static void  WriteInfireBase(List<Trip> trips , String userId) {
          DatabaseReference mDatabase;
       // String userId = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        mDatabase.removeValue();

        if(!trips.isEmpty()){
        for(int i=0;i<trips.size();i++) {
            Trip trip = trips.get(i);
            mDatabase.child(i + 1 + "").child("TripsInfo").child("Date").setValue(trip.getDate());
            mDatabase.child(i + 1 + "").child("TripsInfo").child("Time").setValue(trip.getTime());
            mDatabase.child(i + 1 + "").child("TripsInfo").child("Start").setValue(trip.getStartLoacation());
            mDatabase.child(i + 1 + "").child("TripsInfo").child("End").setValue(trip.getEndLoacation());
            mDatabase.child(i + 1 + "").child("TripsInfo").child("TripName").setValue(trip.getName());
            mDatabase.child(i + 1 + "").child("TripsInfo").child("Status").setValue(trip.getStatus() + "");
            mDatabase.child(i + 1 + "").child("TripsInfo").child("IsRound").setValue(trip.isRound() + "");
            mDatabase.child(i + 1 + "").child("TripsInfo").child("IsRepeated").setValue(trip.isRepeated() + "");
            if (!trip.getNotes().isEmpty()) {
                int noteSize = trip.getNotes().size();
                for (int j = 0; j < noteSize; j++) {
                    mDatabase.child(i + 1 + "").child("Notes").child(j + 1 + "").setValue(trip.getNotes().get(j));

                }
            }
        }


        }



    }
}
