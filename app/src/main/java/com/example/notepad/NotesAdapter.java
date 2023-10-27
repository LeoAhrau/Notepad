package com.example.notepad;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends ArrayAdapter<Notes> {

    public NotesAdapter(Context context, List<Notes> notes) {
        super(context, 0, notes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_note,parent, false);
        }

        Notes currentNote = getItem(position);

        TextView noteTextView = listItemView.findViewById(R.id.note_text);
        noteTextView.setText(currentNote.getTitle());

        return listItemView;
    }
}