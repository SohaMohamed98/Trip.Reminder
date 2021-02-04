package com.mad41.tripreminder.room_database.trip;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mad41.tripreminder.room_database.trip.MyGenericDao;
import com.mad41.tripreminder.room_database.trip.Trip;

import java.util.List;

@Dao
public interface TripDao extends MyGenericDao<Trip> {

    @Override
    @Insert
    long insertTrip(Trip trip);

    @Override
    @Query("update trips_table  SET name=:name ,startLoacation=:startLoacation," +
            "endLoacation=:endLoacation,time=:time,date=:date,status=:status,isRepeated=:isRepeated," +
            "isRound=:isRound Where id=:id")
    void updateTrip(int id, String name, String startLoacation, String endLoacation, String time, String date, int status, boolean isRepeated, boolean isRound);

    @Query("update trips_table  SET status=:status Where id=:id")
    void updateStatus(int id,int status);
    @Override
    @Query("DELETE FROM trips_table WHERE id = :id")
    void deletTripById(int id);

    @Query("DELETE  FROM trips_table")
    void deleteAllTrips();

    @Override
    @Query("SELECT * FROM trips_table WHERE status=2")
    List<Trip> getUpcomingTrips();

    @Override
    @Query("SELECT * FROM trips_table WHERE status!=2")
    List<Trip> getHistoryTrips();


    @Query("SELECT * FROM trips_table")
    List<Trip> getAllTrips();

}