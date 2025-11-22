package com.example.studylogapp.ui.posting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studylogapp.R;
import com.example.studylogapp.ai.GeminiQuizGenerator;
import com.example.studylogapp.database.AppDatabase;
import com.example.studylogapp.model.StudyPost;
import com.example.studylogapp.model.Quiz;
import com.example.studylogapp.storage.ImageStorageManager;
import com.example.studylogapp.utils.DateUtils;
import com.example.studylogapp.utils.PermissionUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PostingActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int MAX_PHOTOS = 3;

    private TextView tvDate;
    private Button btnAddPhoto, btnSave, btnCancel;
    private RecyclerView recyclerView;
    private PhotoPickerAdapter adapter;
    private List<PhotoItem> photoItems;
    private String selectedDate;
    private AppDatabase database;
    private ImageStorageManager imageStorage;
    private long existingLogId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        database = new AppDatabase(this);
        database.open();
        imageStorage = new ImageStorageManager(this);

        selectedDate = getIntent().getStringExtra("date");
        if (selectedDate == null) {
            selectedDate = DateUtils.formatDate(Calendar.getInstance());
        }

        tvDate = findViewById(R.id.tv_date);
        btnAddPhoto = findViewById(R.id.btn_add_photo);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        recyclerView = findViewById(R.id.recycler_photos);

        Calendar date = DateUtils.parseDate(selectedDate);
        tvDate.setText(String.format(getString(R.string.date_format),
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.DAY_OF_MONTH)));

        photoItems = new ArrayList<>();
        adapter = new PhotoPickerAdapter(photoItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        // 기존 데이터 로드
        loadExistingData();

        btnAddPhoto.setOnClickListener(v -> showPhotoSourceDialog());
        btnSave.setOnClickListener(v -> savePost());
        btnCancel.setOnClickListener(v -> finish());

        adapter.setOnDeleteListener(position -> {
            PhotoItem item = photoItems.get(position);
            if (item.getImagePath() != null) {
                imageStorage.deleteImage(item.getImagePath());
            }
            photoItems.remove(position);
            adapter.notifyDataSetChanged();
        });
    }

    private void loadExistingData() {
        List<StudyPost> existingPosts = database.getPostsByDate(selectedDate);
        if (existingPosts != null && !existingPosts.isEmpty()) {
            for (StudyPost post : existingPosts) {
                photoItems.add(new PhotoItem(post.getPhotoUri(), post.getSummary(), post.getKeyword()));
            }
            adapter.notifyDataSetChanged();
            existingLogId = database.getStudyLogByDate(selectedDate).getId();
        }
    }

    private void showPhotoSourceDialog() {
        if (photoItems.size() >= MAX_PHOTOS) {
            Toast.makeText(this, R.string.max_photos, Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_photo_source);
        builder.setItems(new String[]{getString(R.string.camera), getString(R.string.gallery)},
            (dialog, which) -> {
                if (which == 0) {
                    openCamera();
                } else {
                    openGallery();
                }
            });
        builder.show();
    }

    private void openCamera() {
        if (!PermissionUtils.hasCameraPermission(this)) {
            PermissionUtils.requestCameraPermission(this);
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    private void openGallery() {
        if (!PermissionUtils.hasStoragePermission(this)) {
            PermissionUtils.requestStoragePermission(this);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String imagePath = imageStorage.saveImage(bitmap);
            if (imagePath != null) {
                photoItems.add(new PhotoItem(imagePath, "", ""));
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_GALLERY && data != null) {
            Uri uri = data.getData();
            String imagePath = imageStorage.saveImageFromUri(uri);
            if (imagePath != null) {
                photoItems.add(new PhotoItem(imagePath, "", ""));
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == PermissionUtils.REQUEST_CAMERA_PERMISSION) {
                openCamera();
            } else if (requestCode == PermissionUtils.REQUEST_STORAGE_PERMISSION) {
                openGallery();
            }
        }
    }

    private void savePost() {
        if (photoItems.isEmpty()) {
            Toast.makeText(this, R.string.photo_required, Toast.LENGTH_SHORT).show();
            return;
        }

        // 기존 게시물 삭제
        if (existingLogId != -1) {
            database.deleteAllPostsByLogId(existingLogId);
            // 기존 퀴즈도 삭제
            database.deleteQuizByLogId(existingLogId);
        }

        // 새 게시물 저장
        long logId = database.getOrCreateStudyLogId(selectedDate);
        for (int i = 0; i < photoItems.size(); i++) {
            PhotoItem item = photoItems.get(i);
            StudyPost post = new StudyPost(logId, item.getImagePath(), item.getSummary(), item.getKeyword(), i);
            database.insertStudyPost(post);
        }

        // 퀴즈 생성 (첫 번째 게시물을 기반으로)
        if (!photoItems.isEmpty()) {
            PhotoItem firstItem = photoItems.get(0);
            generateQuizAsync(logId, firstItem.getImagePath(), firstItem.getSummary(), firstItem.getKeyword());
        }

        Toast.makeText(this, R.string.save, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void generateQuizAsync(long logId, String imagePath, String summary, String keyword) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            GeminiQuizGenerator generator = new GeminiQuizGenerator(PostingActivity.this);
            Quiz quiz = generator.generateQuiz(imagePath, summary, keyword);
            if (quiz != null) {
                quiz.setStudyLogId(logId);
                database.insertQuiz(quiz);
                runOnUiThread(() -> {
                    Toast.makeText(PostingActivity.this, "퀴즈가 생성되었습니다!", Toast.LENGTH_SHORT).show();
                });
            } else {
                // 퀴즈 생성 실패는 조용히 처리 (사용자에게 큰 영향 없음)
                android.util.Log.w("PostingActivity", "퀴즈 생성 실패");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}

