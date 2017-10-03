package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import model.Database;
import model.Meeting;
import controller.*;

/**
 * Handles the operation of showing Meeting details
 * <p>
 * Created by Ka Kyen Lam on 3/09/2017.
 */
public class MeetingDetails extends AppCompatActivity {

    //Class-use variables
    private String selectedId;
    private Bundle prevBundle;
    Meeting selectedMeeting;
    ArrayAdapter arrayAdapter;
    private Database db;

    //View variables
    static TextView dateInput;
    static TextView startTimeInput;
    static TextView endTimeInput;
    private TextView meetingTitleInput;
    private TextView locationInput;
    private ListView inviteListInput;
    Button editButton;
    Button deleteButton;
    Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);
        db = new Database(this);

        //Connecting View variables to TextViews/ImageViews
        meetingTitleInput = (TextView) findViewById(R.id.meetingTitleView);
        locationInput = (TextView) findViewById(R.id.locationDetailsField);
        dateInput = (TextView) findViewById(R.id.dateDetailsField);
        startTimeInput = (TextView) findViewById(R.id.startTimeDetailsField);
        endTimeInput = (TextView) findViewById(R.id.endTimeDetailsField);
        inviteListInput = (ListView) findViewById(R.id.inviteListDetails);

        //Connecting View variables to Buttons
        editButton = (Button) findViewById(R.id.editMeetingButton);
        deleteButton = (Button) findViewById(R.id.deleteMeetingButton);
        backButton = (Button) findViewById(R.id.meetingDetailsBackButton);

        //Get data from previous intent
        Intent prevIntent = getIntent();
        prevBundle = prevIntent.getExtras();
        if (prevBundle != null) {
            setup();
        }

        //Activities after clicking Buttons
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               sendToEdit(selectedId);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              db.deleteMeeting(selectedMeeting);
                ToastCreator.createToast(getApplicationContext(), getString(R.string.delete_meeting_message));
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setup();
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
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedMeeting.getFriend());
        inviteListInput.setAdapter(arrayAdapter);
    }

    /**
     * Send details of selected Meeting object to EditMeeting class
     *
     * @param selectedId id selected from list
     */
    public void sendToEdit(String selectedId) {
        Intent EditMeetingIntent = new Intent(this, EditMeeting.class);
        EditMeetingIntent.putExtra(getString(R.string.id), selectedId);
        this.startActivityForResult(EditMeetingIntent, 1);
    }
}
