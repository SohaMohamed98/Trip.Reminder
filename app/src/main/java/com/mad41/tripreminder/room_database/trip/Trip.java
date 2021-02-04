package com.mad41.tripreminder.room_database.trip;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mad41.tripreminder.constants.Constants;

import java.util.ArrayList;


@Entity(tableName = Constants.TRIP_TABLE_NAME)
public class Trip {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    private String startLoacation;
    private String endLoacation;
    private String time;
    private String date;
    private ArrayList<String> notes;
    private int status;
    private boolean isRepeated;
    private boolean isRound;

    public Trip(String name, String startLoacation, String endLoacation, String time, String date, ArrayList<String> notes, int status, boolean isRepeated, boolean isRound) {
        this.name = name;
        this.startLoacation = startLoacation;
        this.endLoacation = endLoacation;
        this.time = time;
        this.date = date;
        this.notes = notes;
        this.status = status;
        this.isRepeated = isRepeated;
        this.isRound = isRound;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getStartLoacation() {
        return startLoacation;
    }

    public String getEndLoacation() {
        return endLoacation;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public int getStatus() {
        return status;
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    public boolean isRound() {
        return isRound;
    }
}
