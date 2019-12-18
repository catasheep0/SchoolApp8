package com.example.schoolapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
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

    void deleteScore(int scoreID) {
        Requests.request(this, String.format("/api/score/%d", scoreID), Requests.DELETE,
            (res) -> {
                objectList.remove(selected_score_index);
                adapter.notifyDataSetChanged();
            },
            (res) -> {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            });

    }

    String selected_student;
    int selected_score, selected_score_index;


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_student_menu:
                deleteScore(selected_score);
                return true;
            case R.id.show_student_menu:
                Intent intent = new Intent(this, StudentActivity.class);
                intent.putExtra("student_login", selected_student);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.score_list_view) {
            ListView listView = (ListView)v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.score_menu, menu);

            JSONObject obj = (JSONObject)lv.getItemAtPosition(acmi.position);
            try {
                selected_student = obj.getString("student_login");
                selected_score = obj.getInt("score_id");
                selected_score_index = acmi.position;
            } catch (Exception e) {e.printStackTrace();}
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public void createScore(View v) {
        //Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ShowStudents.class);
        intent.putExtra("id", getIntent().getIntExtra("id", 0));
        intent.putExtra("course_id", getIntent().getIntExtra("course_id", 0));
        intent.putExtra("max", test_max);
        intent.putExtra("min", test_min);
        startActivityForResult(intent, 1);

    }

    ListView lv;
    int test_min = 0, test_max = 0;

    private void refresh () {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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
        FloatingActionButton fab = findViewById(R.id.add_student_button);
        lv = findViewById(R.id.score_list_view);
        registerForContextMenu(lv);

        Requests.request(this, "/api/test/" + Integer.toString(intent.getIntExtra("id", 0)), Requests.GET,
                (res) -> {
                    Log.d("response", res);
                    try {
                        JSONObject object = new JSONObject(res);
                        JSONObject testObject = object.getJSONObject("test");
                        JSONArray array = object.getJSONArray("score");
                        test_max = testObject.getInt("maximum");
                        test_min = testObject.getInt("minimum");

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
                refresh (); return;/*
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
                } catch (JSONException e) {e.printStackTrace();}*/
            }
        }
    }
}
