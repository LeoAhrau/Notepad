package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    ListView notesList;
    ArrayAdapter<Notes> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesList = findViewById(R.id.lv_notes);
        Button createNote = findViewById(R.id.btn_create_note);
        Button loadNote = findViewById(R.id.btn_load);

        noteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, DataManager.notes);
        notesList.setAdapter(noteAdapter);

        DataManager.notes.clear();
        File directory = new File(getFilesDir(), "text");
        if(directory.exists()) {
            File[] files = directory.listFiles();
            for(File file : files) {
                if(file.getName().endsWith("_content.txt")) {
                    String title = file.getName().replace("_content.txt", "");
                    try {
                        Scanner titleScanner = new Scanner(new File(directory, title + ".txt"));
                        String titleContent = titleScanner.nextLine();
                        titleScanner.close();

                        Scanner contentScanner = new Scanner(new File(directory, title + "_content.txt"));
                        String noteContent = contentScanner.nextLine();
                        contentScanner.close();

                        DataManager.notes.add(new Notes(titleContent, noteContent));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                startActivity(intent);

            }
        });

        loadNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteAdapter.notifyDataSetChanged();
            }
        });

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

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();

    }

}

//    private void loadNotesFromInternalStorage() {
//        DataManager.notes.clear();
//        File directory = new File(getFilesDir(), "text");
//        if(directory.exists()) {
//            File[] files = directory.listFiles();
//            for(File file : files) {
//                if(file.getName().endsWith("_content.txt")) {
//                    String title = file.getName().replace("_content.txt", "");
//                    try {
//                        Scanner titleScanner = new Scanner(new File(directory, title + ".txt"));
//                        String titleContent = titleScanner.nextLine();
//                        titleScanner.close();
//
//                        Scanner contentScanner = new Scanner(new File(directory, title + "_content.txt"));
//                        String noteContent = contentScanner.nextLine();
//                        contentScanner.close();
//
//                        DataManager.notes.add(new Notes(titleContent, noteContent));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }