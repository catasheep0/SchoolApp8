package com.example.schoolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowStudents extends AppCompatActivity {

    ArrayList<JSONObject> array;
    ListView lv;
    ArrayAdapter adapter;

    class StudentAdapter extends ArrayAdapter<JSONObject> {

        public StudentAdapter(@NonNull Context context, ArrayList<JSONObject> tests) {
            super(context, 0, tests);
        }
        @Override
        public View getView(int pos, View convert, ViewGroup parent) {
            JSONObject obj = getItem(pos);
            if (convert == null) {
                convert = LayoutInflater.from(getContext()).inflate(R.layout.student_item, parent, false);
            }
            try {
                ((TextView)convert.findViewById(R.id.student_id)).setText(obj.getString("login"));
                ((TextView)convert.findViewById(R.id.full_name))
                    .setText(obj.getString("first_name") + " " + obj.getString("last_name"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convert;
        }
    }

    private void setUp(String res) {
        Log.d("request", res);
        try {
            JSONObject json = new JSONObject(res);
            array = Util.toArrayList(json.getJSONArray("student"));
            adapter = new StudentAdapter(this, array);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener((parent, view, pos, _id) -> {
                try {
                    Intent intent = new Intent(this, CreateScore.class);
                    JSONObject obj = (JSONObject) parent.getItemAtPosition(pos);
                    intent.putExtra("login", obj.getString("login"));
                    intent.putExtra("test_id", getIntent().getIntExtra("id", -50));
                    intent.putExtra("max", getIntent().getIntExtra("max", -50));
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);
        getSupportActionBar().setTitle("Students without a score");
        Intent i = getIntent();
        lv = findViewById(R.id.student_list);

        int test = i.getIntExtra("id", -50);
        //Toast.makeText(this, String.format("test: %d, course: %d",
        //            i.getIntExtra("id", -50), i.getIntExtra("course_id", -50)),
        //Toast.LENGTH_SHORT).show();

        Requests.request(this, "/api/nonscored/" + Integer.toString(test), Requests.GET,
                (res) -> setUp(res),
                (res) -> {Log.e("request", "ERROR");});

    }
}
