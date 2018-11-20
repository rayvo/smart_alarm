package com.cewit.smartalarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Taeyu Im on 18. 11. 8.
 * qvo@cs.stonybrook.edu
 */

public class Util {

    public static String getDate() {
        try{
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
            return sdf.format(date);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    public static String convertToDateTime(String strRawDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        try {
            Date d = sdf.parse(strRawDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);
            return year + "년" + month + "월" + day + "일" + hour + "시" + minute + "분" + second + "초";
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    public static String convertToTime(String strRawDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        try {
            Date d = sdf.parse(strRawDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);
            //return year + "년" + month + "월" + day + "일" +hour + ":" + minute + ":" + second;
            return hour + "시" + minute + "분" + second + "초";
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }


    public static String convertToDate(String strRawDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        try {
            Date d = sdf.parse(strRawDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);
            return year + "년" + month + "월" + day + "일";
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
