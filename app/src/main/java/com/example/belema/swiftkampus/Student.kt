package com.example.belema.swiftkampus

/**
 * Created by belema on 9/13/17.
 */

class Student {
    private lateinit var  FirstName: String
    private lateinit var LastName: String
    private lateinit var Department: String
    private var Imei: String
    private lateinit var StudentId: String
    private  var Email: String
    private var Password: String
    private lateinit var ConfirmPassword: String


    constructor(firstName: String, lastName: String, department: String, imei: String,
                studentId: String, email: String, password: String, confirmPassword: String) {
        FirstName = firstName
        LastName = lastName
        Department = department
        Imei = imei
        StudentId = studentId
        Email = email
        Password = password
        ConfirmPassword = confirmPassword
    }


    constructor(email: String, imei: String, password: String) {
        Email = email
        Imei = imei
        Password = password
    }
}
