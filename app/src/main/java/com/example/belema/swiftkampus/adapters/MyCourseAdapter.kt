package com.example.belema.swiftkampus.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.belema.swiftkampus.MyCourses
import com.example.belema.swiftkampus.R
import com.example.belema.swiftkampus.activities.ModuleActivity
import java.util.*

/**
 * Created by belema on 10/12/17.
 */

class MyCourseAdapter(private val myCourses: ArrayList<MyCourses>?) : RecyclerView.Adapter<MyCourseAdapter.MyCourseViewHolder>() {
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_course_layout, parent, false)
        return MyCourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyCourseViewHolder, position: Int) {
        myCourses?.let {
            val courseName = myCourses[position].courseName
            val courseCode = myCourses[position].courseId
            val courseUnit = myCourses[position].credits

            holder.courseNameTextView.text = context.resources.getString(R.string.e_learning_courses, courseName)
            holder.courseCodeTextView.text = context.resources.getString(R.string.e_learning_course_id, courseCode)
            holder.courseUnitTextView.text = context.resources.getString(R.string.e_learning_course_unit, courseUnit)


            holder.itemView.setOnClickListener {
                val intent = Intent(context, ModuleActivity::class.java)
                intent.putExtra("courseId", courseCode)
                intent.putExtra("courseName", courseName)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return myCourses?.size ?: 0
    }

    inner class MyCourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var courseNameTextView: TextView
        var courseCodeTextView: TextView
        var courseUnitTextView: TextView

        init {
            context = itemView.context
            courseNameTextView = itemView.findViewById(R.id.textview_course_name)
            courseCodeTextView = itemView.findViewById(R.id.textview_course_code)
            courseUnitTextView = itemView.findViewById(R.id.textview_course_unit)
        }
    }
}
