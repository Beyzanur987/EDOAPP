package com.example.edoapp.Pages.Seller;

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

import com.example.edoapp.Adapters.ProcessAdapter;
import com.example.edoapp.Adapters.ProductsAdapter;
import com.example.edoapp.Models.Process;
import com.example.edoapp.Models.Product;
import com.example.edoapp.Pages.ChoosingPage;
import com.example.edoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProcessActivity extends AppCompatActivity {
    ArrayList<Process> processArrayList;
    Process process;
    private FirebaseAuth mAuth;
    ProcessAdapter processAdapter;
    RecyclerView recyclerView;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private FirebaseUser fUser; // firebasein kullanıcı sınıfından bir nesne oluşturduk current user değerlerini alabilmek için
    String selectedIndex;
    EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_process);
        et_search = findViewById(R.id.et_search);
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        fUser = FirebaseAuth.getInstance().getCurrentUser(); // şuanki kullanıcının verilerini firebaseuser classının nesnesinin içine attık
        recyclerView = findViewById(R.id.rc_process);
        processArrayList = new ArrayList<>();
        StoreDataList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        recyclerView.addOnItemTouchListener(new SellerProductsActivity.RecyclerItemClickListener(getApplicationContext(), recyclerView, new SellerProductsActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
                selectedIndex = String.valueOf(position);
                process = processAdapter.getItemName(position);

            }
        }));




    }

    //Ürünleri çağırdığımız yer
    private void StoreDataList() {
        dbRef = db.getReference("Process/"+fUser.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // processArrayList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    for(DataSnapshot thirdDs : ds.getChildren()){
                        Process task=thirdDs.getValue(Process.class);
                        processArrayList.add(task);
                    }
                }


                processAdapter=new ProcessAdapter(getApplicationContext(),processArrayList);
                processAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(processAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                Intent intent = new Intent(ProcessActivity.this, SellerProductsActivity.class);
                startActivity(intent);
                break;

            case R.id.process:
                intent = new Intent(ProcessActivity.this, ProcessActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                intent = new Intent(ProcessActivity.this, ChoosingPage.class);
                startActivity(intent);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }

}