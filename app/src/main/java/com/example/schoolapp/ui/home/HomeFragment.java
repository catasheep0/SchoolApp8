package com.example.schoolapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.schoolapp.R;
import com.example.schoolapp.TestActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    ArrayList<JSONObject> testArray;
    TestAdapter adapter;
    class TestAdapter extends ArrayAdapter<JSONObject> {

        public TestAdapter(@NonNull Context context, ArrayList<JSONObject> tests) {
            super(context, 0, tests);

        }
        @Override
        public View getView(int pos, View convert, ViewGroup parent) {
            JSONObject obj = getItem(pos);
            if (convert == null) {
                convert = LayoutInflater.from(getContext()).inflate(R.layout.test_item, parent, false);
            }
            try {
                ((TextView)convert.findViewById(R.id.test_item_name)).setText(obj.getString("name"));
            } catch (Exception e){}
            return convert;
        }
    }

    public void launchTestActivity(JSONObject obj) {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        JSONArray tests = null;
        try{tests = new JSONArray(getArguments().getString("tests"));}catch (Exception e){} catch (Error e){}
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        if(tests != null) {
            try {
                testArray = new ArrayList<JSONObject>();
                for (int i = 0; i < tests.length(); i++)
                    testArray.add(tests.getJSONObject(i));
            } catch (Exception e){}
            adapter = new TestAdapter(getContext(), testArray);
            ListView lv = ((ListView)root.findViewById(R.id.score_list_view));
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Intent intent = new Intent(getContext(), TestActivity.class);
                        JSONObject obj = (JSONObject) parent.getItemAtPosition(position);
                        intent.putExtra("name", obj.getString("name"));
                        intent.putExtra("id", obj.getInt("test_id"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        return root;
    }
}