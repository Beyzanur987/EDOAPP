package com.example.edoapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.edoapp.Models.Product;
import com.example.edoapp.R;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Product> productArrayList;

    public ProductsAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.listview_text_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nametext.setText(productArrayList.get(position).name);
        holder.sizetext.setText("Boyut:"+" "+productArrayList.get(position).size);
        holder.costtext.setText("Fiyat:"+" "+productArrayList.get(position).cost);
        holder.stacktext.setText("Adet:"+" "+productArrayList.get(position).stack);
        Glide.with(context).load(productArrayList.get(position).imageUrl).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }//loop değerleri kadar döndürür

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

    public Product getItemName(int position) {
        return productArrayList.get(position);
    }

    public void filterList(ArrayList<Product> filteredNames) {
        this.productArrayList = filteredNames;
        notifyDataSetChanged();
    }

}
