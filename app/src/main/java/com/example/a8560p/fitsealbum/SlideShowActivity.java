package com.example.a8560p.fitsealbum;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class SlideShowActivity extends AppCompatActivity {
    Intent intentFromFullImageActivity;
    int position;
    ArrayList<String> images;
    String returnUri;
    ViewFlipper flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slideshow);
        flipper = findViewById(R.id.flipperView);

        intentFromFullImageActivity = getIntent(); // Lấy intent
        position = intentFromFullImageActivity.getExtras().getInt("id");
        images = PicturesActivity.images;

        // Lấy thông tin ảnh lưu vào flipper từ vị trí ảnh nhấn slideshow đến ảnh cuối cùng
        for (int i = position; i < PicturesActivity.images.size(); i++)
        {
            returnUri = images.get(i);
            flipperImage(returnUri);
        }

        flipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {}
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                int displayedChild = flipper.getDisplayedChild(); // Đếm số lượng ảnh đã show
                int childCount = flipper.getChildCount();   // Đếm số lượng ảnh tổng cộng

                // Nếu đang show ảnh cuối thì dừng không show nữa
                if (displayedChild == childCount - 1) {
                    flipper.stopFlipping();
                }
            }
        });
    }


    public void flipperImage(String filePath){
        ImageView ImageFromUri  = new ImageView(this);

        Uri path = Uri.fromFile(new File(filePath));

        // Nhờ Glide load bitmap giùm
        Glide.with(getApplicationContext())
                .load(path)
                .into(ImageFromUri);

        flipper.addView(ImageFromUri);
        flipper.setFlipInterval(500);
        flipper.setAutoStart(true);
        flipper.setInAnimation(this , android.R.anim.slide_in_left);
        flipper.setOutAnimation(this , android.R.anim.slide_out_right);
    }
}
