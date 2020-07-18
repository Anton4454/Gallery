package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

public class ImageFullSize extends AppCompatActivity {

    Button button;
    ZoomageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_size);
        Intent intent = getIntent();
        final String uri = intent.getStringExtra("uri");
        imageView = (ZoomageView) findViewById(R.id.imageFullSize);
        Glide
                .with(this)
                .load(uri)
                //.transition(GenericTransitionOptions.<Drawable>with(R.anim.up_from_bottom))
                .into(imageView)
        ;

        button = findViewById(R.id.sharingText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("image/*");
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(4uri));
                startActivity(Intent.createChooser(sendIntent, "send to..."));
            }
        });
    }

}
