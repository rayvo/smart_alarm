package com.cewit.smartalarm;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cewit.smartalarm.model.StudentActivity;

import java.util.List;

/**
 * Created by Taeyu Im on 18. 11. 8.
 * qvo@cs.stonybrook.edu
 */

public class ActivityCustomListView extends ArrayAdapter<StudentActivity> {

    private List<StudentActivity> dataSet;
    private Activity activity;
    ActivityCustomListView.ViewHolder viewHolder;
    private DBHelper db;


    public ActivityCustomListView(@NonNull Activity activity, List<StudentActivity> dataSet) {
        super(activity, R.layout.activity_custom_list_view, dataSet);
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
            r = layoutInflater.inflate(R.layout.activity_custom_list_view, null, true);
            viewHolder = new ActivityCustomListView.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (ActivityCustomListView.ViewHolder) r.getTag();
        }

        final StudentActivity sActivity = dataSet.get(position);
        viewHolder.tvID.setText(sActivity.getID());
        viewHolder.tvStudentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, QRInputActivity.class);
                intent.putExtra("ID", sActivity.getID());
                activity.startActivities(new Intent[]{intent});
            }
        });
        viewHolder.tvStudentName.setText(sActivity.getName());
        viewHolder.tvDate.setText(Util.convertToDate(sActivity.getDateTime()));
        viewHolder.tvTime.setText(Util.convertToTime(sActivity.getDateTime()));
        viewHolder.tvActivity.setText(sActivity.getActivity());
        viewHolder.ivStatus.setImageResource(sActivity.getIconResource());



        return r;
    }

    class ViewHolder {
        TextView tvID;
        TextView tvStudentName;
        TextView tvDate;
        TextView tvTime;
        TextView tvActivity;
        ImageView ivStatus;
        ViewHolder(View v) {
            tvID = v.findViewById(R.id.tvID);
            tvStudentName = v.findViewById(R.id.tvStudentName);
            tvDate = v.findViewById(R.id.tvDate);
            tvTime = v.findViewById(R.id.tvTime);
            tvActivity = v.findViewById(R.id.tvActivity);
            ivStatus = v.findViewById(R.id.ivStatus);
        }

    }
}
