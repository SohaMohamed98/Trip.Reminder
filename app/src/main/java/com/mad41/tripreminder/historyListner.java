package com.mad41.tripreminder;

import com.mad41.tripreminder.room_database.trip.Trip;

import java.util.List;

public interface historyListner {
    public void showNotes(List<Trip> notes , int id);
    public void DeleteTrip(List<Trip> Trips , int id);
    public void getID( int id);
}
