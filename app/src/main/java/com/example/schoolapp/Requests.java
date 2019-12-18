package com.example.schoolapp;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Requests {

    public static JSONObject handle(String input) {
        try {
            return new JSONObject(input);
        } catch (JSONException e) {
            Log.e("ERROR", e.toString());
            return null;
        }
    }

    interface RequestResponse {
        void processResponse(String response);
    }

    interface RequestJSONResponse {
        void processResponse(JSONObject response);
    }

    interface RequestError {
        void processResponse(VolleyError error);
    }

    public void setContext(Context c) {
        context = c;
    }

    static Context context;


    public static final String host = "http://10.0.2.2:8000";

    public static void request(Context c, String path, int method, final RequestResponse resp, final RequestError err) {
        String url = host + path;

        RequestQueue queue = Volley.newRequestQueue(c);
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                resp.processResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                err.processResponse(error);
            }
        });
        queue.add(request);
    }

    public final static int GET = Request.Method.GET;
    public final static int POST = Request.Method.POST;
    public final static int PUT = Request.Method.PUT;
    public final static int DELETE = Request.Method.DELETE;

    public static void json(Context c, int method, String path, JSONObject body, final RequestJSONResponse resp, final RequestError err) {
        String url = host + path;

        RequestQueue queue = Volley.newRequestQueue(c);

        Response.Listener<JSONObject> liste = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                resp.processResponse(response);
            }

        };

        Response.ErrorListener errorlist = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                err.processResponse(error);

            }
        };

        JsonObjectRequest request = new JsonObjectRequest(method, url,  body, liste, errorlist);

        queue.add(request);
    }

    public static void jsonText(Context c, int method, String path, JSONObject body, final RequestResponse resp, final RequestError err) {
        String url = host + path;

        RequestQueue queue = Volley.newRequestQueue(c);

        Response.Listener<String> liste = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                resp.processResponse(response);
            }

        };

        Response.ErrorListener errorlist = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                err.processResponse(error);

            }
        };

        StringRequest request = new StringRequest(method, url, liste, errorlist) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() {
                try {
                return  body.toString().getBytes("UTF8");} catch (Exception e) {return new byte[0];}
            }

        };

        queue.add(request);
    }



    public static int login(String name, String passwd) {
        return 0;
    }
}
