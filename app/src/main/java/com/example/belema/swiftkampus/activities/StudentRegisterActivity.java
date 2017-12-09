package com.example.belema.swiftkampus.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.belema.swiftkampus.MyUtilClass;
import com.example.belema.swiftkampus.R;
import com.example.belema.swiftkampus.ServiceGenerator;
import com.example.belema.swiftkampus.Student;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * A login screen that offers login via email/password.
 */
public class StudentRegisterActivity extends AppCompatActivity {

    private static final int REQUEST_READ_IMEI = 0;
    String imei;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserRegisterTask mAuthTask = null;

    // UI references.
    private TextView mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private String sLastName;
    private String sFirstName;
    private String s_studentId;
    private String sDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Student Register");
        setContentView(R.layout.activity_student_register);

        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mConfirmPasswordView = findViewById(R.id.confirm_password);
        mProgressView = findViewById(R.id.login_progress);

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        TextView userId = findViewById(R.id.userId_txt);
        TextView fullName = findViewById(R.id.fullName_txt);
        TextView department = findViewById(R.id.department_txt);
        TextView textView = findViewById(R.id.notMe);

        //CHECKPERMISSION
        getImeiFromDevice();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sLastName = bundle.getString("lastName");
            sFirstName = bundle.getString("firstName");
            s_studentId = bundle.getString("userId");
            sDepartment = bundle.getString("department");
        }


        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StudentRegisterActivity.this);
                alertDialogBuilder.setMessage("Kindly report this to the school ICT department. Thank you");
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
        });

        userId.setText("Student ID: " + s_studentId);
        fullName.setText("Full Name: " + sLastName + " " + sFirstName);
        department.setText("Department: " + sDepartment + imei);


        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MyUtilClass.INSTANCE.checkNetworkConnection(getApplicationContext())) {
                    attemptRegistration();
                } else MyUtilClass.INSTANCE.showNoInternetMessage(StudentRegisterActivity.this);
            }
        });

    }


    private void getImeiFromDevice() {
        if (!mayRequestPermission()) {
            return;
        }

        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    private boolean mayRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_PHONE_STATE)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_IMEI);

        } else {
            requestPermissions(new String[]{READ_PHONE_STATE}, REQUEST_READ_IMEI);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_IMEI) {
            try {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    imei = telephonyManager.getDeviceId();
                    System.out.println(imei);
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegistration() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mConfirmPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //check password
        if (TextUtils.isEmpty(confirmPassword) || !confirmPassword.equals(password)) {
            mConfirmPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mConfirmPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            registerStudent(new Student(sFirstName, sLastName, sDepartment, imei,
                    s_studentId, email, password, confirmPassword));
            /*mAuthTask = new UserRegisterTask(email, password, confirmPassword, sLastName, sFirstName, imei, sDepartment, s_studentId);
            mAuthTask.execute((Void) null);*/
        }
    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }

    private void registerStudent(Student student){
        ServiceGenerator.INSTANCE.getApiMethods().registerStudent(student).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @Nullable Response<ResponseBody> response) {
                showProgress(false);
                if (response != null){
                    if (response.isSuccessful()){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StudentRegisterActivity.this);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        alertDialogBuilder.setMessage("Account successfully registered. Please check your mail for the" +
                                " confirmation link");
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                finish();
                            }
                        });
                    } else MyUtilClass.INSTANCE.showErrorMessage(StudentRegisterActivity.this, response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @Nullable Throwable t) {
                showProgress(false);
                if (t != null){
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    /*public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mConfirmPasword;
        private final String mLastName;
        private final String mFirstName;
        private final String mImei;
        private final String mDepartment;
        private final String mStudentId;

        UserRegisterTask(String email, String password, String confirmPasword, String lastName, String firstname,
                         String imei, String department, String studentId) {
            mEmail = email;
            mPassword = password;
            mConfirmPasword = confirmPasword;
            mLastName = lastName;
            mFirstName = firstname;
            mImei = imei;
            mDepartment = department;
            mStudentId = studentId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            ApiMethods registerStudent = ServiceGenerator.INSTANCE.createService(ApiMethods.class);
            Call<ResponseBody> call = registerStudent.registerStudent(new Student(mFirstName, mLastName, mDepartment, mImei, mStudentId, mEmail, mPassword, mConfirmPasword));

            try {
                response = call.execute();
                System.out.println(response.code());
                System.out.println(response.errorBody());
                System.out.println(response.message());
                if (response.isSuccessful()) {
                    System.out.println(response.message());
                    System.out.println(response.code());
                    return true;
                }
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StudentRegisterActivity.this);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            if (success) {
                alertDialogBuilder.setMessage("Account successfully registered. Please check your mail for the" +
                        " confirmation link");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();
                    }
                });

            } else {
                try {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());

                    alertDialogBuilder.setMessage(jObjError.getString("message"));
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            alertDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }*/
}

