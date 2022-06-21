package com.example.edoapp.Pages.Teacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.edoapp.Adapters.ProductsAdapter;
import com.example.edoapp.Models.Product;
import com.example.edoapp.Pages.ChoosingPage;
import com.example.edoapp.Pages.Seller.SellerProductsActivity;
import com.example.edoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TeacherProductsActivity extends AppCompatActivity {
    ArrayList<Product> productArrayList;
    Product product;
    ProductsAdapter productsAdapter;
    RecyclerView recyclerView;
    private FirebaseDatabase db, dbBucket;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef, dbBucketRef; // databasede hangi path e ulaşacağımızı belirlediğimiz sınıftan nesne oluşturduk
    private FirebaseUser fUser; // firebasein kullanıcı sınıfından bir nesne oluşturduk current user değerlerini alabilmek için
    String selectedIndex;
    EditText et_search;
    Bundle bundle;
    String id = "";
    String school="";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_products);
        bundle = getIntent().getExtras();
        id = bundle.getString("sellerId");
        school = bundle.getString("teacherSchool");
        mAuth = FirebaseAuth.getInstance();
        et_search = findViewById(R.id.et_search);
        db = FirebaseDatabase.getInstance();// veritabanına
        dbBucket = FirebaseDatabase.getInstance();// veritabanına erişim
        fUser = FirebaseAuth.getInstance().getCurrentUser(); // şuanki kullanıcının verilerini firebaseuser classının nesnesinin içine attık
        recyclerView = findViewById(R.id.rc_productsTeacher);
        productArrayList = new ArrayList<>();
        StoreDataList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        recyclerView.addOnItemTouchListener(new SellerProductsActivity.RecyclerItemClickListener(getApplicationContext(), recyclerView, new SellerProductsActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                product = productsAdapter.getItemName(position);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TeacherProductsActivity.this);
                final EditText edittext = new EditText(TeacherProductsActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                edittext.setLayoutParams(lp);
                alertDialog.setView(edittext);
                alertDialog.setMessage("Lütfen Adet Giriniz");
                alertDialog.setTitle("Sepete Ekle");

                alertDialog.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            int YourEditTextValue = Integer.parseInt(edittext.getText().toString());
                            if (YourEditTextValue > 0 && YourEditTextValue <= Integer.parseInt(product.stack)) {
                                dbBucketRef = dbBucket.getReference("Bucket/" + fUser.getUid()+"/"+product.name);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", fUser.getUid());
                                hashMap.put("sellerId", id);
                                hashMap.put("name", product.name);
                                hashMap.put("size", product.size);
                                hashMap.put("imageUrl", product.imageUrl);
                                hashMap.put("cost", product.cost);
                                hashMap.put("teacherSchool", school);
                                hashMap.put("stack", String.valueOf(YourEditTextValue));
                                dbBucketRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Ürün sepete eklendi", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(), "Lütfen geçerli bir adet giriniz", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(TeacherProductsActivity.this, "Lütfen bir adet giriniz", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alertDialog.show();

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

        ArrayList<Product> filterdNames = new ArrayList<>();

        for (Product s : productArrayList) {
            if (s.name.toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(s);
            }
        }

        productsAdapter.filterList(filterdNames);
    }

    //Ürünleri çağırdığımız yer
    private void StoreDataList() {
        dbRef = db.getReference("Products/" + id);
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productArrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Product task = ds.getValue(Product.class);
                    productArrayList.add(task);

                }

                productsAdapter = new ProductsAdapter(getApplicationContext(), productArrayList);
                productsAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(productsAdapter);



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
                Intent intent = new Intent(TeacherProductsActivity.this, TeacherBucketActivity.class);
                startActivity(intent);
                break;
            case R.id.profile:
                intent = new Intent(TeacherProductsActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                Intent i = new Intent(TeacherProductsActivity.this, ChoosingPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }
}