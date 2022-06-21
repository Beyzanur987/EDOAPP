package com.example.edoapp.Pages.Teacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.edoapp.Adapters.SellersAdapter;

import com.example.edoapp.Models.Seller;
import com.example.edoapp.Models.Teacher;
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

public class HomeActivity extends AppCompatActivity {
    Teacher teacher;
    Button btn_resend,btn_logout;
    TextView tv_isvalid;
    private FirebaseAuth fAuth;
    ArrayList<Seller> sellerArrayList;
    Seller seller;
    SellersAdapter sellersAdapter;
    RecyclerView recyclerView;
    private FirebaseDatabase db,dbTeacher;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef,dbRefTeacher; // databasede hangi path e ulaşacağımızı belirlediğimiz sınıftan nesne oluşturduk
    private FirebaseUser fUser; // firebasein kullanıcı sınıfından bir nesne oluşturduk current user değerlerini alabilmek için
    String selectedIndex;
    EditText et_search;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fAuth = FirebaseAuth.getInstance();



        et_search=findViewById(R.id.et_search);
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        dbTeacher = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        fUser = FirebaseAuth.getInstance().getCurrentUser(); // şuanki kullanıcının verilerini firebaseuser classının nesnesinin içine attık
        recyclerView=findViewById(R.id.rc_sellers);
        sellerArrayList= new ArrayList<>();
        StoreDataList();
        getTeacher();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //listenin elemanlarının click fonksiyonları

        recyclerView.addOnItemTouchListener(new SellerProductsActivity.RecyclerItemClickListener(getApplicationContext(), recyclerView, new SellerProductsActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(HomeActivity.this, TeacherProductsActivity.class);
                seller=sellersAdapter.getItemName(position);
                intent.putExtra("sellerId",seller.id);
                intent.putExtra("teacherSchool",teacher.school);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        //search edittextine yazı yazıldığında
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

    }

    private void filter(String text) {
        ArrayList<Seller> filterdNames = new ArrayList<>();

        for (Seller s : sellerArrayList) {
            if (s.firm.toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(s);
            }
        }

        sellersAdapter.filterList(filterdNames);
    }

    //Ürünleri çağırdığımız yer
    private void StoreDataList() {
        dbRef = db.getReference("Sellers");
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sellerArrayList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Seller task=ds.getValue(Seller.class);
                    sellerArrayList.add(task);
                }

                sellersAdapter=new SellersAdapter(getApplicationContext(),sellerArrayList);
                sellersAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(sellersAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getTeacher() {
        dbRefTeacher = dbTeacher.getReference("Users").child(fUser.getUid());
        dbRefTeacher.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     teacher=dataSnapshot.getValue(Teacher.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teacher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.buckets:
                Intent intent = new Intent(HomeActivity.this, TeacherBucketActivity.class);
                startActivity(intent);
                break;

            case R.id.profile:
                 intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                Intent i= new Intent(HomeActivity.this, ChoosingPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }

}