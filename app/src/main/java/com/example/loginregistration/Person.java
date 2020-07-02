package com.example.loginregistration;

import android.graphics.Bitmap;

public class Person {

    private int id;
    private String first_name;
    private String last_name;
    private String dob;
    private String email;
    private int phone;
    private String password;
    private Bitmap profile_picture;

    public Person(int id, String first_name, String last_name, String dob, String email, int phone, String password, Bitmap profile_picture) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.profile_picture = profile_picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(Bitmap profile_picture) {
        this.profile_picture = profile_picture;
    }
}
