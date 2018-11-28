package com.example.a8560p.fitsealbum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity{
    MyPrefs myPrefs;
    Button btnAbout, btnSetPassword, btnDeletePassword;
    Switch btnNightMode;
    ActionBar actionBar;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myPrefs = new MyPrefs(this);
        password = myPrefs.getPassword();
        if(myPrefs.loadNightModeState() == true){
            setTheme(R.style.NightAppTheme);
        }
        else{
            setTheme(R.style.DayAppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);

        actionBar = getSupportActionBar();

        actionBar.setTitle("Settings");

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnSetPassword = (Button)findViewById(R.id.btn_setPassword);
        btnSetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPrefs.setPassMode(1);
                if(password.matches("")){
                    startActivity(new Intent(SettingsActivity.this,SetPasswordActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(SettingsActivity.this,PasswordActivity.class));
                }
            }
        });

        btnDeletePassword = (Button)findViewById(R.id.btn_deletePassword);
        btnDeletePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPrefs.setPassMode(2);
                if(password.matches("")){
                    Toast.makeText(SettingsActivity.this, "Password hasn't been set", Toast.LENGTH_SHORT).show();
                }
                else{
                    startActivity(new Intent(SettingsActivity.this,PasswordActivity.class));
                    finish();
                }
            }
        });

        btnNightMode = (Switch)findViewById(R.id.btn_nightMode);
        if(myPrefs.loadNightModeState() == true){
            btnNightMode.setChecked(true);
        }
        else{
            btnNightMode.setChecked(false);
        }
        btnNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myPrefs.setNightModeState(true);
                    restartApp();
                }
                else{
                    myPrefs.setNightModeState(false);
                    restartApp();
                }
            }
        });

        btnAbout = (Button)findViewById(R.id.btn_about);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // user clicked a menu-item from ActionBar
        int id = item.getItemId();

        if(id == android.R.id.home){
            startActivity(new Intent(SettingsActivity.this,MainActivity.class));
            finish();
            return true;
        }
        return false;
    }

    public void restartApp(){
        startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
        finish();
    }
}
