package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main menu class containing all the necessary main options for the program
 *
 * Created by Ka Kyen Lam on 3/09/2017.
 */
public class MainMenu extends AppCompatActivity {

    private SharedPreferences preferences;
    Timer mTimer;

    //View variables
    Button addFriendButton;
    Button friendListButton;
    Button scheduleMeetingButton;
    Button meetingListButton ;
    Button checkMapButton;
    Button userSettingsButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mTimer = new Timer();
        autoSuggest();
        //Connecting view variables to Buttons
        addFriendButton = (Button) findViewById(R.id.addFriendButton);
        friendListButton = (Button) findViewById(R.id.friendListButton);
        scheduleMeetingButton = (Button) findViewById(R.id.scheduleMeetingButton);
        meetingListButton = (Button) findViewById(R.id.meetingListButton);
        checkMapButton = (Button) findViewById(R.id.checkMapButton);
        userSettingsButton = (Button) findViewById(R.id.userSettingsButton);

        //Activities after clicking the Buttons
        addFriendButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                addFriendActivity(v);
            }
        });
        friendListButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                friendListActivity(v);
            }
        });
        scheduleMeetingButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                scheduleMeetingActivity(v);
            }
        });
        meetingListButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                meetingListActivity(v);
            }
        });
        checkMapButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                suggestMeetingMapActivity(v);
            }
        });
        userSettingsButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                userSettingsActivity(v);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTimer = new Timer();
        autoSuggest();
    }

    /**
     * Starts AddFriend activity
     *
     * @param view Listener View object
     */
    public void addFriendActivity(View view)
    {
        mTimer.cancel();
        Intent myIntent = new Intent(this, AddFriend.class);
        this.startActivityForResult(myIntent, 1);
    }

    /**
     * Starts FriendList activity
     *
     * @param view Listener View object
     */
    public void friendListActivity(View view)
    {
        mTimer.cancel();
        Intent myIntent = new Intent(this, FriendList.class);
        this.startActivityForResult(myIntent, 1);
    }

    /**
     * Starts ScheduleMeeting activity
     *
     * @param view Listener View object
     */
    public void scheduleMeetingActivity(View view)
    {
        mTimer.cancel();
        Intent myIntent = new Intent(this, ScheduleMeeting.class);
        this.startActivityForResult(myIntent, 1);
    }

    /**
     * Starts MeetingList activity
     *
     * @param view Listener View object
     */
    public void meetingListActivity(View view)
    {
        mTimer.cancel();
        Intent myIntent = new Intent(this, MeetingList.class);
        this.startActivityForResult(myIntent, 1);
    }

    /**
     * Starts SuggestMeetingMap activity
     *
     * @param view Listener View object
     */
    public void suggestMeetingMapActivity(View view)
    {
        mTimer.cancel();
        Intent myIntent = new Intent(this, SuggestMeetingMap.class);
        this.startActivityForResult(myIntent, 1);
    }

    /**
     * Starts UserSettings activity
     *
     * @param view Listener View object
     */
    public void userSettingsActivity(View view)
    {
        mTimer.cancel();
        Intent myIntent = new Intent(this, UserSettings.class);
        this.startActivityForResult(myIntent, 1);

    }

    public void autoSuggest() {

        long milliSuggest = 0;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String suggestionSec = preferences.getString("suggestionSec", " ");

        SimpleDateFormat f = new SimpleDateFormat("ss");
        try {
            Date d = f.parse(suggestionSec);
            milliSuggest = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mTimer.schedule(new PeriodicTask(), milliSuggest);
    }

    private class PeriodicTask extends TimerTask {

        View v;
        @Override
        public void run() {
            suggestMeetingMapActivity(v);

        }
    }

}
