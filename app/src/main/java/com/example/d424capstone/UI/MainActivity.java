package com.example.d424capstone.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.HealthNote;

import java.util.List;

// dashboard with search, category chips, and a grid for health notes.

public class MainActivity extends AppCompatActivity {

    private Repository repository;
    private HealthNoteAdapter adapter;
    private RecyclerView recyclerView;
    private EditText editSearch;
    private ChipGroup chipGroupFilters;
    private TextView textEmptyState;
    private ExtendedFloatingActionButton fabCreateNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize repository
        repository = new Repository(getApplication());

        // sample data if database is empty
        seedInitialDataIfNeeded();

        // initialize UI
        recyclerView = findViewById(R.id.recyclerViewNotes);
        editSearch = findViewById(R.id.editSearch);
        chipGroupFilters = findViewById(R.id.chipGroupFilters);
        textEmptyState = findViewById(R.id.textEmptyState);
        fabCreateNote = findViewById(R.id.fabCreateNote);

        // setup RecyclerView
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new HealthNoteAdapter();
        recyclerView.setAdapter(adapter);

        // load all notes initially
        loadNotes("");

        // handle clicks on note cards
        adapter.setOnItemClickListener(new HealthNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HealthNote healthNote) {
                Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
                intent.putExtra("noteId", healthNote.getNoteId());
                intent.putExtra("title", healthNote.getTitle());
                intent.putExtra("content", healthNote.getContent());
                intent.putExtra("noteType", healthNote.getNoteType());
                intent.putExtra("bodyLocation", healthNote.getBodyLocation());
                intent.putExtra("date", healthNote.getDate());
                intent.putExtra("severity", healthNote.getSeverity());
                startActivity(intent);
            }
        });

        // handler for click to create a new note
        fabCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
                startActivity(intent);
            }
        });

        // handler for search typing
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // handler for chip filter
        chipGroupFilters.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, int checkedId) {
                if (checkedId == R.id.chipAll) {
                    loadNotes(editSearch.getText().toString());
                } else if (checkedId == R.id.chipIllness) {
                    loadNotesByType("Illness");
                } else if (checkedId == R.id.chipInjury) {
                    loadNotesByType("Injury");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refresh notes list when returning from NoteDetailsActivity
        loadNotes(editSearch.getText().toString());
    }

    // sample data
    private void seedInitialDataIfNeeded() {
        List<HealthNote> existingNotes = repository.getAllNotes();
        if (existingNotes == null || existingNotes.isEmpty()) {
            repository.insert(new HealthNote("Example #1",
                    "Here is the example text that you can put inside the card.", "Illness",
                    "Head", "2026-10-20", 4));
            repository.insert(new HealthNote("Example #2",
                    "Here is another example of and injury or a illness.", "Injury",
                    "Left Ankle", "2026-10-18", 3));
            repository.insert(new HealthNote("Example #3",
                    "Here is the last example data that can be put into this data field.",
                    "Illness", "Throat", "2026-10-19", 2));
        }
    }

    // load notes by search content
    private void loadNotes(String query) {
        List<HealthNote> notes;
        if (query == null || query.trim().isEmpty()) {
            notes = repository.getAllNotes();
        } else {
            notes = repository.searchNotes(query);
        }
        updateNotesDisplay(notes);
    }

    // load notes filtered by category type (Illness / Injury)
    private void loadNotesByType(String type) {
        List<HealthNote> notes = repository.getNotesByType(type);
        updateNotesDisplay(notes);
    }

    // update RecyclerView to search query.
    private void updateNotesDisplay(List<HealthNote> notes) {
        if (notes != null && !notes.isEmpty()) {
            adapter.setHealthNotes(notes);
            recyclerView.setVisibility(View.VISIBLE);
            textEmptyState.setVisibility(View.GONE);
        } else {
            adapter.setHealthNotes(notes);
            recyclerView.setVisibility(View.GONE);
            textEmptyState.setVisibility(View.VISIBLE);
        }
    }
}
