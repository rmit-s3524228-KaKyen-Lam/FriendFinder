package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import static controller.TimeConverter.dateTimeToStringConverter;
import static controller.TimeConverter.dateToStringConverter;
import static controller.TimeConverter.stringToDateConverter;
import static controller.TimeConverter.stringToDateTimeConverter;

/**
 *
 *
 * Created by Ka Kyen Lam on 2/10/2017.
 */

/*https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
* https://stackoverflow.com/questions/5703330/saving-arraylists-in-sqlite-databases*/

public class Database extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "dataManager";

    // Table names
    private static final String TABLE_FRIENDS = "friends";
    private static final String TABLE_MEETINGS = "meetings";

    // Friends Table Columns names
    private static final String KEY_FRIEND_ID = "friendId";
    private static final String KEY_FRIEND_NAME = "friendName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BIRTHDAY = "birthday";
    private static final String KEY_PHOTO = "photo";

    // Friends Table Columns names
    private static final String KEY_MEETING_ID = "meetingId";
    private static final String KEY_TITLE ="title";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_FRIEND_LIST = "friendList";
    private static final String KEY_LOCATION= "location";

    private SQLiteDatabase db;


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FRIENDS_TABLE = "CREATE TABLE " + TABLE_FRIENDS + "("
                + KEY_FRIEND_ID + " STRING PRIMARY KEY," + KEY_FRIEND_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_BIRTHDAY + " TEXT," + KEY_PHOTO + " TEXT" + ")";
        db.execSQL(CREATE_FRIENDS_TABLE);

        String CREATE_MEETINGS_TABLE = "CREATE TABLE " + TABLE_MEETINGS + "("
                + KEY_MEETING_ID + " STRING PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_START_TIME + " TEXT," + KEY_END_TIME + " TEXT," + KEY_FRIEND_LIST + " TEXT," + KEY_LOCATION + " TEXT" + ")";
        db.execSQL(CREATE_MEETINGS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEETINGS);

        // Create tables again
        onCreate(db);
    }

    // Adding new friend
    public void addFriend(Friend friend) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FRIEND_ID, friend.getId());
        values.put(KEY_FRIEND_NAME, friend.getName());
        values.put(KEY_EMAIL, friend.getEmail());
        values.put(KEY_BIRTHDAY, dateToStringConverter(friend.getBirthday()));
        values.put(KEY_PHOTO, friend.getPhotoUri().toString());

        // Inserting Row
        db.insert(TABLE_FRIENDS, null, values);
        db.close(); // Closing database connection
    }

    // Updating single contact
    public int updateFriend(Friend friend) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FRIEND_ID, friend.getId());
        values.put(KEY_FRIEND_NAME, friend.getName());
        values.put(KEY_EMAIL, friend.getEmail());
        values.put(KEY_BIRTHDAY, dateToStringConverter(friend.getBirthday()));
        values.put(KEY_PHOTO, friend.getPhotoUri().toString());

        // updating row
        return db.update(TABLE_FRIENDS, values, KEY_FRIEND_ID + " = ?",
                new String[] { String.valueOf(friend.getId()) });
    }

    // Deleting single contact
    public void deleteFriend(Friend friend) {
        db = this.getWritableDatabase();
        db.delete(TABLE_FRIENDS, KEY_FRIEND_ID + " = ?",
                new String[] { String.valueOf(friend.getId()) });
        db.close();
    }

    // Getting All Friends
    public List<Friend> getAllFriends() {
        List<Friend> friendList = new ArrayList<Friend>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FRIENDS;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                Date birthday = stringToDateConverter(cursor.getString(3));
                Uri photoUri = Uri.parse(cursor.getString(4));
                Friend friend = new Friend(id,name,email,birthday, photoUri);
                // Adding contact to list
                friendList.add(friend);
            } while (cursor.moveToNext());
        }

        // return contact list
        return friendList;
    }

    // Getting single friend
    public Friend getFriend(String id) {
        db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FRIENDS, new String[] { KEY_FRIEND_ID,
                        KEY_FRIEND_NAME, KEY_EMAIL, KEY_BIRTHDAY, KEY_PHOTO }, KEY_FRIEND_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Friend selectedFriend = new Friend(cursor.getString(0), cursor.getString(1), cursor.getString(2), stringToDateConverter(cursor.getString(3)), Uri.parse(cursor.getString(4)));

        return selectedFriend;
    }

    // Adding new meeting
    public void addMeeting(Meeting meeting) {
        db = this.getWritableDatabase();

        StringBuilder convStartTime = new StringBuilder();
        StringBuilder convEndTime = new StringBuilder();
        String[] startTime = dateTimeToStringConverter(meeting.getStartTime());
        String[] endTime = dateTimeToStringConverter(meeting.getEndTime());

        for(String s : startTime) {
            convStartTime.append(s);
            convStartTime.append(" ");
        }
        String startTimeStr = convStartTime.toString();
        Log.d("Add start time", startTimeStr);

        for(String s : endTime) {
            convEndTime.append(s);
            convEndTime.append(" ");
        }
        String endTimeStr = convEndTime.toString();

        ContentValues values = new ContentValues();
        values.put(KEY_MEETING_ID, meeting.getId());
        values.put(KEY_TITLE, meeting.getTitle());
        values.put(KEY_START_TIME, startTimeStr);
        values.put(KEY_END_TIME, endTimeStr);
        values.put(KEY_FRIEND_LIST, meeting.convertToDB(meeting.getFriend()));
        values.put(KEY_LOCATION, meeting.getLocation());

        // Inserting Row
        db.insert(TABLE_MEETINGS, null, values);
        db.close(); // Closing database connection
    }

    // Updating single contact
    public int updateMeeting(Meeting meeting) {
        db = this.getWritableDatabase();

        StringBuilder convStartTime = new StringBuilder();
        StringBuilder convEndTime = new StringBuilder();
        String[] startTime = dateTimeToStringConverter(meeting.getStartTime());
        String[] endTime = dateTimeToStringConverter(meeting.getEndTime());

        for(String s : startTime) {
            convStartTime.append(s);
            convStartTime.append(" ");
        }
        String startTimeStr = convStartTime.toString();

        for(String s : endTime) {
            convEndTime.append(s);
            convEndTime.append(" ");
        }
        String endTimeStr = convEndTime.toString();

        ContentValues values = new ContentValues();
        values.put(KEY_MEETING_ID, meeting.getId());
        values.put(KEY_TITLE, meeting.getTitle());
        values.put(KEY_START_TIME, startTimeStr);
        values.put(KEY_END_TIME, endTimeStr);
        values.put(KEY_FRIEND_LIST, meeting.convertToDB(meeting.getFriend()));
        values.put(KEY_LOCATION, meeting.getLocation());


        // updating row
        return db.update(TABLE_MEETINGS, values, KEY_MEETING_ID + " = ?",
                new String[] { String.valueOf(meeting.getId()) });
    }

    // Deleting single contact
    public void deleteMeeting(Meeting meeting) {
        db = this.getWritableDatabase();
        db.delete(TABLE_MEETINGS, KEY_MEETING_ID + " = ?",
                new String[] { String.valueOf(meeting.getId()) });
        db.close();
    }

    // Getting All Meetings
    public List<Meeting> getAllMeetings() {
        List<Meeting> meetingList = new ArrayList<Meeting>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MEETINGS;
        ArrayList<String> friendList = new ArrayList<>();

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String title = cursor.getString(1);
                String startTime = cursor.getString(2);
                Log.d("Check", startTime);
                String [] dbStartTime = startTime.split(" ");
                Log.d("Check", dbStartTime[0]);
                Log.d("Check", dbStartTime[1]);
                String endTime = cursor.getString(3);
                String [] dbEndTime = endTime.split(" ");
                
                String friend = cursor.getString(4);
                JSONArray jsonArray = new JSONArray();
                try {
                    JSONObject json = new JSONObject(friend);
                    jsonArray = json.getJSONArray("list");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        friendList.add(jsonArray.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String location = cursor.getString(5);

                Date convStartTime = stringToDateTimeConverter(dbStartTime[0], dbStartTime[1]);
                Date convEndTime = stringToDateTimeConverter(dbEndTime[0], dbEndTime[1]);

                Meeting meeting = new Meeting(id,title,convStartTime,convEndTime, friendList, location);
                // Adding contact to list
                meetingList.add(meeting);
            } while (cursor.moveToNext());
        }

        // return contact list
        return meetingList;
    }

    // Getting single friend
    public Meeting getMeeting(String id) {
        db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MEETINGS, new String[] { KEY_MEETING_ID,
                        KEY_TITLE, KEY_START_TIME, KEY_END_TIME, KEY_FRIEND_LIST, KEY_LOCATION }, KEY_MEETING_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ArrayList<String> friendList = new ArrayList<>();

        String title = cursor.getString(1);
        String startTimeStr = cursor.getString(2);
        String [] dbStartTime = startTimeStr.split(" ");
        String endTimeStr = cursor.getString(3);
        String [] dbEndTime = endTimeStr.split(" ");
        String friend = cursor.getString(4);
        JSONArray jsonArray = new JSONArray();
        try {
            JSONObject json = new JSONObject(friend);
            jsonArray = json.getJSONArray("list");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                friendList.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String location = cursor.getString(5);

        Date convStartTime = stringToDateTimeConverter(dbStartTime[0], dbStartTime[1]);
        Date convEndTime = stringToDateTimeConverter(dbEndTime[0], dbEndTime[1]);

        Meeting selectedMeeting = new Meeting(id,title,convStartTime,convEndTime, friendList, location);

        return selectedMeeting;
    }
}
