package com.example.myapplication;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_EXTERNAL_STORAGE = 100;

    RecyclerView recyclerView;
    VideoView videoView;

    private List<Drawable> images = new ArrayList<>();
    private List<Uri> imagesUri = new ArrayList<>();

    private RecyclerAdapter adapter;

    private Drawable drawable_count;

    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Uri uri = imagesUri.get(position);
                        Intent intent = new Intent(getBaseContext(), ImageFullSize.class);
                        intent.putExtra("uri", uri.toString());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            v.vibrate(100);
                        }
                        ArrayList<Uri> imageUris = new ArrayList<Uri>();
                        imageUris.add(imagesUri.get(position));
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                        shareIntent.setType("image/*");
                        startActivity(Intent.createChooser(shareIntent, "Share images to.."));
                    }
                })
        );

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.grazView);
                lottieAnimationView.playAnimation();
                final MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.fireworksound);
                mp.start();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
//                    return;
                        } else {
                            launchGalleryIntent();
                        }
                    }
                }, 1500);

            }
        });
    }

    public void launchGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                //permission granted
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchGalleryIntent();
                } else {
                    //permission denied
                }
                return;
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXTERNAL_STORAGE && resultCode == RESULT_OK) {
            final List<Drawable> drawables = new ArrayList<>();
            ClipData clipData = data.getClipData();

            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    imagesUri.add(imageUri);
                    Log.d("URI", imageUri.toString());
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Drawable drawable = Drawable.createFromStream(inputStream, "");
                        drawables.add(drawable);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Uri imageUri = data.getData();
                imagesUri.add(imageUri);
                Log.d("URI", imageUri.toString());
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Drawable drawable = Drawable.createFromStream(inputStream, "");
                    drawables.add(drawable);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (final Drawable drawable : drawables) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    images.add(drawable);
                                    adapter = new RecyclerAdapter(images, getBaseContext());
                                    recyclerView.setAdapter(adapter);
                                }
                            });
                        }

                    }
                }).start();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error adding holder", Toast.LENGTH_LONG).show();
            }
        }
    }


}