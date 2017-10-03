package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.Date;

import model.Database;
import model.Friend;
import controller.*;

/**
 * Handles the operation of editing Friend object
 * <p>
 * Created by Ka Kyen Lam on 3/09/2017.
 */
public class EditFriend extends AppCompatActivity {

    //Class-use variable
    protected static final int ADD_IMAGES = 101;
    private Date newBirthday;
    private Friend selectedFriend;
    private Bitmap bitmap;
    String selectedId;
    Bitmap selectedPhoto;
    String selectedDate;
    Bundle prevBundle;
    Uri photoUri;
    Database db;

    //View variables
    static TextView dateDisplay;
    ImageView photoDisplay;
    Button changePhotoButton;
    Button changeDateButton;
    Button backButton;
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);
        db = new Database(this);

        //Connecting View variables to TextViews/ImageViews
        dateDisplay = (TextView) findViewById(R.id.birthdayEditField);
        photoDisplay = (ImageView) findViewById(R.id.imageEditView);

        //Connecting View variables to Buttons
        changeDateButton = (Button) findViewById(R.id.changeDateEditButton);
        backButton = (Button) findViewById(R.id.editFriendBackButton);
        changePhotoButton = (Button) findViewById(R.id.changePhotoEditButton);
        confirmButton = (Button) findViewById(R.id.editFriendConfirmButton);

        //Getting data from previous Intent
        Intent prevIntent = getIntent();
        prevBundle = prevIntent.getExtras();
        if (prevBundle != null) {
            selectedId = (String) prevBundle.get(getString(R.string.id));
            selectedFriend = db.getFriend(selectedId);
            photoUri = selectedFriend.getPhotoUri();
            try {
                selectedPhoto = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            selectedDate = TimeConverter.dateToStringConverter(selectedFriend.getBirthday());
            photoDisplay.setImageBitmap(selectedPhoto);
            dateDisplay.setText(selectedDate);
        }

        //Activities after clicking the Buttons
        changeDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog(getString(R.string.edit_friend_message));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage(v);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newBirthday = TimeConverter.stringToDateConverter(dateDisplay.getText().toString());
                selectedFriend.setBirthday(newBirthday);
                selectedFriend.setPhotoUri(photoUri);
                db.updateFriend(selectedFriend);
                ToastCreator.createToast(getApplicationContext(), getString(R.string.edit_friend_message));
                finish();
            }
        });
    }


    /**
     * Performs activity result for ADD_IMAGES request
     *
     * @param requestCode type of request
     * @param resultCode  result of request
     * @param data        intent data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_IMAGES) {
            if (resultCode == RESULT_OK) {
                Uri targetUri = data.getData();
                try {
                    photoUri = targetUri;
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    photoDisplay.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Shows DatePickerDialog for user to select Date
     *
     * @param tag tag to identify activity source
     */
    public void showDatePickerDialog(String tag) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), tag);
    }

    /**
     * Select image from gallery in phone system
     *
     * @param view current view
     */
    public void selectImage(View view) {
        Intent addImageIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(addImageIntent, ADD_IMAGES);
    }

    public static TextView getDateDisplay() {
        return dateDisplay;
    }
}
