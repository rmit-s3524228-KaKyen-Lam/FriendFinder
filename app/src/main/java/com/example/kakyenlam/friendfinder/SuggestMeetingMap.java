package com.example.kakyenlam.friendfinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import controller.DistanceMatrix;
import controller.TimeConverter;
import model.Database;
import model.Friend;

import static controller.DistanceMatrix.getUrl;
import static controller.DistanceMatrix.midpointCalc;

/*http://www.vogella.com/tutorials/AndroidLocationAPI/article.html*/
/*https://stackoverflow.com/questions/25360231/parse-json-array-from-google-maps-api-in-java*/
/*https://stackoverflow.com/questions/14898768/how-to-access-nested-elements-of-json-object-using-getjsonarray-method*/
/*https://www.androidtutorialpoint.com/intermediate/google-maps-draw-path-two-points-using-google-directions-google-map-android-api-v2/*/
/*http://www.c-sharpcorner.com/UploadFile/1e5156/learn-how-to-find-current-location-using-location-manager-in/*/
/*https://stackoverflow.com/questions/4068984/running-multiple-asynctasks-at-the-same-time-not-possible/13800208#13800208*/
/*https://stackoverflow.com/questions/5485705/how-to-call-another-activity-after-certain-time-limit*/
/*https://stackoverflow.com/questions/7578236/how-to-send-hashmap-value-to-another-activity-using-an-intent*/


public class SuggestMeetingMap extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMyLocationButtonClickListener {

    private static final long switchTime = 2000;
    private GoogleMap mMap;
    private int currlatitude;
    private int currlongitude;
    private LocationManager locationManager;
    private String provider;
    private static final int MINUTE_RANGE = 10;
    private static final int SECOND_RANGE = 30;
    ArrayList <String> nameList = new ArrayList<>();
    private HashMap<String,LatLng> midpointMap = new HashMap<>();
    ArrayList <Long> timeList = new ArrayList<>();
    private HashMap<String,Long> timeTotalMap = new HashMap<>();

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        db = new Database (this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Toast.makeText(getApplicationContext(), "Select location button to start suggestion", Toast.LENGTH_LONG).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        // Add a marker in Sydney and move the camera
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

        if (location != null) {
            LatLng initial = new LatLng(location.getLatitude(), location.getLongitude());
            float zoomLevel = (float) 15; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initial, zoomLevel));
        }



    }

    public void setMarker(LatLng location, String title, float v) {
        MarkerOptions mark = new MarkerOptions();
        mark.position(location);
        mark.title(title);
        mark.icon(BitmapDescriptorFactory.defaultMarker(v));
        mMap.addMarker(mark);
    }

    long[] timeSum =  new long[2];

    public void getFriendLocation(LatLng origin) {

        DummyLocationService dls = DummyLocationService.getSingletonInstance(this);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());
        Date selectedTime = TimeConverter.stringToTimeConverter(formattedDate);

//        Date selectedTime = TimeConverter.stringToTimeConverter("09:45");
        List<DummyLocationService.FriendLocation> extractedList = dls.getFriendLocationsForTime(selectedTime, MINUTE_RANGE, SECOND_RANGE);
        List<Friend> friendList= db.getAllFriends();
        HashSet<String> currentFriendsNames = new HashSet<>();

        for (Friend f: friendList) {
            currentFriendsNames.add(f.getName().toLowerCase());
        }

        for (DummyLocationService.FriendLocation friend : extractedList) {

            if (currentFriendsNames.contains(friend.name)) {
                nameList.add(friend.name);
                System.out.println(friend.name);

                LatLng dest = new LatLng(friend.latitude, friend.longitude);
                setMarker(dest, "Friend ", BitmapDescriptorFactory.HUE_BLUE);

                LatLng midpoint = midpointCalc(origin, dest);
                midpointMap.put(friend.name, midpoint);
                // Getting URL to the Google Directions API
                DistanceMatrix.FetchUrl FetchOrigin = new DistanceMatrix.FetchUrl(new DistanceMatrix.myInterface() {
                    @Override
                    public void myMethod(String result) {
                        String time = DistanceMatrix.jsonParser(result);
                        timeSum[0] = Long.parseLong(time);
                    }
                });
                DistanceMatrix.FetchUrl FetchDest = new DistanceMatrix.FetchUrl(new DistanceMatrix.myInterface() {
                    @Override
                    public void myMethod(String result) {
                        String time = DistanceMatrix.jsonParser(result);
                        timeSum[1] = Long.parseLong(time);
                        timeList.add(timeSum[0] + timeSum[1]);

                        if (timeList.size() == nameList.size()) {
                            for (int i = 0; i < nameList.size(); i++) {
                                System.out.println(nameList.get(i) + ": " + timeList.get(i));
                                timeTotalMap.put(nameList.get(i), timeList.get(i));
                            }

                            for (String name : timeTotalMap.keySet()) {
                                System.out.println(name + ": " + timeTotalMap.get(name));
                            }

                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {

                                public void run() {

                                    sendToSuggestMeeting(timeTotalMap,midpointMap);

                                }

                            }, switchTime);
                        }
                    }
                });

                String originCheck = getUrl(origin, midpoint);
                String destCheck = getUrl(dest, midpoint);


                // Start downloading json data from Google Directions API

                FetchOrigin.execute(originCheck);
                FetchDest.execute(destCheck);
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mMap.clear();
        Toast.makeText(this, "Calculating time taken...", Toast.LENGTH_SHORT).show();

        LatLng origin = new LatLng (getLocation().getLatitude(), getLocation().getLongitude());
        setMarker(origin, "Current", BitmapDescriptorFactory.HUE_RED);


        getFriendLocation(origin);


        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        currlatitude = (int) (location.getLatitude());
        currlongitude = (int) (location.getLongitude());
    }

    public Location getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }
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




    /**
     * Send details of selected Friend object to FriendDetails class
     *
     * @param sortedTimeMap id selected from list
     */
    public void sendToSuggestMeeting(HashMap <String,Long> sortedTimeMap , HashMap <String,LatLng> midpointMap) {
        Intent suggestMeetingIntent = new Intent(this, SuggestMeeting.class);
        Bundle suggestData = new Bundle();
        suggestData.putSerializable("midpointMap", midpointMap);
        suggestData.putSerializable("sortedTimeMap", sortedTimeMap);
        suggestMeetingIntent.putExtras(suggestData);
        this.startActivityForResult(suggestMeetingIntent, 1);
    }
}
