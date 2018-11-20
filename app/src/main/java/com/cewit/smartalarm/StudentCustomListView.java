package com.cewit.smartalarm;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cewit.smartalarm.model.Student;

import java.util.List;

/**
 * Created by Taeyu Im on 18. 11. 8.
 * qvo@cs.stonybrook.edu
 */

public class StudentCustomListView extends ArrayAdapter<Student> {

    private List<Student> dataSet;
    private Activity activity;
    StudentCustomListView.ViewHolder viewHolder;
    private DBHelper db;


    public StudentCustomListView(@NonNull Activity activity, List<Student> dataSet) {
        super(activity, R.layout.student_custom_list_view, dataSet);
        this.activity = activity;
        this.dataSet = dataSet;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        db = new DBHelper(activity);
        if (r == null) {
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.student_custom_list_view, null, true);
            viewHolder = new StudentCustomListView.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (StudentCustomListView.ViewHolder) r.getTag();
        }

        final Student student = dataSet.get(position);
        viewHolder.tvID.setText(student.getId());
        viewHolder.tvStudentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, QRInputActivity.class);
                intent.putExtra("ID", student.getId());
                activity.startActivities(new Intent[]{intent});
                activity.finish();
            }
        });
        viewHolder.tvStudentName.setText(student.getName());
        viewHolder.tvBloodType.setText(student.getBloodType());
        viewHolder.tvParentNumber.setText(student.getParentNumber());
        viewHolder.tvRepNumber.setText(student.getRepresentativeNumber());
        viewHolder.ivDelete.setTag(student);
        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Student student = (Student) view.getTag();

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        db.deleteStudent(student);
                        Intent intent = activity.getIntent();
                        activity.finish();
                        activity.startActivity(intent);
                        dialog.dismiss();
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
        });
        return r;
    }

    class ViewHolder {
        TextView tvID;
        TextView tvStudentName;
        TextView tvParentNumber;
        TextView tvBloodType;
        TextView tvRepNumber;
        ImageView ivDelete;
        ViewHolder(View v) {
            tvID = v.findViewById(R.id.tvID);
            tvStudentName = v.findViewById(R.id.tvStudentName);
            tvParentNumber = v.findViewById(R.id.tvParentNumber);
            tvBloodType = v.findViewById(R.id.tvBloodType);
            tvRepNumber = v.findViewById(R.id.tvRepNumber);
            ivDelete = v.findViewById(R.id.ivDelete);
        }

    }
}
