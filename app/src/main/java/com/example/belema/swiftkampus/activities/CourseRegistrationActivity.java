package com.example.belema.swiftkampus.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.belema.swiftkampus.CourseRegAdapter;
import com.example.belema.swiftkampus.R;
import com.example.belema.swiftkampus.ServiceGenerator;
import com.example.belema.swiftkampus.apiMethods.RegisterCourses;
import com.example.belema.swiftkampus.apiMethods.SubmitRegCourses;
import com.example.belema.swiftkampus.gson.Course;
import com.example.belema.swiftkampus.gson.CourseReg;
import com.example.belema.swiftkampus.sessionManagement.UserSessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseRegistrationActivity extends AppCompatActivity {

    private RegisterCourses registerCourses;
    private TextView noOfUnitsTextView;

    private ListView listView;
    private ListView regCourseListView;
    private CourseRegAdapter adapter;
    private CourseRegAdapter regCourseAdapter;
    private ArrayList<Course> courses;
    private Course regCourse;
    private ArrayList<Course> registeredCourse;
    int availableCourseUnit;

    private Button submitRegButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_registeration);

        noOfUnitsTextView = findViewById(R.id.tv_number_of_units);
        listView = findViewById(R.id.courses_to_register_listView);
        regCourseListView = findViewById(R.id.courses_registered_listView);

        courses = new ArrayList<>();
        registeredCourse = new ArrayList<>();
        adapter = new CourseRegAdapter(this, R.layout.course_register_layout, courses);
        regCourseAdapter = new CourseRegAdapter(this, R.layout.course_register_layout, registeredCourse);
        listView.setAdapter(adapter);
        regCourseListView.setAdapter(regCourseAdapter);

        UserSessionManager userSessionManager = new UserSessionManager(getApplicationContext());

        HashMap<String, String> user = userSessionManager.getUserDetails();

        // get userId
        final String userId = user.get(UserSessionManager.KEY_USER_ID);

        //button action to submit the list of registered courses along with studentId
        submitRegButton = findViewById(R.id.submit_registration_button);
        submitRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseReg courseReg = new CourseReg();
                courseReg.setStudentId(userId);
                courseReg.setCourses(registeredCourse);
                courseReg.setAvailableCredit(availableCourseUnit);

                SubmitRegCourses submitRegCourses = ServiceGenerator.createService(SubmitRegCourses.class);
                submitRegCourses.submitCourseReg(courseReg).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            System.out.println("Submission result: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });


                System.err.println(courseReg.getCourses().get(0).getCourseName());
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

        registerCourses = ServiceGenerator.createService(RegisterCourses.class);
        registerCourses.courseRegistration(userId).enqueue(new Callback<CourseReg>() {
            @Override
            public void onResponse(Call<CourseReg> call, Response<CourseReg> response) {
                if (response.isSuccessful()){
                    availableCourseUnit = response.body().getAvailableCredit();
                    noOfUnitsTextView.setText(getString(R.string.no_of_courses_to_be_registered, availableCourseUnit));
                    ArrayList<Course> list = response.body().getCourses();
                    for (int i=0; i< list.size(); i++){
                        regCourse = list.get(i);
                        adapter.add(regCourse);
                        adapter.notifyDataSetChanged();
                    }


                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CourseReg> call, Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }
}
