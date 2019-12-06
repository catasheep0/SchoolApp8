package com.example.schoolapp;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class TeacherDetail extends AppCompatActivity {



    void setUpAfterRequest(String str) {
        try {
            JSONObject json = new JSONObject(str);
            JSONObject teacher = (JSONObject)json.get("teacher");

            Log.d("REQUEST", json.toString());
            CollapsingToolbarLayout title = (CollapsingToolbarLayout)findViewById(R.id.teacher_toolbar_layout);

            title.setTitle(teacher.get("last_name").toString() + " " + teacher.get("first_name").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
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
