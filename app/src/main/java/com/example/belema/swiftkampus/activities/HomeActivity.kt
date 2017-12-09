package com.example.belema.swiftkampus.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.belema.swiftkampus.MyUtilClass
import com.example.belema.swiftkampus.R
import com.example.belema.swiftkampus.ServiceGenerator
import com.example.belema.swiftkampus.gson.Dashboard
import com.example.belema.swiftkampus.sessionManagement.UserSessionManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // User Session Manager Class
    private lateinit var session: UserSessionManager
    private var paymentStatus: Boolean? = true
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Session class instance
        session = UserSessionManager(applicationContext)

        // Check user login
        // If User is not logged in , This will redirect user to LoginActivity.
        if (session.checkLogin())
            finish()

        // get user data from session
        val user = UserSessionManager.getUserDetails()

        // get userId
        userId = user[UserSessionManager.KEY_USER_ID]

        //val imageUrl = "https://unibenportal.azurewebsites.net/ConvertImage/RenderImage?StudentId=" + userId!!

        val headerView = nav_view.getHeaderView(0)
        val textView = headerView.findViewById<TextView>(R.id.drawNav_txt)
        textView.text = userId

        /*CircleImageView circleImageView = headerView.findViewById(R.id.imageView);
        Picasso.with(getApplicationContext()).load(imageUrl).into(circleImageView);*/

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setTitle(R.string.title_activity_home)
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
        if (MyUtilClass.checkNetworkConnection(this)) {
            loadDashboard()
            showMenu()
        } else MyUtilClass.showNoInternetMessage(cd_dashboard_details.context)
    }

    private fun checkPaymentStatus(): Boolean {
        ServiceGenerator.apiMethods.checkPayment(userId)
                .enqueue(object : Callback<Boolean> {
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>?) {
                        if (response != null)
                            if (response.isSuccessful) {
                                paymentStatus = response.body()
                            }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable?) {
                        if (t != null)
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG)
                                    .show()
                    }
                })
        //return paymentStatus
        return paymentStatus ?: false
    }

    private fun showMenu() {
        if (checkPaymentStatus()) {
            nav_view.menu.findItem(R.id.nav_course_reg).isVisible = true
            nav_view.menu.findItem(R.id.nav_accommodation).isVisible = true
            nav_view.menu.findItem(R.id.nav_result).isVisible = true
            nav_view.menu.findItem(R.id.nav_lms).isVisible = true
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_signOut) {
            session.logoutUser()
            finish()
            return true
        } else if (id == R.id.refresh) {
            loadDashboard()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun hideProgressbar() {
        home_progress_bar.visibility = View.INVISIBLE
        cd_dashboard_details.visibility = View.VISIBLE
    }

    private fun showProgressbar() {
        cd_dashboard_details.visibility = View.INVISIBLE
        home_progress_bar.visibility = View.VISIBLE
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        val intent: Intent

        when (id) {
            R.id.nav_course_reg -> {
                intent = Intent(applicationContext, CourseRegistrationActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_accommodation -> {
                intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(resources.getString(R.string.accommodation_payment_url))
                startActivity(intent)
            }

            R.id.nav_payfee -> {
                intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(resources.getString(R.string.fee_payment_url))
                startActivity(intent)
            }

            R.id.nav_result -> {
                intent = Intent(applicationContext, ResultActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_lms -> {
                intent = Intent(applicationContext, ELearning::class.java)
                startActivity(intent)
            }
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadDashboard() {
        showProgressbar()
        ServiceGenerator.apiMethods.getDashboard(userId).enqueue(object : Callback<Dashboard> {

            override fun onResponse(call: Call<Dashboard>, response: Response<Dashboard>?) {

                hideProgressbar()
                if (response != null) {
                    if (response.isSuccessful) {
                        val dashboard = response.body()
                        tv_dashboard_studentId.text = resources.getString(R.string.userId, dashboard!!.studentId)
                        tv_dashboard_fullName.text = resources.getString(R.string.fullName, dashboard.fullName)
                        tv_dashboard_level.text = resources.getString(R.string.levelName, dashboard.levelName)
                        tv_dashboard_programmeName.text = resources.getString(R.string.programmeName, dashboard.programmeName)
                        tv_dashboard_departmentName.text = resources.getString(R.string.departmentName, dashboard.departmentName)
                        tv_dashboard_semesterName.text = resources.getString(R.string.semesterName, dashboard.semesterName)
                        tv_dashboard_sessionName.text = resources.getString(R.string.sessionName, dashboard.sessionName)
                        tv_dashboard_facultyName.text = resources.getString(R.string.facultyName, dashboard.facultyName)
                        tv_dashboard_noOfRegisteredCourses.text = resources.getString(R.string.registeredCourses, dashboard.noOfRegCourses)
                    } else {
                        MyUtilClass.showErrorMessage(this@HomeActivity, response)
                    }
                }
            }

            override fun onFailure(call: Call<Dashboard>, t: Throwable?) {
                hideProgressbar()
                if (t != null)
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }
        })

    }


}
