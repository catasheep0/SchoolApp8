package com.example.schoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentActivity extends AppCompatActivity {

    private TextView name, mail, id;

    private void setUp(JSONObject json) throws JSONException {
        json = json.getJSONObject("student");
        name = findViewById(R.id.student_full_name);
        mail = findViewById(R.id.student_mail);
        id = findViewById(R.id.student_login);

        name.setText(json.getString("first_name") + " " + json.getString("last_name"));
        id.setText(json.getString("login"));
        mail.setText(json.getString("email"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        getSupportActionBar().setTitle("Student");
        Intent intent = getIntent();
        String student_login = intent.getStringExtra("student_login");
        Requests.request(this,"/api/student/" + student_login, Requests.GET,
            (res) -> {
                try {
                    setUp(new JSONObject(res));
                } catch (Exception e){e.printStackTrace();}
            },
            (res) -> {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            });
    }
}
