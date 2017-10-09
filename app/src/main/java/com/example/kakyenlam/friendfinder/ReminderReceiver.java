package com.example.kakyenlam.friendfinder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class ReminderReceiver extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", 0);

        System.out.println(notificationId);

        createReminder(context);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }

    public void createReminder(Context context) {
        long milliReminder = 0;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
        Intent notifIntent = new Intent(context, NotificationService.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, notifIntent, 0);

        //set the alarm for particular time
        alarmManager.set(AlarmManager.RTC_WAKEUP, milliAlarm, pendingIntent);

    }
}


