package com.example.kakyenlam.friendfinder;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/*https://stackoverflow.com/questions/3624280/how-to-use-sharedpreferences-in-android-to-store-fetch-and-edit-values*/

public class UserSettings extends AppCompatActivity {

    SharedPreferences preferences;

    EditText alarmWarningMin;
    EditText suggestionMin;
    EditText suggestionSec;
    EditText alarmReminderMin;
    Button confirmButton;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if (preferences.getString("modified", "nope").equals("nope")){
            editor.putString("alarmWarningMin", "5").apply();
            editor.putString("suggestionMin", "10").apply();
            editor.putString("suggestionSec", "0").apply();
            editor.putString("alarmReminderMin", "1").apply();
            editor.putString("modified", "true").apply();
        }

        alarmWarningMin = (EditText) findViewById(R.id.alarmWarningMinute);
        suggestionMin = (EditText) findViewById(R.id.suggestionMinute);
        suggestionSec = (EditText) findViewById(R.id.suggestionSeconds);
        alarmReminderMin = (EditText) findViewById(R.id.alarmReminderMinutes);
        confirmButton = (Button) findViewById(R.id.settingsConfirmButton);
        backButton = (Button) findViewById(R.id.settingsBackButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("alarmWarningMin", alarmWarningMin.getText().toString()).apply();
                editor.putString("suggestionMin", suggestionMin.getText().toString()).apply();
                editor.putString("suggestionSec", suggestionSec.getText().toString()).apply();
                editor.putString("alarmReminderMin", alarmReminderMin.getText().toString()).apply();
                finish();
            }
        });

        alarmWarningMin.setText(preferences.getString("alarmWarningMin", ""));
        suggestionMin.setText(preferences.getString("suggestionMin", ""));
        suggestionSec.setText(preferences.getString("suggestionSec", ""));
        alarmReminderMin.setText(preferences.getString("alarmReminderMin", ""));
    }


}
