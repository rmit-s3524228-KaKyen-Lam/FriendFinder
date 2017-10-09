package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SuggestMeeting extends AppCompatActivity {

    protected static final int SUGGEST_MEETING = 12;
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<LatLng> midpointList = new ArrayList<>();
    private ArrayList<String> timeList = new ArrayList<>();
    LinkedHashMap <String,Long> sortedTimeMap = new LinkedHashMap<>();
    private int counter = 0;

    //View variables
    TextView nameInput;
    TextView timeInput;
    TextView midpointInput;
    Button suggestMeetingButton;
    Button nextFriendButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_meeting);

        //Connecting View variables to TextViews/ImageViews
        nameInput = (TextView) findViewById(R.id.suggestMeetingNameField);
        timeInput = (TextView) findViewById(R.id.timeInput);
        midpointInput = (TextView) findViewById(R.id.midpointField);

        //Connecting View variables to Buttons
        suggestMeetingButton = (Button) findViewById(R.id.suggestMeetingButton);
        nextFriendButton = (Button) findViewById(R.id.nextFriendButton);
        cancelButton = (Button) findViewById(R.id.suggestMeetingCancelButton);

        //Activities after clicking the Buttons
        suggestMeetingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String location = midpointInput.getText().toString();
                String time = timeInput.getText().toString();
                sendToScheduleMeeting(name, location, time);
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
        timeList.clear();
        Intent prevIntent = getIntent();
        Bundle prevBundle = prevIntent.getExtras();
        HashMap<String, Long> timeMap = (HashMap<String, Long>) prevBundle.getSerializable("sortedTimeMap");;
        HashMap<String, LatLng> midpointMap = (HashMap<String, LatLng>) prevBundle.getSerializable("midpointMap");;

        sortedTimeMap = sortTime(timeMap);

        for (String name: sortedTimeMap.keySet()) {
            nameList.add(name);
            midpointList.add(midpointMap.get(name));
            timeList.add(addTime(timeMap.get(name)));
        }

        fillInput();
    }

    public String addTime(Long seconds) {
        Calendar c = Calendar.getInstance();

        Date currentDate = c.getTime();
        long currentMilli = currentDate.getTime();
        long addedMilli = currentMilli + (seconds*1000);

        Date addedDate = new Date(addedMilli);
        System.out.println(addedDate);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String meetingTime = df.format(addedDate);
        return meetingTime;


    }

    public void fillInput() {
        nameInput.setText(nameList.get(counter));
        timeInput.setText(timeList.get(counter).toString());
        Double latitude = midpointList.get(counter).latitude;
        Double longitude = midpointList.get(counter).longitude;
        midpointInput.setText(latitude.toString() + ", " + longitude.toString());
    }

    public LinkedHashMap<String, Long> sortTime(HashMap<String, Long> TimeMap) {


        Set<Map.Entry<String, Long>> TimeMapEntrySet = TimeMap.entrySet();
        List<Map.Entry<String, Long>> entryList = new ArrayList<>(TimeMapEntrySet);

        Collections.sort(entryList, new Comparator<Map.Entry<String, Long>>() {

            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });


        LinkedHashMap<String, Long> sortedHashMap = new LinkedHashMap<>();

        for (Map.Entry<String, Long> entry : entryList) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return sortedHashMap;
    }

    public void sendToScheduleMeeting(String name, String location, String time) {
        Intent myIntent = new Intent(this, ScheduleMeeting.class);
        Bundle suggestData = new Bundle();
        suggestData.putInt(getString(R.string.request_code), SUGGEST_MEETING);
        suggestData.putString(getString(R.string.name), name);
        suggestData.putString("location", location);
        suggestData.putString("startTime", time);
        myIntent.putExtras(suggestData);
        this.startActivityForResult(myIntent, 1);
    }

}
