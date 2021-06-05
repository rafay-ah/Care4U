package com.innova.care4u.Model;

import java.io.Serializable;

public class Patient implements Serializable {
    public String Name;
    public String Phone;
    public  String ParentName;
    public  String Gender;
    public String DOB;
    public  String Location;
    public String Uid; // used to create tables for patient treatment records
    public String Pid; // used by database module

    public Patient(String name, String phone, String parentName, String gender,String dob, String location, String pid, String uid) {
        Name = name;
        Phone = phone;
        ParentName = parentName;
        DOB= dob;
        Location = location;
        Uid =  uid; // Unique ID
        Pid = pid;
        Gender = gender;
    }

    public Patient(String name, String phone, String parentName, String gender ,String pid, String uid) {
        Name = name;
        Phone = phone;
        ParentName = parentName;
        Uid =  uid; // Unique ID
        Pid = pid;
        Gender = gender;
    }
}