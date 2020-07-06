package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

public class ImageFullSize extends AppCompatActivity {

    ZoomageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_size);
        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getStringExtra("uri"));
        imageView = (ZoomageView) findViewById(R.id.imageFullSize);
        Glide
                .with(this)
                .load(uri)
                //.transition(GenericTransitionOptions.<Drawable>with(R.anim.up_from_bottom))
                .into(imageView)
        ;
    }

}
