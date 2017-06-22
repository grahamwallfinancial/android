package com.Fleming.sharemarket.common;


import org.json.JSONArray;

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
            value = 20*1000;
        } else if (value == 4) {
            value = 1 * 60 * 1000;
        } else if (value == 5) {
            value = 5 * 60 * 1000;
        }


        return value;
    }

}


