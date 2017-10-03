package com.example.kakyenlam.friendfinder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import java.util.*;


/*http://www.java2s.com/Tutorial/Java/0040__Data-Type/SimpleDateFormat.htm*/

/**
 * DatePickerDialog for users to select date. Writes into fields based on tag
 *
 * Created by Ka Kyen Lam on 5/08/2017.
 */



public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private int year;
    private int month;
    private int day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
        year = selectedYear;
        month = selectedMonth;
        day = selectedDay;

        if (getTag().equals(getString(R.string.add_friend_message))) {
            AddFriend.getDateDisplay().setText(new StringBuilder().append(day)
                    .append("/").append(month + 1).append("/").append(year)
                    .append(" "));
        }

        if (getTag().equals(getString(R.string.edit_friend_message))) {
            EditFriend.getDateDisplay().setText(new StringBuilder().append(day)
                    .append("/").append(month + 1).append("/").append(year)
                    .append(" "));
        }


    }


}
