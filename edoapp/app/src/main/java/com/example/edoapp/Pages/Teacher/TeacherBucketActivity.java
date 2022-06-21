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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edoapp.Adapters.BucketsAdapter;
import com.example.edoapp.Adapters.ProductsAdapter;
import com.example.edoapp.Models.Bucket;
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

public class TeacherBucketActivity extends AppCompatActivity {

    ArrayList<Bucket> bucketArrayList;
    Bucket bucket;
    Product product;
    private FirebaseAuth mAuth;
    BucketsAdapter bucketsAdapter;
    RecyclerView recyclerView;
    private FirebaseDatabase db,dbBucket,dbProduct;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
    private DatabaseReference dbRef,dbBucketRef,dbProductRef; // databasede hangi path e ulaşacağımızı belirlediğimiz sınıftan nesne oluşturduk
    private FirebaseUser fUser; // firebasein kullanıcı sınıfından bir nesne oluşturduk current user değerlerini alabilmek için
    String selectedIndex;
    TextView tv_productcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_teacher_bucket);
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        dbBucket = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        dbProduct = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        fUser = FirebaseAuth.getInstance().getCurrentUser(); // şuanki kullanıcının verilerini firebaseuser classının nesnesinin içine attık
        recyclerView=findViewById(R.id.rv_buckets);
        bucketArrayList = new ArrayList<>();
        tv_productcount=findViewById(R.id.tv_productcount);
        StoreDataList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        //listenin elemanlarının click fonksiyonları

        recyclerView.addOnItemTouchListener(new SellerProductsActivity.RecyclerItemClickListener(getApplicationContext(), recyclerView, new SellerProductsActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
                selectedIndex=String.valueOf(position);
                bucket= bucketsAdapter.getItemName(position);
                getSellerProducts();
                selectIslem();
            }
        }));



    }
    void getSellerProducts(){
        dbProductRef=dbProduct.getReference("Products/"+bucket.sellerId).child(bucket.name);
        dbProductRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                product=snapshot.getValue(Product.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Ürünleri çağırdığımız yer
    private void StoreDataList() {
        dbRef = db.getReference("Bucket/"+fUser.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bucketArrayList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Bucket task=ds.getValue(Bucket.class);
                    bucketArrayList.add(task);

                }

                bucketsAdapter =new BucketsAdapter(getApplicationContext(), bucketArrayList);
                bucketsAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(bucketsAdapter);



                tv_productcount.setText(""+bucketArrayList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void selectIslem() {
        final CharSequence[] options = { "Sil","Güncelle"};
        AlertDialog.Builder builder = new AlertDialog.Builder(TeacherBucketActivity.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Sil"))
                {
                    db.getReference("Bucket").child(fUser.getUid()).child(bucket.name).removeValue();
                    Toast.makeText(TeacherBucketActivity.this, "Ürün Sepetten Silindi", Toast.LENGTH_SHORT).show();

                    bucketsAdapter.notifyDataSetChanged();//sayfayı yenilemeye yarar
                }
                else if (options[item].equals("Güncelle"))
                {
                    //copy text

                    // sadece miktar değiştirebilir veya ürünü siler
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TeacherBucketActivity.this);
                    final EditText edittext = new EditText(TeacherBucketActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    edittext.setLayoutParams(lp);
                    alertDialog.setView(edittext);
                    alertDialog.setMessage("Lütfen Adet Giriniz");
                    alertDialog.setTitle("Sepeti Güncelle");


                    alertDialog.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            boolean isNotEmpty=true;
                            try {

                                try{
                                    if(Integer.parseInt(edittext.getText().toString())>0){
                                        isNotEmpty =true;
                                    }
                                }catch (Exception e){
                                    isNotEmpty =false;
                                }
                                if(isNotEmpty){


                                    int YourEditTextValue = Integer.parseInt(edittext.getText().toString());
                                    if (YourEditTextValue > 0 && YourEditTextValue <= Integer.parseInt(product.stack)) {
                                        dbBucketRef = dbBucket.getReference("Bucket/" + fUser.getUid() + "/" + bucket.name);
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("stack", String.valueOf(YourEditTextValue));
                                        dbBucketRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Adet güncellendi!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Satıcının stoğunda yeterli miktarda ürün yok.", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(TeacherBucketActivity.this, "Lütfen Değer giriniz", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                Toast.makeText(TeacherBucketActivity.this, " Satıcı ürünü kaldırmış. ", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });

                    alertDialog.show();

                    dialog.dismiss();

                }
            }
        });
        builder.show();
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
                Intent intent = new Intent(TeacherBucketActivity.this, TeacherBucketActivity.class);
                startActivity(intent);
                break;
            case R.id.profile:
                intent = new Intent(TeacherBucketActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                Intent i= new Intent(TeacherBucketActivity.this, ChoosingPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }
}