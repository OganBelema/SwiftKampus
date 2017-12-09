package com.example.belema.swiftkampus.activities

import android.Manifest
import android.Manifest.permission.READ_PHONE_STATE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.belema.swiftkampus.MyUtilClass
import com.example.belema.swiftkampus.R
import com.example.belema.swiftkampus.ServiceGenerator
import com.example.belema.swiftkampus.Student
import com.example.belema.swiftkampus.gson.UserId
import com.example.belema.swiftkampus.sessionManagement.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    // User Session Manager Class
    private lateinit var session: UserSessionManager

    // UI references.
    private lateinit var mEmailView: EditText
    private lateinit var mPasswordView: EditText
    private lateinit var mProgressView: View
    private lateinit var mLoginFormView: View
    private lateinit var imei: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.title = "Login"
        }

        // User Session Manager
        session = UserSessionManager(applicationContext)

        // Set up the login form.
        mEmailView = findViewById(R.id.email_et)


        mPasswordView = findViewById(R.id.password_et)

        val mEmailSignInButton = findViewById<Button>(R.id.sign_in_btn)
        mEmailSignInButton.setOnClickListener {
            getDeviceId()
            if (MyUtilClass.checkNetworkConnection(applicationContext)) {
                attemptLogin()
            } else MyUtilClass.showNoInternetMessage(this@LoginActivity)
        }

        val button = findViewById<Button>(R.id.register_btn)
        button.setOnClickListener {
            val intent = Intent(applicationContext, CheckIdActivity::class.java)
            startActivity(intent)
        }

        mLoginFormView = findViewById(R.id.login_form)
        mProgressView = findViewById(R.id.login_progress)
    }

    private fun getDeviceId() {
        if (!mayRequestPermission()) {
            return
        }

        try {
            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            imei = telephonyManager.deviceId
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun mayRequestPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_PHONE_STATE)) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    REQUEST_READ_IMEI)
        } else {
            requestPermissions(arrayOf(READ_PHONE_STATE), REQUEST_READ_IMEI)
        }
        return false
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_READ_IMEI) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    imei = telephonyManager.deviceId
                    println(imei)
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {

        // Reset errors.
        mEmailView.error = null
        mPasswordView.error = null

        // Store values at the time of the login attempt.
        val email = mEmailView.text.toString()
        val password = mPasswordView.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.error = getString(R.string.error_invalid_password)
            focusView = mPasswordView
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.error = getString(R.string.error_field_required)
            focusView = mEmailView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            showProgressbar()
            login(Student(email, imei, password))
        }
    }


    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    private fun login(student: Student) {
        ServiceGenerator.apiMethods.login(student).enqueue(object : Callback<UserId> {
            override fun onResponse(call: Call<UserId>, response: Response<UserId>?) {
                hideProgressbar()
                if (response != null) {
                    if (response.isSuccessful) {
                        val userId = response.body()?.studentId
                        session.createUserLoginSession(userId, mPasswordView.text.toString())
                        Toast.makeText(applicationContext, "Login Successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        println(response.message())
                        println(response.code())
                    } else
                        MyUtilClass.showErrorMessage(this@LoginActivity, response)
                }
            }

            override fun onFailure(call: Call<UserId>, t: Throwable?) {
                hideProgressbar()
                if (t != null)
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG)
                            .show()
            }
        })
    }

    private fun showProgressbar() {
        mLoginFormView.visibility = View.GONE
        mProgressView.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
        mProgressView.visibility = View.GONE
        mLoginFormView.visibility = View.VISIBLE
    }

    companion object {
        private val REQUEST_READ_IMEI = 0
    }

}

