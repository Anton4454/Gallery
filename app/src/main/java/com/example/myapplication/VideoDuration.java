package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class VideoDuration {
    public static List<String> listOfDuration(Context context){
        Uri uri;
        Cursor cursorDuration;
        int column_index_data, column_index_folder_name;
        List<String> listOfAllDurations = new ArrayList<>();
        String duration;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.DURATION};
        String orderBy = MediaStore.Video.Media.DATE_TAKEN;

        //GET FOLDER NAME
        //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        cursorDuration = context.getContentResolver().query(uri, projection, null,
                null, orderBy+" DESC");

        column_index_data = cursorDuration.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
        while (cursorDuration.moveToNext()){
            duration = cursorDuration.getString(column_index_data);

            listOfAllDurations.add(duration);
        }
        return listOfAllDurations;
    }
}

