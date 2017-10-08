package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SuggestMeeting extends AppCompatActivity {

    protected static final int SUGGEST_MEETING = 12;
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<Double> distanceList = new ArrayList<>();
    private int counter = 0;

    //View variables
    TextView nameInput;
    TextView distanceInput;
    Button suggestMeetingButton;
    Button nextFriendButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_meeting);

        //Connecting View variables to TextViews/ImageViews
        nameInput = (TextView) findViewById(R.id.suggestMeetingNameField);
        distanceInput = (TextView) findViewById(R.id.distanceInput);

        //Connecting View variables to Buttons
        suggestMeetingButton = (Button) findViewById(R.id.suggestMeetingButton);
        nextFriendButton = (Button) findViewById(R.id.nextFriendButton);
        cancelButton = (Button) findViewById(R.id.suggestMeetingCancelButton);

        //Activities after clicking the Buttons
        suggestMeetingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = nameList.get(counter);
                sendToScheduleMeeting(name);
            }
        });

        nextFriendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                counter++;
                if (counter == nameList.size()) {
                    finish();
                } else {
                    fillInput();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();

            }
        });

        nameList.clear();
        distanceList.clear();
        Intent intent = getIntent();
        HashMap<String, Double> distanceMap= (HashMap<String, Double>)intent.getSerializableExtra("sortedDistanceMap");

        for (String name: distanceMap.keySet()) {
            nameList.add(name);
            distanceList.add(distanceMap.get(name));
        }

        fillInput();
    }

    public void fillInput() {
        nameInput.setText(nameList.get(counter));
        distanceInput.setText(distanceList.get(counter).toString() + " m");
    }

    public void sendToScheduleMeeting(String name) {
        Intent myIntent = new Intent(this, ScheduleMeeting.class);
        Bundle suggestData = new Bundle();
        suggestData.putString(getString(R.string.name), name);
        suggestData.putInt(getString(R.string.request_code), SUGGEST_MEETING);
        myIntent.putExtras(suggestData);
        this.startActivityForResult(myIntent, 1);
    }

}
