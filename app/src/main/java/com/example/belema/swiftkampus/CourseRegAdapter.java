package com.example.belema.swiftkampus;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.belema.swiftkampus.gson.Course;

import java.util.List;

/**
 * Created by belema on 9/28/17.
 */

public class CourseRegAdapter extends ArrayAdapter<Course>{

    public CourseRegAdapter(@NonNull Context context, @LayoutRes int resource, List<Course> courseList) {
        super(context, resource, courseList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.course_register_layout, parent, false);
        }

        TextView courseCodeTextview = convertView.findViewById(R.id.tv_course_code);
        TextView courseNameTextView = convertView.findViewById(R.id.tv_course_name);

        Course course = getItem(position);

        courseCodeTextview.setText(String.valueOf(course.getCredit()));
        courseNameTextView.setText(course.getCourseName());

        return convertView;
    }
}
