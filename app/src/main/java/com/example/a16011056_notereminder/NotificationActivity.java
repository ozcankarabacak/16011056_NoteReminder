package com.example.a16011056_notereminder;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity {

    private EditText title, text, date, hour;
    private String time;
    private Button button;
    private NotificationManager nManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        title = findViewById(R.id.notification_title);
        text = findViewById(R.id.notification_text);
        date = findViewById(R.id.notification_time);
        hour = findViewById(R.id.notification_hour);
        button = findViewById(R.id.button);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                final int day = c.get(Calendar.DAY_OF_MONTH);
                final int month = c.get(Calendar.MONTH);
                final int year = c.get(Calendar.YEAR);

                final DatePickerDialog dpd = new DatePickerDialog(NotificationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int sYear, int sMonth, int sDay) {
                        sMonth+=1;
                        time = String.format("%02d/%02d/%d", sDay, sMonth, sYear);
                        date.setText(time);
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                final int mHour = c.get(Calendar.HOUR_OF_DAY);
                final int mMin = c.get(Calendar.MINUTE);

                final TimePickerDialog tpd = new TimePickerDialog(NotificationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, mHour, mMin, true);
                tpd.show();
            }
        });
        final Context context = this;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = "ozcan";
                String channelTitle ="Notification Channel";
                Intent intent;
                PendingIntent pendingIntent;
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder builder;
                int importance = NotificationManager.IMPORTANCE_HIGH;
                if (nManager == null) {
                    nManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                }
                NotificationChannel mChannel = nManager.getNotificationChannel(id);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, channelTitle, importance);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.enableLights(true);


                    nManager.createNotificationChannel(mChannel);
                }

                builder = new NotificationCompat.Builder(context, id);
                intent = new Intent(context, NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(context, 1, intent, 0);

                builder.setContentTitle(title.getText().toString())
                        .setSmallIcon(R.drawable.notification)
                        .setColor(0xFFFF0000)
                        .setContentText(text.getText())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker("ticker")
                        .setLights(0xFF00FF00, 100, 50)
                        .setSound(alarmSound);
                Notification notification = builder.build();
                nManager.notify(1, notification);
                finish();
            }

        });

    }
}
