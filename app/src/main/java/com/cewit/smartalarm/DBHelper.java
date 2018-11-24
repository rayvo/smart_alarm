package com.cewit.smartalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cewit.smartalarm.model.Activity;
import com.cewit.smartalarm.model.Student;
import com.cewit.smartalarm.model.StudentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taeyu Im on 18. 10. 25.
 * qvo@cs.stonybrook.edu
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();

    //information of database
    //Database Version
    private static final int DATABASE_VERSION = 1;
    //Database Name
    private static final String DATABASE_NAME = "SMART_ALARM";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create tables
        sqLiteDatabase.execSQL(Student.CREATE_TABLE);
        sqLiteDatabase.execSQL(Activity.CREATE_TABLE);
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Drop older table if exists
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Student.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Activity.TABLE_NAME);

        //Create tables again
        onCreate(sqLiteDatabase);
    }

    public long insertStudent(Student student) {
        Log.d(TAG, "insertStudent(" + student.getId() + "): Started" );
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Student.COLUMN_ID, student.getId());
        values.put(Student.COLUMN_NAME, student.getName());
        values.put(Student.COLUMN_PARENT_NUMBER, student.getParentNumber());
        values.put(Student.COLUMN_REPRESENTATIVE_NUMBER, student.getRepresentativeNumber());
        values.put(Student.COLUMN_BLOOD_TYPE, student.getBloodType());
        values.put(Student.COLUMN_IS_ENCRYPTED, student.getIsEncrypted());

        // insert row
        long id = db.insert(Student.TABLE_NAME, null, values);

        // close db connection
        db.close();
        Log.d(TAG, "insertStudent(" + student.getId() + "): Success" );


        // return newly inserted row id
        return id;
    }

    public Student getStudent(String studentID) {
        Log.d(TAG, "getStudent(" + studentID + "): Started" );

        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Student.TABLE_NAME,
                new String[]{Student.COLUMN_ID, Student.COLUMN_NAME, Student.COLUMN_PARENT_NUMBER, Student.COLUMN_REPRESENTATIVE_NUMBER,
                Student.COLUMN_BLOOD_TYPE, Student.COLUMN_IS_ENCRYPTED},
                Student.COLUMN_ID + "=?",
                new String[]{String.valueOf(studentID)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Student student = null;
        // prepare note object
        if(cursor.getCount() > 0) {
             student = new Student(
                    cursor.getString(cursor.getColumnIndex(Student.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Student.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(Student.COLUMN_PARENT_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(Student.COLUMN_REPRESENTATIVE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(Student.COLUMN_BLOOD_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(Student.COLUMN_IS_ENCRYPTED))
             );
        } else {
            Log.d(TAG, "getStudent(" + studentID + "): FAIL" );
        }


        // close the db connection
        cursor.close();
        Log.d(TAG, "getStudent(" + studentID + "): Success" );

        return student;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Student.TABLE_NAME + " ORDER BY " +
                Student.COLUMN_NAME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getString(cursor.getColumnIndex(Student.COLUMN_ID)));
                student.setName(cursor.getString(cursor.getColumnIndex(Student.COLUMN_NAME)));
                student.setParentNumber(cursor.getString(cursor.getColumnIndex(Student.COLUMN_PARENT_NUMBER)));
                student.setRepresentativeNumber(cursor.getString(cursor.getColumnIndex(Student.COLUMN_REPRESENTATIVE_NUMBER)));
                student.setBloodType(cursor.getString(cursor.getColumnIndex(Student.COLUMN_BLOOD_TYPE)));
                student.setIsEncrypted(cursor.getInt(cursor.getColumnIndex(Student.COLUMN_IS_ENCRYPTED)));

                students.add(student);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return students;
    }

    public int getStudentsCount() {
        String countQuery = "SELECT  * FROM " + Student.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Student.COLUMN_NAME, student.getName());
        values.put(Student.COLUMN_PARENT_NUMBER, student.getParentNumber());
        values.put(Student.COLUMN_REPRESENTATIVE_NUMBER, student.getRepresentativeNumber());
        values.put(Student.COLUMN_BLOOD_TYPE, student.getBloodType());
        values.put(Student.COLUMN_IS_ENCRYPTED, student.getIsEncrypted());


        // updating row
        return db.update(Student.TABLE_NAME, values, Student.COLUMN_ID + " = ?",
                new String[]{String.valueOf(student.getId())});
    }

    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Student.TABLE_NAME, Student.COLUMN_ID + " = ?",
                new String[]{String.valueOf(student.getId())});
        db.close();
    }

    public long insertActivity(Activity activity) {
        Log.d(TAG, "insertActivity(" + activity.getStudentID() + "-" + activity.getAction() + "): Started" );
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Activity.COLUMN_STUDENT_ID, activity.getStudentID());
        values.put(Activity.COLUMN_ACTION, activity.getAction());
        values.put(Activity.COLUMN_DATE_TIME, activity.getDateTime());

        // insert row
        long id = db.insert(Activity.TABLE_NAME, null, values);

        // close db connection
        db.close();
        Log.d(TAG, "insertActivity(" + activity.getStudentID() + "-" + activity.getAction() + "): Success" );


        // return newly inserted row id
        return id;
    }

    public List<StudentActivity> getCurrentActivities() {

        List<StudentActivity> sActivities = new ArrayList<>();

        // Select All Query
        /*String selectQuery = "SELECT  a." + Activity.COLUMN_STUDENT_ID + ", s." + Student.COLUMN_NAME
                + ", a."  + Activity.COLUMN_DATE_TIME + ", a." + Activity.COLUMN_ACTION
                + " FROM " + Activity.TABLE_NAME + " a, " + Student.TABLE_NAME + " s"
                + " WHERE a." + Activity.COLUMN_STUDENT_ID + "=" + "s." + Student.COLUMN_ID
                + " ORDER BY " + Student.COLUMN_NAME + " DESC";*/

        /*String selectQuery = "SELECT  s." + Student.COLUMN_ID + ", s." + Student.COLUMN_NAME
                + ", a."  + Activity.COLUMN_DATE_TIME + ", a." + Activity.COLUMN_ACTION
                + " FROM " + Student.TABLE_NAME + " s"
                + " LEFT JOIN " + Activity.TABLE_NAME + " a ON s." + Student.COLUMN_ID + "=a." + Activity.COLUMN_STUDENT_ID
                + " ORDER BY " + Student.COLUMN_NAME + " DESC";*/

        String selectQuery = "SELECT  * FROM " + Student.TABLE_NAME + " ORDER BY " +
                Student.COLUMN_NAME + " DESC";

        Log.d(TAG, "SQL: " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StudentActivity sActivity = new StudentActivity();

                sActivity.setID(cursor.getString(cursor.getColumnIndex(Student.COLUMN_ID)));
                sActivity.setName(cursor.getString(cursor.getColumnIndex(Student.COLUMN_NAME)));
                Activity activity = getLatestActivity(sActivity.getID());
                if (activity != null) {
                    sActivity.setDateTime(activity.getDateTime());
                    sActivity.setActivity(activity.getAction());
                    sActivity.setIconResource(R.drawable.check);
                } else {
                    sActivity.setDateTime("");
                    sActivity.setActivity("");
                    sActivity.setIconResource(R.drawable.not_yet);
                }

                sActivities.add(sActivity);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return sActivities;
    }

    private Activity getLatestActivity(String studentID) {
        Log.d(TAG, "getLatestActivity(" + studentID + "): Started" );

        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + Activity.TABLE_NAME
                + " WHERE " + Activity.COLUMN_STUDENT_ID + "=" + studentID
                + " ORDER BY " + Activity.COLUMN_DATE_TIME + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        Activity activity = null;
        // prepare note object
        if(cursor.getCount() > 0) {
            activity = new Activity(
                    cursor.getString(cursor.getColumnIndex(Activity.COLUMN_STUDENT_ID)),
                    cursor.getString(cursor.getColumnIndex(Activity.COLUMN_DATE_TIME)),
                    cursor.getString(cursor.getColumnIndex(Activity.COLUMN_ACTION))
            );
        } else {
            Log.d(TAG, "getLatestActivity(" + studentID + "): FAIL" );
        }


        // close the db connection
        cursor.close();
        Log.d(TAG, "getLatestActivity(" + studentID + "): Success" );

        return activity;

    }
}
