package com.example.a8560p.fitsealbum;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.a8560p.fitsealbum.FavoriteActivity.favoriteImages;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolBar;
    private MyPrefs myPrefs;

    FragmentTransaction ft;
    PicturesActivity pictures;
    AlbumActivity album;
    FavoriteActivity favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myPrefs = new MyPrefs(this);

        if(myPrefs.loadNightModeState()){
            setTheme(R.style.NightNoActionBarTheme);
        }
        else{
            setTheme(R.style.DayNoActionBarTheme);
        }

        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPreferences.getString("savedFavoriteImages","");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        favoriteImages = gson.fromJson(json, type);

        setContentView(R.layout.activity_main);

        toolBar = (Toolbar) findViewById(R.id.nav_actionBar);
        setSupportActionBar(toolBar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ft = getFragmentManager().beginTransaction();
        pictures = PicturesActivity.newInstance();
        ft.replace(R.id.content_frame, pictures);
        ft.commit();

        toolBar.setTitle("Images");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; add items to the action bar
        getMenuInflater().inflate(R.menu.status_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // user clicked a menu-item from ActionBar
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_search) {
            // perform SEARCH operations...
            return true;
        }
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pictures) {
            toolBar.setTitle("Images");

            ft = getFragmentManager().beginTransaction();
            pictures = PicturesActivity.newInstance();
            ft.replace(R.id.content_frame, pictures);
            ft.commit();
        }
        else if (id == R.id.nav_album) {
            toolBar.setTitle("Album");

            ft = getFragmentManager().beginTransaction();
            album = AlbumActivity.newInstance();
            ft.replace(R.id.content_frame, album);
            ft.commit();
        }
        else if (id == R.id.nav_favorite) {
            toolBar.setTitle("Favorite");

            ft = getFragmentManager().beginTransaction();
            favorite = FavoriteActivity.newInstance();
            ft.replace(R.id.content_frame, favorite);
            ft.commit();
        }
        else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            finish();
        }
        else if (id == R.id.nav_feedback) {

        }
        else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
