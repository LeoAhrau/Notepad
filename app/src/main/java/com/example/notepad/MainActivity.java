package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


//byggd enligt MVC
public class MainActivity extends AppCompatActivity {

    // Deklarera ListView och Adapter.
    ListView notesList;
    NotesAdapter notesAdapter;
    DataManager dataManager = new DataManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Koppla ListView och Knapparna till elementen i xml filen.
        notesList = findViewById(R.id.lv_notes);
        Button createNote = findViewById(R.id.btn_create_note);

        // Initialisera Adaptern med notes listan från DataManager.
        notesAdapter = new NotesAdapter(this, DataManager.notes);
        notesList.setAdapter(notesAdapter);

        // Rensa existerande notes i DataManager för att förhindra kopior.
        DataManager.notes.clear();

        // Skapar en directory referens för att läsa sparade notes.
        dataManager.loadNotes(this);

        // När createNote clickas navigerar man till NotesActivity.
        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                startActivity(intent);
            }
        });

        // När en note i listviewen clickas navigerar man till NotesActivity och skickar med NoteTitle.
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notes selectedNote = (Notes) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                intent.putExtra("NoteTitle", selectedNote.getTitle());
                startActivity(intent);
            }
        });
    }

    // Varje gång man kommer tillbaka till MainActivity uppdaterar man listan genom notifyDataSetChanged.
    @Override
    protected void onResume() {
        super.onResume();
        notesAdapter.notifyDataSetChanged();
    }
}