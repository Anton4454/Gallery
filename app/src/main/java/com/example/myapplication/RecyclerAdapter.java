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


public class RecyclerAdapter extends RecyclerView.Adapter {


    private static final String TAG2 = "date";
    private List<String> uris;
    private List<String> duration;
    private List<String> dates;
    private List<String> positions;
    private String menuCount = "pictures";
    private Context context;
    private int position_date_count = -1;

    RecyclerView recyclerView;

    public RecyclerAdapter(List<String> images, String menuCount, Context context) {
        this.uris = images;
        this.menuCount = menuCount;
        this.context = context;
    }

    public RecyclerAdapter(List<String> images, String menuCount, Context context, List<String> dates) {
        this.uris = images;
        this.menuCount = menuCount;
        this.context = context;
        this.dates = dates;
    }

    public RecyclerAdapter(List<String> images, String menuCount, List<String> duration, Context context, List<String> dates) {
        this.uris = images;
        this.menuCount = menuCount;
        this.duration = duration;
        this.context = context;
        this.dates = dates;
    }

    @Override
    public int getItemViewType(int position) {
        if (uris.get(position) != null) {
            return 1;
        } else {
            return 2;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.album_layout, parent, false);
            return new ViewHolderImage(view);
        }

        view = layoutInflater.inflate(R.layout.date_layout, parent, false);
        return new ViewHolderDate(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (uris.get(position) != null) {
            ViewHolderImage viewHolderImage = (ViewHolderImage) holder;
            String image_id = uris.get(position);
            Glide
                    .with(context)
                    .load(image_id)
                    .override(800, 800)
                    //.fitCenter()
                    .into(viewHolderImage.album)
            ;

            if (menuCount == "videos") {
                String finalDuration = parseDuration(Integer.parseInt(VideoDuration.listOfDuration(context).get(position)));
                viewHolderImage.durationView.setText(finalDuration);
                viewHolderImage.durationView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            }

        } else if (uris.get(position) == null) {
            ViewHolderDate viewHolderDate = (ViewHolderDate) holder;
            viewHolderDate.dateView.setText(dates.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    class ViewHolderImage extends RecyclerView.ViewHolder {

        ImageView album;
        TextView durationView;

        public ViewHolderImage(@NonNull View itemView) {
            super(itemView);
            album = itemView.findViewById(R.id.album);
            durationView = itemView.findViewById(R.id.durationView);
        }
    }

    class ViewHolderDate extends RecyclerView.ViewHolder {

        TextView dateView;

        public ViewHolderDate(@NonNull View itemView) {
            super(itemView);
            dateView = itemView.findViewById(R.id.dateView);
        }
    }

    private String parseDuration(int ms){
        int time = (int) ms / 1000;
        int seconds = time % 60;
        int minutes = (time % 3600) / 60;
        int hours = time / 3600;
        String format = String.format("%%0%dd", 2);
        String secondsString = String.format(format, seconds);
        String minutesString = String.format(format, minutes);
        String hoursString = String.format(format, hours);
        String finalDuration = hoursString + ":" + minutesString + ":" + secondsString;
        if (hours == 0) {
            finalDuration = minutesString + ":" + secondsString;
        }

        return finalDuration;
    }
}
