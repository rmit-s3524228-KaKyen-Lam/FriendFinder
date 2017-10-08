package com.example.kakyenlam.friendfinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

/*https://stackoverflow.com/questions/15698790/broadcast-receiver-for-checking-internet-connection-in-android-app*/

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if(isNetworkAvailable(context))
        {
//            Toast.makeText(context, "Network Available! Time to meet some friends!", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(context, SuggestMeetingMap.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }

    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
