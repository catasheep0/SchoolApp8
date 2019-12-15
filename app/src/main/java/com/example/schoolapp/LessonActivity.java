package com.example.schoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class LessonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        try {

            JSONObject object = new JSONObject(getIntent().getStringExtra("object"));
            getSupportActionBar().setTitle(object.getString("name"));
            ((TextView)findViewById(R.id.lesson_description)).setText(object.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
