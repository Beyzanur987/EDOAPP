package com.example.edoapp.AuthPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.edoapp.Pages.Seller.SellerHomeActivity;
import com.example.edoapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class SellerRegister extends AppCompatActivity {

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 22;


    FirebaseStorage storage;
    StorageReference storageReference;

    private EditText registerPassword;
    private EditText registerEmail;
    private EditText registerFirmName;
    private EditText registerAddress;
    private ImageView iv_logo;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    UploadTask uploadTask;

    private String userPassword;
    private String userEmail;
    private String userFirm;
    private String userAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);
        registerPassword = (EditText)findViewById(R.id.registerPassword);
        registerEmail = (EditText)findViewById(R.id.registerUserEmail);
        registerFirmName = (EditText)findViewById(R.id.registerFirmName);
        registerAddress = (EditText)findViewById(R.id.registerAddress);
        iv_logo = findViewById(R.id.iv_logo);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mAuth = FirebaseAuth.getInstance();

        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        // kayıt buton
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                userPassword = registerPassword.getText().toString();
                userEmail = registerEmail.getText().toString();
                userAddress = registerAddress.getText().toString();
                userFirm = registerFirmName.getText().toString();
                if(userPassword.isEmpty() || userEmail.isEmpty() || userAddress.isEmpty() || userFirm.isEmpty()){

                    Toast.makeText(getApplicationContext(),"Lütfen gerekli alanları doldurunuz!",Toast.LENGTH_SHORT).show();

                }else{

                    if(filePath==null){
                        Toast.makeText(SellerRegister.this, "Lütfen bir logo seçiniz.", Toast.LENGTH_SHORT).show();
                    }else{
                        register();
                    }


                }

            }
        });
    }
    private void register() { //veritabanına kaydeder

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
                        mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                                .addOnCompleteListener(SellerRegister.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(Task<AuthResult> task) {
                                        if(task.isSuccessful()){

                                            FirebaseUser firebaseUser=mAuth.getCurrentUser();

                                            String userid=firebaseUser.getUid();

                                            reference= FirebaseDatabase.getInstance().getReference("Sellers").child(userid);

                                            HashMap<String, String> hashMap= new HashMap<>();
                                            hashMap.put("id",userid);
                                            hashMap.put("logo",mUri);
                                            hashMap.put("address",userAddress);
                                            hashMap.put("firm",userFirm);

                                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Intent i = new Intent(SellerRegister.this, SellerLogin.class);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                }
                                            });



                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"Geçerli bir mail adresi giriniz.",Toast.LENGTH_SHORT).show();
                                        }

                                    }


                                });

                    } else {
                        Toast.makeText(getApplicationContext(),"Lütfen bir logo seçiniz",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



    }

    // Select Image method
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
                iv_logo.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

}