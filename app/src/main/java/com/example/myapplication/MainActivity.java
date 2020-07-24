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
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_EXTERNAL_STORAGE = 101;

    RecyclerView recyclerView;
    TextView menuText;
    Button picturesButton;
    Button favoriteButton;
    Button videoButton;
    com.example.myapplication.SharedPreferences sharedPreferences;

    private List<String> imagesUri = new ArrayList<>();
    public List<String> favoriteImagesUri = new ArrayList<>();
    private List<String> videosUri = new ArrayList<>();
    private List<String> dates = new ArrayList<>();
    private String countMenu = "pictures";
    private RecyclerAdapter adapter;
    private GridLayoutManager layoutManager;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = new SharedPreferences();
        favoriteImagesUri = sharedPreferences.getFavorites(this);
        setContentView(R.layout.activity_main);
        picturesButton = findViewById(R.id.picturesButton);
        picturesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countMenu = "pictures";
                        newMenu();
                    }
                }
        );

        favoriteButton = findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countMenu = "favorite";
                        newMenu();
                    }
                }
        );

        videoButton = findViewById(R.id.videosButton);
        videoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countMenu = "videos";
                        newMenu();
                    }
                }
        );

        menuText = findViewById(R.id.menuTypeView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case 1:
                        return 1;
                    default:
                        return 3;
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String uri;
                        if (countMenu == "pictures") {
                            uri = imagesUri.get(position);
                        } else if (countMenu == "favorite") {
                            uri = favoriteImagesUri.get(position);
                        } else {
                            uri = videosUri.get(position);
                        }
                        if (uri != null) {
                            Intent intent = new Intent(getBaseContext(), ImageFullSize.class);
                            intent.putExtra("uri", uri);
                            startActivity(intent);
                        }
                    }


                    @Override
                    public void onLongItemClick(View view, int position) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                                && countMenu == "pictures") {
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
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

    private void loadFavoriteImages() {
        adapter = new RecyclerAdapter(sharedPreferences.getFavorites(this), countMenu, this);
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void loadImages() {
        imagesUri = ImagesGallery.listOfImages(this);
        dates = parseDate(PhotoDate.listOfImages(this));
        for (int i = 1; i < dates.size(); i++) {
            if (!dates.get(i).equals(dates.get(i - 1))) {
                imagesUri.add(i, null);
                dates.add(i, dates.get(i));
            }
        }

        imagesUri.add(0, null);
        adapter = new RecyclerAdapter(imagesUri, countMenu, this, dates, sharedPreferences.getFavorites(this));
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void loadVideos() {
        videosUri = VideoGallery.listOfVideos(this);
        dates = parseDate(VideoDate.listOfImages(this));
        for (int i = 1; i < dates.size(); i++) {
            if (!dates.get(i).equals(dates.get(i - 1))) {
                videosUri.add(i, null);
                dates.add(i, dates.get(i));
            }
        }

        videosUri.add(0, null);
        adapter = new RecyclerAdapter(videosUri, countMenu, VideoDuration.listOfDuration(this), this, dates);
        recyclerView.setAdapter(adapter);
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
        }
    }

    private List<String> parseDate(List<String> dates) {
        for (int i = 0; i < dates.size(); i++) {
            StringTokenizer tokenizer = new StringTokenizer(dates.get(i), ".");
            String day = tokenizer.nextToken();
            String month = tokenizer.nextToken();
            String year = tokenizer.nextToken();
            switch (month) {
                case "1":
                    month = "янв.";
                    break;
                case "2":
                    month = "фев.";
                    break;
                case "3":
                    month = "мар.";
                    break;
                case "4":
                    month = "апр.";
                    break;
                case "5":
                    month = "мая";
                    break;
                case "6":
                    month = "июн.";
                    break;
                case "7":
                    month = "июл.";
                    break;
                case "8":
                    month = "авг.";
                    break;
                case "9":
                    month = "сен.";
                    break;
                case "10":
                    month = "окт.";
                    break;
                case "11":
                    month = "нояб.";
                    break;
                case "12":
                    month = "дек.";
                    break;
            }
            dates.set(i, day + " " + month + " " + year + " г.");
        }

        return dates;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.saveFavorites(sharedPreferences.getFavorites(this), this);
    }
}