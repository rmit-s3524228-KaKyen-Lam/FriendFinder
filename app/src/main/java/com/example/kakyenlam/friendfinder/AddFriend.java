package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.Date;

import model.Database;
import model.Friend;
import controller.*;

/*http://www.mkyong.com/android/android-date-picker-example/
https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
https://stackoverflow.com/questions/11144783/how-to-access-an-image-from-the-phones-photo-gallery*/

/**
 * Handles the operation of adding Friend object to the program
 * <p>
 * Created by Ka Kyen Lam on 3/09/2017.
 */

public class AddFriend extends AppCompatActivity {

    //Class-use variables
    protected static final int PICK_CONTACTS = 100;
    protected static final int ADD_IMAGES = 101;
    private String LOG_TAG = this.getClass().getName();
    private String name;
    private String email;
    private String id;
    private Date birthday;
    private Uri photoUri;
    private Bitmap bitmap;
    private Database db;

    //View variables
    static TextView dateDisplay;
    TextView nameDisplay;
    TextView emailDisplay;
    ImageView photoDisplay;
    Button changeDateButton;
    Button addFriendButton;
    Button importContactButton;
    Button addImageButton;
    Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        //Connecting View variables to TextViews/ImageViews
        nameDisplay = (TextView) findViewById(R.id.nameAddField);
        emailDisplay = (TextView) findViewById(R.id.emailAddField);
        dateDisplay = (TextView) findViewById(R.id.birthdayAddField);
        photoDisplay = (ImageView) findViewById(R.id.photoAddView);

        //Connecting View variables to Buttons
        changeDateButton = (Button) findViewById(R.id.changeDateAddButton);
        addFriendButton = (Button) findViewById(R.id.addFriendConfirmButton);
        backButton = (Button) findViewById(R.id.addFriendBackButton);
        importContactButton = (Button) findViewById(R.id.importContactAddButton);
        addImageButton = (Button) findViewById(R.id.addImageAddButton);

        db = new Database(this);

        //Activities after clicking the Buttons
        changeDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog(getString(R.string.add_friend_message));

            }
        });

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                name = nameDisplay.getText().toString();
                email = emailDisplay.getText().toString();
                birthday = TimeConverter.stringToDateConverter(dateDisplay.getText().toString());

                if (name.isEmpty() || email.isEmpty() || birthday == null) {
                    ToastCreator.createToast(getApplicationContext(), getString(R.string.incomplete_message));
                } else {
                    id = IDGenerator.idGen();
                    Friend newFriend = new Friend(id, name, email, birthday, photoUri);
                    db.addFriend(newFriend);
                    ToastCreator.createToast(getApplicationContext(), getString(R.string.add_friend_message));
                    finish();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        importContactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pickContact(v);
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage(v);

            }
        });
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
     * Pick contact from current contact list in phone system
     *
     * @param view current view
     */
    public void pickContact(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, PICK_CONTACTS);
        onActivityResult(PICK_CONTACTS, RESULT_OK, contactPickerIntent);
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

    /**
     * Performs activity result based on type of request (PICK_CONTACTS or ADD_IMAGES)
     *
     * @param requestCode type of request
     * @param resultCode  result of request
     * @param data        intent data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACTS) {
            if (resultCode == RESULT_OK) {
                ContactDataManager contactsManager = new ContactDataManager(this, data);
                try {
                    name = contactsManager.getContactName();
                    nameDisplay.setText(name);
                    email = contactsManager.getContactEmail();
                    emailDisplay.setText(email);
                } catch (ContactDataManager.ContactQueryException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }

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

    public static TextView getDateDisplay() {
        return dateDisplay;
    }


}
