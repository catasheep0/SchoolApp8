package com.example.schoolapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Util {

    public static ArrayList<JSONObject> toArrayList(JSONArray array) {
        ArrayList<JSONObject> ret = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            try {
                ret.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return ret;
    }
}
