package com.cewit.smartalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.cewit.smartalarm.model.StudentActivity;

import java.util.List;

/**
 * Created by Taeyu Im on 18. 11. 8.
 * qvo@cs.stonybrook.edu
 */

public class ActivityConfirmActivity extends AppCompatActivity {
    private final String TAG = ActivityConfirmActivity.class.getSimpleName();

    private List<StudentActivity> sActivities;
    private ListView list;
    private DBHelper db;
    private Button btnExit;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        list = findViewById(R.id.lvActivityList);
        Intent intent = this.getIntent();


        loadData();

        ActivityCustomListView customListView = new ActivityCustomListView(this,sActivities);
        list.setAdapter(customListView);

        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void loadData() {
        db = new DBHelper(this);
        sActivities = db.getCurrentActivities();

    }
}
