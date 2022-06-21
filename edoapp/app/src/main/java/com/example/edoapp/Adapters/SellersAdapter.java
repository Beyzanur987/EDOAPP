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
import com.example.edoapp.Models.Product;
import com.example.edoapp.Models.Seller;
import com.example.edoapp.R;

import java.util.ArrayList;

public class SellersAdapter extends RecyclerView.Adapter<SellersAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Seller> sellerArrayList;

    public SellersAdapter(Context context, ArrayList<Seller> sellerArrayList) {
        this.context = context;
        this.sellerArrayList = sellerArrayList;
    }

    @NonNull
    @Override
    public SellersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.listview_seller_row,parent,false);
        return new SellersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellersAdapter.ViewHolder holder, int position) {

        holder.firmtext.setText(""+ sellerArrayList.get(position).firm);
        holder.addresstext.setText(""+ sellerArrayList.get(position).address);
        Glide.with(context).load(sellerArrayList.get(position).logo).into(holder.iv_adapterLogo);



    }

    @Override
    public int getItemCount() {
        return sellerArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView firmtext,addresstext;
        ImageView iv_adapterLogo;



        ViewHolder(View itemView) {
            super(itemView);
            firmtext = itemView.findViewById(R.id.tv_firm);
            addresstext = itemView.findViewById(R.id.tv_address);
            iv_adapterLogo = itemView.findViewById(R.id.iv_adapterLogo);



        }

    }

    public Seller getItemName(int position) {
        return sellerArrayList.get(position);
    }

    public void filterList(ArrayList<Seller> filteredNames) {
        this.sellerArrayList = filteredNames;
        notifyDataSetChanged();
    }

}

