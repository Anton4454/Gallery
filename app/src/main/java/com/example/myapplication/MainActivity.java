package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    final int REQUEST_EXTERNAL_STORAGE = 101;

    RecyclerView recyclerView;
    TextView menuText;
    Button picturesButton;
    Button favoriteButton;
    Button videoButton;
    Button albumsButton;

    private List<String> imagesUri = new ArrayList<>();
    private List<String> favoriteImagesUri = new ArrayList<>();
    private List<String> videos = new ArrayList<>();

    private String countMenu = "pictures";
    private RecyclerAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picturesButton = (Button) findViewById(R.id.picturesButton);
        picturesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countMenu = "pictures";
                        newMenu();
                    }
                }
        );

        favoriteButton = (Button) findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countMenu = "favorite";
                        newMenu();
                    }
                }
        );

        videoButton = (Button) findViewById(R.id.videosButton);
        videoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countMenu = "videos";
                        newMenu();
                    }
                }
        );

        albumsButton = (Button) findViewById(R.id.albumsButton);
        albumsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countMenu = "albums";
                        newMenu();
                    }
                }
        );

        menuText = (TextView) findViewById(R.id.menuTypeView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String uri = imagesUri.get(position);;
                        if (countMenu == "pictures") {
                            //...
                        }
                        else if (countMenu == "favorite"){
                            uri = favoriteImagesUri.get(position);
                        }
                        else if (countMenu == "videos"){
                            uri = videos.get(position);
                        }
                        Intent intent = new Intent(getBaseContext(), ImageFullSize.class);
                        intent.putExtra("uri", uri);
                        startActivity(intent);
                    }


                    @Override
                    public void onLongItemClick(View view, int position) {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && countMenu == "pictures") {
                            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                            favoriteImagesUri.add(imagesUri.get(position));
                            Toast.makeText(getBaseContext(), "Add to favorite", Toast.LENGTH_SHORT).show();
                        } else {
                            v.vibrate(100);
                        }
                    }
                })
        );

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        } else {
            loadImages();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void loadImages() {
        imagesUri = ImagesGallery.listOfImages(this);
        adapter = new RecyclerAdapter(imagesUri, countMenu, this);
        recyclerView.setAdapter(adapter);
    }

    private void loadFavoriteImages() {
        adapter = new RecyclerAdapter(favoriteImagesUri, countMenu, this);
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void loadVideos(){
        videos = VideoGallery.listOfImages(this);
        RecyclerAdapter adapter1 = new RecyclerAdapter(videos, countMenu, VideoDuration.listOfDuration(this), this);
        recyclerView.setAdapter(adapter1);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                //permission granted
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImages();
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void newMenu() {
        switch (countMenu) {
            case "pictures": {
                loadImages();
                menuText.setText("Картинки");
                break;
            }

            case "favorite": {
                loadFavoriteImages();
                menuText.setText("Избранное");
                break;
            }

            case "videos": {
                loadVideos();
                menuText.setText("Видео");
                break;
            }

            case "albums": {
                //loadAlbums();
                break;
            }
        }
    }
}