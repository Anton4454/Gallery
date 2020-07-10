package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {


    private List<String> uris;
    private List<String> duration;
    private String menuCount = "pictures";
    private Context context;

    RecyclerView recyclerView;


    public RecyclerAdapter(List<String> images, String menuCount, Context context) {
        this.uris = images;
        this.menuCount = menuCount;
        this.context = context;
    }

    public RecyclerAdapter(List<String> images, String menuCount, List<String> duration, Context context) {
        this.uris = images;
        this.menuCount = menuCount;
        this.duration = duration;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_layout, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String image_id = uris.get(position);
        Glide
                .with(context)
                .load(image_id)
                .override(800, 800)
                //.fitCenter()
                .into(holder.Album)
        ;

        if (menuCount == "videos") {
            int ms = Integer.parseInt(VideoDuration.listOfDuration(context).get(position));
            int time = (int) ms / 1000;
            int seconds = time % 60;
            int minutes = (time % 3600) / 60;
            int hours = time / 3600;
            String format = String.format("%%0%dd", 2);
            String secondsString = String.format(format, seconds);
            String minutesString = String.format(format, minutes);
            String hoursString = String.format(format, hours);
            String finalDuration = hoursString + ":" + minutesString + ":" + secondsString;
            if (hours == 0){
                finalDuration = minutesString + ":" + secondsString;
            }

            holder.DurationView.setText(finalDuration);
            holder.DurationView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
        }
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView Album;
        TextView DurationView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            Album = itemView.findViewById(R.id.album);
            
            DurationView = itemView.findViewById(R.id.durationView);
        }
    }
}
