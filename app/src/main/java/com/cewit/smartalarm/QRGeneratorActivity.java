package com.cewit.smartalarm;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cewit.smartalarm.model.Student;
import com.google.zxing.WriterException;

import java.io.File;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

/**
 * Created by Taeyu Im on 18. 11. 2.
 * qvo@cs.stonybrook.edu
 */

public class QRGeneratorActivity extends Activity {

    private static final String TAG = QRGeneratorActivity.class.getSimpleName();

    Button btnSend, btnCancel;

    QRGEncoder qrgEncoder;
    ImageView qrImage;
    Bitmap bitmap;

    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";

    private boolean isGenerated = false;
    private DBHelper db;
    private String strPIN, strName, strParentNumber, strRepresentativeNumber, strBloodType, strIsModified;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_generator);
        db = new DBHelper(this);

        Intent intent = getIntent();
        strPIN = intent.getStringExtra("PIN");
        strName = intent.getStringExtra("NAME");
        strParentNumber = intent.getStringExtra("PARENT_NUMBER");
        strRepresentativeNumber = intent.getStringExtra("REPRESENTATIVE_NUMBER");
        strBloodType = intent.getStringExtra("BLOOD_TYPE");
        strIsModified = intent.getStringExtra("IS_MODIFIED");

        btnSend = findViewById(R.id.btnSend);
        btnCancel = findViewById(R.id.btnCancel);
        qrImage = findViewById(R.id.ivQR);

        String strText = strPIN + "," + strBloodType + "," + strRepresentativeNumber;
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        qrgEncoder = new QRGEncoder(strText, null, QRGContents.Type.TEXT, smallerDimension);
        QRGeneratorActivity.verifyStoragePermissions(QRGeneratorActivity.this);
        boolean save;
        String result;

        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            bitmap = ProcessingBitmap(bitmap, strName + "-" + strPIN);
            save = QRGSaver.save(savePath, strPIN.trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);
            result = save ? "Image Saved" : "Image Not Saved";

            qrImage.setImageBitmap(bitmap);
            saveToDB();
            isGenerated = true;
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (isSaved) {*/
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("image/jpeg");

                    File picFile = new File(savePath + strPIN.trim() + ".jpg");

                    //sendIntent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
                    //sendIntent.putExtra("sms_body", "some text");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(picFile));

                    startActivity(Intent.createChooser(sendIntent, "Send your picture using:"));
                /*} else {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(QRGeneratorActivity.this);
                    dlgAlert.setMessage("You have to save the QR code first!");
                    dlgAlert.setTitle("SMART ALARM");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }*/


                //startActivity(sendIntent);;
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGenerated) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                } else {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                }
                finish();
            }
        });
    }

    private void saveToDB() {
        try {
            Student student = new Student(strPIN, strName, strParentNumber, strRepresentativeNumber, strBloodType);

            if(strIsModified.equalsIgnoreCase("YES")) {
                db.updateStudent(student);
            } else {
                db.insertStudent(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Data is not saved", Toast.LENGTH_LONG).show();
        }

    }

    private Bitmap ProcessingBitmap(Bitmap bm1, String captionString) {
        //Bitmap bm1 = null;
        Bitmap newBitmap = null;
        try {
            //Toast.makeText(MainActivity.this, pickedImage.getPath(), Toast.LENGTH_LONG).show();
            File picFile = new File(savePath + strPIN.trim() + ".jpg");

            //Uri pickedImage = Uri.fromFile(picFile);
            //bm1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(pickedImage));
            Bitmap.Config config = bm1.getConfig();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }
            newBitmap = Bitmap.createBitmap(bm1.getWidth(), bm1.getHeight(), config);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(bm1, 0, 0, null);
            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setColor(Color.BLACK);
            paintText.setTextSize(50);
            paintText.setStyle(Paint.Style.FILL);
            //paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);
            Rect textRect = new Rect();
            paintText.getTextBounds(captionString, 0, captionString.length(), textRect);
            /*if(textRect.width() >= (canvas.getWidth() - 4))
                paintText.setTextSize(convertToPixels(7));*/
            //int xPos = (canvas.getWidth()/2) - 2;
            int xPos = 15;
            //int yPos = (int) ((canvas.getHeight()/2) - ((paintText.descent() + paintText.ascent()) / 2)) ;
            int yPos = canvas.getHeight() - 10 ;
            canvas.drawText(captionString, xPos, yPos, paintText);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newBitmap;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
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
