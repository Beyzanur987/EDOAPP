package com.example.edoapp.Models;

public class Teacher {
    public String id;
    public String username;
    public String email;
    public String school;
    public String city;


    public Teacher(String id,String username, String email, String school, String city) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.school = school;
        this.city = city;

    }
    public Teacher() {}
}
