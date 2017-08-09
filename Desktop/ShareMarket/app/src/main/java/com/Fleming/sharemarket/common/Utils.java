package com.Fleming.sharemarket.common;


import android.util.Log;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {


    public static String get_month_list(String value) {
        if (value.equals("1")) {
            value = "Jan";

        } else if (value.equals("2")) {
            value = "Feb";

        } else if (value.equals("3")) {
            value = "March";

        } else if (value.equals("4")) {
            value = "April";

        } else if (value.equals("5")) {
            value = "May";

        } else if (value.equals("6")) {
            value = "June";

        } else if (value.equals("7")) {
            value = "July";

        } else if (value.equals("8")) {
            value = "August";

        } else if (value.equals("9")) {
            value = "Sep";

        } else if (value.equals("10")) {
            value = "Oct";

        } else if (value.equals("11")) {
            value = "Nov";

        } else if (value.equals("12")) {
            value = "Dec";


        }

        return value;
    }


    public static int get_postion(int value) {
        if (value == 1) {
            value = 2000;
        } else if (value == 2) {
            value = 5000;
        } else if (value == 3) {
            value = 20 * 1000;
        } else if (value == 4) {
            value = 1 * 60 * 1000;
        } else if (value == 5) {
            value = 5 * 60 * 1000;
        }


        return value;
    }

    public static String getDate() {
        DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
        String date = dfDate.format(Calendar.getInstance().getTime());
        DateFormat dfTime = new SimpleDateFormat("hh:mm:ss");
        String time = dfTime.format(Calendar.getInstance().getTime());
        return date;
    }


    public static String Datetime() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    public static boolean get_current_match_values() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DATE);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; //todo month+1?
        int currentYear = calendar.get(Calendar.YEAR);
        int currentHours = calendar.get(Calendar.HOUR);
        int currentMinutes = calendar.get(Calendar.MINUTE);
        int currentSeconds = calendar.get(Calendar.SECOND);

        String currentDateWidTime = currentYear + "/" + currentMonth + "/" + currentDay + " " + currentHours + ":" + currentMinutes + ":" + currentSeconds;

        int nextDay = currentDay + 1;

        String startDateTime = currentYear + "/" + currentMonth + "/" + currentDay + " 16:00:00";
        String endDateTime = currentYear + "/" + currentMonth + "/" + nextDay + " 9:00:00";

        Date currentDateObj = new Date(currentDateWidTime);
        Date startDateObj = new Date(startDateTime);
        Date endDateObj = new Date(endDateTime);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String formatStartDateTime = dateFormat.format(startDateObj);
        Date formatStartDateTimeObj = new Date(formatStartDateTime);

        if (currentDateObj.after(formatStartDateTimeObj) || currentDateObj.before(endDateObj)) {
            System.out.println("Done ");
            return true;

        } else {
            System.out.println("Sorry time not varry");
            return false;

        }

    }

    public static String getYesterdayValue(String format) {
        Date date1 = null;
        final Calendar c = Calendar.getInstance();
        int day = 0, month = 0, year = 0;
        String yesterdayAsString = null;

        final Calendar myCalendar = Calendar.getInstance();

        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, day);
        try {
            String myFormat = "yyyy/MM/dd"; // your format

            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

            date1 = sdf.parse(format);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.DATE, -1);
            yesterdayAsString = sdf.format(calendar.getTime());
            Log.e("Demo testng ", yesterdayAsString + yesterdayAsString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return yesterdayAsString;
    }
}


