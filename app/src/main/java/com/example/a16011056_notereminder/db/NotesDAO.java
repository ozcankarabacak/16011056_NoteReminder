package com.example.a16011056_notereminder.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.a16011056_notereminder.model.Note;

import java.util.List;

@Dao
public interface NotesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);

    @Query("SELECT * FROM notes ORDER BY date DESC")
    List<Note> getNotesByDate();


    @Query("SELECT * FROM NOTES WHERE id = :noteId")
    Note getNoteById(int noteId);

    @Query("DELETE FROM notes WHERE id = :noteId")
    void deleteNoteById(int noteId);

    @Query("SELECT * FROM notes ORDER BY priority DESC")
    List<Note> getNotesByPriority();

    @Query("SELECT * FROM notes WHERE header LIKE '%' || :search || '%' OR text LIKE '%' || :search || '%'")
    List<Note> getNotesBySearch(String search);
}
