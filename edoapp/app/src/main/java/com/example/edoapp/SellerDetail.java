package com.example.edoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.edoapp.Adapters.ProcessAdapter;
import com.example.edoapp.AuthPages.DonaterLogin;
import com.example.edoapp.AuthPages.MainActivity;
import com.example.edoapp.AuthPages.SellerLogin;
import com.example.edoapp.Pages.ChoosingPage;
import com.example.edoapp.Pages.Seller.ProcessActivity;
import com.example.edoapp.Pages.Seller.SellerHomeActivity;
import com.example.edoapp.Pages.Seller.SellerProductsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SellerDetail extends AppCompatActivity {

    ImageView iv_addProdact,iv_prodact,iv_orders;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);

        mAuth = FirebaseAuth.getInstance();
        iv_addProdact=findViewById(R.id.iv_addProdact);
        iv_prodact=findViewById(R.id.iv_prodact);
        iv_orders=findViewById(R.id.iv_orders);

        iv_addProdact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerDetail.this, SellerHomeActivity.class);
                startActivity(intent);
            }
        });

        iv_prodact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerDetail.this, SellerProductsActivity.class);
                startActivity(intent);
            }
        });

        iv_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SellerDetail.this, ProcessActivity.class);
                startActivity(intent);
            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.products:
                Intent intent = new Intent(SellerDetail.this, SellerProductsActivity.class);
                startActivity(intent);
                break;

            case R.id.process:
                intent = new Intent(SellerDetail.this, ProcessActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                Intent i = new Intent(SellerDetail.this, ChoosingPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }
}