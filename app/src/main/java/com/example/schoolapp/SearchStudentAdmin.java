package com.example.schoolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchStudentAdmin extends AppCompatActivity {

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
        @Override
        public Filter getFilter() {
            return new Filter () {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    //Log.d("HERE", "cons " + constraint);
                    FilterResults results = new FilterResults();
                    ArrayList<JSONObject>  arr = new ArrayList<>();
                    for(JSONObject obj : array) {
                        try {
                            String names = obj.getString("first_name") + " " + obj.getString("last_name");
                            //Log.d("HERE", names);
                            if(names.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                //Log.d("HERE", "cons " + constraint);
                                arr.add(obj);
                            }
                        } catch (Exception e) {e.printStackTrace();}
                    }
                    results.count = arr.size();
                    results.values = arr;
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if(true) {
                        adapter.clear();
                        showArray = (ArrayList<JSONObject>)results.values;
                        adapter.addAll(showArray);
                        adapter.notifyDataSetChanged();
                    }
                }
            };
        }
    }

    StudentAdapter adapter;
    ArrayList<JSONObject> array;
    ArrayList<JSONObject> showArray;
    ListView lv;
    EditText find_text;

    private void setUpList() {
        adapter = new StudentAdapter(this, showArray);
        lv.setAdapter(adapter);


    }

    String str;

    public void findByStr(String string) {

        Filter filter = adapter.getFilter();
        filter.filter(string);
    }

    public void find(View view) {
        str = find_text.getText().toString();
        Filter filter = adapter.getFilter();
        filter.filter(find_text.getText());
    }

    void deleteScore() {
        try{
            String login = selected.getString("login");
            Log.d("request", "/api/student/" + login);
            Requests.request(this, "/api/student/" + login, Requests.DELETE,
                    (res) -> {
                        if(res.equals("ok")) {
                            showArray.remove(selected);
                            array.remove(selected);
                            findByStr(str == null ? "" : str);
                        }
                    },
                    (res) -> {});
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        //find(null);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.student_menu_delete:
                deleteScore();
                return true;
            case R.id.student_menu_edit:
                Intent intent = new Intent(this, StudentActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private JSONObject selected;

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.admin_search_list) {
            ListView listView = (ListView)v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.student_menu, menu);

            selected = (JSONObject)lv.getItemAtPosition(acmi.position);
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        str = null;
        getSupportActionBar().setTitle("Student Search");
        setContentView(R.layout.activity_search_student_admin);
        lv = findViewById(R.id.admin_search_list);
        registerForContextMenu(lv);
        find_text = findViewById(R.id.admin_search_text);
        find_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                find(v);
                return true;
            }
        });

        lv.setOnItemClickListener((parent, v, pos, id) -> {
            try {
                JSONObject obj = (JSONObject) parent.getItemAtPosition(pos);
                Intent intent = new Intent(this, StudentActivity.class);
                intent.putExtra("student_login", obj.getString("login"));
                startActivity(intent);
            } catch (JSONException e) {e.printStackTrace();}
        });

        Requests.request(this,"/api/student", Requests.GET,
            (res) -> {
            try {
                array = Util.toArrayList(new JSONArray(res));
                showArray = (ArrayList<JSONObject>) array.clone();
                setUpList();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            },
            (res) -> {});


    }
}
