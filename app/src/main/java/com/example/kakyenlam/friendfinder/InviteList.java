package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.kakyenlam.friendfinder.EditMeeting.EDIT_MEETING;
import static com.example.kakyenlam.friendfinder.ScheduleMeeting.SCHEDULE_MEETING;

/**
 * Handles the operation of showing list of invited Friends
 * <p>
 * Created by Ka Kyen Lam on 3/09/2017.
 */
public class InviteList extends AppCompatActivity {

    //Class-use variables
    ArrayList<String> nameList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Bundle prevBundle;
    int requestCode;

    //View variables
    ListView inviteListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_list);

        //Connecting variable to ListView
        inviteListView = (ListView) findViewById(R.id.inviteListDeleteDisplay);

        //Get data from previous Intent
        Intent prevIntent = getIntent();
        prevBundle = prevIntent.getExtras();
        requestCode = prevBundle.getInt(getString(R.string.request_code));
        nameList = prevBundle.getStringArrayList(getString(R.string.list));
        if (nameList != null) {
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameList);
        }
        inviteListView.setAdapter(arrayAdapter);

        if (requestCode == SCHEDULE_MEETING) { //If previous intent is from ScheduleMeeting

            inviteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View v,
                                        int pos, long id) {
                    String selectedName = nameList.get((int) id);
                    ScheduleMeeting.getArrayAdapter().remove(selectedName);
                    ScheduleMeeting.getArrayAdapter().notifyDataSetChanged();
                    finish();
                }
            });
        } else if (requestCode == EDIT_MEETING) {//If previous intent is from EditMeeting
            inviteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View v,
                                        int pos, long id) {
                    String selectedName = nameList.get((int) id);
                    EditMeeting.getArrayAdapter().remove(selectedName);
                    EditMeeting.getArrayAdapter().notifyDataSetChanged();
                    finish();
                }
            });
        }

    }
}
