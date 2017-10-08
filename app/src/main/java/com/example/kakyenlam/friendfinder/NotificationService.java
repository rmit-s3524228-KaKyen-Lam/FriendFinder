package com.example.kakyenlam.friendfinder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){

        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(), MeetingList.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification mBuilder =
                new NotificationCompat.Builder(this)
                .setContentTitle("Log Steps!")
                .setContentText("Log your steps for today")
                .setSmallIcon(R.drawable.ic_stat_meeting)
                        .setContentIntent(pIntent)
                        .build();


        mNM.notify(1, mBuilder);
    }
}
