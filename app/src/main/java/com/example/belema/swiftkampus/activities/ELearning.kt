package com.example.belema.swiftkampus.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.belema.swiftkampus.MyCourses
import com.example.belema.swiftkampus.MyUtilClass
import com.example.belema.swiftkampus.R
import com.example.belema.swiftkampus.ServiceGenerator
import com.example.belema.swiftkampus.adapters.MyCourseAdapter
import com.example.belema.swiftkampus.sessionManagement.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ELearning : AppCompatActivity() {

    private lateinit var myCourseRecycleView: RecyclerView
    private lateinit var myCourseAdapter: MyCourseAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)
        title = "My Courses"

        myCourseRecycleView = findViewById(R.id.my_courses_recyclerView)
        progressBar = findViewById(R.id.e_learning_progressbar)
        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        myCourseRecycleView.layoutManager = layoutManager
        myCourseRecycleView.addItemDecoration(itemDecoration)
    }

    override fun onStart() {
        super.onStart()
        val user = UserSessionManager.getUserDetails()

        // get userId
        val userId = user[UserSessionManager.KEY_USER_ID]

        loadData(userId)
    }

    private fun loadData(userId: String?) {
        ServiceGenerator.apiMethods.getStudentCourses(userId)
                .enqueue(object : Callback<ArrayList<MyCourses>> {
                    override fun onResponse(call: Call<ArrayList<MyCourses>>, response: Response<ArrayList<MyCourses>>?) {
                        progressBar.visibility = View.GONE
                        if (response != null) {
                            if (response.isSuccessful) {
                                val courses = response.body()
                                myCourseAdapter = MyCourseAdapter(courses)
                                myCourseRecycleView.adapter = myCourseAdapter
                            } else MyUtilClass.showErrorMessage(this@ELearning, response)
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<MyCourses>>, t: Throwable?) {
                        hideProgressbar()
                        if (t != null)
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG)
                                .show()
                    }
                })
    }

    private fun hideProgressbar(){
        progressBar.visibility = View.GONE
    }
}
