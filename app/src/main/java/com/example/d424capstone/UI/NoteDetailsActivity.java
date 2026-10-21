package com.example.d424capstone.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.HealthNote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// handles creating, editing, and deleting health notes

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText editTitle, editContent, editBodyLocation, editDate;
    private RadioGroup radioGroupType, radioGroupSeverity;
    private RadioButton radioIllness, radioInjury;
    private RadioButton radioSeverity1, radioSeverity2, radioSeverity3, radioSeverity4, radioSeverity5;
    private MaterialButton btnSave, btnDelete, btnSelectBodyMap;
    private TextView textHeaderTitle;
    private View btnBack;

    private Repository repository;
    private int noteId = -1; // Default -1 for new note creation
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        repository = new Repository(getApplication());

        // initialize UI views
        editTitle = findViewById(R.id.editNoteTitle);
        editContent = findViewById(R.id.editNoteContent);
        editBodyLocation = findViewById(R.id.editBodyLocation);
        editDate = findViewById(R.id.editDate);
        radioGroupType = findViewById(R.id.radioGroupType);
        radioGroupSeverity = findViewById(R.id.radioGroupSeverity);
        radioIllness = findViewById(R.id.radioIllness);
        radioInjury = findViewById(R.id.radioInjury);
        radioSeverity1 = findViewById(R.id.radioSeverity1);
        radioSeverity2 = findViewById(R.id.radioSeverity2);
        radioSeverity3 = findViewById(R.id.radioSeverity3);
        radioSeverity4 = findViewById(R.id.radioSeverity4);
        radioSeverity5 = findViewById(R.id.radioSeverity5);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnSelectBodyMap = findViewById(R.id.btnSelectBodyMap);
        textHeaderTitle = findViewById(R.id.textHeaderTitle);
        btnBack = findViewById(R.id.btnBack);

        // back button click listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // body map button click listener
        btnSelectBodyMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NoteDetailsActivity.this, "Body Map selection feature", Toast.LENGTH_SHORT).show();
            }
        });

        // if intent contains existing note data for editing
        if (getIntent().hasExtra("noteId")) {
            isEditing = true;
            noteId = getIntent().getIntExtra("noteId", -1);
            editTitle.setText(getIntent().getStringExtra("title"));
            editContent.setText(getIntent().getStringExtra("content"));
            String type = getIntent().getStringExtra("noteType");
            if ("Injury".equals(type)) {
                radioInjury.setChecked(true);
            } else {
                radioIllness.setChecked(true);
            }
            editBodyLocation.setText(getIntent().getStringExtra("bodyLocation"));
            editDate.setText(getIntent().getStringExtra("date"));

            int severity = getIntent().getIntExtra("severity", 3);
            switch (severity) {
                case 1: radioSeverity1.setChecked(true); break;
                case 2: radioSeverity2.setChecked(true); break;
                case 3: radioSeverity3.setChecked(true); break;
                case 4: radioSeverity4.setChecked(true); break;
                case 5: radioSeverity5.setChecked(true); break;
                default: radioSeverity3.setChecked(true); break;
            }

            textHeaderTitle.setText("Edit Health Note");
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            isEditing = false;
            textHeaderTitle.setText("Add Health Note");
            // current date
            String currentDate = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(new Date());
            editDate.setText(currentDate);
            radioSeverity2.setChecked(true);
            btnDelete.setVisibility(View.GONE);
        }

        // save button click listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        // delete button click listener
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
    }

    // validates input and saves or updates the health note in the database.
    private void saveNote() {
        String title = editTitle.getText() != null ? editTitle.getText().toString().trim() : "";
        String content = editContent.getText() != null ? editContent.getText().toString().trim() : "";
        String bodyLocation = editBodyLocation.getText() != null ? editBodyLocation.getText().toString().trim() : "";
        String date = editDate.getText() != null ? editDate.getText().toString().trim() : "";

        // make sure title valid
        if (title.isEmpty()) {
            editTitle.setError("Title is required");
            editTitle.requestFocus();
            return;
        }

        String noteType = radioInjury.isChecked() ? "Injury" : "Illness";

        int severity = 3;
        int checkedSeverityId = radioGroupSeverity.getCheckedRadioButtonId();
        if (checkedSeverityId == R.id.radioSeverity1) {
            severity = 1;
        } else if (checkedSeverityId == R.id.radioSeverity2) {
            severity = 2;
        } else if (checkedSeverityId == R.id.radioSeverity3) {
            severity = 3;
        } else if (checkedSeverityId == R.id.radioSeverity4) {
            severity = 4;
        } else if (checkedSeverityId == R.id.radioSeverity5) {
            severity = 5;
        }

        if (isEditing) {
            // update note
            HealthNote note = new HealthNote(title, content, noteType, bodyLocation, date, severity);
            note.setNoteId(noteId);
            repository.update(note);
            Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // insert new note
            HealthNote note = new HealthNote(title, content, noteType, bodyLocation, date, severity);
            repository.insert(note);
            Toast.makeText(this, "Note created successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    // deletes the current note from the database
    private void deleteNote() {
        if (isEditing) {
            int severity = 3;
            int checkedSeverityId = radioGroupSeverity.getCheckedRadioButtonId();
            if (checkedSeverityId == R.id.radioSeverity1) severity = 1;
            else if (checkedSeverityId == R.id.radioSeverity2) severity = 2;
            else if (checkedSeverityId == R.id.radioSeverity3) severity = 3;
            else if (checkedSeverityId == R.id.radioSeverity4) severity = 4;
            else if (checkedSeverityId == R.id.radioSeverity5) severity = 5;

            HealthNote note = new HealthNote(
                    editTitle.getText().toString(),
                    editContent.getText().toString(),
                    radioInjury.isChecked() ? "Injury" : "Illness",
                    editBodyLocation.getText().toString(),
                    editDate.getText().toString(),
                    severity
            );
            note.setNoteId(noteId);
            repository.delete(note);
            Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
