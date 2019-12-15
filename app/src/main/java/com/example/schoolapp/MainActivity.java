package com.example.schoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txt = (TextView)findViewById(R.id.testText);
        EditText login = (EditText)findViewById(R.id.logInName);
        Log.d("test", "shore");
        try {
            String str = SettingsFile.getSettings(this);
            if(str != null) {
                JSONObject obj = new JSONObject(str);
                launchTeacherDetail(obj.getString("passwd"), obj.getString("login"));
            }
        } catch (Exception e) {

        }
        //Log.d("file", .toString());

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void launchTeacherDetail(String passwd, String login) {
        Intent intent = new Intent(this, TeacherDetail.class);
        intent.putExtra("passwd", passwd);
        intent.putExtra("login", login);
        UserData.userName = login;
        startActivity(intent);
    }



    public void logIn(View view) throws Exception {
        EditText name = (EditText)findViewById(R.id.logInName);
        EditText password = (EditText)findViewById(R.id.logInPassword);
        JSONObject obj = new JSONObject();
        obj.put("login", name.getText());
        obj.put("password", password.getText());
        TextView txt = (TextView)findViewById(R.id.testText);
        Requests.jsonText(this, Requests.POST,"/api/teacherlog", obj,
                (str) -> {if(str == "bad") txt.setText("Wrong username or password");
                            else {
                                txt.setText(str);
                                UserData.userName = name.getText().toString();
                                try {
                                    launchTeacherDetail(obj.getString("password"), obj.getString("login"));
                                } catch (Exception e) {e.printStackTrace();}
                            }
                            hideKeyboard(this);},
                (err) -> {txt.setText("Error"); hideKeyboard(this);});
    }
}
