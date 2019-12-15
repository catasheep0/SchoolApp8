package com.example.schoolapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ScoreActivity extends AppCompatActivity  {

    TextView description, student, score_column;
    int min, max, seekMax;
    String jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Log.d("here", "HERE");
        getSupportActionBar().setTitle("Score");
        try {
            double points, dmax, dmin;
            Intent intent = getIntent();
            JSONObject json = new JSONObject(intent.getStringExtra("object"));
            jsonStr = intent.getStringExtra("object");
            description = findViewById(R.id.score_description);
            student = findViewById(R.id.score_student_name);
            score_column = findViewById(R.id.score_column);
            points = Double.parseDouble(json.getString("points"));

            min = intent.getIntExtra("min", 0);
            max = intent.getIntExtra("max", 0);
            dmax = (double)max;
            dmin = (double)min;
            seekMax = (int)(dmax * 10);
            double diff = dmax - dmin;
            double difp = points - dmin;
            description.setText(json.getString("commentary"));
            student.setText(json.getString("student_login"));
            score_column.setText(String.format("%.1f out of %d", points, max));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editScore(View view) {
        Intent intent = new Intent(this, EditScoreActivity.class);
        intent.putExtra("object", jsonStr);
        intent.putExtra("max", max);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent();
        i.putExtra("object", jsonStr);
        i.putExtra("index", getIntent().getIntExtra("index", 0));
        setResult(RESULT_OK, i);

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    jsonStr = data.getStringExtra("object");
                    JSONObject json = new JSONObject(jsonStr);
                    description.setText(json.getString("commentary"));
                    score_column.setText(json.getString("points"));
                }catch (JSONException e) {e.printStackTrace();}
            }
        }
    }

}


