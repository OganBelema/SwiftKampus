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
import com.example.belema.swiftkampus.MyUtilClass
import com.example.belema.swiftkampus.R
import com.example.belema.swiftkampus.ServiceGenerator
import com.example.belema.swiftkampus.adapters.TopicAdapter
import com.example.belema.swiftkampus.gson.Topic
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TopicActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var topicAdapter: TopicAdapter
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        title = intent.getStringExtra("moduleName") ?: "Topics"

        recyclerView = findViewById(R.id.my_courses_recyclerView)
        progressBar = findViewById(R.id.e_learning_progressbar)
        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(itemDecoration)

    }

    override fun onStart() {
        super.onStart()
        val topicId = intent.getIntExtra("moduleId", 0)
        val topicID = topicId.toString()
        if (MyUtilClass.checkNetworkConnection(applicationContext))
            loadData(topicID)
        else MyUtilClass.showNoInternetMessage(this@TopicActivity)
    }

    private fun loadData(topicId: String) {
        ServiceGenerator.apiMethods.getTopics(topicId).enqueue(object : Callback<ArrayList<Topic>> {
            override fun onResponse(call: Call<ArrayList<Topic>>, response: Response<ArrayList<Topic>>?) {
                hideProgressbar()
                if (response != null) {
                    println(response.message())
                    if (response.isSuccessful) {
                        val listOfTopics = response.body()
                        topicAdapter = TopicAdapter(listOfTopics)
                        recyclerView.adapter = topicAdapter
                    } else MyUtilClass.showErrorMessage(this@TopicActivity, response)
                }
            }

            override fun onFailure(call: Call<ArrayList<Topic>>, t: Throwable?) {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                super@TopicActivity.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
