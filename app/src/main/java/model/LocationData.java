package model;

import java.util.ArrayList;

/**
 * LocationData class storing dummy location variables
 * <p>
 * Created by Ka Kyen Lam on 3/09/2017.
 */

public class LocationData {
    private static ArrayList<Double[]> locationList = new ArrayList<>();

    private static Double[] loc1 = new Double[] {-120.01,80.20};
    private static Double[] loc2 = new Double[] {-100.01,90.20};
    private static Double[] loc3 = new Double[] {-90.01,100.20};
    private static Double[] loc4 = new Double[] {-80.01,110.20};

    public static ArrayList<Double[]> getLocation() {
        locationList.add(loc1);
        locationList.add(loc2);
        locationList.add(loc3);
        locationList.add(loc4);

        return locationList;
    }
}
