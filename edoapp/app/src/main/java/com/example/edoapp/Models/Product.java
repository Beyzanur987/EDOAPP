package com.example.edoapp.Models;

public class Product {
   public String name;
   public String size;
   public String imageUrl;
   public String cost;
   public String code;
   public String stack;

    public Product(String name, String size, String imageUrl, String cost,String code, String stack) {
        this.name = name;
        this.size = size;
        this.imageUrl = imageUrl;
        this.cost = cost;
        this.code = code;
        this.stack = stack;
    }
    public Product() {}
}
