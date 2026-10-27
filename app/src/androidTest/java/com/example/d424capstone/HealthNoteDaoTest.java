package com.example.d424capstone;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.d424capstone.dao.HealthNoteDAO;
import com.example.d424capstone.database.HealthDatabaseBuilder;
import com.example.d424capstone.entities.HealthNote;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class HealthNoteDaoTest {

    private HealthDatabaseBuilder db;
    private HealthNoteDAO dao;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, HealthDatabaseBuilder.class)
                .allowMainThreadQueries()
                .build();
        dao = db.healthNoteDAO();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertAndLoadHealthNoteReturnsIdenticalData() {
        // arrange
        String title = "Fever and Cough";
        String content = "Started feeling feverish with a mild cough.";
        String noteType = "Illness";
        String bodyLocation = "Chest";
        String date = "2026-10-26";
        int severity = 3;

        HealthNote note = new HealthNote(title, content, noteType, bodyLocation, date, severity);

        // act
        dao.insert(note);
        List<HealthNote> notes = dao.getAllNotes();

        // assert
        assertFalse("Notes list should not be empty after insert", notes.isEmpty());
        HealthNote loadedNote = notes.get(0);

        assertNotNull(loadedNote);
        assertEquals("Title should match", title, loadedNote.getTitle());
        assertEquals("Content should match", content, loadedNote.getContent());
        assertEquals("Note type should match", noteType, loadedNote.getNoteType());
        assertEquals("Body location should match", bodyLocation, loadedNote.getBodyLocation());
        assertEquals("Date should match", date, loadedNote.getDate());
        assertEquals("Severity should match", severity, loadedNote.getSeverity());
    }
}
