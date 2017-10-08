package com.example.kakyenlam.friendfinder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Database;
import model.Meeting;

public class UpcomingMeetingService extends Service {

    Database db;
    HashMap<String,Date> entryMap = new HashMap<>();
    HashMap<String,Date> sortedMap = new HashMap<>();

    public UpcomingMeetingService() {
        db =  new Database(this);
        sortedMap = createSortedMap();

        for (String title : sortedMap.keySet()) {
            
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public HashMap<String, Date> createSortedMap() {
        List<Meeting> meetingList = db.getAllMeetings();

        entryMap.clear();

        for (Meeting meeting : meetingList) {
            entryMap.put(meeting.getTitle(), meeting.getStartTime());
        }

        Set<Map.Entry<String, Date>> startTimeEntrySet = entryMap.entrySet();
        List<Map.Entry<String, Date>> entryList = new ArrayList<>(startTimeEntrySet);

            Collections.sort(entryList, new Comparator<Map.Entry<String, Date>>() {

                @Override
                public int compare(Map.Entry<String, Date> o1, Map.Entry<String, Date> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });


        LinkedHashMap<String, Date> sortedHashMap = new LinkedHashMap<>();

        for (Map.Entry<String, Date> entry : entryList) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return sortedHashMap;
    }
}
