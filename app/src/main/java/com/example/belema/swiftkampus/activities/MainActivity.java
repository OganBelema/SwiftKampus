package com.example.belema.swiftkampus.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.belema.swiftkampus.NetworkConnectivity;
import com.example.belema.swiftkampus.R;
import com.example.belema.swiftkampus.ServiceGenerator;
import com.example.belema.swiftkampus.apiMethods.CheckStudentId;
import com.example.belema.swiftkampus.gson.StudentDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.parent_rl);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.pr_checkID);
        final EditText editText = (EditText) findViewById(R.id.studentId_et);
        Button button = (Button) findViewById(R.id.studentId_chk_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().isEmpty() || editText.getText().toString() == null){
                    Toast.makeText(getApplicationContext(), "Please enter your Student ID", Toast.LENGTH_LONG)
                            .show();
                } else {
                    if (NetworkConnectivity.checkNetworkConnecttion(getApplicationContext())) {
                        relativeLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        CheckStudentId checkStudentId = ServiceGenerator.createService(CheckStudentId.class);
                        Call<StudentDetails> call = checkStudentId.checkStudentId(editText.getText().toString());
                        call.enqueue(new Callback<StudentDetails>() {
                            @Override
                            public void onResponse(Call<StudentDetails> call, Response<StudentDetails> response) {
                                progressBar.setVisibility(View.GONE);
                                if (response.isSuccessful()) {
                                    StudentDetails details = response.body();
                                    String userId = details.getUserId();
                                    String firstName = details.getFirstName();
                                    String lastName = details.getLastName();
                                    String department = details.getDepartment();

                                    Intent intent = new Intent(getApplicationContext(), StudentRegisterActivity.class);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("firstName", firstName);
                                    intent.putExtra("lastName", lastName);
                                    intent.putExtra("department", department);
                                    finish();
                                    startActivity(intent);

                                } else {

                                    if (response.message().equals("Bad Request")) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                        alertDialogBuilder.setMessage("Account already activated!");
                                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialogInterface) {
                                                finish();
                                            }
                                        });
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<StudentDetails> call, Throwable t) {

                                System.out.println(t.getMessage());
                                progressBar.setVisibility(View.GONE);
                                relativeLayout.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "You are not connected to the internet",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

}
