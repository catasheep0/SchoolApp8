package com.example.schoolapp.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.schoolapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    ArrayList<JSONObject> lessonArray;
    LessonAdapter adapter;
    class LessonAdapter extends ArrayAdapter<JSONObject> {

        public LessonAdapter(@NonNull Context context, ArrayList<JSONObject> tests) {
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

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        JSONArray tests = null;
        try{tests = new JSONArray(getArguments().getString("lessons"));}catch (Exception e){} catch (Error e){}
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        if(tests != null) {
            try {
                lessonArray = new ArrayList<JSONObject>();
                for (int i = 0; i < tests.length(); i++)
                    lessonArray.add(tests.getJSONObject(i));
            } catch (Exception e){}
            adapter = new LessonAdapter(getContext(), lessonArray);
            ListView lv = ((ListView)root.findViewById(R.id.score_list_view));
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast t = Toast.makeText(getContext(), "TheStalwart", Toast.LENGTH_LONG);
                    t.show();
                }
            });

            Log.d("tests", tests.toString());
        }
        return root;
    }
}