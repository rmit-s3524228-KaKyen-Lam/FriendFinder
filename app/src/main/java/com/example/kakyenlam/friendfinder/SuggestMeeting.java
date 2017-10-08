package com.example.kakyenlam.friendfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

public class SuggestMeeting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_friend_meeting);

        Intent intent = getIntent();
        HashMap<String, Double> hashMap = (HashMap<String, Double>)intent.getSerializableExtra("sortedDistanceMap");
        for (String name: hashMap.keySet()) {
            Log.v("HashMapTest", name);
        }
    }
}
