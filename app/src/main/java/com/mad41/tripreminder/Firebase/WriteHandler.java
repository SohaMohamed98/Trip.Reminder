package com.mad41.tripreminder.Firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteHandler{
    private DatabaseReference mDatabase;

    public void WriteInfireBase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        mDatabase.child("1").child("TripsInfo").child("Date").setValue("12/2/2021");
        mDatabase.child("1").child("TripsInfo").child("Time").setValue("11:22");
        mDatabase.child("1").child("TripsInfo").child("Start").setValue("el salam");
        mDatabase.child("1").child("TripsInfo").child("End").setValue("el mihwr");
        mDatabase.child("1").child("TripsInfo").child("TripName").setValue("secondTRIP");
        mDatabase.child("1").child("TripsInfo").child("Status").setValue("complete");
        mDatabase.child("1").child("Notes").child("1").setValue("make me remember");
        mDatabase.child("1").child("Notes").child("2").setValue("make me remember22");

    }
}
