package com.example.belema.swiftkampus.adapters


import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.belema.swiftkampus.Module
import com.example.belema.swiftkampus.R
import com.example.belema.swiftkampus.activities.TopicActivity
import java.util.*

/**
 * Created by belema on 10/12/17.
 */

class ModuleAdapter(private val modules: ArrayList<Module>?) : RecyclerView.Adapter<ModuleAdapter.MyModuleViewHolder>() {

    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyModuleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_course_layout, parent, false)
        return MyModuleViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyModuleViewHolder, position: Int) {
        modules?.let {
            val moduleName = modules[position].moduleName
            val moduleId = modules[position].moduleId
            val expectedTime = modules[position].expectedTime

            holder.courseNameTextView.text = context.resources.getString(R.string.module_name, moduleName)
            holder.courseCodeTextView.text = context.resources.getString(R.string.module_id, moduleId)
            holder.courseUnitTextView.text = context.resources.getString(R.string.module_time, expectedTime)

            holder.itemView.setOnClickListener {
                val intent = Intent(context, TopicActivity::class.java)
                intent.putExtra("moduleId", moduleId)
                intent.putExtra("moduleName,", moduleName)
                context.startActivity(intent)
            }
        }


    }

    override fun getItemCount(): Int {
        return modules?.size ?: 0
    }

    inner class MyModuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
