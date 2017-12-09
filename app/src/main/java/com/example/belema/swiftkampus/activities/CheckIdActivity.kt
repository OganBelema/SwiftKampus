package com.example.belema.swiftkampus.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.belema.swiftkampus.MyUtilClass
import com.example.belema.swiftkampus.R
import com.example.belema.swiftkampus.ServiceGenerator
import com.example.belema.swiftkampus.gson.StudentDetails
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckIdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        studentId_chk_btn.setOnClickListener {
            val studentId = studentId_et.text.toString()
            if (studentId.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter your Student ID", Toast.LENGTH_LONG)
                        .show()
            } else {
                if (MyUtilClass.checkNetworkConnection(applicationContext))
                    checkId(studentId)
                else MyUtilClass.showNoInternetMessage(this@CheckIdActivity)
            }
        }
    }

    private fun checkId(studentID: String) {
        showProgressbar()
        ServiceGenerator.apiMethods.checkStudentId(studentID)
                .enqueue(object : Callback<StudentDetails> {
                    override fun onResponse(call: Call<StudentDetails>, response: Response<StudentDetails>?) {
                        hideProgressbar()
                        if (response != null)
                            if (response.isSuccessful) {
                                val details = response.body()
                                val userId = details?.userId
                                val firstName = details?.firstName
                                val lastName = details?.lastName
                                val department = details?.department

                                val intent = Intent(applicationContext, StudentRegisterActivity::class.java)
                                intent.putExtra("userId", userId)
                                intent.putExtra("firstName", firstName)
                                intent.putExtra("lastName", lastName)
                                intent.putExtra("department", department)
                                startActivity(intent)
                                finish()

                            } else {
                                if (response.errorBody() != null) {
                                    val errorMessage = JSONObject(response.errorBody()?.string())
                                    AlertDialog.Builder(this@CheckIdActivity)
                                            .setMessage(errorMessage.getString("message"))
                                            //underscores because passing parameters with lambda but not using them
                                            .setPositiveButton("OK") { _, _ -> finish() }
                                            .show()
                                            .setOnCancelListener { finish() }
                                }
                            }

                    }

                    override fun onFailure(call: Call<StudentDetails>, t: Throwable?) {
                        hideProgressbar()
                        if (t != null)
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }
                })
    }

    private fun showProgressbar() {
        parent_rl.visibility = View.GONE
        pr_checkID.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
        pr_checkID.visibility = View.GONE
        parent_rl.visibility = View.VISIBLE
    }

}
