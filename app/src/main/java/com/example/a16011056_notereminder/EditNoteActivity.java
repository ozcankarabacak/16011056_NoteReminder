package com.example.a16011056_notereminder;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.a16011056_notereminder.db.NotesDAO;
import com.example.a16011056_notereminder.db.NotesDB;
import com.example.a16011056_notereminder.model.Note;

import java.util.ArrayList;
import java.util.Date;

import petrov.kristiyan.colorpicker.ColorPicker;

public class EditNoteActivity extends AppCompatActivity {
    private EditText inputHeader, inputText;
    private ImageView image;
    private VideoView video;
    private SeekBar priority;
    private NotesDAO dao;
    private Note temp;
    public static final String NOTE_EXTRA_KEY = "note_id";
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        inputHeader = findViewById(R.id.input_header);
        inputText = findViewById(R.id.input_text);
        priority = findViewById(R.id.priority);
        image = findViewById(R.id.imageView);
        video = findViewById(R.id.videoView);
        dao = NotesDB.getInstance(this).notesDAO();
        if (getIntent().getExtras() != null){
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_KEY, 0);
            temp = dao.getNoteById(id);
            String imgPath = getIntent().getExtras().getString("image");
            String vidPath = getIntent().getExtras().getString("video");
            int pri = getIntent().getExtras().getInt("priority");
            inputHeader.setText(temp.getNoteHeader());
            inputText.setText(temp.getNoteText());
            priority.setProgress(pri);
            if(imgPath != null)
                image.setImageURI(Uri.parse(imgPath));
            if(vidPath != null) {
                video.setVideoURI(Uri.parse(vidPath));
                video.start();
            }
        }else
            temp = new Note();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note)
            onSaveNote();
        else if (id == R.id.upload_img)
            onUploadImg();
        else if (id == R.id.change_color)
            onChangeColor();
        else if(id == R.id.upload_video)
            onUploadVideo();
        else if(id == R.id.notification)
            onAddNotification();
        return super.onOptionsItemSelected(item);
    }

    private void onAddNotification() {
        Intent i = new Intent(this, NotificationActivity.class);
        startActivity(i);
    }


    public void onChangeColor() {
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#258174");
        colors.add("#3C8D2F");
        colors.add("#20724F");
        colors.add("#6a3ab2");
        colors.add("#323299");
        colors.add("#800080");
        colors.add("#b79716");
        colors.add("#966d37");
        colors.add("#b77231");
        colors.add("#808000");

        colorPicker.setColors(colors)
                .setColumns(5)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        temp.setColor(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();


    }


    private void onUploadImg() {
        Intent galleryInt = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryInt, 0);
    }

    private void onUploadVideo() {

        Intent videoInt = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(videoInt, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0){
            imageUri = data.getData();
            image.setImageURI(imageUri);
            video.start();
        }else if (resultCode == RESULT_OK && requestCode == 1){
            videoUri = data.getData();
            video.setVideoURI(videoUri);
            video.start();
        }
    }

    private void onSaveNote() {
        String header = inputHeader.getText().toString();
        String text = inputText.getText().toString();
        int pr = priority.getProgress();
    if (!header.isEmpty() || !text.isEmpty() || temp.getImagePath() != null || imageUri != null || videoUri != null || temp.getVideoPath() != null){
            long date = new Date().getTime();
            temp.setNoteHeader(header);
            temp.setNoteText(text);
            temp.setNoteDate(date);
            temp.setPriority(pr);
            if (imageUri != null)
                temp.setImagePath(imageUri.toString());
            if (videoUri != null)
                temp.setVideoPath(videoUri.toString());
            Note n = dao.getNoteById(temp.getId());
            if (n == null)
                dao.insertNote(temp);
            else
                dao.updateNote(temp);
            finish();
        }

    }
}
