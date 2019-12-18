package com.example.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class CreateScore extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    int test_id, max;
    double score;
    String login;
    TextView score_text, login_text;
    EditText descr;
    SeekBar score_bar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_score);
        Intent i = getIntent();
        max = i.getIntExtra("max", -50);
        login = i.getStringExtra("login");
        test_id = i.getIntExtra("test_id", -50);

        score = 0;
        score_text = findViewById(R.id.edit_points);
        score_bar = findViewById(R.id.edit_seek);
        descr = findViewById(R.id.edit_desc);
        score_text.setText("0");
        score_bar.setMax(max * 10);
        score_bar.setOnSeekBarChangeListener(this);


        Log.d("here", "HERE");
        getSupportActionBar().setTitle("Create Score");
    }

    public void saveClick(View v) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("points", Double.toString(score));
            obj.put("login", login);
            obj.put("id", test_id);
            obj.put("commentary", descr.getText());
            Requests.json(this, Requests.POST, "/api/score", obj,
            (r) -> {
                Log.d("request1", r.toString());
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
            },
            (r) -> {
                Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT)
                        .show();
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        score_text.setText(String.format("%.2f", ((double)progress)/10));
        score = ((double)progress)/10;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
