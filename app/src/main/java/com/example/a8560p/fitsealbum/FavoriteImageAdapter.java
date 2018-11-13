package com.example.a8560p.fitsealbum;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FavoriteImageAdapter extends BaseAdapter {

    private Activity context;
    public FavoriteImageAdapter(Activity localContext) {
        context = localContext;
        //FavoriteActivity.favoriteImages = new ArrayList<>();
    }

    public int getCount() {
        return FavoriteActivity.favoriteImages.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView picturesView;
        if (convertView == null) {
            int column = 4;
            int screenWidth =  Resources.getSystem().getDisplayMetrics().widthPixels;
            int screenHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
            if (screenWidth > screenHeight)
            {
                column = 6;
            }
            int sizeOfImage = (screenWidth - (column + 1) * 8) / column;
            picturesView = new ImageView(context);
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            picturesView
                    .setLayoutParams(new GridView.LayoutParams(sizeOfImage, sizeOfImage));

        } else {
            picturesView = (ImageView) convertView;
        }

        Glide.with(context).load(FavoriteActivity.favoriteImages.get(position))
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher).centerCrop())
                .into(picturesView);
        return picturesView;
    }
}