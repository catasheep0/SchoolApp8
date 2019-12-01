package com.example.schoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

    public void logIn(View view) throws Exception {
        EditText name = (EditText)findViewById(R.id.logInName);
        EditText password = (EditText)findViewById(R.id.logInPassword);
        JSONObject obj = new JSONObject();
        obj.put("login", name.getText());
        obj.put("password", password.getText());
        TextView txt = (TextView)findViewById(R.id.testText);
        Requests.jsonText(this, Requests.POST,"/api/teacherlog", obj,
                (str) -> txt.setText(str),
                (err) -> txt.setText(err.toString()));
    }
}
