package com.mad41.tripreminder.room_database.view_model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mad41.tripreminder.room_database.MyRoomDataBase;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.room_database.trip.TripDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TripRepository {
    private TripDao tripDao;
    private LiveData<List<Trip>> allTrips;
    private LiveData<List<Trip>> upcomingTrips;
    private LiveData<List<Trip>> historyTrips;

    public TripRepository(Application application) {
        MyRoomDataBase database = MyRoomDataBase.getUserDataBaseInstance(application);
        tripDao = database.tripDao();
        allTrips=tripDao.getAllTrips();
        upcomingTrips=tripDao.getUpcomingTrips();
        historyTrips=tripDao.getHistoryTrips();

    }
    public long insertTrip(Trip trip) {
        long id=0;
        try {
             id= new InsertTripAsyncTask(tripDao).execute(trip).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }
    public void updateStatus(int id,int status) {
        new updateStatusAsyncTask(tripDao).execute(id,status);
    }
    public void updateTrip(Trip note) {
        new UpdateTripAsyncTask(tripDao).execute(note);
    }
    public void deleteTrip(Trip note) {
        new UpdateTripAsyncTask(tripDao).execute(note);
    }
    public void deleteTripById(int id) {
        new deleteTripByIdAsyncTask(tripDao).execute(id);
    }
    public void deleteAllTrips() {
        new DeleteAllTripsAsyncTask(tripDao).execute();
    }
    public Trip getTripById(int id) {
        Trip trip=null;
        try {
             trip= new getTripByIdAsyncTask(tripDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return trip;
    }

    public LiveData<List<Trip>> getAllNotes() {
        return allTrips;
    }
    public LiveData<List<Trip>> getUpcomingNotes() {
        return upcomingTrips;
    }
    public LiveData<List<Trip>> getHistoryNotes() {
        return historyTrips;
    }

    private static class updateStatusAsyncTask extends AsyncTask<Integer, Void, Void> {
        private TripDao tripDao;
        private updateStatusAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }
        @Override
        protected Void doInBackground(Integer... integers) {
            tripDao.updateStatus(integers[0],integers[1]);
            return null;
        }
    }

    private static class getTripByIdAsyncTask extends AsyncTask<Integer, Void, Trip> {
        private TripDao tripDao;
        private getTripByIdAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }
        @Override
        protected Trip doInBackground(Integer... integers) {
           Trip trip= tripDao.getTripById(integers[0]);
            return trip;
        }
    }

    private static class deleteTripByIdAsyncTask extends AsyncTask<Integer, Void, Void> {
        private TripDao tripDao;
        private deleteTripByIdAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }
        @Override
        protected Void doInBackground(Integer... integers) {
            tripDao.deletTripById(integers[0]);
            return null;
        }
    }
    private static class InsertTripAsyncTask extends AsyncTask<Trip, Void, Long> {
        private TripDao tripDao;
        private InsertTripAsyncTask(TripDao tripDao) {
            this.tripDao = tripDao;
        }
        @Override
        protected Long doInBackground(Trip... trips) {
           long id= tripDao.insertTrip(trips[0]);
            return id;
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






