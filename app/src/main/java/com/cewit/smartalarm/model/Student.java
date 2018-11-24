package com.cewit.smartalarm.model;

/**
 * Created by Taeyu Im on 18. 10. 9.
 * qvo@cs.stonybrook.edu
 */

public class Student {

    public static final String TABLE_NAME = "students";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PARENT_NUMBER = "parent_number";
    public static final String COLUMN_REPRESENTATIVE_NUMBER = "representative_number";
    public static final String COLUMN_BLOOD_TYPE = "blood_type";
    public static final String COLUMN_IS_ENCRYPTED = "is_encrypted";


    //Blood types
    //
    // 대표전화번호

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PARENT_NUMBER + " TEXT, "
                    + COLUMN_REPRESENTATIVE_NUMBER + " TEXT, "
                    + COLUMN_BLOOD_TYPE + " TEXT, "
                    + COLUMN_IS_ENCRYPTED + " INTEGER"
                    + ")";


    private String id;
    private String name;
    private String parentNumber;
    private String representativeNumber;
    private String bloodType;
    private int isEncrypted;


    public Student() {
    }

    public Student(String id, String name, String parentNumber) {
        this.id = id;
        this.name = name;
        this.parentNumber = parentNumber;
    }

    public Student(String id, String name, String parentNumber, String representativeNumber, String bloodType, int isEncrypted) {
        this.id = id;
        this.name = name;
        this.parentNumber = parentNumber;
        this.representativeNumber = representativeNumber;
        this.bloodType = bloodType;
        this.isEncrypted = isEncrypted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentNumber() {
        return parentNumber;
    }

    public void setParentNumber(String parentNumber) {
        this.parentNumber = parentNumber;
    }

    public String getRepresentativeNumber() {
        return representativeNumber;
    }

    public void setRepresentativeNumber(String representativeNumber) {
        this.representativeNumber = representativeNumber;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public int getIsEncrypted() {
        return isEncrypted;
    }

    public void setIsEncrypted(int isEncrypted) {
        this.isEncrypted = isEncrypted;
    }


}
