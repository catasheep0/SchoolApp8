package com.example.schoolapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    public ArrayList<JSONObject> objectList;
    public ScoreAdapter adapter;

    class ScoreAdapter extends ArrayAdapter<JSONObject> {
        int min, max;
        public ScoreAdapter(@NonNull Context context, ArrayList<JSONObject> tests, int ma) {
            super(context, 0, tests);
            max = ma;
        }
        @Override
        public View getView(int pos, View convert, ViewGroup parent) {
            JSONObject obj = getItem(pos);
            if (convert == null) {
                convert = LayoutInflater.from(getContext()).inflate(R.layout.score_item, parent, false);
            }
            try {
                ((TextView)convert.findViewById(R.id.textView)).setText(obj.getString("student_login"));
                ProgressBar pr = (ProgressBar)convert.findViewById(R.id.progressBar);
                double points = Double.parseDouble(obj.getString("points"));

                int score = (int)((points/(double)max) * 100);
                pr.setProgress(score);

            } catch (Exception e){e.printStackTrace();}
            return convert;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        objectList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("name"));
        FloatingActionButton fab = findViewById(R.id.fab);
        ListView lv = findViewById(R.id.score_list_view);


        Requests.request(this, "/api/test/" + Integer.toString(intent.getIntExtra("id", 0)),
                (res) -> {
                    Log.d("response", res);
                    try {
                        JSONObject object = new JSONObject(res);
                        JSONArray array = object.getJSONArray("score");
                        for(int i = 0; i < array.length(); i++)
                            objectList.add((JSONObject) array.get(i));
                        adapter = new ScoreAdapter(this, objectList, object.getJSONObject("test").getInt("maximum"));
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    JSONObject obj = (JSONObject)parent.getItemAtPosition(position);
                                    Intent intent = new Intent(parent.getContext(), ScoreActivity.class);
                                    intent.putExtra("id", obj.getInt("score_id"));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                (res) -> Log.e("responseError", res.toString()));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
