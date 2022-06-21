package com.example.edoapp.Models;

public class Bucket {
    public String id;
    public String teacherSchool;
    public String sellerId;
    public String name;
    public String size;
    public String imageUrl;
    public String cost;
    public String stack;

    public Bucket(String id,String teacherSchool,String sellerId, String name, String size, String imageUrl, String cost, String stack) {
        this.id = id;
        this.teacherSchool = teacherSchool;
        this.sellerId = sellerId;
        this.name = name;
        this.size = size;
        this.imageUrl = imageUrl;
        this.cost = cost;
        this.stack = stack;
    }
    public Bucket() {}
}
