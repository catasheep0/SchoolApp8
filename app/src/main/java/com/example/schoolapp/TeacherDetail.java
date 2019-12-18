package com.example.schoolapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;

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

                try{i.putExtra("id", obj.getInt("course_id"));}catch (Exception e){}
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




        Requests.request(this, "/api/teacher/" + UserData.userName, Requests.GET,
                (str) -> setUpAfterRequest(str),
                (err) -> err.printStackTrace());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_student_button);
        fab.setOnClickListener((view) -> {
            Intent i = getIntent();
            Intent intent = new Intent(this, Settings.class);
            Log.d("file", i.getStringExtra("passwd"));
            intent.putExtra("passwd", i.getStringExtra("passwd"));
            intent.putExtra("login", i.getStringExtra("login"));
            startActivityForResult(intent, 1);
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

            }
        }
    }
}
