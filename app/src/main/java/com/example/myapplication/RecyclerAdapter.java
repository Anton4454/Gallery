package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;



public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {



    private List<Uri> uris;

    private Context context;

    RecyclerView recyclerView;


    public RecyclerAdapter(List<Uri> images, Context context) {
        this.uris = images;
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
        Uri image_id = uris.get(position);
        Glide
                .with(context)
                .load(image_id)
                .override(600,600)
                .centerInside()
                .into(holder.Album)
        ;
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView Album;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            Album = itemView.findViewById(R.id.album);
        }
    }
}
