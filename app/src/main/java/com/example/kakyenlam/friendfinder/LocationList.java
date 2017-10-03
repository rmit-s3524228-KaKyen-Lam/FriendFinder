package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import model.LocationData;

import static com.example.kakyenlam.friendfinder.EditMeeting.EDIT_MEETING;
import static com.example.kakyenlam.friendfinder.ScheduleMeeting.SCHEDULE_MEETING;

/**
 * Handles the operation of showing list of available dummy locations
 * <p>
 * Created by Ka Kyen Lam on 3/09/2017.
 */
public class LocationList extends AppCompatActivity {

    //Class-use variables
    private ArrayList<Double[]> extractedList = new ArrayList<>();
    private ArrayList<String> locationList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Bundle prevBundle;
    int requestCode;

    //View variables
    private ListView locationListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        //Connecting variable to ListView
        locationListView = (ListView) findViewById(R.id.locationListView);

        setupLocation();

        //Get data from previous Intent
        Intent prevIntent = getIntent();
        prevBundle = prevIntent.getExtras();
        requestCode = prevBundle.getInt(getString(R.string.request_code));

        if (requestCode == SCHEDULE_MEETING) {

            locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View v,
                                        int pos, long id) {
                    String selectedLocation= locationList.get((int) id);
                    ScheduleMeeting.getLocationInput().setText(selectedLocation);
                    finish();
                }
            });
        } else if (requestCode == EDIT_MEETING) {
            locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View v,
                                        int pos, long id) {
                    String selectedLocation = locationList.get((int) id);
                    EditMeeting.getLocationInput().setText(selectedLocation);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLocation();
    }

    /**
     * Setup the data for the ListView
     */
    public void setupLocation() {
        extractedList.clear();
        locationList.clear();

        extractedList = LocationData.getLocation();
        for (int i = 0; i < extractedList.size(); i++) {
            locationList.add(extractedList.get(i)[0].toString() + ", " + extractedList.get(i)[1].toString());
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationList);
        locationListView.setAdapter(arrayAdapter);
    }
}
