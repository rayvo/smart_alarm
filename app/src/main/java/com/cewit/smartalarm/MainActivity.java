package com.cewit.smartalarm;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cewit.smartalarm.barcode.BarcodeCaptureActivity;
import com.cewit.smartalarm.model.Activity;
import com.cewit.smartalarm.model.Student;
import com.cewit.smartalarm.utils.AES256Cipher;
import com.cewit.smartalarm.utils.Util;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CODE_READER_GETON_REQUEST_CODE = 2 ;
    private static final int CODE_READER_GETOFF_REQUEST_CODE = 3;
    private static final int SETTINGS_REQUEST_CODE = 4;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;


    private DBHelper db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

        ImageView ivGetOn = findViewById(R.id.scan_barcode_geton);
        ivGetOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BarcodeCaptureActivity.class);
                startActivityForResult(intent, CODE_READER_GETON_REQUEST_CODE);

            }
        });

        ImageView ivGetOff = findViewById(R.id.scan_barcode_getoff);
        ivGetOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BarcodeCaptureActivity.class);
                startActivityForResult(intent, CODE_READER_GETOFF_REQUEST_CODE);

            }
        });


        ImageView ivSetting = findViewById(R.id.ivSetting);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, SETTINGS_REQUEST_CODE);

            }
        });

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);

                // MY_PERMISSIONS_REQUEST_SEND_SMS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String strCode = "";
        String parentNumber;
        Student student = null;

        String studentID;
        String dateTime = Util.getDate();

        if (requestCode == CODE_READER_GETON_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] points = barcode.cornerPoints;
                    String barcodeValue = barcode.displayValue;
                    Log.d(TAG, "barcodeValue: " + barcodeValue);
                    strCode = decode(barcodeValue);
                    Log.d(TAG, "strCode: " + strCode);
                    studentID = strCode.substring(0, strCode.indexOf(","));
                    Log.d(TAG, "ID: " + studentID);

                    student = db.getStudent(studentID);
                    if (student != null) {
                        parentNumber = student.getParentNumber();
                        String messageContent = "[원생승하차알리미]\n" + student.getName() + "가 " + hour + "시 " + minute + "분에 " + "탑승하였습니다.";

                        try {
                            SmsManager.getDefault().sendTextMessage(parentNumber, null, messageContent, null, null);
                            Activity activity = new Activity(studentID, dateTime, "승차");
                            db.insertActivity(activity);
                            showConfirmMessage(student.getName());

                            //Toast.makeText(this, "Messsage sent!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "Cannot find the user!", Toast.LENGTH_SHORT).show();
                    }






                }
            } else
                Log.e(TAG, String.format(getString(R.string.barcode_error_format),
                        CommonStatusCodes.getStatusCodeString(resultCode)));

        } else if (requestCode == CODE_READER_GETOFF_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] points = barcode.cornerPoints;
                    String barcodeValue = barcode.displayValue;
                    Log.d(TAG, "barcodeValue: " + barcodeValue);
                    strCode = decode(barcodeValue);
                    Log.d(TAG, "strCode: " + strCode);
                    studentID = strCode.substring(0, strCode.indexOf(","));
                    Log.d(TAG, "ID: " + studentID);

                    student = db.getStudent(studentID);
                    if (student != null) {
                        parentNumber = student.getParentNumber();
                        String messageContent = "[원생승하차알리미]\n" + student.getName() + "가 " + hour + "시 " + minute + "분에 " + "하차하였습니다.";

                        try {
                            SmsManager.getDefault().sendTextMessage(parentNumber, null, messageContent, null, null);
                            Activity activity = new Activity(studentID, dateTime, "하차");
                            db.insertActivity(activity);
                            showConfirmMessage(student.getName());
                            //Toast.makeText(this, "Messsage sent!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "Cannot find the user!", Toast.LENGTH_SHORT).show();
                    }

                }
            } else
                Log.e(TAG, String.format(getString(R.string.barcode_error_format),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
        } else {

        }
    }

    private String decode(String barcodeValue) {
        String strDecode = barcodeValue;
        if (barcodeValue.length() >= 36 ) { //TODO will be checked later
            try {
                byte[] keyBytes = AES256Cipher.key.getBytes("UTF-8");
                byte[] cipherData = AES256Cipher.decrypt(AES256Cipher.ivBytes, keyBytes, Base64.decode(barcodeValue.getBytes("UTF-8"),Base64.DEFAULT));
                strDecode = new String(cipherData,"UTF-8");
                Log.d(TAG, "Decrypted Code: " + strDecode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
        }
        return strDecode;
    }

    private void showConfirmMessage(String strContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("확인");
        strContent = strContent +   "의 부모님께 문자 발송을 하였습니다!";
        builder.setMessage(strContent + "\n계속 진행할까요?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                Intent intent = new Intent(MainActivity.this, BarcodeCaptureActivity.class);
                startActivityForResult(intent, CODE_READER_GETON_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
