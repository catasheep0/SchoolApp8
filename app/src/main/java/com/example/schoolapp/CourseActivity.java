package com.example.schoolapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.schoolapp.ui.dashboard.DashboardFragment;
import com.example.schoolapp.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class CourseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static JSONArray tests, lessons;
    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Course");
        setContentView(R.layout.activity_course);
        Log.d("request", "after set content");
        nav = findViewById(R.id.nav_view);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    void refresh() {
        Integer id = getIntent().getIntExtra("id", 0);

        Requests.request(this, "/api/course/" + id.toString(), Requests.GET ,(res) -> {
            Log.d("request", res);
            try {
                JSONObject result = new JSONObject(res);
                tests = result.getJSONArray("tests");
                lessons = result.getJSONArray("lessons");
                Fragment f = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("tests", tests.toString());
                f.setArguments(bundle);
                loadFragment(f);
                nav.setOnNavigationItemSelectedListener(this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, (res) -> {
            Log.d("request", res.toString());
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:

                Bundle bundle = new Bundle();
                bundle.putString("tests", tests.toString());
                fragment = new HomeFragment();
                fragment.setArguments(bundle);

                break;
            case R.id.navigation_dashboard:
                bundle = new Bundle();
                bundle.putString("lessons", lessons.toString());
                fragment = new DashboardFragment();
                fragment.setArguments(bundle);
                break;
        }

        return loadFragment(fragment);
    }
}
