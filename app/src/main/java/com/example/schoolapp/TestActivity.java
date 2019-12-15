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
                        JSONObject testObject = object.getJSONObject("test");
                        JSONArray array = object.getJSONArray("score");
                        for(int i = 0; i < array.length(); i++)
                            objectList.add((JSONObject) array.get(i));
                        TextView tv = findViewById(R.id.test_description);
                        tv.setText(testObject.getString("description"));
                        adapter = new ScoreAdapter(this, objectList, object.getJSONObject("test").getInt("maximum"));
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    JSONObject obj = (JSONObject)parent.getItemAtPosition(position);
                                    Intent intent = new Intent(parent.getContext(), ScoreActivity.class);
                                    intent.putExtra("id", obj.getInt("score_id"));
                                    intent.putExtra("object", obj.toString());
                                    intent.putExtra("min", testObject.getInt("minimum"));
                                    intent.putExtra("max", testObject.getInt("maximum"));
                                    intent.putExtra("index", position);
                                    startActivityForResult(intent, 1);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    String jsonStr = data.getStringExtra("object");
                    JSONObject json = new JSONObject(jsonStr);
                    Log.d("return", jsonStr);
                    objectList.set(data.getIntExtra("index", 0), json);
                    synchronized (adapter) {
                        adapter.notifyDataSetChanged();
                    }
                    //description.setText(json.getString("commentary"));
                    //score_column.setText(json.getString("points"));
                }catch (JSONException e) {e.printStackTrace();}
            }
        }
    }
}
