package Video;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class VideoDate {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<String> listOfImages(Context context) {
        Uri uri;
        Cursor cursorDate;
        int column_index_data;
        List<String> listOfAllDates = new ArrayList<>();
        long date;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.Media.DATE_TAKEN};
        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursorDate = context.getContentResolver().query(uri, projection, null,
                null, orderBy + " DESC");
        column_index_data = cursorDate.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN);
        //column_index_data = cursorDate.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_TAKEN);
        //LocalDateTime dateNormalView = LocalDateTime.ofInstant(Instant.ofEpochMilli(column_index_data), ZoneId.systemDefault());
        while (cursorDate.moveToNext()) {
            date = cursorDate.getLong(column_index_data);
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault());
            String outputDate = dateTime.getDayOfMonth() + "." + dateTime.getMonthValue() + "." + dateTime.getYear();
            listOfAllDates.add(outputDate);
        }

        return listOfAllDates;
    }
}
