package com.example.edoapp.Pages.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.edoapp.AuthPages.SellerRegister;
import com.example.edoapp.Pages.ChoosingPage;
import com.example.edoapp.R;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class SellerHomeActivity extends AppCompatActivity {
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 22;

    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    UploadTask uploadTask;

    EditText et_productName, et_productStack, et_productCost, et_productSize,et_productCode;
    ImageView iv_productImage;
    Button btn_productAdd;

    String userid;

    Bundle bundle;
    String name="";
    String stack="";
    String cost="";
    String size="";
    String image="";
    String code="";
    boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        try{
            bundle = getIntent().getExtras();
            name=bundle.getString("name");
            stack=bundle.getString("stack");
            cost=bundle.getString("cost");
            size=bundle.getString("size");
            image=bundle.getString("imageUrl");
            code=bundle.getString("code");

            isUpdate=bundle.getBoolean("isUpdate");
        }catch (Exception e){
            e.printStackTrace();
        }


        et_productName = findViewById(R.id.et_productname);
        et_productStack = findViewById(R.id.et_productstack);
        et_productCost = findViewById(R.id.et_productcost);
        et_productCode = findViewById(R.id.et_productcode);
        et_productSize = findViewById(R.id.et_productsize);
        iv_productImage = findViewById(R.id.iv_productimage);
        btn_productAdd = findViewById(R.id.btn_productadd);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        FirebaseUser firebaseUser=mAuth.getCurrentUser();

        userid=firebaseUser.getUid();
        if(isUpdate){
            et_productName.setText(name);
            et_productStack.setText(stack);
            et_productCost.setText(cost);
            et_productSize.setText(size);
            et_productCode.setText(code);
            Glide.with(SellerHomeActivity.this).load(image).into(iv_productImage);
            btn_productAdd.setText("Güncelle");
        }

        btn_productAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_productName.getText().toString().isEmpty() && !et_productStack.getText().toString().isEmpty() && !et_productCost.getText().toString().isEmpty() && !et_productSize.getText().toString().isEmpty() && !iv_productImage.getResources().toString().isEmpty()) {
                    if(isUpdate){
                        UpdateProduct();
                    }else{
                        uploadImage();
                    }

                }else{
                    Toast.makeText(SellerHomeActivity.this, "Boş Alan Bırakmayınız.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();

            }
        });


    }


    private void SelectImage() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);


        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            filePath = data.getData();
            try {


                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                iv_productImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {
        if (filePath != null) {

            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());


            uploadTask = ref.putFile(filePath);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }


                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri=downloadUri.toString();
                        reference= FirebaseDatabase.getInstance().getReference("Products").child(userid).child(et_productName.getText().toString());

                    HashMap<String, String> hashMap= new HashMap<>();
                    hashMap.put("name",et_productName.getText().toString());
                    hashMap.put("stack",et_productStack.getText().toString());
                    hashMap.put("size",et_productSize.getText().toString());
                    hashMap.put("imageUrl",mUri);
                    hashMap.put("cost",et_productCost.getText().toString());

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(SellerHomeActivity.this, "Ürün  eklendi", Toast.LENGTH_SHORT).show();
                                et_productCode.setText("");
                                et_productCost.setText("");
                                et_productSize.setText("");
                                et_productName.setText("");
                                et_productStack.setText("");
                                iv_productImage.setImageResource(R.drawable.seller);
                                Intent i = new Intent(SellerHomeActivity.this, SellerProductsActivity.class);
                                startActivity(i);
                                finish();


                            }
                        }
                    });
                    } else {

                    }
                }
            });
        }
    }
    private void UpdateProduct() {
        if (filePath != null) {


            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Yükleniyor...");
            progressDialog.show();


            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());


            uploadTask = ref.putFile(filePath);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }


                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri=downloadUri.toString();

                        reference= FirebaseDatabase.getInstance().getReference("Products").child(userid).child(et_productName.getText().toString());

                        HashMap<String, String> hashMap= new HashMap<>();
                        hashMap.put("name",et_productName.getText().toString());
                        hashMap.put("stack",et_productStack.getText().toString());
                        hashMap.put("size",et_productSize.getText().toString());
                        hashMap.put("imageUrl",mUri);
                        hashMap.put("cost",et_productCost.getText().toString());
                        hashMap.put("code",et_productCode.getText().toString());

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(SellerHomeActivity.this, "Ürün eklendi", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {

                    }
                }
            });
        }else{
            reference= FirebaseDatabase.getInstance().getReference("Products").child(userid).child(et_productName.getText().toString());

            HashMap<String, String> hashMap= new HashMap<>();
            hashMap.put("name",et_productName.getText().toString());
            hashMap.put("stack",et_productStack.getText().toString());
            hashMap.put("size",et_productSize.getText().toString());
            hashMap.put("imageUrl",image);
            hashMap.put("cost",et_productCost.getText().toString());
            hashMap.put("code",et_productCode.getText().toString());

            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SellerHomeActivity.this, "Ürün güncellendi", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SellerHomeActivity.this, SellerProductsActivity.class);
                        startActivity(intent);

                    }
                }
            });
        }
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
                Intent intent = new Intent(SellerHomeActivity.this, SellerProductsActivity.class);
                startActivity(intent);
                break;

            case R.id.process:
                 intent = new Intent(SellerHomeActivity.this, ProcessActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                Intent i= new Intent(SellerHomeActivity.this, ChoosingPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }


}