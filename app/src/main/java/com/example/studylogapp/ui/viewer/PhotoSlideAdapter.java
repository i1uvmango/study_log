package com.example.studylogapp.ui.viewer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studylogapp.R;

import java.io.File;
import java.util.List;

public class PhotoSlideAdapter extends RecyclerView.Adapter<PhotoSlideAdapter.PhotoViewHolder> {
    private List<String> photoUris;
    private android.content.Context context;

    public PhotoSlideAdapter(android.content.Context context, List<String> photoUris) {
        this.context = context;
        this.photoUris = photoUris;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
            .inflate(R.layout.item_photo_slide, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String photoUri = photoUris.get(position);
        File imageFile = new File(photoUri);
        if (imageFile.exists()) {
            Glide.with(context)
                .load(imageFile)
                .fitCenter()
                .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return photoUris.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_photo);
        }
    }
}

