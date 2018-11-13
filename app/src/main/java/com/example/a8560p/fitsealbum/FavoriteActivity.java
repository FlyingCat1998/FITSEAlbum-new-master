package com.example.a8560p.fitsealbum;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoriteActivity extends Fragment{
    View favorite;
    public static ArrayList<String> favoriteImages = new ArrayList<>();

    public static FavoriteActivity newInstance() {
        FavoriteActivity fragment = new FavoriteActivity();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        favorite =(View) inflater.inflate(R.layout.activity_favorite,container, false);

        if (null != favoriteImages && !favoriteImages.isEmpty()) {
            GridView favoriteGallery = (GridView) favorite.findViewById(R.id.favoriteGalleryGridView);

            favoriteGallery.setAdapter(new FavoriteImageAdapter(FavoriteActivity.super.getActivity()));

            int column = favoriteGallery.getNumColumns();
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
            if (screenWidth > screenHeight) {
                column = 6;
                favoriteGallery.setNumColumns(column);
            }
        }

        return favorite;
    }
}

