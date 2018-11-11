package com.example.a8560p.fitsealbum;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.WallpaperManager;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import static android.view.View.VISIBLE;

public class FullImageActivity extends AppCompatActivity {
    Toolbar toolBar;
    ImageView imageView;
    TextView txtDateModified;
    int position;
    BottomNavigationView mainNav;
    private float x1, x2, y1, y2;
    static final int MIN_DISTANCE = 150;
    View decorView;
    MyPrefs myPrefs;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        myPrefs = new MyPrefs(this); // Khởi tạo biến để kiểm tra có đang trong NightMode hay không
        //Màn hình fullscreen
        decorView = getWindow().getDecorView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_image);

        // setup ActionBar
        toolBar = (Toolbar) findViewById(R.id.nav_actionBar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        mainNav = (BottomNavigationView) findViewById(R.id.nav_bottom);
        txtDateModified = (TextView)findViewById(R.id.txtDateModified);
        if (PicturesActivity.hideToolbar == 0) {
            //decorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
            mainNav.setVisibility(View.VISIBLE);
            txtDateModified.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
        } else {
            getSupportActionBar().hide();
            mainNav.setVisibility(View.GONE);
            txtDateModified.setVisibility(View.GONE);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        Intent i = getIntent();
        imageView = (ImageView) findViewById(R.id.imageView);

        //Navigation bottom onClickListener
        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_edit: {
                        Toast.makeText(getApplicationContext(), "Edit Image", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    case R.id.nav_crop: {
                        Toast.makeText(getApplicationContext(), "Crop Image", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    case R.id.nav_share: {
                        startActivity(Intent.createChooser(emailIntent(), "Share image using"));
                        return true;
                    }
                    case R.id.nav_delete: {
                        Intent i = getIntent(); // Lấy intent
                        final String returnUri = i.getExtras().getString("path"); // Lấy đường dẫn trong intent

                        final File photoFile = new File( returnUri);

                        // Tạo biến builder để tạo dialog để xác nhận có xoá file hay không
                        AlertDialog builder;

                        if (myPrefs.loadNightModeState()) {
                            builder = new AlertDialog.Builder(FullImageActivity.this, android.R.style.Theme_DeviceDefault_Dialog_Alert).create();
                        } else {
                            builder = new AlertDialog.Builder(FullImageActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert).create();
                        }

                        builder.setMessage("Are you sure you want to delete this item ?");
                        builder.setButton(Dialog.BUTTON_POSITIVE,"YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //---- Dưới đây là bài hướng dẫn xoá ảnh sử dụng ContentResolver trên diễn đàn stackoverflow ----
                                // Nguồn: http://stackoverflow.com/a/20780472#1#L0

                                // Khởi tạo ID
                                String[] projection = { MediaStore.Images.Media._ID };

                                // Lấy thông tin đường dẫn
                                String selection = MediaStore.Images.Media.DATA + " = ?";
                                String[] selectionArgs = new String[] { photoFile.getAbsolutePath() };

                                //
                                Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                                ContentResolver contentResolver = getContentResolver();
                                Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
                                if (c.moveToFirst()) {
                                    // Tìm thấy ID. Xoá ảnh dựa nhờ content provider
                                    long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                                    Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                                    contentResolver.delete(deleteUri, null, null);
                                } else {
                                    // File không có database
                                }
                                c.close();

                                Toast.makeText(FullImageActivity.this, "Item has been deleted", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        builder.setButton(Dialog.BUTTON_NEGATIVE,"NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.show();
                        return true;
                    }
                }
                return false;
            }
        });

        position = i.getExtras().getInt("id");
        Glide.with(getApplicationContext()).load(PicturesActivity.images.get(position))
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher).fitCenter())
                .into(imageView);

        String returnUri = i.getExtras().getString("path"); // Lấy đường dẫn trong intent
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm"); // Tạo format date để lưu Date
        File file = new File(returnUri);
        txtDateModified.setText(sdf.format(file.lastModified()));

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        x2 = event.getX();
                        y2 = event.getY();
                        float deltaX = x2 - x1;
                        float deltaY = y2 - y1;
                        if (Math.abs(deltaX) >= MIN_DISTANCE && Math.abs(deltaY) <= MIN_DISTANCE/2) {
                            // Left to Right swipe action
                            if (x2 > x1) {
                                if (position > 0) {
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                                    i.putExtra("id", position - 1);
                                    i.putExtra("path", PicturesActivity.images.get(position - 1));
                                    i.putExtra("allPath", PicturesActivity.images);
                                    startActivity(i);
                                }
                            }
                            // Right to left swipe action
                            else if (x2 < x1) {
                                if (position < PicturesActivity.images.size() - 1) {
                                    finish();
                                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                                    i.putExtra("id", position + 1);
                                    i.putExtra("path", PicturesActivity.images.get(position + 1));
                                    i.putExtra("allPath", PicturesActivity.images);
                                    startActivity(i);
                                }
                            }
                        } else if(Math.abs(deltaY) >= MIN_DISTANCE && Math.abs(deltaX) <= MIN_DISTANCE/2 && y2 < y1){
                            finish();
                        }
                        else {
                            // consider as something else - a screen tap for example
                            PicturesActivity.hideToolbar = (PicturesActivity.hideToolbar + 1) % 2;
                            if (PicturesActivity.hideToolbar == 1) {
                                getSupportActionBar().hide();
                                mainNav.setVisibility(View.GONE);
                                txtDateModified.setVisibility(View.GONE);
                                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                            } else {
                                //decorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
                                mainNav.setVisibility(View.VISIBLE);
                                txtDateModified.setVisibility(View.VISIBLE);
                                getSupportActionBar().show();
                            }
                        }
                        break;
                    }
                }
                if (PicturesActivity.hideToolbar == 0) {
                    //decorView.setSystemUiVisibility(View.SYSTEM_UI_LAYOUT_FLAGS);
                    mainNav.setVisibility(View.VISIBLE);
                    txtDateModified.setVisibility(View.VISIBLE);
                    getSupportActionBar().show();
                } else {
                    getSupportActionBar().hide();
                    mainNav.setVisibility(View.GONE);
                    txtDateModified.setVisibility(View.GONE);
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; add items to the action bar
        getMenuInflater().inflate(R.menu.image_main, menu);
        return true;
    }

    // return a SHARED intent to deliver an email
    private Intent emailIntent() {

        Intent i = getIntent(); // Lấy intent
        String returnUri = i.getExtras().getString("path"); // Lấy đường dẫn trong intent

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        final File photoFile = new File(returnUri);

        shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(FullImageActivity.this, "com.example.a8560p.fitsealbum", photoFile));

        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // user clicked a menu-item from ActionBar

        int id = item.getItemId();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (id == R.id.action_favorite) {
            return true;
        } else if (id == R.id.nav_share) {
            startActivity(Intent.createChooser(emailIntent(), "Share image using..."));
            return true;
        } else if (id == R.id.action_slideshow) {
            // perform SLIDESHOW operations...
            return true;
        } else if (id == R.id.action_setBackground) {
            // perform SETBACKGROUND operations...
            WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                myWallpaperManager.setBitmap(((BitmapDrawable) imageView.getDrawable()).getBitmap(), null, false,
                        WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);
                Toast.makeText(getApplicationContext(), "Image Successfully Set.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        } else if (id == R.id.action_print) {
            // perform PRINT operations...
            return true;
        } else if (id == R.id.action_details) {
            // perform INFORMATION operations...
            Intent i = getIntent(); // Lấy intent
            String returnUri = i.getExtras().getString("path"); // Lấy đường dẫn trong intent
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm"); // Tạo format date để lưu Date
            File file = new File(returnUri);

            final DecimalFormat format = new DecimalFormat("#.##"); // Tạo format cho size
            final double length = file.length();    // Lấy độ dài file
            String sLength;

            if (length > 1024 * 1024) {
                sLength = format.format(length / (1024 * 1024)) + " MB";
            } else {
                if (length > 1024) {
                    sLength = format.format(length / 1024) + " KB";
                } else {
                    sLength = format.format(length) + " B";
                }
            }

            try {
                ExifInterface exif = new ExifInterface(returnUri);
                String Details = ShowExif(exif);    // Lấy thông tin của ảnh

                Details = "Date: " + sdf.format(file.lastModified()) +
                        "\n\nSize: " + sLength +
                        "\n\nFile path: " + returnUri +
                        Details;

                // -----  Tạo dialog để xuất ra detail -----

                TextView title = new TextView(getApplicationContext());
                title.setPadding(46, 40, 0, 0);
                title.setText("Details");
                title.setTextSize(23.0f);
                title.setTypeface(null, Typeface.BOLD);
                AlertDialog dialog;

                if (myPrefs.loadNightModeState()) {
                    title.setTextColor(Color.WHITE);

                    dialog = new AlertDialog.Builder(FullImageActivity.this, android.R.style.Theme_DeviceDefault_Dialog_Alert).create();
                } else {
                    title.setTextColor(Color.BLACK);

                    dialog = new AlertDialog.Builder(FullImageActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert).create();
                }

                dialog.setCustomTitle(title);
                dialog.setMessage(Details);
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return true;
        } else if (id == R.id.action_delete) {
            // perform DELETE operations...
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    private String ShowExif(ExifInterface exif) {
        String myAttribute = "";

        if (exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0) == 0) {
            return myAttribute;
        } else {
            myAttribute += "\n\nResolution: " + exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) +
                    "x" + exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);

            if (exif.getAttribute(ExifInterface.TAG_MODEL) == null) {
                return myAttribute;
            }
        }

        // Lấy aperture
        final DecimalFormat apertureFormat = new DecimalFormat("#.#"); // Tạo format cho aperture
        String aperture = exif.getAttribute(ExifInterface.TAG_F_NUMBER);
        if (aperture != null) {
            Double aperture_double = Double.parseDouble(aperture);
            apertureFormat.format(aperture_double);
            myAttribute += "\n\nAperture: f/" + aperture_double + "\n\n";
        } else {
            myAttribute += "\n\nAperture: unknown\n\n";
        }

        // Lấy exposure time
        String ExposureTime = exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
        Double ExposureTime_double = Double.parseDouble(ExposureTime);
        Double Denominator = 1 / ExposureTime_double;

        ExposureTime = 1 + "/" + String.format("%.0f", Denominator);

        myAttribute += "Exposure Time: " + ExposureTime + "s\n\n";

        if (exif.getAttributeInt(ExifInterface.TAG_FLASH, 0) == 0) {
            myAttribute += "Flash: Off\n\n";
        } else {
            myAttribute += "Flash: On\n\n";
        }

        myAttribute += "Focal Length: " + exif.getAttributeDouble(ExifInterface.TAG_FOCAL_LENGTH, 0) + "mm\n\n";
        myAttribute += "ISO Value: " + exif.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS) + "\n\n";
        myAttribute += "Model: " + exif.getAttribute(ExifInterface.TAG_MODEL);

        return myAttribute;
    }
}
