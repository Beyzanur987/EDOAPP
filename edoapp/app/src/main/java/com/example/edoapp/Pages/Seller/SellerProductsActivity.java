package com.example.edoapp.Pages.Seller;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.edoapp.Adapters.ProductsAdapter;
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

public class SellerProductsActivity extends AppCompatActivity {
    ArrayList<Product> productArrayList;
    Product product;
    private FirebaseAuth mAuth;
    ProductsAdapter productsAdapter;
    RecyclerView recyclerView;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private FirebaseUser fUser;
    String selectedIndex;
    EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_products);
        mAuth = FirebaseAuth.getInstance();
        et_search=findViewById(R.id.et_search);
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        fUser = FirebaseAuth.getInstance().getCurrentUser(); // şuanki kullanıcının verilerini firebaseuser classının nesnesinin içine attık
        recyclerView=findViewById(R.id.rc_products);
        productArrayList= new ArrayList<>();
        StoreDataList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
                selectedIndex=String.valueOf(position);
                product=productsAdapter.getItemName(position);
                selectIslem();
            }
        }));


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
        dbRef = db.getReference("Products/"+fUser.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productArrayList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Product task=ds.getValue(Product.class);
                    productArrayList.add(task);
                }

                productsAdapter=new ProductsAdapter(getApplicationContext(),productArrayList);
                productsAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(productsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void selectIslem() {
        final CharSequence[] options = { "Sil","Güncelle"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SellerProductsActivity.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Sil"))
                {
                    db.getReference("Products").child(fUser.getUid()).child(product.name).removeValue();
                    Toast.makeText(SellerProductsActivity.this, "Ürün Silindi", Toast.LENGTH_SHORT).show();

                    productsAdapter.notifyDataSetChanged();//sayfayı yeniler
                }
                else if (options[item].equals("Güncelle"))
                {
                    //copy text

                   Intent intent=new Intent(SellerProductsActivity.this, SellerHomeActivity.class);
                    intent.putExtra("name",product.name);
                    intent.putExtra("stack",product.stack);
                    intent.putExtra("cost",product.cost);
                    intent.putExtra("code",product.code);
                    intent.putExtra("size",product.size);
                    intent.putExtra("imageUrl",product.imageUrl);
                    intent.putExtra("isUpdate",true);
                    startActivity(intent);

                    dialog.dismiss();

                }
            }
        });
        builder.show();
    }


    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;
        //click için bu kısmı yukarıda recylerview ile kullanıyoruz
        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onLongItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
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
                Intent intent = new Intent(SellerProductsActivity.this, SellerProductsActivity.class);
                startActivity(intent);
                break;

            case R.id.process:
                intent = new Intent(SellerProductsActivity.this, ProcessActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                Intent i = new Intent(SellerProductsActivity.this, ChoosingPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }

}