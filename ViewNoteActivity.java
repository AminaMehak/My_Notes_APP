package com.example.mynotesapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

public class ViewNoteActivity extends AppCompatActivity {

    TextView tvViewTitle, tvViewContent;
    Button btnDeleteNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        tvViewTitle = findViewById(R.id.tvViewTitle);
        tvViewContent = findViewById(R.id.tvViewContent);
        btnDeleteNote = findViewById(R.id.btnDeleteNote);

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        tvViewTitle.setText(title);
        tvViewContent.setText(content);

        btnDeleteNote.setOnClickListener(v -> {

            new AlertDialog.Builder(ViewNoteActivity.this)
                    .setTitle("Delete Note")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Delete", (dialog, which) -> {

                        SharedPreferences prefs =
                                getSharedPreferences("MyNotes", MODE_PRIVATE);

                        String savedNotes = prefs.getString("notes", "[]");

                        try {
                            JSONArray notes = new JSONArray(savedNotes);

                            for (int i = 0; i < notes.length(); i++) {

                                JSONObject note = notes.getJSONObject(i);

                                if (note.getString("title").equals(title)
                                        && note.getString("content").equals(content)) {

                                    notes.remove(i);
                                    break;
                                }
                            }

                            prefs.edit()
                                    .putString("notes", notes.toString())
                                    .apply();

                            Toast.makeText(ViewNoteActivity.this,
                                    "Note Deleted Successfully!",
                                    Toast.LENGTH_SHORT).show();

                            finish();

                        } catch (Exception e) {
                            Toast.makeText(ViewNoteActivity.this,
                                    "Error deleting note",
                                    Toast.LENGTH_SHORT).show();
                        }

                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}