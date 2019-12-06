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
        /*Requests.jsonText(this, Requests.POST,"/api/course/2",
            (str) -> {
                //JSONObject obj = Requests.handle(str);
                if(str)

            },
            (err) -> txt.setText(err.toString())
        );*/

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

    public void launchTeacherDetail() {
        Intent intent = new Intent(this, TeacherDetail.class);
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
                                launchTeacherDetail();
                            }
                            hideKeyboard(this);},
                (err) -> {txt.setText("Error"); hideKeyboard(this);});
    }
}
