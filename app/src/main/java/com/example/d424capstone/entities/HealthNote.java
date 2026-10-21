package com.example.d424capstone.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "health_notes")
public class HealthNote {

    @PrimaryKey(autoGenerate = true)
    private int noteId;

    private String title;
    private String content;
    private String noteType;
    private String bodyLocation;
    private String date;
    private int severity;

    public HealthNote(String title, String content, String noteType, String bodyLocation, String date, int severity) {
        this.title = title;
        this.content = content;
        this.noteType = noteType;
        this.bodyLocation = bodyLocation;
        this.date = date;
        this.severity = severity;
    }

    // --- Getters and Setters ---

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getBodyLocation() {
        return bodyLocation;
    }

    public void setBodyLocation(String bodyLocation) {
        this.bodyLocation = bodyLocation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }
}
