package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class VideoGallery {
    public static List<String> listOfVideos(Context context){
        Uri uri;
        Cursor cursorVideo;
        int column_index_data, column_index_folder_name;
        List<String> listOfAllVideos = new ArrayList<>();
        String absolutePathOfImage;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME};

        String[] projection1 = {MediaStore.MediaColumns.DATA,
        MediaStore.Video.Media.DURATION};
        String orderBy = MediaStore.Video.Media.DATE_TAKEN;

        cursorVideo = context.getContentResolver().query(uri, projection, null,
                null, orderBy+" DESC");

        column_index_data = cursorVideo.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        //GET FOLDER NAME
        //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursorVideo.moveToNext()){
            absolutePathOfImage = cursorVideo.getString(column_index_data);

            listOfAllVideos.add(absolutePathOfImage);
        }


        return listOfAllVideos;
    }
}
