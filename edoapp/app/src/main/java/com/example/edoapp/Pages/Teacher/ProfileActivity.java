package com.example.edoapp.Pages.Teacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edoapp.Adapters.TeacherAdapter;
import com.example.edoapp.AuthPages.SellerLogin;
import com.example.edoapp.Models.Teacher;
import com.example.edoapp.Pages.ChoosingPage;
import com.example.edoapp.R;
import com.example.edoapp.TeacherDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    Teacher teacher;
    private FirebaseDatabase db,dbBucket;
    private DatabaseReference dbRef,dbBucketRef;
    private FirebaseAuth mAuth;
    TextView tv_profileName,tv_profileEmail;
    EditText et_profileSchool,et_profileAddress;
    Button btn_güncelle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        dbBucket = FirebaseDatabase.getInstance();// veritabanına erişim sağladık
        mAuth = FirebaseAuth.getInstance();
        StoreDataList();
        tv_profileEmail=findViewById(R.id.tv_profileEmail);
        tv_profileName=findViewById(R.id.tv_profileName);
        et_profileSchool=findViewById(R.id.et_profileSchool);
        et_profileAddress=findViewById(R.id.et_profileAddress);
        btn_güncelle=findViewById(R.id.btn_update);

        btn_güncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_profileSchool.getText().toString().isEmpty() &&
                        !et_profileAddress.getText().toString().isEmpty()) {
                    dbBucketRef = dbBucket.getReference("Users/" + mAuth.getUid());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("city", et_profileAddress.getText().toString());
                    hashMap.put("school", et_profileSchool.getText().toString());
                    dbBucketRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Profil güncellendi!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ProfileActivity.this, TeacherDetail.class);

                                startActivity(intent);
                            }
                        }
                    });
                }


            }
        });
    }

    private void StoreDataList() {
        dbRef = db.getReference("Users").child(mAuth.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("teacher snapshot= ", dataSnapshot.toString());
                    Teacher task = dataSnapshot.getValue(Teacher.class);
                    teacher=task;
                    fillTexts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    void fillTexts(){
        et_profileAddress.setText(teacher.city);
        et_profileSchool.setText(teacher.school);
        tv_profileName.setText(teacher.username);
        tv_profileEmail.setText(teacher.email);
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
                Intent intent = new Intent(ProfileActivity.this, TeacherBucketActivity.class);
                startActivity(intent);
                break;
            case R.id.profile:
                intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                Intent i= new Intent(ProfileActivity.this, ChoosingPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                mAuth.signOut();
                finish();
                break;
        }
        return true;
    }
}