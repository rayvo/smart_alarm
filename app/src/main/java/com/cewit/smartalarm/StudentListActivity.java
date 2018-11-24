package com.cewit.smartalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.cewit.smartalarm.model.Student;

import java.util.List;

/**
 * Created by Taeyu Im on 18. 11. 8.
 * qvo@cs.stonybrook.edu
 */

public class StudentListActivity extends AppCompatActivity {
    private final String TAG = StudentListActivity.class.getSimpleName();

    private List<Student> students;
    private ListView list;
    private DBHelper db;
    private Button btnExit;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list);

        setTitle("QR코드 확인/수정");
        list = findViewById(R.id.lvStudentList);
        Intent intent = this.getIntent();


        loadData();

        StudentCustomListView customListView = new StudentCustomListView(this,students);
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
        students = db.getAllStudents();

    }
}
