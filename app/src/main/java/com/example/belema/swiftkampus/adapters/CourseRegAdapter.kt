package com.example.belema.swiftkampus.adapters

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.example.belema.swiftkampus.R
import com.example.belema.swiftkampus.gson.Course

/**
 * Created by belema on 9/28/17.
 */

class CourseRegAdapter(context: Context, @LayoutRes resource: Int, courseList: List<Course>) : ArrayAdapter<Course>(context, resource, courseList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.course_register_layout, parent, false)
            }

        if (view != null) {
            val courseCodeTextView = view.findViewById<TextView>(R.id.tv_course_code)
            val courseNameTextView = view.findViewById<TextView>(R.id.tv_course_name)

            val course = getItem(position)
            if (course != null) {
                courseCodeTextView?.text = course.credit.toString()
                courseNameTextView?.text = course.courseName
            }
        }
        return view
    }
}
