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
    private LiveData<List<Trip>> upComingTrips;
    private LiveData<List<Trip>> historyTrips;

    public TripViewModel(@NonNull Application application) {
        super(application);
        repository = new TripRepository(application);
        allTrips = repository.getAllNotes();
        historyTrips = repository.getHistoryNotes();
        upComingTrips = repository.getUpcomingNotes();
    }
    public long insert(Trip trip) {
        return repository.insertTrip(trip);
    }

    public void update(Trip trip) {
        repository.updateTrip(trip);
    }
    public void deleteTripById(int id) {
        repository.deleteTripById(id);
    }
    public void updateStatus(int id,int status) {
        repository.updateStatus(id,status);
    }
    public void delete(Trip trip) {
        repository.deleteTrip(trip);
    }
    public void deleteAllTrips() {
        repository.deleteAllTrips();
    }
    public Trip getTripById(int id) {
      return repository.getTripById(id);
    }

    public LiveData<List<Trip>> getAllNotes() {
        return allTrips;
    }
    public LiveData<List<Trip>> getUpcomingNotes() {
        return upComingTrips;
    }
    public LiveData<List<Trip>> getHistoryNotes() {
        return historyTrips;
    }
}
