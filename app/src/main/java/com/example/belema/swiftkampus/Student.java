package com.example.belema.swiftkampus;

/**
 * Created by belema on 9/13/17.
 */

public class Student {
    String FirstName;
    String LastName;
    String Department;
    String Imei;
    String StudentId;
    String Email;
    String Password;
    String ConfirmPassword;

    Student(String firstName, String lastName, String department, String imei,
            String studentId, String email, String password, String confirmPassword){
        FirstName = firstName;
        LastName = lastName;
        Department = department;
        Imei = imei;
        StudentId = studentId;
        Email = email;
        Password = password;
        ConfirmPassword = confirmPassword;
    }

    Student(String email, String imei, String password)
    {
        Email = email;
        Imei = imei;
        Password = password;
    }
}
