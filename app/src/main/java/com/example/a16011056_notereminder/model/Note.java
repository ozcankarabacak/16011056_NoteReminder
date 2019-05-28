package com.example.a16011056_notereminder.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id; //default



    @ColumnInfo(name = "color")
    private int color;
    @ColumnInfo(name = "priority")
    private int priority;

    @ColumnInfo(name = "header")
    private String noteHeader;

    @ColumnInfo(name = "text")
    private String noteText;

    @ColumnInfo(name = "date")
    private long noteDate;

    @ColumnInfo(name = "image")
    private String imagePath;

    @ColumnInfo(name = "video")
    private String videoPath;

    public Note() {

    }

    public Note(String noteHeader, String noteText, long noteDate, String imagePath, int priority, int color, String videoPath) {
        this.noteHeader = noteHeader;
        this.noteText = noteText;
        this.noteDate = noteDate;
        this.imagePath = imagePath;
        this.priority = priority;
        this.videoPath = videoPath;
        this.color = color;
    }

    public String getNoteHeader() {
        return noteHeader;
    }

    public void setNoteHeader(String noteHeader) {
        this.noteHeader = noteHeader;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
