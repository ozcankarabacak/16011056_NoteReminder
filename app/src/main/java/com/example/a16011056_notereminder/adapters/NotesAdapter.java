package com.example.a16011056_notereminder.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a16011056_notereminder.R;
import com.example.a16011056_notereminder.callbacks.NoteEventListener;
import com.example.a16011056_notereminder.model.Note;
import com.example.a16011056_notereminder.utils.NoteUtils;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder>{

    private Context context;
    private ArrayList<Note> notes;
    private NoteEventListener listener;

    public NotesAdapter(Context context, ArrayList<Note> notes) {
        this.notes = notes;
        this.context = context;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_layout, parent,false);
        return new NoteHolder(v);
    }
    @Override
    public void onBindViewHolder(NoteHolder noteHolder, int position) {
        final Note note = getNote(position);
        if (note != null){
            noteHolder.ll.setBackgroundColor(note.getColor());
            noteHolder.noteHeader.setText(note.getNoteHeader());
            noteHolder.noteText.setText(note.getNoteText());
            noteHolder.noteDate.setText(NoteUtils.dateFromLong(note.getNoteDate()));
            noteHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNoteClick(note);
                }
            });
            noteHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onNoteLongClick(note);
                    return false;
                }
            });
        }
    }

    private Note getNote(int position){
        return notes.get(position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        TextView noteHeader, noteText, noteDate;
        LinearLayout ll;
        public NoteHolder(View itemView) {
            super(itemView);
            noteHeader = itemView.findViewById(R.id.note_header);
            noteText = itemView.findViewById(R.id.note_text);
            noteDate = itemView.findViewById(R.id.note_date);
            ll = itemView.findViewById(R.id.color_this);
        }
    }

    public void setListener(NoteEventListener listener) {
        this.listener = listener;
    }
}
