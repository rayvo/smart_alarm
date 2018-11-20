package com.cewit.smartalarm.model;

/**
 * Created by Taeyu Im on 18. 11. 8.
 * qvo@cs.stonybrook.edu
 */

public class StudentActivity {
    private String ID;
    private String name;
    private String activity;
    private String dateTime;
    private int iconResource;

    public StudentActivity() {

    }

    public StudentActivity(String ID, String name, String activity, String dateTime) {
        this.ID = ID;
        this.name = name;
        this.activity = activity;
        this.dateTime = dateTime;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }
}
