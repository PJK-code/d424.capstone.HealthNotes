package com.example.d424capstone.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.d424capstone.dao.HealthNoteDAO;
import com.example.d424capstone.entities.HealthNote;


@Database(entities = {HealthNote.class}, version = 3, exportSchema = false)
public abstract class HealthDatabaseBuilder extends RoomDatabase {

    // method that returns our DAO
    public abstract HealthNoteDAO healthNoteDAO();

    // singleton instance to prevent multiple database instances
    private static volatile HealthDatabaseBuilder INSTANCE;

    private static final String DATABASE_NAME = "health_notes_database";

    //retrieves the singleton database instance
    public static HealthDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HealthDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            HealthDatabaseBuilder.class,
                            DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration() // Recreates database if schema changes during development
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
