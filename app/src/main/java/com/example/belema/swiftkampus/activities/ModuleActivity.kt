package com.example.belema.swiftkampus.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.belema.swiftkampus.Module
import com.example.belema.swiftkampus.MyUtilClass
import com.example.belema.swiftkampus.R
import com.example.belema.swiftkampus.ServiceGenerator
import com.example.belema.swiftkampus.adapters.ModuleAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ModuleActivity : AppCompatActivity() {

    private lateinit var moduleRecycleView: RecyclerView
    private lateinit var moduleAdapter: ModuleAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        title = intent.getStringExtra("courseName") ?: "Modules"

        moduleRecycleView = findViewById(R.id.my_courses_recyclerView)
        progressBar = findViewById(R.id.e_learning_progressbar)
        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        moduleRecycleView.layoutManager = layoutManager
        moduleRecycleView.addItemDecoration(itemDecoration)

    }

    override fun onStart() {
        super.onStart()
        val courseId = intent.getIntExtra("courseId", 0)
        val courseID = courseId.toString()
        loadData(courseID)
    }

    private fun loadData(courseID: String) {
        ServiceGenerator.apiMethods.getModule(courseID)
                .enqueue(object : Callback<ArrayList<Module>> {
                    override fun onResponse(call: Call<ArrayList<Module>>, response: Response<ArrayList<Module>>?) {
                        hideProgressbar()
                        if (response != null)
                            if (response.isSuccessful) {
                                val courses = response.body()
                                moduleAdapter = ModuleAdapter(courses)
                                moduleRecycleView.adapter = moduleAdapter
                            } else MyUtilClass.showErrorMessage(this@ModuleActivity, response)
                    }

                    override fun onFailure(call: Call<ArrayList<Module>>, t: Throwable?) {
                        hideProgressbar()
                        t?.let {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG)
                                    .show()
                        }
                    }
                })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                super@ModuleActivity.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hideProgressbar(){
        progressBar.visibility = View.GONE
    }
}
