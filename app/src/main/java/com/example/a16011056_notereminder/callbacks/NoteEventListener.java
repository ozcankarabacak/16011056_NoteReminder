package com.example.a16011056_notereminder.callbacks;

import com.example.a16011056_notereminder.model.Note;

public interface NoteEventListener {

    void onNoteClick(Note note);

    void onNoteLongClick(Note note);
}
