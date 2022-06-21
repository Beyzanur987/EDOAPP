package com.example.edoapp.Pages.Donater;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.edoapp.Adapters.ProductsAdapter;
import com.example.edoapp.Adapters.TeacherAdapter;
import com.example.edoapp.Models.Product;
import com.example.edoapp.Models.Teacher;
import com.example.edoapp.Pages.ChoosingPage;
import com.example.edoapp.Pages.Seller.SellerProductsActivity;
import com.example.edoapp.Pages.Teacher.TeacherBucketActivity;
import com.example.edoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonaterHomeActivity extends AppCompatActivity {

    RecyclerView rc_donatersTeacher;
    ArrayList<Teacher> teacherArrayList;
    Teacher teacher;
    TeacherAdapter teacherAdapter;
    private FirebaseDatabase db, dbBucket;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef, dbBucketRef; // databasede hangi path e ulaşacağımızı belirlediğimiz sınıftan nesne oluşturduk
    private FirebaseUser fUser;
    String selectedIndex;
    EditText et_search;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donater_home);
        mAuth = FirebaseAuth.getInstance();
        et_search = findViewById(R.id.et_search);
        db = FirebaseDatabase.getInstance();
        dbBucket = FirebaseDatabase.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser(); // şuanki kullanıcının verilerini firebaseuser classının nesnesinin içine attık
        rc_donatersTeacher = findViewById(R.id.rc_donatersTeacher);
        teacherArrayList = new ArrayList<>();
        StoreDataList();
        rc_donatersTeacher.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rc_donatersTeacher.addOnItemTouchListener(new SellerProductsActivity.RecyclerItemClickListener(getApplicationContext(), rc_donatersTeacher, new SellerProductsActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                teacher = teacherAdapter.getItemName(position);
                Intent intent =new Intent(DonaterHomeActivity.this,DonaterBucketActivity.class);
                intent.putExtra("id",teacher.id);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                selectedIndex = String.valueOf(position);


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

        ArrayList<Teacher> filterdNames = new ArrayList<>();


        for (Teacher s : teacherArrayList) {

            if (s.username.toLowerCase().contains(text.toLowerCase())) {

                filterdNames.add(s);
            }
        }


        teacherAdapter.filterList(filterdNames);
    }


    private void StoreDataList() {
        dbRef = db.getReference("Users");
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherArrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Teacher task = ds.getValue(Teacher.class);
                    teacherArrayList.add(task);


                }

                teacherAdapter = new TeacherAdapter(getApplicationContext(), teacherArrayList);
                teacherAdapter.notifyDataSetChanged();
                rc_donatersTeacher.setAdapter(teacherAdapter);



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
               Intent i = new Intent(DonaterHomeActivity.this, ChoosingPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }
}