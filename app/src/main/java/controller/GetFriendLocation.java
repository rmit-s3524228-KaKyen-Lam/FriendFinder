package controller;

import com.example.kakyenlam.friendfinder.DummyLocationService;

import java.util.ArrayList;
import java.util.List;

import model.Database;
import model.Friend;
/**
 * Created by Ka Kyen Lam on 8/10/2017.
 */

public class GetFriendLocation {

    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> idList = new ArrayList<>();
    Database db;

    /**
     * Create friend list for ListView
     */
    public void createFriendList() {
        nameList.clear();
        idList.clear();

        List<Friend> friendList = db.getAllFriends();

        for (Friend friend : friendList) {
            nameList.add(friend.getName());
        }
    }
}
