package com.example.schoolapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ListAdapter;

import com.example.schoolapp.CourseActivity;
import com.example.schoolapp.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
            ((ListView)root.findViewById(R.id.test_list_view)).setAdapter(adapter);

            Log.d("tests", tests.toString());
        }
        return root;
    }
}