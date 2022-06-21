package com.example.edoapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edoapp.Models.Teacher;
import com.example.edoapp.R;

import java.util.ArrayList;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Teacher> teacherArrayList;

    public TeacherAdapter(Context context, ArrayList<Teacher> teacherArrayList) {
        this.context = context;
        this.teacherArrayList = teacherArrayList;
    }

    @NonNull
    @Override
    public TeacherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.listview_teacher_row,parent,false);
        return new TeacherAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherAdapter.ViewHolder holder, int position) {
        holder.firmtext.setText("ÖĞRETMEN : "+ teacherArrayList.get(position).username);
        holder.addresstext.setText("E-MAİL : "+ teacherArrayList.get(position).email);
        holder.schooltext.setText("OKUL : "+ teacherArrayList.get(position).school);
        holder.citytext.setText("ŞEHİR : "+ teacherArrayList.get(position).city);



    }

    @Override
    public int getItemCount() {
        return teacherArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView firmtext,addresstext,schooltext,citytext;


        ViewHolder(View itemView) {
            super(itemView);
            firmtext = itemView.findViewById(R.id.tv_firm);
            addresstext = itemView.findViewById(R.id.tv_address);
            schooltext = itemView.findViewById(R.id.tv_school);
            citytext = itemView.findViewById(R.id.tv_city);



        }

    }

    public Teacher getItemName(int position) {
        return teacherArrayList.get(position);
    }

    public void filterList(ArrayList<Teacher> filteredNames) {
        this.teacherArrayList = filteredNames;
        notifyDataSetChanged();
    }

}

