package com.mad41.tripreminder.room_database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mad41.tripreminder.constants.Constants;
import com.mad41.tripreminder.room_database.note.Note;
import com.mad41.tripreminder.room_database.note.NoteDao;
import com.mad41.tripreminder.room_database.trip.Trip;
import com.mad41.tripreminder.room_database.trip.TripDao;


@Database(entities = {Trip.class, Note.class},version = 2)
public abstract class MyRoomDataBase extends RoomDatabase {
    private static MyRoomDataBase myRoomDataBaseInstance;
    public abstract TripDao tripDao();
    public abstract NoteDao noteDao();

    public static synchronized MyRoomDataBase getUserDataBaseInstance(Context context){
        if (myRoomDataBaseInstance==null){
            myRoomDataBaseInstance= Room.databaseBuilder(context.getApplicationContext(),MyRoomDataBase.class, Constants.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return myRoomDataBaseInstance;
    }

}
