package com.example.belema.swiftkampus.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.belema.swiftkampus.MyUtilClass;
import com.example.belema.swiftkampus.R;
import com.example.belema.swiftkampus.ServiceGenerator;
import com.example.belema.swiftkampus.adapters.CourseRegAdapter;
import com.example.belema.swiftkampus.apiMethods.ApiMethods;
import com.example.belema.swiftkampus.gson.Course;
import com.example.belema.swiftkampus.gson.CourseReg;
import com.example.belema.swiftkampus.sessionManagement.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseRegistrationActivity extends AppCompatActivity {

    private ApiMethods registerCourses;
    private TextView noOfUnitsTextView;

    private ListView listView;
    private ListView regCourseListView;
    private CourseRegAdapter adapter;
    private CourseRegAdapter regCourseAdapter;
    private ArrayList<Course> courses;
    private Course regCourse;
    private ArrayList<Course> registeredCourse;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private int availableCourseUnit;

    private Button submitRegButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_registeration);

        noOfUnitsTextView = findViewById(R.id.tv_number_of_units);
        listView = findViewById(R.id.courses_to_register_listView);
        regCourseListView = findViewById(R.id.courses_registered_listView);
        linearLayout = findViewById(R.id.course_reg_container);
        progressBar = findViewById(R.id.pr_courseReg);

        courses = new ArrayList<>();
        registeredCourse = new ArrayList<>();
        adapter = new CourseRegAdapter(this, R.layout.course_register_layout, courses);
        regCourseAdapter = new CourseRegAdapter(this, R.layout.course_register_layout, registeredCourse);
        listView.setAdapter(adapter);
        regCourseListView.setAdapter(regCourseAdapter);

        HashMap<String, String> user = UserSessionManager.getUserDetails();

        // get userId
        final String userId = user.get(UserSessionManager.KEY_USER_ID);

        //button action to submit the list of registered courses along with studentId
        submitRegButton = findViewById(R.id.submit_registration_button);
        submitRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                CourseReg courseReg = new CourseReg();
                courseReg.setStudentId(userId);
                courseReg.setCourses(registeredCourse);
                courseReg.setAvailableCredit(availableCourseUnit);

                ServiceGenerator.INSTANCE.getApiMethods().submitCourseReg(courseReg).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @Nullable Response<ResponseBody> response) {

                        progressBar.setVisibility(View.GONE);
                        if (response != null){
                        if (response.isSuccessful()) {
                            if (response.message().equals("OK")) {
                                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG)
                                        .show();
                            }
                            finish();
                        }
                    }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @Nullable Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        if (t != null)
                        Toast.makeText(getApplicationContext(),  t.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                regCourse = adapter.getItem(i);
                regCourseAdapter.add(regCourse);
                adapter.remove(regCourse);
                adapter.notifyDataSetChanged();
                regCourseAdapter.notifyDataSetChanged();
            }
        });

        regCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course course = regCourseAdapter.getItem(i);
                adapter.add(course);
                regCourseAdapter.remove(course);
                regCourseAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
        });

        registerCourses = ServiceGenerator.INSTANCE.createService(ApiMethods.class);
        registerCourses.courseRegistration(userId).enqueue(new Callback<CourseReg>() {
            @Override
            public void onResponse(@NonNull Call<CourseReg> call, @Nullable Response<CourseReg> response) {
                if (response != null)
                if (response.isSuccessful()){

                    availableCourseUnit = response.body().getAvailableCredit();
                    noOfUnitsTextView.setText(getString(R.string.no_of_courses_to_be_registered, availableCourseUnit));
                    ArrayList<Course> list = response.body().getCourses();
                    for (int i=0; i< list.size(); i++){
                        regCourse = list.get(i);
                        adapter.add(regCourse);
                        adapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);


                } else MyUtilClass.INSTANCE.showErrorMessage(CourseRegistrationActivity.this, response);
            }

            @Override
            public void onFailure(@NonNull Call<CourseReg> call, @Nullable Throwable t) {
                if (t != null)
                System.err.println(t.getMessage());
            }
        });
    }
}
