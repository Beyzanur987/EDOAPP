package com.example.edoapp.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.edoapp.AuthPages.MainActivity;
import com.example.edoapp.AuthPages.SellerLogin;
import com.example.edoapp.AuthPages.DonaterLogin;
import com.example.edoapp.R;

public class ChoosingPage extends AppCompatActivity {

    Button btn_donater,btn_teacher,btn_seller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_page);
        btn_donater=findViewById(R.id.btn_donater);
        btn_teacher=findViewById(R.id.btn_teacher);
        btn_seller=findViewById(R.id.btn_seller);

        btn_donater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChoosingPage.this, DonaterLogin.class);
                startActivity(intent);
            }
        });

        btn_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChoosingPage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btn_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChoosingPage.this, SellerLogin.class);
                startActivity(intent);
            }
        });

    }
}