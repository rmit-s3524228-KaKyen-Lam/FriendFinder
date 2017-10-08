package controller;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

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

/*https://stackoverflow.com/questions/16752073/how-do-i-return-a-boolean-from-asynctask*/

/**
 * Created by Ka Kyen Lam on 8/10/2017.
 */

public class DistanceMatrix {



    public static String getUrl(LatLng origin, LatLng dest) {

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

    public static interface myInterface {
        public void myMethod(String result);
    }

    // Fetches data from url passed
    public static class FetchUrl extends AsyncTask<String, Void, String> {

        private myInterface mListener;

        public FetchUrl(myInterface mListener) {

            this.mListener  = mListener;
        }


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

            if (mListener != null)
                mListener.myMethod(result);
        } // protected void onPostExecute(Void v)

    }

    public static String jsonParser(String result) {
        String displayValue = null;
        JSONObject obj = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            obj = new JSONObject(result);
            JSONArray rows = obj.getJSONArray("rows");
            JSONArray elements = rows.getJSONObject(0).getJSONArray("elements");
            JSONObject distance = elements.getJSONObject(0).getJSONObject("duration");
            String displayText = distance.getString("text");
            displayValue = distance.getString("value");
            return displayValue;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String downloadUrl(String strUrl) throws IOException {
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

    public static LatLng midpointCalc(LatLng origin, LatLng target) {
        double midLat = (origin.latitude + target.latitude) / 2;
        double midLong = (origin.longitude + target.longitude) / 2;

        LatLng midpointLatLng = new LatLng (midLat, midLong);

        return midpointLatLng;
    }
}
