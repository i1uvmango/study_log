package com.example.studylogapp.ui.posting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studylogapp.R;

import java.io.File;
import java.util.List;

public class PhotoPickerAdapter extends RecyclerView.Adapter<PhotoPickerAdapter.PhotoViewHolder> {
    private List<PhotoItem> photoItems;
    private PostingActivity activity;
    private OnDeleteListener deleteListener;

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public PhotoPickerAdapter(List<PhotoItem> photoItems, PostingActivity activity) {
        this.photoItems = photoItems;
        this.activity = activity;
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_photo_picker, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        PhotoItem item = photoItems.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return photoItems.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private EditText etSummary, etKeyword;
        private Button btnDelete;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            etSummary = itemView.findViewById(R.id.et_summary);
            etKeyword = itemView.findViewById(R.id.et_keyword);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(PhotoItem item, int position) {
            File imageFile = new File(item.getImagePath());
            if (imageFile.exists()) {
                Glide.with(itemView.getContext())
                    .load(imageFile)
                    .centerCrop()
                    .into(ivPhoto);
            }

            etSummary.setText(item.getSummary());
            etSummary.setHint(activity.getString(R.string.summary_hint));
            etSummary.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    item.setSummary(etSummary.getText().toString());
                }
            });

            etKeyword.setText(item.getKeyword());
            etKeyword.setHint(activity.getString(R.string.keyword_hint));
            etKeyword.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    item.setKeyword(etKeyword.getText().toString());
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDelete(position);
                }
            });
        }
    }
}

