package com.mad41.tripreminder.room_database.view_model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.room_database.trip.TripDao;

import java.util.List;

public class tripRepository {
    private TripDao tripDao;
    private LiveData<List<Trip>> allTrips;

    public tripRepository(Application application) {
        MyRoomDataBase database = MyRoomDataBase.getUserDataBaseInstance(application);
        tripDao = database.tripDao();
        allTrips=tripDao.getAllTrips();
    }
    public void insertTrip(Trip trip) {
        new InsertTripAsyncTask(tripDao).execute(trip);
    }

    
    public void updateTrip(Trip note) {
        new UpdateTripAsyncTask(tripDao).execute(note);
    }
    public void deleteTrip(Trip note) {
        new UpdateTripAsyncTask(tripDao).execute(note);
    }
    public void deleteAllTrips() {
        new DeleteAllTripsAsyncTask(tripDao).execute();
    }
    public LiveData<List<Trip>> getAllNotes() {
        return allTrips;
    }


    private static class InsertTripAsyncTask extends AsyncTask<Trip, Void, Void> {
        private TripDao tripDao;
        private InsertTripAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }
        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.insertTrip(trips[0]);
            return null;
        }
    }
    private static class UpdateTripAsyncTask extends AsyncTask<Trip, Void, Void> {
        private TripDao tripDao;
        private UpdateTripAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }
        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.updateRow(trips[0]);
            return null;
        }
    }
    private static class DeleteTripAsyncTask extends AsyncTask<Trip, Void, Void> {
        private TripDao tripDao;
        private DeleteTripAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }
        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.deletTrip(trips[0]);
            return null;
        }
    }
    private static class DeleteAllTripsAsyncTask extends AsyncTask<Void, Void, Void> {
        private TripDao tripDao;
        private DeleteAllTripsAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            tripDao.deleteAllTrips();
            return null;
        }
    }

    }






