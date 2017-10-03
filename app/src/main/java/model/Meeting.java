package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Meeting class storing all its variables and a HashMap containing Meeting objects with their associated id
 * <p>
 * Created by Ka Kyen Lam on 3/08/2017.
 */

public class Meeting {

//    private static HashMap<String, Meeting> meetingMap = new HashMap<>();
    private String id;
    private String title;
    private Date startTime;
    private Date endTime;
    private ArrayList<String> friend;
    private String location;

    public Meeting(String id, String title, Date startTime, Date endTime, ArrayList<String> friend, String location) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.friend = friend;
        this.location = location;
    }

    public String convertToDB(ArrayList<String> friend) {
        JSONObject json = new JSONObject();
        try {
            json.put("list", new JSONArray(friend));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String stringList = json.toString();

        return stringList;
    }
//
//    public static HashMap<String, Meeting> getMeetingMap() {
//        return meetingMap;
//    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public ArrayList<String> getFriend() {
        return friend;
    }

    public String getLocation() {
        return location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setFriend(ArrayList<String> friend) {
        this.friend = friend;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
