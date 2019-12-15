package com.example.schoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class EditScoreActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    JSONObject json;

    SeekBar seek;
    EditText desc;
    TextView score;
    int max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_score);
        getSupportActionBar().setTitle("Edit Score");
        seek = findViewById(R.id.edit_seek);
        score = findViewById(R.id.edit_points);
        desc = findViewById(R.id.edit_desc);
        try {
            Intent intent = getIntent();
            json = new JSONObject(intent.getStringExtra("object"));
            max = intent.getIntExtra("max", 0);
            desc.setText(json.getString("commentary"));
            double points = Double.parseDouble(json.getString("points"));
            seek.setMax(max * 10);
            seek.setProgress((int)(points * 10));
            score.setText(String.format("%.1f", points));
            seek.setOnSeekBarChangeListener(this);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void saveClick(View view) throws JSONException {

        json.put("points", score.getText());
        json.put("commentary", desc.getText());
        Requests.json(this, Requests.PUT,"/api/score", json,
                (res) -> {
                    Intent i = new Intent();
                    i.putExtra("object", json.toString());
                    setResult(RESULT_OK, i);
                    finish();
                },
                (res) -> {
                    Toast.makeText(this, res.toString(), Toast.LENGTH_LONG).show();
                }
        );
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        score.setText(String.format("%.1f", ((double)progress) / 10));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
