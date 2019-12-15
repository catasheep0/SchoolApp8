package com.example.schoolapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONObject;

public class ScoreActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    TextView score, description, student, score_column;
    SeekBar seekBar;
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
            score = findViewById(R.id.score_value);
            seekBar = findViewById(R.id.score_slider);
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
            seekBar.setMax(0);
            seekBar.setMax(seekMax);
            seekBar.setProgress((int) (points * 10));
            seekBar.refreshDrawableState();
            description.setText(json.getString("commentary"));
            student.setText(json.getString("student_login"));
            score_column.setText(String.format("%.1f out of %d", points, max));

            seekBar.setOnSeekBarChangeListener(this);
            score.setText(String.format("%.2f", points));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editScore(View view) {
        Intent intent = new Intent(this, EditScoreActivity.class);
        intent.putExtra("object", jsonStr);
        intent.putExtra("max", max);
        startActivity(intent);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d("seek", Integer.toString(progress));
        score.setText(Double.toString(((double) progress)/ 10));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
