package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Main menu class containing all the necessary main options for the program
 *
 * Created by Ka Kyen Lam on 3/09/2017.
 */
public class MainMenu extends AppCompatActivity {

    //View variables
    Button addFriendButton;
    Button friendListButton;
    Button scheduleMeetingButton;
    Button meetingListButton ;
    Button findFriendButton;
    Button checkMapButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Connecting view variables to Buttons
        addFriendButton = (Button) findViewById(R.id.addFriendButton);
        friendListButton = (Button) findViewById(R.id.friendListButton);
        scheduleMeetingButton = (Button) findViewById(R.id.scheduleMeetingButton);
        meetingListButton = (Button) findViewById(R.id.meetingListButton);
        findFriendButton = (Button) findViewById(R.id.findFriendButton);
        checkMapButton = (Button) findViewById(R.id.checkMapButton);

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
        findFriendButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                findFriendActivity(v);
            }
        });
        checkMapButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                checkMapActivity(v);
            }
        });
    }

    /**
     * Starts AddFriend activity
     *
     * @param view Listener View object
     */
    public void addFriendActivity(View view)
    {
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
        Intent myIntent = new Intent(this, MeetingList.class);
        this.startActivityForResult(myIntent, 1);
    }

    /**
     * Starts FindFriend activity
     *
     * @param view Listener View object
     */
    public void findFriendActivity(View view)
    {
        Intent myIntent = new Intent(this, FindFriend.class);
        this.startActivityForResult(myIntent, 1);
    }

    /**
     * Starts CheckMap activity
     *
     * @param view Listener View object
     */
    public void checkMapActivity(View view)
    {
//        Intent myIntent = new Intent(this, MapsActivity.class);
//        this.startActivityForResult(myIntent, 1);
    }

}
