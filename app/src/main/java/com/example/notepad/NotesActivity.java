package com.example.notepad;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class NotesActivity extends AppCompatActivity {

    // Deklarerar
    EditText titleInput;
    EditText contentInput;

    String originalTitle = null;


    // Skapar en DataManager för att hantera notes
    DataManager dataManager = new DataManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Kopplar och initialiserar

        titleInput = findViewById(R.id.et_title_input);
        contentInput = findViewById(R.id.et_content_input);
        Button deleteNote = findViewById(R.id.btn_delete);
        Button saveNote = findViewById(R.id.btn_save_note);
        Button clearNote = findViewById(R.id.btn_clear_note);
        Button cancelNote = findViewById(R.id.btn_cancel);

        // Skapar eller referar till ett directory där filerna är sparade
        File file = new File(NotesActivity.this.getFilesDir(), "myNotes");
        if (!file.exists()) {
            file.mkdir();
        }

        // Hämtar note titeln från setOnItemClickListener.
        String titleFromMain = getIntent().getStringExtra("NoteTitle");


        // när Delete knappen trycks tas title och content filerna samt noten från DataManager listan bort.
        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File directory = new File(getFilesDir(), "myNotes");
                File titleFile = new File(directory, titleFromMain + ".txt");
                File contentFile = new File(directory, titleFromMain + "_content.txt");

                // Ta bort title och content filerna om de existerar.
                if (titleFile.exists()) {
                    titleFile.delete();
                }
                if (contentFile.exists()) {
                    contentFile.delete();
                }

                // tar bor noten från DataManager listan.
                dataManager.deleteNote(titleFromMain);

                finish();
            }
        });

        // När save clickas sparas både title och content som filer samt till DataManager listan.
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String content = contentInput.getText().toString();

                if (!title.isEmpty() && !content.isEmpty()) {
                    try {

                        dataManager.deleteNoteFile(NotesActivity.this, titleFromMain);

                        dataManager.deleteNote(titleFromMain);

                        // Spara title och content till filer.
                        File titleFile = new File(file, title + ".txt");
                        FileWriter titleWriter = new FileWriter(titleFile);
                        titleWriter.write(title);
                        titleWriter.close();

                        File contentFile = new File(file, title + "_content.txt");
                        FileWriter contentWriter = new FileWriter(contentFile);
                        contentWriter.write(content);
                        contentWriter.close();

                        // Kollar ifall en note med samma titel finns, om den gör det uppdatera content, annars skapas en ny note.
                        Notes existingNote = null;

                        for (Notes note : DataManager.notes) {
                            if (note.getTitle().equals(title)) {
                                existingNote = note;
                                break;
                            }
                        }

                        if (existingNote != null) {
                            existingNote.setContent(content);
                        } else {
                            dataManager.createNote(title, content);
                        }


                        Toast.makeText(NotesActivity.this, "Note saved!", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(NotesActivity.this, "Error saving note.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NotesActivity.this, "Please input text in both title and content fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // När man clickar Clear sätts både titleInput och contentInput på blank.
        clearNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleInput.setText("");
                contentInput.setText("");
            }
        });

        // Cancel avbryter vad man gör och navigerar till MainActivity
        cancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // Ifall en notes title skickas från MainActivity laddas den notens title och content in genom loadNoteWithTitle metoden.
        if (titleFromMain != null) {
            originalTitle = titleFromMain;
            loadNoteTitleAndContent(titleFromMain);
        }
    }

    // Laddar notens content och title från filer.
    private void loadNoteTitleAndContent(String title) {
        File directory = new File(getFilesDir(), "myNotes");
        try {

            Scanner titleScanner = new Scanner(new File(directory, title + ".txt"));
            StringBuilder titleBuilder = new StringBuilder();
            while (titleScanner.hasNextLine()) {
                titleBuilder.append(titleScanner.nextLine());
                if (titleScanner.hasNextLine()) {
                    titleBuilder.append("\n");  // Append newline if there are more lines to read
                }
            }
            titleInput.setText(titleBuilder.toString());


            // For the content
            Scanner contentScanner = new Scanner(new File(directory, title + "_content.txt"));
            StringBuilder contentBuilder = new StringBuilder();
            while (contentScanner.hasNextLine()) {
                contentBuilder.append(contentScanner.nextLine());
                if (contentScanner.hasNextLine()) {
                    contentBuilder.append("\n");  // Append newline if there are more lines to read
                }
            }
            contentInput.setText(contentBuilder.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
