package com.cewit.smartalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Taeyu Im on 18. 11. 2.
 * qvo@cs.stonybrook.edu
 */

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = SettingActivity.class.getSimpleName();

    ImageView ivSetting, ivQRCodeGenerate, ivQRModify, ivActivityConfirm;
    private Button btnExit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setTitle("Settings");
        ivSetting = (ImageView) findViewById(R.id.ivSetting);
        ivQRCodeGenerate = (ImageView) findViewById(R.id.ivQRGenerate);
        ivQRModify = (ImageView) findViewById(R.id.ivQRModify);
        ivActivityConfirm = (ImageView) findViewById(R.id.ivActivityConfirm);

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ivQRCodeGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRInputActivity.class);
                startActivities(new Intent[]{intent});
            }
        });

        ivQRModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StudentListActivity.class);
                startActivities(new Intent[]{intent});
            }
        });

        ivActivityConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityConfirmActivity.class);
                startActivities(new Intent[]{intent});
            }
        });

        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
