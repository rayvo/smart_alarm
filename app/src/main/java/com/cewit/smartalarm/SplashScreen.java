package com.cewit.smartalarm;

/**
 * Created by Taeyu Im on 18. 5. 2.
 * qvo@cs.stonybrook.edu
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cewit.smartalarm.model.Student;
import com.google.zxing.WriterException;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = SplashScreen.class.getSimpleName();

    private static boolean isFirstLaunch = false;
    private DBHelper db;


    //Layout
    private ImageView ivMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();


        //Layout
        ivMain = (ImageView) findViewById(R.id.ivMainImage);
        ivMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivities(new Intent[]{intent});
                finish();
            }
        });
        //DB Initialization

        db = new DBHelper(this);

        //checkFirstLaunch();
        //if (isFirstLaunch) {
        //    initializeDB();
        //}

        initializeDB();
        generateQR();

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivities(new Intent[]{intent});
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        myThread.start();

    }




   /* private boolean checkFirstLaunch() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            int currentVersion = info.versionCode;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            int lastVersion = prefs.getInt(PreferencesActivity.KEY_HELP_VERSION_SHOWN, 0);
            if (lastVersion == 0) {
                isFirstLaunch = true;
            } else {
                isFirstLaunch = false;
            }
            *//*if (currentVersion > lastVersion) {

                // Record the last version for which we last displayed the What's New (Help) page
                prefs.edit().putInt(PreferencesActivity.KEY_HELP_VERSION_SHOWN, currentVersion).commit();
                Intent intent = new Intent(this, HelpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Show the default page on a clean install, and the what's new page on an upgrade.
                String page = lastVersion == 0 ? HelpActivity.DEFAULT_PAGE : HelpActivity.WHATS_NEW_PAGE;
                intent.putExtra(HelpActivity.REQUESTED_PAGE_KEY, page);
                startActivity(intent);*//*
                return true;
           // }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, e);
        }
        return false;
    }*/

    /*private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            isFirstLaunch = false;
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            // TODO This is a new install (or the user cleared the shared preferences)
            isFirstLaunch = true;

        } else if (currentVersionCode > savedVersionCode) {
            isFirstLaunch = false;

            // TODO This is an upgrade
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }*/

    private void initializeDB() {
        db.insertStudent(new Student("108583913", "임태유", "01088818687", "0326261253", "AB+"));
        db.insertStudent(new Student("108583914", "최민섭", "01062240968", "0326261253", "O+"));
        db.insertStudent(new Student("108583915", "이경훈", "01088818687", "0326261253", "AB+"));
        db.insertStudent(new Student("108583916", "강동우", "01062240968", "0326261253", "A+"));
        db.insertStudent(new Student("108583917", "임태원", "01088818687", "0326261253", "B+"));
        db.insertStudent(new Student("108583918", "로안보", "01088818687", "0326261253", "B+"));
        db.insertStudent(new Student("108583919", "김민수", "01071362679", "0326261253", "O+"));
        db.insertStudent(new Student("108583920", "나수빈", "01071362679", "0326261253", "B+"));
        db.insertStudent(new Student("108583921", "김수구", "01088818687", "0326261253", "O+"));
        db.insertStudent(new Student("108583922", "김혜나", "01088818687", "0326261253", "A+"));
        db.insertStudent(new Student("108583923", "이영호", "01088818687", "0326261253", "O+"));
        db.insertStudent(new Student("108583924", "강일철", "01062240968", "0326261253", "A+"));
        db.insertStudent(new Student("108583925", "최택훈", "01088818687", "0326261253", "A+"));
        db.insertStudent(new Student("108583926", "남수미", "01088818687", "0326261253", "AB+"));
        db.insertStudent(new Student("108583927", "김영수", "01071362679", "0326261253", "B+"));
        db.insertStudent(new Student("108583928", "나수복", "01071362679", "0326261253", "B+"));
    }

    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;

    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";

    private void generateQR() {
        List<Student> students = db.getAllStudents();
        QRGeneratorActivity.verifyStoragePermissions(SplashScreen.this);

        if (students != null && students.size() > 0) {
            for (Student student : students) {
                String strText = student.getId() + "&" + student.getBloodType() + "&" + student.getRepresentativeNumber();
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3 / 4;

                qrgEncoder = new QRGEncoder(
                        strText, null,
                        QRGContents.Type.TEXT,
                        smallerDimension);
                boolean save = false;

                try {
                    bitmap = qrgEncoder.encodeAsBitmap();
                    save = QRGSaver.save(savePath, student.getId(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
                } catch (WriterException e) {
                    Log.v(TAG, e.toString());
                }
                if (!save) {
                    Log.d(TAG,"Cannot save QR Code of student " + student.getName());
                }

            }
        }

    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


}
