package com.example.belema.swiftkampus.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.belema.swiftkampus.R
import com.example.belema.swiftkampus.activities.TopicContentActivity
import com.example.belema.swiftkampus.gson.Topic
import java.util.*

/**
 * Created by belema on 10/29/17.
 */

class TopicAdapter(private val topics: ArrayList<Topic>?) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_course_layout, parent, false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        topics?.let {
            val topicName = topics[position].topicName
            val topicId = topics[position].topicId
            val expectedTime = topics[position].expectedTime

            holder.courseNameTextView.text = context.resources.getString(R.string.topic_name, topicName)
            holder.courseCodeTextView.text = context.resources.getString(R.string.topic_id, topicId)
            holder.courseUnitTextView.text = context.resources.getString(R.string.topic_time, expectedTime)

            holder.itemView.setOnClickListener {
                val intent = Intent(context, TopicContentActivity::class.java)
                intent.putExtra("topicName", topicName)
                intent.putExtra("topicId", topicId)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return topics?.size ?: 0
    }

    inner class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val courseNameTextView: TextView
        val courseCodeTextView: TextView
        val courseUnitTextView: TextView

        init {
            context = itemView.context
            courseNameTextView = itemView.findViewById(R.id.textview_course_name)
            courseCodeTextView = itemView.findViewById(R.id.textview_course_code)
            courseUnitTextView = itemView.findViewById(R.id.textview_course_unit)

        }
    }

}
