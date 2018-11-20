package com.cewit.smartalarm.model;

/**
 * Created by Taeyu Im on 18. 11. 8.
 * qvo@cs.stonybrook.edu
 */

public class Activity {
    public static final String TABLE_NAME = "activities";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_DATE_TIME = "date_time";
    public static final String COLUMN_ACTION = "action";


    //Blood types
    //
    // 대표전화번호

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_STUDENT_ID + " TEXT,"
                    + COLUMN_DATE_TIME + " TEXT, "
                    + COLUMN_ACTION + " TEXT"
                    + ")";


    private String id;
    private String studentID;
    private String dateTime;
    private String action;


    public Activity() {
    }

    public Activity(String studentID, String dateTime, String action) {
        this.studentID = studentID;
        this.dateTime = dateTime;
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
