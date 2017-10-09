package com.example.kakyenlam.friendfinder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Reminder extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(1);
        createReminder();
        finish();

    }

    public void createReminder() {
        long milliReminder = 0;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String alarmReminderMin = preferences.getString("alarmReminderMin", " ");

        SimpleDateFormat f = new SimpleDateFormat("mm");
        try {
            Date d = f.parse(alarmReminderMin);
            milliReminder = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar c = Calendar.getInstance();
        Date currentDate = c.getTime();
        long milliCurrent = currentDate.getTime();

        long milliAlarm = milliCurrent + milliReminder;
        Intent notifIntent = new Intent(this, NotificationService.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, notifIntent, 0);

        //set the alarm for particular time
        alarmManager.set(AlarmManager.RTC_WAKEUP, milliAlarm, pendingIntent);

    }
}
