package com.example.a16011056_notereminder.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.example.a16011056_notereminder.model.Note;

@Database(entities = Note.class, version = 1)
public abstract class NotesDB extends RoomDatabase {

    public abstract NotesDAO notesDAO();

    public static final String DATABASE_NAME = "notesDB";
    private static NotesDB instance;

    public static NotesDB getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, NotesDB.class, DATABASE_NAME)
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
}
