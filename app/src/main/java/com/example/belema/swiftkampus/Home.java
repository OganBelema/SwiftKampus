package com.example.belema.swiftkampus;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.belema.swiftkampus.Gson.Dashboard;
import com.example.belema.swiftkampus.SessionManagement.UserSessionManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    // User Session Manager Class
    UserSessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ProgressBar progressBar = findViewById(R.id.home_progress_bar);
        final LinearLayout dashboardView = findViewById(R.id.cd_dashboard_details);

        // Session class instance
        session = new UserSessionManager(getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // get email
        String email = user.get(UserSessionManager.KEY_EMAIL);

        String imageUrl = "https://unibenportal.azurewebsites.net/ConvertImage/RenderImage?StudentId=UNIBEN-203";

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView textView =  headerView.findViewById(R.id.drawNav_txt);
        textView.setText(email);

        CircleImageView circleImageView = headerView.findViewById(R.id.imageView);

        Picasso.with(getApplicationContext()).load(imageUrl).into(circleImageView);


        // Check user login
        // If User is not logged in , This will redirect user to LoginActivity.
        if(session.checkLogin())
            finish();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.title_activity_home);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);


        GetDashboard getDashboard = ServiceGenerator.createService(GetDashboard.class);
        getDashboard.getDashboard("uniben-203").enqueue(new Callback<Dashboard>() {

            @Override
            public void onResponse(Call<Dashboard> call, Response<Dashboard> response) {

                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()){
                    dashboardView.setVisibility(View.VISIBLE);
                    Dashboard dashboard = response.body();
                    TextView StudentIdTextView = findViewById(R.id.tv_dashboard_studentId);
                    TextView FullnameTextView = findViewById(R.id.tv_dashboard_fullName);
                    TextView LevelTextView = findViewById(R.id.tv_dashboard_level);
                    TextView ProgrammeTextView = findViewById(R.id.tv_dashboard_programmeName);
                    TextView DepartmentTextView = findViewById(R.id.tv_dashboard_departmentName);
                    TextView SemesterTextView = findViewById(R.id.tv_dashboard_semesterName);
                    TextView SessionTextView = findViewById(R.id.tv_dashboard_sessionName);
                    TextView FacultyTextView = findViewById(R.id.tv_dashboard_facultyName);
                    TextView RegisteredTextView = findViewById(R.id.tv_dashboard_noOfRegisteredCourses);

                    StudentIdTextView.setText(getResources().getString(R.string.userId,dashboard.getStudentId()));
                    FullnameTextView.setText(getResources().getString(R.string.fullName, dashboard.getFullName()));
                    LevelTextView.setText(getResources().getString(R.string.levelName, dashboard.getLevelName()));
                    ProgrammeTextView.setText(getResources().getString(R.string.programmeName, dashboard.getProgrammeName()));
                    DepartmentTextView.setText(getResources().getString(R.string.departmentName, dashboard.getDepartmentName()));
                    SemesterTextView.setText(getResources().getString(R.string.semesterName, dashboard.getSemesterName()));
                    SessionTextView.setText(getResources().getString(R.string.sessionName, dashboard.getSessionName()));
                    FacultyTextView.setText(getResources().getString(R.string.facultyName, dashboard.getFacultyName()));
                    RegisteredTextView.setText(getResources().getString(R.string.registeredCourses, dashboard.getNoOfRegCourses()));
                } else {

                    Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Dashboard> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signOut) {
            session.logoutUser();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } */

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
