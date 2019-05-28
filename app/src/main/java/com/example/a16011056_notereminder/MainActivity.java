package com.example.a16011056_notereminder;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.example.a16011056_notereminder.adapters.NotesAdapter;
import com.example.a16011056_notereminder.callbacks.NoteEventListener;
import com.example.a16011056_notereminder.db.NotesDAO;
import com.example.a16011056_notereminder.db.NotesDB;
import com.example.a16011056_notereminder.model.Note;
import com.example.a16011056_notereminder.utils.NoteUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.a16011056_notereminder.EditNoteActivity.NOTE_EXTRA_KEY;

public class MainActivity extends AppCompatActivity implements NoteEventListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;
    private NotesDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //init recyclerview
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //init fab button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ADD NEW NOTE
                onAddNewNote();
            }
        });
        dao = NotesDB.getInstance(this).notesDAO();
    }

    private void loadNotes(int i, boolean resetNotes) {
        if(resetNotes) {
            this.notes = new ArrayList<>();
            List<Note> list;
            if (i == 0)
                list = dao.getNotesByDate();
            else
                list = dao.getNotesByPriority();
            this.notes.addAll(list);
        }
        this.adapter = new NotesAdapter(this, notes);
        recyclerView.setAdapter(adapter);
        this.adapter.setListener(this);
    }

    private void onAddNewNote() {
        startActivity(new Intent(this, EditNoteActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        android.support.v7.widget.SearchView sv = (android.support.v7.widget.SearchView) (menu.findItem(R.id.search).getActionView());
        SearchManager sm = (SearchManager) (getSystemService(Context.SEARCH_SERVICE));
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                notes = new ArrayList<>();
                List <Note> list = dao.getNotesBySearch(query);
                notes.addAll(list);
                loadNotes(0,false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                notes = new ArrayList<>();
                List <Note> list = dao.getNotesBySearch(newText);
                notes.addAll(list);
                loadNotes(0, false);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sortBy) {
            onSortBy();
        }

        return super.onOptionsItemSelected(item);
    }



    private void onSortBy() {
        new AlertDialog.Builder(this)
                .setTitle("Sort by")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("DATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadNotes(0,true);
                    }
                })
                .setNegativeButton("PRIORITY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadNotes(1,true);
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes(0, true);
    }

    @Override
    public void onNoteClick(Note note) {
        Intent edit = new Intent(this, EditNoteActivity.class);
        edit.putExtra(NOTE_EXTRA_KEY, note.getId());
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }
        edit.putExtra("image", note.getImagePath());
        edit.putExtra("priority", note.getPriority());
        edit.putExtra("video", note.getVideoPath());
        startActivity(edit);
    }


    @Override
    public void onNoteLongClick(final Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Note Reminder")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao.deleteNote(note);
                        loadNotes(0, true);
                    }
                })
                .setNegativeButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        String text = note.getNoteHeader() + "\n" +
                                      note.getNoteText() + "\n" +
                                      NoteUtils.dateFromLong(note.getNoteDate()) + "\n" +
                                      note.getImagePath() + "\n" +
                                      note.getVideoPath();
                        share.putExtra(Intent.EXTRA_TEXT, text);
                        startActivity(share);
                    }
                })
                .create()
                .show();

    }
}