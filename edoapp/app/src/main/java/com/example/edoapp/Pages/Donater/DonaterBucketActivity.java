package com.example.edoapp.Pages.Donater;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.edoapp.Adapters.BucketsAdapter;
import com.example.edoapp.Adapters.DonaterAdapter;
import com.example.edoapp.Models.Bucket;
import com.example.edoapp.Pages.ChoosingPage;
import com.example.edoapp.Pages.Seller.SellerProductsActivity;
import com.example.edoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonaterBucketActivity extends AppCompatActivity {
    ArrayList<Bucket> bucketArrayList;
    Bucket bucket;
    private FirebaseAuth mAuth;
    DonaterAdapter donaterAdapter;
    RecyclerView recyclerView;
    private FirebaseDatabase db,dbBucket;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef,dbBucketRef; // databasede hangi path e ulaşacağımızı belirlediğimiz sınıftan nesne oluşturduk
    private FirebaseUser fUser; // firebasein kullanıcı sınıfından bir nesne oluşturduk current user değerlerini alabilmek için
    String selectedIndex;
    TextView tv_productcount;
    Bundle bundle;
    String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donater_bucket);
        mAuth = FirebaseAuth.getInstance();
        bundle = getIntent().getExtras();
        id = bundle.getString("id");
        db = FirebaseDatabase.getInstance();
        dbBucket = FirebaseDatabase.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser(); // şuanki kullanıcının verilerini firebaseuser classının nesnesinin içine attık
        recyclerView=findViewById(R.id.rv_buckets);
        bucketArrayList = new ArrayList<>();
        tv_productcount=findViewById(R.id.tv_productcount);
        StoreDataList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



    }


    private void StoreDataList() {
        dbRef = db.getReference("Bucket/"+id); //sepetteki ürünleri çağırma
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bucketArrayList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Bucket task=ds.getValue(Bucket.class);
                    bucketArrayList.add(task);

                }


                donaterAdapter =new DonaterAdapter(getApplicationContext(), bucketArrayList);
                donaterAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(donaterAdapter);



                tv_productcount.setText(""+bucketArrayList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_donater, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Intent i  = new Intent(DonaterBucketActivity.this, ChoosingPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }
}