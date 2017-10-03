package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import model.Database;
import model.Meeting;
import controller.TimeConverter;


/* https://stackoverflow.com/questions/7916834/android-adding-listview-sub-item-text*/
/*https://www.java2blog.com/how-to-sort-hashmap-in-java-by-keys-and/*/

/**
 * Handles the operation of showing list of Meetings
 * <p>
 * Created by Ka Kyen Lam on 3/09/2017.
 */
public class MeetingList extends AppCompatActivity {

    //Class-use variables
    private List<Map<String, String>> listDisplay = new ArrayList<>();
    private ArrayList<String> idList = new ArrayList<>();
    private HashMap<String, Date> entryMap = new HashMap<>();
    private String selectedId;
    String startDate;
    String startTime;
    Meeting selectedMeeting;
    SimpleAdapter simpleAdapter;
    private Database db;

    //View variables
    private ListView meetingListView;
    Button backButton;
    Button sortAscButton;
    Button sortDescButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);

        db = new Database(this);

        //Connecting variables with View objects
        meetingListView = (ListView) findViewById(R.id.meetingList);
        backButton = (Button) findViewById(R.id.meetingListBackButton);
        sortAscButton = (Button) findViewById(R.id.sortAscendingButton);
        sortDescButton = (Button) findViewById(R.id.sortDescendingButton);

        createList(sort(getString(R.string.asc)));

        //Activities after clicking Buttons
        meetingListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int pos, long id) {
                selectedId = idList.get((int) id);
                sendToMeetingDetails(selectedId);
                return true;
            }
        });

        sortAscButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createList(sort(getString(R.string.asc)));
            }
        });

        sortDescButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createList(sort(getString(R.string.desc)));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        createList(sort("asc"));

    }

    /**
     * Send details of selected Meeting object to MeetingDetails class
     *
     * @param selectedId id selected from list
     */
    public void sendToMeetingDetails(String selectedId) {
        Intent friendDetailsIntent = new Intent(this, MeetingDetails.class);
        friendDetailsIntent.putExtra(getString(R.string.id), selectedId);
        this.startActivityForResult(friendDetailsIntent, 1);
    }

    /**
     * Create ListView based on sorted HashMap
     *
     * @param sortedMap sorted HashMap containing Date with associated Meeting ID
     */
    public void createList(HashMap<String, Date> sortedMap) {
        listDisplay.clear();
        idList.clear();

        for (String id : sortedMap.keySet()) {

            idList.add(id);
            selectedMeeting = db.getMeeting(id);


            if (selectedMeeting != null) {

                startDate = (TimeConverter.dateTimeToStringConverter(selectedMeeting.getStartTime()))[0];
                startTime = (TimeConverter.dateTimeToStringConverter(selectedMeeting.getStartTime()))[1];
                Map<String, String> listItem = new HashMap<>();
                listItem.put(getString(R.string.title_list), selectedMeeting.getTitle());
                listItem.put(getString(R.string.details_list), "Location: " + selectedMeeting.getLocation() + "\nDate: " + startDate + "\nTime: " + startTime + "\nNo. of attendees: " + selectedMeeting.getFriend().size());
                listDisplay.add(listItem);

            }
        }

        simpleAdapter = new SimpleAdapter(this, listDisplay, android.R.layout.simple_list_item_2, new String[]{getString(R.string.title_list), getString(R.string.details_list)}, new int[]{android.R.id.text1, android.R.id.text2});
        meetingListView.setAdapter(simpleAdapter);
    }

    /**
     * Sorts list by Date based on given sort order
     *
     * @param order sort order (asc or desc)
     * @return sorted HashMap containing Date with associated Meeting ID
     */
    public HashMap<String, Date> sort(String order) {
        List<Meeting> meetingList = db.getAllMeetings();

        entryMap.clear();

        for (Meeting meeting : meetingList) {
            entryMap.put(meeting.getId(), meeting.getStartTime());
        }

        Set<Entry<String, Date>> startTimeEntrySet = entryMap.entrySet();
        List<Entry<String, Date>> entryList = new ArrayList<>(startTimeEntrySet);

        if (order.equals(getString(R.string.asc))) {

            Collections.sort(entryList, new Comparator<Entry<String, Date>>() {

                @Override
                public int compare(Entry<String, Date> o1, Entry<String, Date> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });

        } else if (order.equals(getString(R.string.desc))) {

            Collections.sort(entryList, new Comparator<Entry<String, Date>>() {

                @Override
                public int compare(Entry<String, Date> o1, Entry<String, Date> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });

        }

        LinkedHashMap<String, Date> sortedHashMap = new LinkedHashMap<>();

        for (Entry<String, Date> entry : entryList) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return sortedHashMap;
    }
}

