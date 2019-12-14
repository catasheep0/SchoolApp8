package com.example.schoolapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class TeacherDetail extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;

    void setUpAfterRequest(String str) {
        try {


            JSONObject json = new JSONObject(str);
            JSONObject teacher = (JSONObject)json.get("teacher");
            UserData.firstName = teacher.get("first_name").toString();
            UserData.lastName = teacher.get("last_name").toString();

            mAdapter = new TeacherAdapter(json.getJSONArray("courses"), (obj) -> {
                Intent i = new Intent(this, CourseActivity.class);

                try{i.putExtra("id", obj.getInt("id"));}catch (Exception e){}
                startActivity(i);
            });
            recyclerView.setAdapter(mAdapter);
            Log.d("REQUEST", json.toString());
            CollapsingToolbarLayout title = (CollapsingToolbarLayout)findViewById(R.id.teacher_toolbar_layout);

            title.setTitle(UserData.firstName + " " + UserData.lastName);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);

        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView)findViewById(R.id.teacher_recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setLayoutManager(layoutManager);




        Requests.request(this, "/api/teacher/" + UserData.userName,
                (str) -> setUpAfterRequest(str),
                (err) -> err.printStackTrace());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
