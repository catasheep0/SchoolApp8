package com.example.schoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class Settings extends AppCompatActivity {

    Switch sw;

    synchronized void saveJson(JSONObject object, boolean empty) throws IOException {
        FileOutputStream out = null;

        try {
            out = openFileOutput("settings.json", Context.MODE_PRIVATE);
            if (!empty)
                out.write(object.toString().getBytes(Charset.forName("UTF-8")));
            else
                out.write("".getBytes(Charset.forName("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out != null)
                out.close();
        }

    }

    public void onSwitch(View view) throws JSONException, IOException {
        if (sw.isChecked()) {
            Intent i = getIntent();
            JSONObject obj = new JSONObject();
            obj.put("passwd", i.getStringExtra("passwd"));
            obj.put("login", i.getStringExtra("login"));
            Log.d("file", obj.toString());
            saveJson(obj, false);
        } else {
            saveJson(null, true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sw = findViewById(R.id.switch1);
        String s = SettingsFile.getSettings(this);
        if(s == null)
            sw.setChecked(false);
        else
            sw.setChecked(true);
    }
}
