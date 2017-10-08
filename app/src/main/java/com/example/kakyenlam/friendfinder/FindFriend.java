package com.example.kakyenlam.friendfinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import controller.DistanceMatrix;
import controller.TimeConverter;

/**
 * Handles the operation of finding Friend based on selected time
 * <p>
 * Created by Ka Kyen Lam on 3/09/2017.
 */
public class FindFriend extends AppCompatActivity implements LocationListener {

    //Class-use variables
    private static final int MINUTE_RANGE = 1;
    private static final int SECOND_RANGE = 30;
    private ArrayList<String> friendLocationList = new ArrayList<>();
    DistanceMatrix dm = new DistanceMatrix();
    private LocationManager locationManager;
    private String provider;
    private String distance;
    private double currlatitude;
    private double currlongitude;

    //Connecting View variables to Buttons
    static TextView timeFindInput;
    private ListView friendLocationListView;
    TextView totalTime;
    Button timeSelectFindButton;
    Button locationFindButton;
    Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        }

        //Connecting View variables to TextViews/ImageViews
        timeFindInput = (TextView) findViewById(R.id.timeFindInput);
        totalTime = (TextView) findViewById(R.id.totalTimeInput);
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
            LatLng friendLocation = new LatLng(friend.latitude, friend.longitude);

            LatLng origin = new LatLng(getLocation().getLatitude(),getLocation().getLongitude());
            String distanceUrl = dm.getUrl(origin, friendLocation);
            DistanceMatrix.FetchUrl FetchOrigin = new DistanceMatrix.FetchUrl(new DistanceMatrix.myInterface() {
                @Override
                public void myMethod(String result) {
                    String distance = DistanceMatrix.jsonParser(result);
                    totalTime.setText(distance);
                }
            });
//            DistanceMatrix.FetchUrl FetchDest = new DistanceMatrix.FetchUrl();

            FetchOrigin.execute(distanceUrl);

        }


        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friendLocationList);
        friendLocationListView.setAdapter(arrayAdapter);
    }

    public Location getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
            System.out.println (location);
        } else {
            System.out.println ("Location unavailable");
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        currlatitude = location.getLatitude();
        currlongitude = location.getLongitude();
        System.out.println ("Lat: " + currlatitude + "Long: " + currlongitude);
    }


    public static TextView getTimeFindInput() {
        return timeFindInput;
    }
}
