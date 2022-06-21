package com.example.edoapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edoapp.Models.Process;
import com.example.edoapp.R;

import java.util.ArrayList;

public class ProcessAdapter extends RecyclerView.Adapter<ProcessAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Process> processArrayList;

    public ProcessAdapter(Context context, ArrayList<Process> processArrayList) {
        this.context = context;
        this.processArrayList = processArrayList;
    }

    @NonNull
    @Override
    public ProcessAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Recylerviewin her bir rowu için hangi layoutu kullanacağımızı belirlediğimzi yer
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.listview_process_row,parent,false);
        return new ProcessAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessAdapter.ViewHolder holder, int position) {

        holder.tv_process_text.setText(processArrayList.get(position).name.toUpperCase() +" adlı ürününüzün "+processArrayList.get(position).teacherSchool+" okuluna "+processArrayList.get(position).cost+" Tl fiyatından "+processArrayList.get(position).piece+" tane satımı onaylanmıştır.");




    }

    @Override
    public int getItemCount() {
        return processArrayList.size();
    }//loop değerleri kadar döndürür

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_process_text;



        ViewHolder(View itemView) {
            super(itemView);
            tv_process_text = itemView.findViewById(R.id.tv_process_text);


        }

    }

    public Process getItemName(int position) {
        return processArrayList.get(position);
    }


}