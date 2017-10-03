package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import model.Database;
import model.Meeting;
import controller.*;

/**
 * Handles the operation of editing Meeting object
 * <p>
 * Created by Ka Kyen Lam on 3/09/2017.
 */
public class EditMeeting extends AppCompatActivity {

    //Class-use variables
    protected static final int EDIT_MEETING = 21;
    private static ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> nameList = new ArrayList<>();
    private String selectedId;
    private Meeting selectedMeeting;
    private String title;
    private Date startTime;
    private Date endTime;
    private Database db;

    //View variables
    static TextView locationInput;
    static TextView startTimeInput;
    static TextView endTimeInput;
    private TextView meetingTitleInput;
    private TextView dateInput;
    private ListView inviteListInput;
    Button locationButton;
    Button startTimeButton;
    Button endTimeButton;
    Button inviteFriendButton;
    Button removeInviteButton;
    Button confirmMeetingChangeButton;
    Button backButton;
    Bundle prevBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);
        db = new Database (this);

        //Connecting View variables to TextViews/ImageViews
        meetingTitleInput = (TextView) findViewById(R.id.meetingEditTitleField);
        locationInput = (TextView) findViewById(R.id.meetingLocationEditField);
        dateInput = (TextView) findViewById(R.id.dateEditField);
        startTimeInput = (TextView) findViewById(R.id.startTimeEditField);
        endTimeInput = (TextView) findViewById(R.id.endTimeEditField);
        inviteListInput = (ListView) findViewById(R.id.inviteListEdit);

        //Connecting View variables to Buttons
        locationButton = (Button) findViewById(R.id.locationEditButton);
        startTimeButton = (Button) findViewById(R.id.startTimeEditButton);
        endTimeButton = (Button) findViewById(R.id.endTimeEditButton);
        inviteFriendButton = (Button) findViewById(R.id.inviteFriendEditButton);
        removeInviteButton = (Button) findViewById(R.id.removeInviteEditButton);
        confirmMeetingChangeButton = (Button) findViewById(R.id.confirmMeetingChangeButton);
        backButton = (Button) findViewById(R.id.editMeetingBackButton);

        //Getting data from previous Intent
        Intent prevIntent = getIntent();
        prevBundle = prevIntent.getExtras();
        if (prevBundle != null) {
            setup();
        }

        //Activities after clicking the Buttons
        locationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locationListActivity(v);
            }
        });

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialog(getString(R.string.start_time_edit));

            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialog(getString(R.string.end_time_edit));

            }
        });

        inviteFriendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                friendListActivity(v);

            }
        });

        removeInviteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inviteListActivity(v);

            }
        });

        confirmMeetingChangeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                title = meetingTitleInput.getText().toString();
                startTime = TimeConverter.stringToDateTimeConverter(dateInput.getText().toString(), startTimeInput.getText().toString());
                endTime = TimeConverter.stringToDateTimeConverter(dateInput.getText().toString(), endTimeInput.getText().toString());

                if (title.isEmpty()) { //If title is empty
                    ToastCreator.createToast(getApplicationContext(), getString(R.string.incomplete_message));
                } else if (startTime.compareTo(endTime) > 0 || startTime.compareTo(endTime) == 0) { //Check for invalid time selection
                    ToastCreator.createToast(getApplicationContext(), getString(R.string.invalid_time));
                } else if (nameList.isEmpty()) { //Check if invite list is empty
                    ToastCreator.createToast(getApplicationContext(), getString(R.string.empty_invite));
                } else {
                    selectedMeeting.setTitle(title);
                    selectedMeeting.setStartTime(startTime);
                    selectedMeeting.setEndTime(endTime);
                    selectedMeeting.setFriend(nameList);
                    selectedMeeting.setLocation(locationInput.getText().toString());
                    db.updateMeeting(selectedMeeting);
                    ToastCreator.createToast(getApplicationContext(), getString(R.string.edit_meeting_message));
                    finish();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Shows TimePickerDialog for user to select time
     *
     * @param tag tag to identify activity source
     */
    public void showTimePickerDialog(String tag) {
        DialogFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getSupportFragmentManager(), tag);
    }

    /**
     * Starts FriendList activity
     *
     * @param view Listener View object
     */
    public void friendListActivity(View view) {
        Intent myIntent = new Intent(this, FriendList.class);
        myIntent.putExtra(getString(R.string.request_code), EDIT_MEETING);
        this.startActivityForResult(myIntent, 1);
    }

    /**
     * Starts LocationList activity
     *
     * @param view Listener View object
     */
    public void locationListActivity(View view) {
        Intent myIntent = new Intent(this, LocationList.class);
        myIntent.putExtra(getString(R.string.request_code), EDIT_MEETING);
        this.startActivityForResult(myIntent, 1);
    }

    /**
     * Setup the data for the view fields
     */
    public void setup() {
        selectedId = (String) prevBundle.get(getString(R.string.id));
        selectedMeeting = db.getMeeting(selectedId);
        meetingTitleInput.setText(selectedMeeting.getTitle());
        locationInput.setText(selectedMeeting.getLocation());
        dateInput.setText(TimeConverter.dateTimeToStringConverter(selectedMeeting.getStartTime())[0]);
        startTimeInput.setText(TimeConverter.dateTimeToStringConverter(selectedMeeting.getStartTime())[1]);
        endTimeInput.setText(TimeConverter.dateTimeToStringConverter(selectedMeeting.getEndTime())[1]);
        nameList = selectedMeeting.getFriend();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameList);
        inviteListInput.setAdapter(arrayAdapter);
    }

    /**
     * Starts InviteList activity
     *
     * @param view Listener View object
     */
    public void inviteListActivity(View view) {
        Intent myIntent = new Intent(this, InviteList.class);
        Bundle inviteData = new Bundle();
        inviteData.putStringArrayList(getString(R.string.list), nameList);
        inviteData.putInt(getString(R.string.request_code), EDIT_MEETING);
        myIntent.putExtras(inviteData);
        this.startActivityForResult(myIntent, 1);
    }

    public static TextView getStartTimeInput() {
        return startTimeInput;
    }

    public static TextView getEndTimeInput() {
        return endTimeInput;
    }

    public static ArrayAdapter<String> getArrayAdapter() {
        return arrayAdapter;
    }

    public static TextView getLocationInput() {
        return locationInput;
    }

}
