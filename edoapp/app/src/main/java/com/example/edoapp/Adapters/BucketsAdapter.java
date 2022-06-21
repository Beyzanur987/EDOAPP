package com.example.edoapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.edoapp.Models.Bucket;
import com.example.edoapp.R;

import java.util.ArrayList;

public class BucketsAdapter extends RecyclerView.Adapter<BucketsAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Bucket> bucketArrayList;

    public BucketsAdapter(Context context, ArrayList<Bucket> bucketArrayList) {
        this.context = context;
        this.bucketArrayList = bucketArrayList;
    }

    @NonNull
    @Override
    public BucketsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.listview_bucket_row,parent,false);
        return new BucketsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BucketsAdapter.ViewHolder holder, int position) {


        holder.nametext.setText(bucketArrayList.get(position).name);
        holder.sizetext.setText("Boyut: "+ bucketArrayList.get(position).size);
        holder.costtext.setText("Fiyat: "+ bucketArrayList.get(position).cost);
        holder.stacktext.setText("Adet: "+ bucketArrayList.get(position).stack);
        Glide.with(context).load(bucketArrayList.get(position).imageUrl).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return bucketArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nametext,sizetext,costtext,stacktext;



        ViewHolder(View itemView) {
            super(itemView);
            nametext = itemView.findViewById(R.id.tv_name);
            sizetext = itemView.findViewById(R.id.tv_size);
            costtext = itemView.findViewById(R.id.tv_cost);
            stacktext = itemView.findViewById(R.id.tv_stack);
            imageView = itemView.findViewById(R.id.iv_wrex);


        }

    }

    public Bucket getItemName(int position) {
        return bucketArrayList.get(position);
    }


}
