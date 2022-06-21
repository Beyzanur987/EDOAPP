package com.example.edoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.edoapp.Pages.Seller.ProcessActivity;
import com.example.edoapp.Pages.Seller.SellerHomeActivity;
import com.example.edoapp.Pages.Seller.SellerProductsActivity;
import com.example.edoapp.Pages.Teacher.HomeActivity;
import com.example.edoapp.Pages.Teacher.ProfileActivity;
import com.example.edoapp.Pages.Teacher.TeacherBucketActivity;
import com.example.edoapp.Pages.Teacher.TeacherProductsActivity;

public class TeacherDetail extends AppCompatActivity {
    ImageView iv_seller1,iv_shoping,iv_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);

        iv_seller1=findViewById(R.id.iv_seller1);
        iv_shoping=findViewById(R.id.iv_shoping);
        iv_profile=findViewById(R.id.iv_profile);

        iv_seller1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TeacherDetail.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        iv_shoping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TeacherDetail.this, TeacherBucketActivity.class);
                startActivity(intent);
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TeacherDetail.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}