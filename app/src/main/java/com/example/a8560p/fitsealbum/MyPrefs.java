package com.example.a8560p.fitsealbum;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.SharedPreferences;

public class MyPrefs {
    SharedPreferences myPrefs;

    public MyPrefs(Context context){
        myPrefs = context.getSharedPreferences("data",Context.MODE_PRIVATE);
    }

    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putBoolean("NightMode",state);
        editor.commit();
    }

    public boolean loadNightModeState(){
        Boolean state = myPrefs.getBoolean("NightMode", false);

        return state;
    }

    public void setPassword(String password){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("Password",password);
        editor.commit();
    }

    public String getPassword(){
        String password = myPrefs.getString("Password","");

        return password;
    }

    public void setPassMode(Integer passMode){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putInt("PassMode",passMode);
        editor.commit();
    }

    public Integer getPassMode(){
        //passMode = 0 là chế độ login, passMode = 1 là chế độ sửa đổi password, passMode = 2 là chế độ xoá password
        Integer passMode = myPrefs.getInt("PassMode",0);

        return passMode;
    }
}