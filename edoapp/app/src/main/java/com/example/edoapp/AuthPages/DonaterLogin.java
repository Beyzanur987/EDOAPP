package com.example.edoapp.AuthPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edoapp.Pages.Donater.DonaterHomeActivity;
import com.example.edoapp.Pages.Teacher.HomeActivity;
import com.example.edoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DonaterLogin extends AppCompatActivity {


    //Login sayfası pek anlatılacak bir şeyi yok diğerlerinden ayrı olarak

    private EditText editTextUserName;
    private EditText editTextUserPassword;
    private Button buttonLogin;
    private TextView txtRegister;
    private TextView txtReset;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String userName;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donater_login);
        editTextUserName = (EditText)findViewById(R.id.editTextUserName);
        editTextUserPassword = (EditText)findViewById(R.id.editTextUserPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        txtReset=findViewById(R.id.forgot_password);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser(); // authenticate olan kullaniciyi aliyoruz eger var ise




        txtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DonaterLogin.this, ResetPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = editTextUserName.getText().toString();
                userPassword = editTextUserPassword.getText().toString();
                if(userName.isEmpty() || userPassword.isEmpty()){

                    Toast.makeText(getApplicationContext(),"Lütfen gerekli alanları doldurunuz!",Toast.LENGTH_SHORT).show();

                }else{

                    login();
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonaterLogin.this, DonaterRegister.class);
                startActivity(intent);
            }
        });
    }
    private void login() {
     //böyle bir kullanıcı var mı yok mu kontrol ediyor hazır bir kod
        mAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(DonaterLogin.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Intent i = new Intent(DonaterLogin.this, DonaterHomeActivity.class);
                            startActivity(i);
                            finish();

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Mail yada Şifre yanlış",Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}