package com.example.kakyenlam.friendfinder;

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
import java.util.List;

import controller.TimeConverter;

/**
 * Handles the operation of finding Friend based on selected time
 * <p>
 * Created by Ka Kyen Lam on 3/09/2017.
 */
public class FindFriend extends AppCompatActivity {

    //Class-use variables
    private static final int MINUTE_RANGE = 1;
    private static final int SECOND_RANGE = 30;
    private ArrayList<String> friendLocationList = new ArrayList<>();

    //Connecting View variables to Buttons
    static TextView timeFindInput;
    private ListView friendLocationListView;
    Button timeSelectFindButton;
    Button locationFindButton;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        //Connecting View variables to TextViews/ImageViews
        timeFindInput = (TextView) findViewById(R.id.timeFindInput);
        friendLocationListView = (ListView) findViewById(R.id.friendLocationListView);

        //Connecting View variables to Buttons
        timeSelectFindButton = (Button) findViewById(R.id.timeSelectFindButton);
        locationFindButton = (Button) findViewById(R.id.locationFindButton);
        backButton =(Button) findViewById(R.id.backFindButton);

                //Activities after clicking the Buttons
        timeSelectFindButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTimePickerDialog(getString(R.string.find_friend));
            }
        });

        locationFindButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getLocationService();
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
     * Extract data from DummyLocationService and use them in local ArrayList
     */
    public void getLocationService() {
        friendLocationList.clear();

        DummyLocationService dls = DummyLocationService.getSingletonInstance(this);
        Date selectedTime = TimeConverter.stringToTimeConverter(timeFindInput.getText().toString());
        List<DummyLocationService.FriendLocation> extractedList = dls.getFriendLocationsForTime(selectedTime, MINUTE_RANGE, SECOND_RANGE);

        for (DummyLocationService.FriendLocation friend : extractedList) {
            friendLocationList.add(friend.toString());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendLocationList);
        friendLocationListView.setAdapter(arrayAdapter);
    }


    public static TextView getTimeFindInput() {
        return timeFindInput;
    }
}
