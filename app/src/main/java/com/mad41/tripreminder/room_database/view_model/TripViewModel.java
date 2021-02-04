package com.mad41.tripreminder.room_database.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mad41.tripreminder.room_database.trip.Trip;

import java.util.List;

public class TripViewModel extends AndroidViewModel {
    private TripRepository repository;
    private LiveData<List<Trip>> allTrips;

    public TripViewModel(@NonNull Application application) {
        super(application);
        repository = new TripRepository(application);
        allTrips = repository.getAllNotes();
    }
    public void insert(Trip trip) {
        repository.insertTrip(trip);
    }
    public void update(Trip trip) {
        repository.updateTrip(trip);
    }
    public void delete(Trip trip) {
        repository.deleteTrip(trip);
    }
    public void deleteAllNotes() {
        repository.deleteAllTrips();
    }
    public LiveData<List<Trip>> getAllNotes() {

        return allTrips;
    }
}
