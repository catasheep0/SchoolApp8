package com.example.schoolapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherHolder> {

    interface onClick {
        void click(JSONObject obj);
    }

    public Object getItem(int pos) {
        try {
            return  mDataset.get(pos);
        } catch (Exception e) {e.printStackTrace(); return null;}

    }
    onClick listener;

    JSONArray mDataset;
    public TeacherAdapter(JSONArray myDataset, onClick onclick) {
        listener = onclick;
        mDataset = myDataset;
    }

    public static class TeacherHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;

        public TeacherHolder(View v) {
            super(v);
            textView = (TextView)v.findViewById(R.id.teacher_item_course_name);
        }

    }
    private ViewGroup parents;
    @Override
    public TeacherAdapter.TeacherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.teacheritem, parent, false);
        parents = parent;
        TeacherHolder vh = new TeacherHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherHolder holder, int position) {
        try {
            JSONObject obj = mDataset.getJSONObject(position);
            holder.textView.setText(obj.getString("name"));
            holder.itemView.setOnClickListener((view) -> {
                try {
                    listener.click(obj);

                }catch (Exception e){}
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.length();
    }

}
