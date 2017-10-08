package com.example.kakyenlam.friendfinder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/*http://androidideasblog.blogspot.com.au/2011/07/alarmmanager-and-notificationmanager.html*/

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager mNM;
        mNM = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        // Set the icon, scrolling text and timestamp
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_meeting)
                        .setContentTitle("Upcoming meeting");

//        Define the Notification'
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MeetingList.class), 0);
        // Set the info for the views that show in the notification panel.


    }
}
