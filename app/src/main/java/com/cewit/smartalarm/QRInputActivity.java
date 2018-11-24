package com.cewit.smartalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cewit.smartalarm.model.Student;

/**
 * Created by Taeyu Im on 18. 11. 2.
 * qvo@cs.stonybrook.edu
 */

public class QRInputActivity extends Activity {

    private static final String TAG = QRInputActivity.class.getSimpleName();
    private static final int QR_GENERATE = 1;

    EditText edtPIN, edtName, edtParentNumber, edtBloodType, edtRepresentativeNumber;
    Button btnStart, btnCancel;
    CheckBox ckIsEncrypted;
    boolean isModificated = false;
    boolean isEncrypted;

    private DBHelper db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_input);

        edtPIN = findViewById(R.id.edtPIN);
        edtName = findViewById(R.id.edtName);
        edtParentNumber = findViewById(R.id.edtParentNumber);
        edtBloodType = findViewById(R.id.edtBloodType);
        edtRepresentativeNumber = findViewById(R.id.edtRepresentativeNumber);
        ckIsEncrypted = findViewById(R.id.ckIsEncrypted);

        btnStart = findViewById(R.id.btnStart);
        btnCancel = findViewById(R.id.btnCancel);

        Intent intent = getIntent();
        String studentID = intent.getStringExtra("ID");
        if (studentID != null && studentID.length() > 0) {
            db = new DBHelper(this);
            Student student = db.getStudent(studentID);
            edtPIN.setText(student.getId());
            edtPIN.setEnabled(false);
            edtName.setText(student.getName());
            edtParentNumber.setText(student.getParentNumber());
            edtBloodType.setText(student.getBloodType());
            edtRepresentativeNumber.setText(student.getRepresentativeNumber());
            if (student.getIsEncrypted() == 1) {
                ckIsEncrypted.setChecked(true);
            } else {
                ckIsEncrypted.setChecked(false);
            }
            isModificated = true;
        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRGeneratorActivity.class);
                intent.putExtra("PIN", edtPIN.getText().toString().trim());
                intent.putExtra("NAME", edtName.getText().toString().trim());
                intent.putExtra("BLOOD_TYPE", edtBloodType.getText().toString().trim());
                intent.putExtra("PARENT_NUMBER", edtParentNumber.getText().toString().trim());
                intent.putExtra("REPRESENTATIVE_NUMBER", edtRepresentativeNumber.getText().toString().trim());
                if (ckIsEncrypted.isChecked()) {
                    intent.putExtra("IS_ENCRYPTED", 1);

                } else {
                    intent.putExtra("IS_ENCRYPTED", 0);

                }
                if (isModificated) {
                    intent.putExtra("IS_MODIFIED", "YES");
                } else {
                    intent.putExtra("IS_MODIFIED", "NO");
                }

                startActivityForResult(intent, QR_GENERATE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isModificated) {
                    Intent intent = new Intent(getApplicationContext(), StudentListActivity.class);
                    startActivities(new Intent[]{intent});
                }
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_GENERATE) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(getApplicationContext(), StudentListActivity.class);
                startActivities(new Intent[]{intent});
                finish();
            }
        }
    }
}
