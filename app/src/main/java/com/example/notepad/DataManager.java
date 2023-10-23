package com.example.notepad;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Iterator;

public class DataManager{

    public static ArrayList<Notes> notes = new ArrayList<>();

    public Notes createNote(String title, String content){
        Notes note = new Notes(title, content);
        notes.add(note);

        return note;
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
}
