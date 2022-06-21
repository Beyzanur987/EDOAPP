package com.example.edoapp.Models;

public class Process {
    public String teacherId;
    public String teacherSchool;
    public String sellerId;
    public String name;
    public String size;
    public String imageUrl;
    public String cost;
    public String piece;

    public Process(String teacherId,String teacherSchool,String sellerId, String name, String size, String imageUrl, String cost, String piece) {
        this.teacherId = teacherId;
        this.teacherSchool = teacherSchool;
        this.sellerId = sellerId;
        this.name = name;
        this.size = size;
        this.imageUrl = imageUrl;
        this.cost = cost;
        this.piece = piece;
    }
    public Process() {}
}
