package com.example.edoapp.Adapters;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.edoapp.AuthPages.MainActivity;
import com.example.edoapp.AuthPages.RegisterActivity;
import com.example.edoapp.Models.Bucket;
import com.example.edoapp.Models.Product;
import com.example.edoapp.Pages.Teacher.TeacherBucketActivity;
import com.example.edoapp.Pages.Teacher.TeacherProductsActivity;
import com.example.edoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DonaterAdapter extends RecyclerView.Adapter<DonaterAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Bucket> bucketArrayList;


    public DonaterAdapter(Context context, ArrayList<Bucket> bucketArrayList) {
        this.context = context;
        this.bucketArrayList = bucketArrayList;

    }

    @NonNull
    @Override
    public DonaterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.listview_donater_row,parent,false);
        return new DonaterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonaterAdapter.ViewHolder holder, int position) {

        holder.nametext.setText(bucketArrayList.get(position).name);
        holder.sizetext.setText("Boyut: "+ bucketArrayList.get(position).size);
        holder.costtext.setText("Fiyat: "+ bucketArrayList.get(position).cost);
        holder.stacktext.setText("Adet: "+ bucketArrayList.get(position).stack);
        Glide.with(context).load(bucketArrayList.get(position).imageUrl).into(holder.imageView);

        holder.btn_islem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final Product[] product = new Product[1];
                    FirebaseDatabase dbBucket,dbProduct;
                    dbBucket = FirebaseDatabase.getInstance();
                    dbProduct = FirebaseDatabase.getInstance();
                    DatabaseReference  dbProductRef = dbProduct.getReference("Products/" + bucketArrayList.get(position).sellerId+"/"+bucketArrayList.get(position).name);
                    dbProductRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Product task=dataSnapshot.getValue(Product.class);
                            product[0] =task;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getRootView().getContext());
                    final EditText edittext = new EditText(view.getRootView().getContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    edittext.setLayoutParams(lp);
                    alertDialog.setView(edittext);
                    alertDialog.setMessage("Lütfen Adet Giriniz");
                    alertDialog.setTitle("Satın al");


                    alertDialog.setPositiveButton("Seç", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            try{
                                int YourEditTextValue = Integer.parseInt(edittext.getText().toString());
                                if (YourEditTextValue > 0 && YourEditTextValue < Integer.parseInt(bucketArrayList.get(position).stack)) {

                                    LayoutInflater li = LayoutInflater.from(view.getRootView().getContext());
                                    View alertDialogView = li.inflate(R.layout.card_dialog, null);

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            view.getRootView().getContext());


                                    alertDialogBuilder.setView(alertDialogView);

                                    TextView tv_totalPrice=alertDialogView.findViewById(R.id.tv_totalPrice);
                                    tv_totalPrice.setText("Toplam Ödenecek Tutar: "+Integer.parseInt
                                            (bucketArrayList.get(position).cost) * YourEditTextValue+"TL");


                                    FirebaseDatabase dbBucket,dbProduct,dbProcess;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
                                    dbBucket = FirebaseDatabase.getInstance();
                                    dbProduct = FirebaseDatabase.getInstance();
                                    dbProcess = FirebaseDatabase.getInstance();


                                    alertDialogBuilder
                                            .setCancelable(false) // dış ekrana dokunludğunda kapanmaması için
                                            .setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    DatabaseReference  dbBucketRef = dbBucket.getReference("Bucket/" + bucketArrayList.get(position).id+"/"+bucketArrayList.get(position).name);
                                                    DatabaseReference  dbProductRef = dbProduct.getReference("Products/" + bucketArrayList.get(position).sellerId+"/"+bucketArrayList.get(position).name);
                                                    DatabaseReference  dbProcessRef = dbProcess.getReference("Process/" + bucketArrayList.get(position).sellerId+"/"+bucketArrayList.get(position).name).push();


                                                    if(Integer.parseInt(product[0].stack)>Integer.parseInt(bucketArrayList.get(position).stack)){
                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("stack", String.valueOf(Integer.parseInt(bucketArrayList.get(position).stack)- Integer.parseInt(String.valueOf(YourEditTextValue))));
                                                        HashMap<String, Object> hashMapProduct = new HashMap<>();
                                                        hashMapProduct.put("stack", String.valueOf(Integer.valueOf(product[0].stack) - Integer.parseInt(String.valueOf(YourEditTextValue))));
                                                        dbBucketRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(view.getRootView().getContext(), "Ürün Satın Alındı!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        dbProductRef.updateChildren(hashMapProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                }
                                                            }
                                                        });

                                                        HashMap<String, Object> hashMapProcess = new HashMap<>();
                                                        hashMapProcess.put("teacherId", bucketArrayList.get(position).id);
                                                        hashMapProcess.put("sellerId", bucketArrayList.get(position).sellerId);
                                                        hashMapProcess.put("name", bucketArrayList.get(position).name);
                                                        hashMapProcess.put("size", bucketArrayList.get(position).size);
                                                        hashMapProcess.put("imageUrl", bucketArrayList.get(position).imageUrl);
                                                        hashMapProcess.put("cost", bucketArrayList.get(position).cost);
                                                        hashMapProcess.put("teacherSchool", bucketArrayList.get(position).teacherSchool);
                                                        hashMapProcess.put("piece", String.valueOf(YourEditTextValue));
                                                        dbProcessRef.setValue(hashMapProcess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                }
                                                            }
                                                        });


                                                    }else if(Integer.valueOf(product[0].stack)==Integer.parseInt(bucketArrayList.get(position).stack)){
                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("stack", String.valueOf(Integer.parseInt(bucketArrayList.get(position).stack)- Integer.parseInt(String.valueOf(YourEditTextValue))));
                                                        HashMap<String, Object> hashMapProduct = new HashMap<>();
                                                        hashMapProduct.put("stack", "0");
                                                        dbBucketRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(view.getRootView().getContext(), "Ürün Satın Alındı!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        dbProductRef.updateChildren(hashMapProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                }
                                                            }
                                                        });
                                                        HashMap<String, Object> hashMapProcess = new HashMap<>();
                                                        hashMapProcess.put("teacherId", bucketArrayList.get(position).id);
                                                        hashMapProcess.put("sellerId", bucketArrayList.get(position).sellerId);
                                                        hashMapProcess.put("name", bucketArrayList.get(position).name);
                                                        hashMapProcess.put("size", bucketArrayList.get(position).size);
                                                        hashMapProcess.put("imageUrl", bucketArrayList.get(position).imageUrl);
                                                        hashMapProcess.put("cost", bucketArrayList.get(position).cost);
                                                        hashMapProcess.put("teacherSchool", bucketArrayList.get(position).teacherSchool);
                                                        hashMapProcess.put("piece", String.valueOf(YourEditTextValue));
                                                        dbProcessRef.setValue(hashMapProcess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                }
                                                            }
                                                        });

                                                    }else{
                                                        Toast.makeText(view.getRootView().getContext(), "Satıcının Stoğunda Yeterli Ürün Yok!", Toast.LENGTH_SHORT).show();
                                                    }


                                                }
                                            })
                                            .setNegativeButton("İptal",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });


                                    AlertDialog alertDialog = alertDialogBuilder.create();


                                    alertDialog.show();

                                }else if(YourEditTextValue == Integer.parseInt(bucketArrayList.get(position).stack)){
                                    LayoutInflater li = LayoutInflater.from(view.getRootView().getContext());
                                    View alertDialogView = li.inflate(R.layout.card_dialog, null);


                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            view.getRootView().getContext());


                                    // içeriğini doldurmak için....
                                    alertDialogBuilder.setView(alertDialogView);
                                    TextView tv_totalPrice=alertDialogView.findViewById(R.id.tv_totalPrice);
                                    tv_totalPrice.setText("Toplam Ödenecek Tutar: "+Integer.parseInt(bucketArrayList.get(position).cost) * YourEditTextValue);


                                    FirebaseDatabase dbBucket,dbProduct,dbProcess;// Database e ulaşmamızı sağlayan sınıftan nesne oluşturduk
                                    dbBucket = FirebaseDatabase.getInstance();
                                    dbProduct = FirebaseDatabase.getInstance();
                                    dbProcess=FirebaseDatabase.getInstance();

                                    alertDialogBuilder
                                            .setCancelable(false) // dış ekrana dokunulduğunda kapanmaması için
                                            .setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    DatabaseReference  dbProcessRef = dbProcess.getReference("Process/" + bucketArrayList.get(position).sellerId+"/"+bucketArrayList.get(position).name).push();
                                                    DatabaseReference  dbProductRef = dbProduct.getReference("Products/" + bucketArrayList.get(position).sellerId+"/"+bucketArrayList.get(position).name);
                                                    try{
                                                        if(Integer.valueOf(product[0].stack)>Integer.parseInt(bucketArrayList.get(position).stack)){
                                                            dbBucket.getReference("Bucket/" + bucketArrayList.get(position).id+"/"+bucketArrayList.get(position).name).removeValue();
                                                            HashMap<String, Object> hashMapProduct = new HashMap<>();
                                                            hashMapProduct.put("stack", String.valueOf(Integer.valueOf(product[0].stack) - YourEditTextValue));
                                                            dbProductRef.updateChildren(hashMapProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(context, "Satın Alım Tamamlandı", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            HashMap<String, Object> hashMapProcess = new HashMap<>();
                                                            hashMapProcess.put("teacherId", bucketArrayList.get(position).id);
                                                            hashMapProcess.put("sellerId", bucketArrayList.get(position).sellerId);
                                                            hashMapProcess.put("name", bucketArrayList.get(position).name);
                                                            hashMapProcess.put("size", bucketArrayList.get(position).size);
                                                            hashMapProcess.put("imageUrl", bucketArrayList.get(position).imageUrl);
                                                            hashMapProcess.put("cost", bucketArrayList.get(position).cost);
                                                            hashMapProcess.put("teacherSchool", bucketArrayList.get(position).teacherSchool);
                                                            hashMapProcess.put("piece", String.valueOf(YourEditTextValue));
                                                            dbProcessRef.setValue(hashMapProcess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                    }
                                                                }
                                                            });

                                                        }else if(Integer.valueOf(product[0].stack)==Integer.parseInt(bucketArrayList.get(position).stack)){
                                                            dbBucket.getReference("Bucket/" + bucketArrayList.get(position).id+"/"+bucketArrayList.get(position).name).removeValue();
                                                            HashMap<String, Object> hashMapProduct = new HashMap<>();
                                                            hashMapProduct.put("stack", "0");
                                                            dbProductRef.updateChildren(hashMapProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(context, "Satın Alım Tamamlandı", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            HashMap<String, Object> hashMapProcess = new HashMap<>();
                                                            hashMapProcess.put("teacherId", bucketArrayList.get(position).id);
                                                            hashMapProcess.put("sellerId", bucketArrayList.get(position).sellerId);
                                                            hashMapProcess.put("name", bucketArrayList.get(position).name);
                                                            hashMapProcess.put("size", bucketArrayList.get(position).size);
                                                            hashMapProcess.put("imageUrl", bucketArrayList.get(position).imageUrl);
                                                            hashMapProcess.put("cost", bucketArrayList.get(position).cost);
                                                            hashMapProcess.put("teacherSchool", bucketArrayList.get(position).teacherSchool);
                                                            hashMapProcess.put("piece", String.valueOf(YourEditTextValue));
                                                            dbProcessRef.setValue(hashMapProcess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                    }
                                                                }
                                                            });
                                                        }else{
                                                            Toast.makeText(context, "Satıcının stoğunda yeterli ürün bulunmamaktadır", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }catch (Exception e){
                                                        Toast.makeText(context, "Satıcının stoğunda ürün bulunmamakta", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            })
                                            .setNegativeButton("İptal",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });


                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }else{
                                    Toast.makeText(context, "Lütfen geçerli bir adet giriniz", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                Toast.makeText(context, "Lütfen adet giriniz", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });

                    alertDialog.show();
                }catch (Exception e){
                    Toast.makeText(context, "Satıcı ürünü kaldırmış", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return bucketArrayList.size();
    }//loop değerleri kadar döndürür

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nametext,sizetext,costtext,stacktext;
        Button btn_islem;



        ViewHolder(View itemView) {
            super(itemView);
            nametext = itemView.findViewById(R.id.tv_name);
            sizetext = itemView.findViewById(R.id.tv_size);
            costtext = itemView.findViewById(R.id.tv_cost);
            stacktext = itemView.findViewById(R.id.tv_stack);
            imageView = itemView.findViewById(R.id.iv_wrex);
            btn_islem = itemView.findViewById(R.id.btn_islem);


        }

    }

    public Bucket getItemName(int position) {
        return bucketArrayList.get(position);
    }



}