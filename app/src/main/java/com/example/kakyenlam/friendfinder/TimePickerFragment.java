package com.example.kakyenlam.friendfinder;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * TimePickerDialog for users to select date. Writes into fields based on tag
 *
 * Created by Ka Kyen Lam on 3/09/2017.
 */

/*https://stackoverflow.com/questions/19933499/how-to-print-the-integer-00-instead-of-java-printing-0*/

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int hour;
    private int minute;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        DecimalFormat formatter = new DecimalFormat("00");
        hour = selectedHour;
        minute = selectedMinute;

        if (getTag().equals(getString(R.string.start_time_new))) {
        ScheduleMeeting.getStartTimeInput().setText(new StringBuilder().append(formatter.format(hour))
               .append(":").append(formatter.format(minute)));
        }

        if (getTag().equals(getString(R.string.end_time_new))) {
            ScheduleMeeting.getEndTimeInput().setText(new StringBuilder().append(formatter.format(hour))
                    .append(":").append(formatter.format(minute)));
        }

        if (getTag().equals(getString(R.string.start_time_edit))) {
            EditMeeting.getStartTimeInput().setText(new StringBuilder().append(formatter.format(hour))
                    .append(":").append(formatter.format(minute)));
        }

        if (getTag().equals(getString(R.string.end_time_edit))) {
            EditMeeting.getEndTimeInput().setText(new StringBuilder().append(formatter.format(hour))
                    .append(":").append(formatter.format(minute)));
        }

        if (getTag().equals(getString(R.string.find_friend))) {
            FindFriend.getTimeFindInput().setText(new StringBuilder().append(formatter.format(hour))
                    .append(":").append(formatter.format(minute)));
        }
    }


    }
