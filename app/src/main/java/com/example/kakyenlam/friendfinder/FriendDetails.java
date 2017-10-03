package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;

import model.Database;
import model.Friend;
import controller.*;

/**
 * Shows details of Friend object
 */
public class FriendDetails extends AppCompatActivity {

    //Class-use variables
    private String selectedId;
    private Friend selectedFriend;
    private Bundle prevBundle;
    private Database db;

    //View variables
    private TextView nameDisplay;
    private TextView emailDisplay;
    private TextView dateDisplay;
    private ImageView photoDisplay;
    Button editButton;
    Button deleteButton;
    Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);
        db = new Database(this);

        //Connecting View variables to TextViews/ImageViews
        nameDisplay = (TextView) findViewById(R.id.nameDetailsField);
        emailDisplay = (TextView) findViewById(R.id.emailDetailsField);
        dateDisplay = (TextView) findViewById(R.id.birthdayDetailsField);
        photoDisplay = (ImageView) findViewById(R.id.photoDetailsView);

        //Connecting View variables to Buttons
        backButton = (Button) findViewById(R.id.backButton);
        editButton = (Button) findViewById(R.id.editButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        //Getting data from previous Intent
        Intent prevIntent = getIntent();
        prevBundle = prevIntent.getExtras();
        if (prevBundle != null) {
           setup();
        }

        //Activities after clicking the Buttons
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendToEdit(selectedId);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.deleteFriend(selectedFriend);
                ToastCreator.createToast(getApplicationContext(), getString(R.string.delete_friend_message));
                finish();
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
        setup();
    }

    /**
     * Send details of selected Friend object to EditFriend class
     *
     * @param selectedId id selected from list
     */
    public void sendToEdit(String selectedId) {
        Intent EditFriendIntent = new Intent(this, EditFriend.class);
        EditFriendIntent.putExtra(getString(R.string.id), selectedId);
        this.startActivityForResult(EditFriendIntent, 1);
    }

    /**
     * Setup the data for the view fields
     */
    public void setup() {
        selectedId = (String) prevBundle.get(getString(R.string.id));
        selectedFriend = db.getFriend(selectedId);
        nameDisplay.setText(selectedFriend.getName());
        emailDisplay.setText(selectedFriend.getEmail());
        dateDisplay.setText(TimeConverter.dateToStringConverter(selectedFriend.getBirthday()));
        try {
            photoDisplay.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedFriend.getPhotoUri())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
