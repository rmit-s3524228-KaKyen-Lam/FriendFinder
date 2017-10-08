package com.example.kakyenlam.friendfinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*http://www.vogella.com/tutorials/AndroidLocationAPI/article.html*/
/*https://stackoverflow.com/questions/25360231/parse-json-array-from-google-maps-api-in-java*/
/*https://stackoverflow.com/questions/14898768/how-to-access-nested-elements-of-json-object-using-getjsonarray-method*/
/*http://www.c-sharpcorner.com/UploadFile/1e5156/learn-how-to-find-current-location-using-location-manager-in/*/

public class MapsActivity extends FragmentActivity implements OnMyLocationButtonClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private double latitude;
    private double longitude;
    private LatLng origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            origin = new LatLng (latitude, longitude);
                            float zoomLevel = (float) 15; //This goes up to 21
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, zoomLevel));

                            MarkerOptions originMark = new MarkerOptions();
                            originMark.position(origin);
                            originMark.title("Current Position");
                            originMark.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            mMap.addMarker(originMark);
                        }
                    }
                });
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);





    }

    @Override
    public boolean onMyLocationButtonClick() {

        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();


        // Add a marker in Melbourne and move the camera
//        origin = new LatLng (-37.818288, 144.967040);
        LatLng dest = new LatLng(-37.809837, 144.965215);
        float zoomLevel = (float) 15; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, zoomLevel));


        // Getting URL to the Google Directions API
        String url = getUrl(origin, dest);
        Log.d("onMapClick", url.toString());
        FetchUrl FetchUrl = new FetchUrl();



        MarkerOptions destMark = new MarkerOptions();
        destMark.position(dest);
        destMark.title("Target Position");
        destMark.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));


        mMap.addMarker(destMark);


        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origins=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destinations=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String mode = "mode=walking";
        String apiKey = "key=AIzaSyCazZbc7CQAL2aIaXYBndwTRZrsZ58pqPw";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode + "&" + apiKey;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/distancematrix/" + output + "?" + parameters;


        return url;
    }

    String result = "";
    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            System.out.println(data);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            //parse JSON data
            JSONObject obj = null;
            ArrayList<String> list = new ArrayList<String>();
            try {
                obj = new JSONObject(result);
                JSONArray rows = obj.getJSONArray("rows");
                JSONArray elements = rows.getJSONObject(0).getJSONArray("elements");
                JSONObject distance = elements.getJSONObject(0).getJSONObject("distance");
                String displayText = distance.getString("text");
                String displayValue = distance.getString("value");

                System.out.println(displayText + ", " + displayValue);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } // protected void onPostExecute(Void v)

    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


}
