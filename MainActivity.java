
package com.example.mynotesapp;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnAddNote;
    ListView listNotes;
    TextView tvEmpty;

    ArrayList<String> noteTitles;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddNote = findViewById(R.id.btnAddNote);
        listNotes = findViewById(R.id.listNotes);
        tvEmpty = findViewById(R.id.tvEmpty);

        noteTitles = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                noteTitles
        );

        listNotes.setAdapter(adapter);
        listNotes.setOnItemClickListener((parent, view, position, id) -> {

            try {
                SharedPreferences prefs =
                        getSharedPreferences("MyNotes", MODE_PRIVATE);

                String savedNotes = prefs.getString("notes", "[]");

                JSONArray notes = new JSONArray(savedNotes);

                JSONObject note = notes.getJSONObject(position);

                String title = note.getString("title");
                String content = note.getString("content");

                Intent intent = new Intent(MainActivity.this,
                        ViewNoteActivity.class);

                intent.putExtra("title", title);
                intent.putExtra("content", content);

                startActivity(intent);

            } catch (Exception e) {
                Toast.makeText(this,
                        "Unable to open note",
                        Toast.LENGTH_SHORT).show();
            }

        });

        btnAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddNoteActivity.class
            );
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {

        noteTitles.clear();

        SharedPreferences prefs =
                getSharedPreferences("MyNotes", MODE_PRIVATE);

        String savedNotes = prefs.getString("notes", "[]");

        try {
            JSONArray notes = new JSONArray(savedNotes);

            for (int i = 0; i < notes.length(); i++) {

                JSONObject note = notes.getJSONObject(i);

                String title = note.getString("title");

                noteTitles.add(title);
            }

        } catch (Exception e) {
            Toast.makeText(this,
                    "Error loading notes",
                    Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();

        if (noteTitles.isEmpty()) {
            tvEmpty.setVisibility(TextView.VISIBLE);
            listNotes.setVisibility(ListView.GONE);
        } else {
            tvEmpty.setVisibility(TextView.GONE);
            listNotes.setVisibility(ListView.VISIBLE);
        }
    }
}
