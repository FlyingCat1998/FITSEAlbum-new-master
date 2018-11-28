package com.example.a8560p.fitsealbum;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class PicturesActivity extends Fragment{
    View pictures;
    public static ArrayList<String> images;

    static int hideToolbar = 0;

    public static PicturesActivity newInstance() {
        PicturesActivity fragment = new PicturesActivity();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        pictures = (View) inflater.inflate(R.layout.activity_pictures, container,false);

        GridView gallery = (GridView) pictures.findViewById(R.id.galleryGridView);

        gallery.setAdapter(new ImageAdapter(PicturesActivity.super.getActivity()));
        int column = gallery.getNumColumns();
        int screenWidth =  Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
        if (screenWidth > screenHeight)
        {
            column = 6;
            gallery.setNumColumns(column);
        }
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (null != images && !images.isEmpty()) {
                    Intent i = new Intent(PicturesActivity.super.getActivity(), FullImageActivity.class);
                    i.putExtra("id", position);
                    i.putExtra("path", images.get(position));
                    i.putExtra("allPath", images);
                    startActivity(i);
                }
            }
        });
        String msg = "Screen res: " + String.valueOf(screenHeight) + " x " + String.valueOf(screenWidth) + "\n";
        msg += "Number of columns: " + String.valueOf(column);
        Toast.makeText(this.getActivity(),msg, Toast.LENGTH_SHORT).show();
        return pictures;
    }
}