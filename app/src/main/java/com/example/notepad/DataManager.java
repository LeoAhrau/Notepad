package com.example.notepad;

import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class DataManager{

    private static final String DIRECTORY_NAME = "myNotes";
    public static ArrayList<Notes> notes = new ArrayList<>();

    public Notes createNote(String title, String content){
        Notes note = new Notes(title, content);
        notes.add(note);

        return note;
    }

    public void loadNotes(Context context){
        File directory = new File(context.getFilesDir(), "myNotes");
        if(directory.exists()) {
            File[] files = directory.listFiles();
            for(File file : files) {
                // For each som går igenom filerna i directory..
                if(file.getName().endsWith("_content.txt")) {
                    String title = file.getName().replace("_content.txt", "");
                    try {
                        // Läser in title och content från varje not och addar dem till notes listan
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
    }

    public void deleteNote(String title) {
        Iterator<Notes> iterator = notes.iterator();
        while (iterator.hasNext()) {
            Notes note = iterator.next();
            if (note.getTitle().equals(title)) {
                iterator.remove();
            }
        }
    }

    public void deleteNoteFile(Context context, String titleFromMain) {
        File directory = getOrCreateDirectory(context);
        File titleFile = new File(directory, titleFromMain + ".txt");
        File contentFile = new File(directory, titleFromMain + "_content.txt");

        if (titleFile.exists()) titleFile.delete();
        if (contentFile.exists()) contentFile.delete();
    }
    private File getOrCreateDirectory(Context context) {
        File directory = new File(context.getFilesDir(), DIRECTORY_NAME);
        if (!directory.exists()) {
            directory.mkdir();
        }
        return directory;
    }
}
