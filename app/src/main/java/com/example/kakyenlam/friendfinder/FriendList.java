package com.example.kakyenlam.friendfinder;

/*https://stackoverflow.com/questions/31572294/the-simplest-way-to-display-strings-with-icons-in-a-recyclerview*/
/*https://stackoverflow.com/questions/8846707/how-to-implement-a-long-click-listener-on-a-listview*/
/*https://stackoverflow.com/questions/19455649/creating-a-dialog-after-an-onclick-event*/

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import model.Database;
import model.Friend;

import static com.example.kakyenlam.friendfinder.EditMeeting.EDIT_MEETING;
import static com.example.kakyenlam.friendfinder.ScheduleMeeting.SCHEDULE_MEETING;

/**
 * Handles the operation of showing list of Friends
 * <p>
 * Created by Ka Kyen Lam on 3/09/2017.
 */
public class FriendList extends AppCompatActivity {

    //Class-use variables
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> idList = new ArrayList<>();
    private String selectedId;
    int requestCode;
    Bundle prevBundle;
    ArrayAdapter<String> arrayAdapter;
    Database db;


    //View variables
    private ListView friendListView;
    Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        db = new Database (this);

        //Connecting variables to View objects
        friendListView = (ListView) findViewById(R.id.friendList);
        backButton = (Button) findViewById(R.id.friendListBackButton);

        createFriendList();

        //Get data from previous Intent
        Intent prevIntent = getIntent();
        prevBundle = prevIntent.getExtras();
        requestCode = 0;
        if (prevBundle != null) {
            requestCode = (int) prevBundle.get(getString(R.string.request_code));
        }

        if (requestCode == SCHEDULE_MEETING) {//If previous intent is from ScheduleMeeting

            friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View v,
                                        int pos, long id) {
                    String selectedName = nameList.get((int) id);
                    ScheduleMeeting.getArrayAdapter().add(selectedName);
                    ScheduleMeeting.getArrayAdapter().notifyDataSetChanged();
                    finish();
                }
            });

        } else if (requestCode == EDIT_MEETING) {//If previous intent is from EditMeeting

            friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View v,
                                        int pos, long id) {
                    String selectedName = nameList.get((int) id);
                    EditMeeting.getArrayAdapter().add(selectedName);
                    EditMeeting.getArrayAdapter().notifyDataSetChanged();
                    finish();
                }
            });

        } else {//If previous intent is from MainMenu
            friendListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                               int pos, long id) {
                    selectedId = idList.get((int) id);
                    sendToFriendDetails(selectedId);
                    return true;
                }
            });
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        createFriendList();

    }

    /**
     * Send details of selected Friend object to FriendDetails class
     *
     * @param selectedId id selected from list
     */
    public void sendToFriendDetails(String selectedId) {
        Intent friendDetailsIntent = new Intent(this, FriendDetails.class);
        friendDetailsIntent.putExtra(getString(R.string.id), selectedId);
        this.startActivityForResult(friendDetailsIntent, 1);
    }

    /**
     * Create friend list for ListView
     */
    public void createFriendList() {
        nameList.clear();
        idList.clear();

        List<Friend> friendList = db.getAllFriends();

        for (Friend friend : friendList) {
            nameList.add(friend.getName());
            idList.add(friend.getId());
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameList);
        friendListView.setAdapter(arrayAdapter);
    }


}

