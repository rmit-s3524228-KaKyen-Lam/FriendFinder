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
import java.util.Calendar;
import java.util.Date;

import model.Database;
import model.Meeting;
import controller.*;

/*https://stackoverflow.com/questions/8452526/android-can-i-use-putextra-to-pass-multiple-values*/

/**
 * Handles the operation of adding Meeting object to the program
 * <p>
 * Created by Ka Kyen Lam on 3/09/2017.
 */
public class ScheduleMeeting extends AppCompatActivity {

    //Class-use variables
    protected static final int SCHEDULE_MEETING = 11;
    private static ArrayAdapter<String> arrayAdapter;
    private String title;
    private String location;
    private Date startTime;
    private Date endTime;
    ArrayList<String> inviteList = new ArrayList<>();
    int year;
    int month;
    int day;
    private Database db;

    //View variables
    static TextView startTimeInput;
    static TextView endTimeInput;
    static TextView locationInput;
    private TextView meetingTitleInput;
    private TextView dateInput;
    ListView inviteListInput;
    Button locationButton;
    Button startTimeButton;
    Button endTimeButton;
    Button inviteFriendButton;
    Button removeInviteButton;
    Button createMeetingButton;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_meeting);


        //Connecting View variables to TextViews/ImageViews
        meetingTitleInput = (TextView) findViewById(R.id.meetingTitleInput);
        locationInput = (TextView) findViewById(R.id.newLocationField);
        dateInput = (TextView) findViewById(R.id.dateScheduleView);
        startTimeInput = (TextView) findViewById(R.id.startTimeInput);
        endTimeInput = (TextView) findViewById(R.id.endTimeInput);
        inviteListInput = (ListView) findViewById(R.id.inviteListInput);

        //Connecting View variables to Buttons
        locationButton = (Button) findViewById(R.id.locationNewButton);
        startTimeButton = (Button) findViewById(R.id.startTimeButton);
        endTimeButton = (Button) findViewById(R.id.endTimeButton);
        inviteFriendButton = (Button) findViewById(R.id.addInviteScheduleButton);
        removeInviteButton = (Button) findViewById(R.id.removeInviteScheduleButton);
        createMeetingButton = (Button) findViewById(R.id.createMeetingButton);
        backButton = (Button) findViewById(R.id.scheduleMeetingBackButton);

        //Setup view fields
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, inviteList);
        inviteListInput.setAdapter(arrayAdapter);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        dateInput.setText(new StringBuilder().append(day)
                .append("/").append(month + 1).append("/").append(year)
                .append(" "));

        db = new Database (this);

        //Activities after clicking Buttons
        locationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                locationListActivity(v);
            }
        });

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialog(getString(R.string.start_time_new));

            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialog(getString(R.string.end_time_new));

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

        createMeetingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                title = meetingTitleInput.getText().toString();
                location = locationInput.getText().toString();
                startTime = TimeConverter.stringToDateTimeConverter(dateInput.getText().toString(), startTimeInput.getText().toString());
                endTime = TimeConverter.stringToDateTimeConverter(dateInput.getText().toString(), endTimeInput.getText().toString());

                if (title.isEmpty() || startTime == null || endTime == null || location.isEmpty()) {//If fields are incomplete
                    ToastCreator.createToast(getApplicationContext(), getString(R.string.incomplete_message));
                } else if (startTime.compareTo(endTime) > 0 || startTime.compareTo(endTime) == 0) {//If time is invalid
                    ToastCreator.createToast(getApplicationContext(), getString(R.string.invalid_time));
                } else if (inviteList.isEmpty()) {//If invite list is empty
                    ToastCreator.createToast(getApplicationContext(), getString(R.string.empty_invite));
                } else {
                    String meetingId = IDGenerator.idGen();
                    Meeting newMeeting = new Meeting(meetingId, title, startTime, endTime, inviteList, location);
                    db.addMeeting(newMeeting);
                    ToastCreator.createToast(getApplicationContext(), getString(R.string.create_meeting_message));
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
        myIntent.putExtra(getString(R.string.request_code), SCHEDULE_MEETING);
        this.startActivityForResult(myIntent, 1);
    }

    /**
     * Starts InviteList activity
     *
     * @param view Listener View object
     */
    public void inviteListActivity(View view) {
        Intent myIntent = new Intent(this, InviteList.class);
        Bundle inviteData = new Bundle();
        inviteData.putStringArrayList(getString(R.string.list), inviteList);
        inviteData.putInt(getString(R.string.request_code), SCHEDULE_MEETING);
        myIntent.putExtras(inviteData);
        this.startActivityForResult(myIntent, 1);
    }

    /**
     * Starts LocationList activity
     *
     * @param view Listener View object
     */
    public void locationListActivity(View view) {
        Intent myIntent = new Intent(this, LocationList.class);
        myIntent.putExtra(getString(R.string.request_code), SCHEDULE_MEETING);
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
