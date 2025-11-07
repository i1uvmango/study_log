package com.example.studylogapp.ui.calendar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studylogapp.R;
import com.example.studylogapp.storage.ImageStorageManager;

import java.io.File;

public class CalendarViewHolder extends RecyclerView.ViewHolder {
    private TextView tvDate;
    private ImageView ivThumbnail;
    private View rootView;

    public CalendarViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDate = itemView.findViewById(R.id.tv_date);
        ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
        rootView = itemView;
    }

    public void bind(CalendarDay day) {
        if (day.isHeader()) {
            tvDate.setText(day.getDate());
            tvDate.setTextSize(14);
            ivThumbnail.setVisibility(View.GONE);
            rootView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.calendar_cell_bg));
        } else {
            tvDate.setText(day.getDate());
            tvDate.setTextSize(12);
            
            if (day.isToday()) {
                rootView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.today_highlight));
            } else {
                rootView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.white));
            }

            if (day.hasPost() && day.getThumbnailUri() != null) {
                ivThumbnail.setVisibility(View.VISIBLE);
                File imageFile = new File(day.getThumbnailUri());
                if (imageFile.exists()) {
                    Glide.with(itemView.getContext())
                        .load(imageFile)
                        .centerCrop()
                        .into(ivThumbnail);
                } else {
                    ivThumbnail.setVisibility(View.GONE);
                }
            } else {
                ivThumbnail.setVisibility(View.GONE);
            }
        }
    }
}

