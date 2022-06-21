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

import com.example.edoapp.Pages.Teacher.HomeActivity;
import com.example.edoapp.R;
import com.example.edoapp.TeacherDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {



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
        setContentView(R.layout.activity_main);

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
                Intent i = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = editTextUserName.getText().toString();//yazılan login maili gettext ile çekiliyor.
                userPassword = editTextUserPassword.getText().toString();// yazılan şifreyi gettext ile çekiyoruz
                if(userName.isEmpty() || userPassword.isEmpty() || !userName.contains("outlook.com")){

                    Toast.makeText(getApplicationContext(),"Lütfen gerekli alanları doğru şekilde doldurunuz!",Toast.LENGTH_SHORT).show();

                }else{

                    login();
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {

        mAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(MainActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){// dönen sonuç basarılı ise
                            if(!mAuth.getCurrentUser().isEmailVerified()){

                                Toast.makeText(MainActivity.this, "Lütfen Emailinizi onaylayın!", Toast.LENGTH_SHORT).show();

                            }else{
                                Intent i = new Intent(MainActivity.this, TeacherDetail.class);
                                startActivity(i);
                                finish();
                            }
                        }
                        else{

                            Toast.makeText(getApplicationContext(),"Mail ya da şifre yanlış",Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}