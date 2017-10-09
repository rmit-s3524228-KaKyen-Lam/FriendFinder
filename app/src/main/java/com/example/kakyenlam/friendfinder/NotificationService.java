package com.example.kakyenlam.friendfinder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/*https://www.youtube.com/watch?v=tyVaPHv-RGo*/

public class NotificationService extends Service {

    private static final int NOTIFICATION_ID = 1;
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
        Intent meetingIntent = new Intent(this.getApplicationContext(), MeetingList.class);
        Intent remindIntent = new Intent(this.getApplicationContext(), Reminder.class);
        remindIntent.putExtra("notificationId",NOTIFICATION_ID);
        PendingIntent pIntentOpen = PendingIntent.getActivity(this, 0, meetingIntent, 0);
        PendingIntent pIntentRemind = PendingIntent.getActivity(this, 0, remindIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setContentTitle("You have an upcoming meeting.")
                .setSmallIcon(R.drawable.ic_stat_meeting)
                        .addAction(R.drawable.common_google_signin_btn_icon_dark, "Open", pIntentOpen)
                        .addAction(R.drawable.common_google_signin_btn_icon_dark, "Remind", pIntentRemind)
                        .setAutoCancel(true);


        mNM.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
