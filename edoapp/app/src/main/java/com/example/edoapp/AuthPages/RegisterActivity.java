package com.example.edoapp.AuthPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.edoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    //Kayıt ol sayfası

    private EditText registerUserName;
    private EditText registerPassword;
    private EditText registerEmail;
    private EditText registerSchoolName;
    private EditText registerCityName;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userSchool;
    private String userCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        registerUserName = (EditText)findViewById(R.id.registerUserName);
        registerPassword = (EditText)findViewById(R.id.registerPassword);
        registerEmail = (EditText)findViewById(R.id.registerUserEmail);
        registerSchoolName = (EditText)findViewById(R.id.registerSchoolName);
        registerCityName = (EditText)findViewById(R.id.registerCityName);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = registerUserName.getText().toString();
                userPassword = registerPassword.getText().toString();
                userEmail = registerEmail.getText().toString();
                userSchool = registerSchoolName.getText().toString();
                userCity = registerCityName.getText().toString();
                if(userName.isEmpty() || userPassword.isEmpty() || userEmail.isEmpty() || userSchool.isEmpty() || userCity.isEmpty()){

                    Toast.makeText(getApplicationContext(),"Lütfen gerekli alanları doldurunuz!",Toast.LENGTH_SHORT).show();

                }else{
                    if(userEmail.toLowerCase(Locale.ROOT).contains("@outlook.com")){
                        register();
                    }else{
                        Toast.makeText(RegisterActivity.this, "Lütfen geçerli bir email uzantısı giriniz", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }
    private void register() {//Girilen bilgileri veritabanına gönderdiğimiz kısım

        mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser=mAuth.getCurrentUser();
                            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(),"Email adresinize doğrulama maili gönderildi.",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener(){
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Email onayı gönderilemedi!",Toast.LENGTH_SHORT).show();
                                }
                            });
                            String userid=firebaseUser.getUid();
                            reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap= new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",userName);
                            hashMap.put("school",userSchool);
                            hashMap.put("city",userCity);
                            hashMap.put("email",userEmail);


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });



                        }
                        else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }


                });

    }
}