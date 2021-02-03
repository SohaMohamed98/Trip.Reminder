package com.mad41.tripreminder.room_database.trip;

import java.util.List;

public interface MyGenericDao<T> {
    long insertTrip(T trip);
    void updateTrip(int id, String name, String startLoacation, String endLoacation,
                    String time, String date, int status, boolean isRepeated, boolean isRound);
    void deletTripById(int id);
    void deleteAllTrips();
    List<Trip> getUpcomingTrips();
    List<Trip> getHistoryTrips();

}
