package com.example.d424capstone.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424capstone.entities.HealthNote;

import java.util.List;


 //SQL queries and methods for inserting, updating, deleting, and querying health notes.
@Dao
public interface HealthNoteDAO {

    // insert a new note. if a conflict occurs, replace the existing record.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HealthNote healthNote);

    // update an existing note.
    @Update
    void update(HealthNote healthNote);

    // delete a note.
    @Delete
    void delete(HealthNote healthNote);

    // get all health notes sorted by ID descending (newest first).
    @Query("SELECT * FROM health_notes ORDER BY noteId DESC")
    List<HealthNote> getAllNotes();

    // get health notes filtered by type (Illness or Injury).
    @Query("SELECT * FROM health_notes WHERE noteType = :type ORDER BY noteId DESC")
    List<HealthNote> getNotesByType(String type);

    // search health notes where title, content, or body location matches the query.
    @Query("SELECT * FROM health_notes WHERE title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%' OR bodyLocation LIKE '%' || :searchQuery || '%' ORDER BY noteId DESC")
    List<HealthNote> searchNotes(String searchQuery);
}
