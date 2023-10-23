package com.example.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class NotesActivity extends AppCompatActivity {

    EditText titleInput;
    EditText contentInput;


    DataManager dataManager = new DataManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        titleInput = findViewById(R.id.et_title_input);
        contentInput = findViewById(R.id.et_content_input);

        Button deleteNote = findViewById(R.id.btn_delete);
        Button saveNote = findViewById(R.id.btn_save_note);
        Button clearNote = findViewById(R.id.btn_clear_note);
        Button cancelNote = findViewById(R.id.btn_cancel);


        File file = new File(NotesActivity.this.getFilesDir(), "text");

        String titleFromIntent = getIntent().getStringExtra("NoteTitle");

        if (!file.exists()) {
            file.mkdir();
        }

        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File directory = new File(getFilesDir(), "text");
                File titleFile = new File(directory, titleFromIntent + ".txt");
                File contentFile = new File(directory, titleFromIntent + "_content.txt");
                if (titleFile.exists()) {
                    titleFile.delete();
                }
                if (contentFile.exists()) {
                    contentFile.delete();
                }
                dataManager.deleteNote(titleFromIntent);


                Intent intent = new Intent(NotesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String content = contentInput.getText().toString();

                if (!title.isEmpty() && !content.isEmpty()) {
                    try {
                        File titleFile = new File(file, title + ".txt");
                        FileWriter titleWriter = new FileWriter(titleFile);
                        titleWriter.write(title);
                        titleWriter.close();

                        File contentFile = new File(file, title + "_content.txt");
                        FileWriter contentWriter = new FileWriter(contentFile);
                        contentWriter.write(content);
                        contentWriter.close();

                        Notes existingNote = null;
                        for (Notes note : DataManager.notes) {
                            if (note.getTitle().equals(title)) {
                                existingNote = note;
                                break;
                            }
                        }

                        if (existingNote != null) {
                            // Update the content of the existing note
                            existingNote.setContent(content);
                        } else {
                            // If the note doesn't exist, create a new one
                            dataManager.createNote(title, content);
                        }


                        Toast.makeText(NotesActivity.this, "Note saved!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(NotesActivity.this, "Error saving note.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        cancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        if (titleFromIntent != null) {
            loadNoteWithTitle(titleFromIntent);
        }
    }

    private void loadNoteWithTitle(String title) {
        File directory = new File(getFilesDir(), "text");
        try {
            Scanner titleScanner = new Scanner(new File(directory, title + ".txt"));
            titleInput.setText(titleScanner.nextLine());
            titleScanner.close();

            Scanner contentScanner = new Scanner(new File(directory, title + "_content.txt"));
            contentInput.setText(contentScanner.nextLine());
            contentScanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}