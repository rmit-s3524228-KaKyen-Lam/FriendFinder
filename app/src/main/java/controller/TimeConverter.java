package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Converts Date to String and vice versa
 *
 * Created by Ka Kyen Lam on 3/09/2017.
 */

public class TimeConverter {

    /**
     * Converts String parameter into Date object
     *
     * @param date String to be converted
     * @return Date object
     */
    public static Date stringToDateConverter(String date) {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date formattedDate = sdf.parse(date);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts Date parameter into String object
     *
     * @param date Date to be converted
     * @return String object
     */
    public static String dateToStringConverter(Date date) {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String stringDate = sdf.format(date);
        return stringDate;
    }

    /**
     * Converts date and time Strings to Date object
     *
     * @param date date String to be converted
     * @param time time String to be converted
     * @return Date object
     */
    public static Date stringToDateTimeConverter(String date, String time) {
        String pattern = ("dd/MM/yyyy HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date formattedDate = sdf.parse(date + " " + time);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts Date object to date and time Strings
     *
     * @param time Date to be converted
     * @return String[0] = date, String[1] = time
     */
    public static String[] dateTimeToStringConverter(Date time) {
        String pattern = ("dd/MM/yyyy HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String timeWithDate = sdf.format(time);
        String[] splitTime = timeWithDate.split(" ");
        return splitTime;
    }

    /**
     * Converts time String to Date object
     *
     * @param time String to be converted
     * @return Date object
     */
    public static Date stringToTimeConverter(String time) {
        String pattern = ("HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date formattedDate = sdf.parse(time);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
