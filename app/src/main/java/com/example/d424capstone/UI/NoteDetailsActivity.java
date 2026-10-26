package com.example.d424capstone.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import com.example.d424capstone.R;
import com.example.d424capstone.database.Repository;
import com.example.d424capstone.entities.HealthNote;
import com.example.d424capstone.utils.SecurityUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// handles creating, editing, and deleting health notes

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText editTitle, editContent, editBodyLocation, editDate;
    private RadioGroup radioGroupType, radioGroupSeverity;
    private RadioButton radioIllness, radioInjury;
    private RadioButton radioSeverity1, radioSeverity2, radioSeverity3, radioSeverity4, radioSeverity5;
    private MaterialButton btnSave, btnDelete, btnFrontBodyMap, btnRearBodyMap;
    private TextView textHeaderTitle;
    private View btnBack, btnShare;

    private Repository repository;
    private int noteId = -1; // Default -1 for new note creation
    private boolean isEditing = false;

    // request codes used when launching body map activities
    private static final int REQUEST_CODE_FRONT_BODY = 101;
    private static final int REQUEST_CODE_BACK_BODY = 102;

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

        // allow internal scrolling inside editContent when inside ScrollView
        editContent.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            if ((event.getAction() & android.view.MotionEvent.ACTION_MASK) == android.view.MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                v.performClick();
            }
            return false;
        });
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
        btnFrontBodyMap = findViewById(R.id.btnFrontBodyMap);
        btnRearBodyMap = findViewById(R.id.btnRearBodyMap);
        textHeaderTitle = findViewById(R.id.textHeaderTitle);
        btnBack = findViewById(R.id.btnBack);
        btnShare = findViewById(R.id.btnShare);

        // back button click listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // share button click listener
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareOptions();
            }
        });

        // front body map button click listener
        btnFrontBodyMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteDetailsActivity.this, FrontBodyActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FRONT_BODY);
            }
        });

        // rear body map button click listener
        btnRearBodyMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteDetailsActivity.this, BackBodyActivity.class);
                startActivityForResult(intent, REQUEST_CODE_BACK_BODY);
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

            textHeaderTitle.setText(getString(R.string.edit_health_note));
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            isEditing = false;
            textHeaderTitle.setText(getString(R.string.add_health_note));
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

    // gets the selected body location result from FrontBodyActivity or BackBodyActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_FRONT_BODY || requestCode == REQUEST_CODE_BACK_BODY) {
                String location = data.getStringExtra("selectedBodyLocation");
                if (location != null) {
                    editBodyLocation.setText(location);
                }
            }
        }
    }

    // validates input fields and inserts a new note or updates it
    private void saveNote() {
        String rawTitle = editTitle.getText() != null ? editTitle.getText().toString() : "";
        String rawContent = editContent.getText() != null ? editContent.getText().toString() : "";
        String rawBodyLocation = editBodyLocation.getText() != null ? editBodyLocation.getText().toString() : "";
        String rawDate = editDate.getText() != null ? editDate.getText().toString() : "";

        // sanitize inputs
        String title = SecurityUtils.sanitizeInput(rawTitle);
        String content = SecurityUtils.sanitizeInput(rawContent);
        String bodyLocation = SecurityUtils.sanitizeInput(rawBodyLocation);
        String date = SecurityUtils.sanitizeInput(rawDate);

        // make sure title valid
        if (title.isEmpty()) {
            editTitle.setError("Title is required");
            editTitle.requestFocus();
            return;
        }

        if (title.length() > 100) {
            editTitle.setError("Title cannot exceed 100 characters");
            editTitle.requestFocus();
            return;
        }

        if (content.length() > 5000) {
            editContent.setError("Note content cannot exceed 5000 characters");
            editContent.requestFocus();
            return;
        }

        if (!SecurityUtils.isValidText(title, 100) || !SecurityUtils.isValidText(content, 5000)) {
            Toast.makeText(this, "Input contains invalid characters", Toast.LENGTH_SHORT).show();
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

    // shows a dialog with sharing options (Email, Clipboard, Print, Other Apps) populated with note details
    private void showShareOptions() {
        String title = editTitle.getText() != null ? editTitle.getText().toString().trim() : "";
        String content = editContent.getText() != null ? editContent.getText().toString().trim() : "";
        String bodyLocation = editBodyLocation.getText() != null ? editBodyLocation.getText().toString().trim() : "";
        String date = editDate.getText() != null ? editDate.getText().toString().trim() : "";
        String noteType = radioInjury.isChecked() ? "Injury" : "Illness";

        int severity = 3;
        int checkedSeverityId = radioGroupSeverity.getCheckedRadioButtonId();
        if (checkedSeverityId == R.id.radioSeverity1) severity = 1;
        else if (checkedSeverityId == R.id.radioSeverity2) severity = 2;
        else if (checkedSeverityId == R.id.radioSeverity3) severity = 3;
        else if (checkedSeverityId == R.id.radioSeverity4) severity = 4;
        else if (checkedSeverityId == R.id.radioSeverity5) severity = 5;

        final int finalSeverity = severity;

        String shareText = "Health Note: " + (title.isEmpty() ? "Untitled" : title) + "\n" +
                "Type: " + noteType + "\n" +
                "Body Location: " + (bodyLocation.isEmpty() ? "N/A" : bodyLocation) + "\n" +
                "Date: " + date + "\n" +
                "Severity: " + finalSeverity + "/5\n\n" +
                "Details:\n" + (content.isEmpty() ? "N/A" : content);

        CharSequence[] options = {"Share via E-mail", "Copy to Clipboard", "Print Note", "Other Share Apps"};
        new AlertDialog.Builder(NoteDetailsActivity.this)
                .setTitle("Share Note Details")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Email
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(android.net.Uri.parse("mailto:"));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Health Note: " + (title.isEmpty() ? "Note" : title));
                        emailIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                        try {
                            startActivity(emailIntent);
                        } catch (android.content.ActivityNotFoundException e) {
                            Intent sendIntent = new Intent(Intent.ACTION_SEND);
                            sendIntent.setType("text/plain");
                            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Health Note: " + (title.isEmpty() ? "Note" : title));
                            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                            startActivity(Intent.createChooser(sendIntent, "Share via E-mail"));
                        }
                    } else if (which == 1) {
                        // Clipboard
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("Health Note", shareText);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(NoteDetailsActivity.this, "Note copied to clipboard", Toast.LENGTH_SHORT).show();
                    } else if (which == 2) {
                        // Print
                        android.print.PrintManager printManager = (android.print.PrintManager) getSystemService(PRINT_SERVICE);
                        String printJobName = getString(R.string.app_name) + " - " + (title.isEmpty() ? "Health Note" : title);
                        android.webkit.WebView webView = new android.webkit.WebView(NoteDetailsActivity.this);
                        String htmlDocument = "<html><body>" +
                                "<h2>" + android.text.TextUtils.htmlEncode(title.isEmpty() ? "Health Note" : title) + "</h2>" +
                                "<p><b>Type:</b> " + android.text.TextUtils.htmlEncode(noteType) + "</p>" +
                                "<p><b>Body Location:</b> " + android.text.TextUtils.htmlEncode(bodyLocation) + "</p>" +
                                "<p><b>Date:</b> " + android.text.TextUtils.htmlEncode(date) + "</p>" +
                                "<p><b>Severity:</b> " + finalSeverity + "/5</p>" +
                                "<p><b>Details:</b><br/>" + android.text.TextUtils.htmlEncode(content).replace("\n", "<br/>") + "</p>" +
                                "</body></html>";
                        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);
                        android.print.PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(printJobName);
                        if (printManager != null) {
                            printManager.print(printJobName, printAdapter, new android.print.PrintAttributes.Builder().build());
                        }
                    } else if (which == 3) {
                        // Other Apps
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Health Note: " + (title.isEmpty() ? "Note" : title));
                        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                        startActivity(Intent.createChooser(sendIntent, "Share Note"));
                    }
                })
                .show();
    }
}
