package com.example.schoolapp;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SettingsFile {
    static String getSettings(Context context) {
        FileInputStream in = null;
        try {
            in = context.openFileInput("settings.json");
            StringBuilder builder = new StringBuilder();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while(( line = reader.readLine()) != null ) {
                builder.append( line );
                builder.append( '\n' );
            }
            if (builder.toString() == "")
                return null;
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch (Exception e){}

        }
    }
}
