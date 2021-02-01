package com.mad41.tripreminder.room_database.note;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;


import com.mad41.tripreminder.constants.Constants;
import com.mad41.tripreminder.room_database.trip.Trip;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = Constants.NOTE_TABLE_NAME, primaryKeys = {"trip_id","note"},
        foreignKeys = @ForeignKey(entity = Trip.class,
        parentColumns = "id",
        childColumns = "trip_id",
        onDelete = CASCADE))
public class Note {
    @ColumnInfo(name = "trip_id")
    private int id;
    @NonNull
    private String note;

    public Note(String note) {
        this.note = note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }
}
