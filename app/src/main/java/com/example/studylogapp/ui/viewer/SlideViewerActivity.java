package com.example.studylogapp.ui.viewer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.studylogapp.R;
import com.example.studylogapp.database.AppDatabase;
import com.example.studylogapp.model.StudyPost;
import com.example.studylogapp.storage.ImageStorageManager;
import com.example.studylogapp.ui.posting.PostingActivity;

import java.util.List;

public class SlideViewerActivity extends FragmentActivity {
    private ViewPager2 viewPager;
    private SlideViewPagerAdapter adapter;
    private Button btnEdit, btnDelete;
    private String selectedDate;
    private AppDatabase database;
    private ImageStorageManager imageStorage;
    private List<StudyPost> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_viewer);

        selectedDate = getIntent().getStringExtra("date");
        if (selectedDate == null) {
            finish();
            return;
        }

        database = new AppDatabase(this);
        database.open();
        imageStorage = new ImageStorageManager(this);

        viewPager = findViewById(R.id.viewpager_slides);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);

        loadPosts();

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(SlideViewerActivity.this, PostingActivity.class);
            intent.putExtra("date", selectedDate);
            startActivityForResult(intent, 100);
        });

        btnDelete.setOnClickListener(v -> showDeleteConfirmDialog());
    }

    private void loadPosts() {
        posts = database.getPostsByDate(selectedDate);
        if (posts == null || posts.isEmpty()) {
            Toast.makeText(this, R.string.no_photo, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new SlideViewPagerAdapter(this, posts);
        viewPager.setAdapter(adapter);
    }

    private void showDeleteConfirmDialog() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.delete)
            .setMessage(R.string.delete_confirm)
            .setPositiveButton(R.string.yes, (dialog, which) -> {
                deleteAllPosts();
            })
            .setNegativeButton(R.string.no, null)
            .show();
    }

    private void deleteAllPosts() {
        for (StudyPost post : posts) {
            imageStorage.deleteImage(post.getPhotoUri());
            database.deleteStudyPost(post.getId());
        }
        database.deleteStudyLog(selectedDate);
        
        Toast.makeText(this, R.string.delete, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadPosts();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}

