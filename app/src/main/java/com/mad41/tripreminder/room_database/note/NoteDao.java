package com.mad41.tripreminder.room_database.note;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insertNote(Note note);


    @Query("update notes_table  SET note=:note Where trip_id=:id")
    void updateTrip(int id, String note);


    @Query("DELETE FROM notes_table WHERE trip_id= :id")
    void deletNoteById(int id);

    @Query("DELETE  FROM notes_table")
    void deleteAllNotes();

 @Query("SELECT * FROM notes_table")
 List<Note> getAllNotes();
}
