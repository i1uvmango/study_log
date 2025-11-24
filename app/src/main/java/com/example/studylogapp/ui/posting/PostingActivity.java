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
    private static final int REQUEST_CHANGE_IMAGE = 3;
    private static final int MAX_PHOTOS = 3;
    
    private int changingImagePosition = -1;

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
        btnSave.setOnClickListener(v -> {
            android.util.Log.d("PostingActivity", "🔵 저장 버튼 클릭됨!");
            if (btnSave == null) {
                android.util.Log.e("PostingActivity", "❌ btnSave가 null입니다!");
            } else {
                android.util.Log.d("PostingActivity", "✅ btnSave 정상, savePost() 호출");
            }
            savePost();
        });
        btnCancel.setOnClickListener(v -> finish());

        adapter.setOnDeleteListener(position -> {
            PhotoItem item = photoItems.get(position);
            if (item.getImagePath() != null) {
                imageStorage.deleteImage(item.getImagePath());
            }
            photoItems.remove(position);
            adapter.notifyDataSetChanged();
        });
        
        adapter.setOnImageClickListener(position -> {
            // 이미지 클릭 시 갤러리에서 선택
            if (!PermissionUtils.hasStoragePermission(this)) {
                PermissionUtils.requestStoragePermission(this);
                return;
            }
            changingImagePosition = position;
            openGalleryForChange();
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
    
    private void openGalleryForChange() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CHANGE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            changingImagePosition = -1;
            return;
        }

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
        } else if (requestCode == REQUEST_CHANGE_IMAGE && data != null && changingImagePosition >= 0) {
            // 기존 이미지 교체
            Uri uri = data.getData();
            String newImagePath = imageStorage.saveImageFromUri(uri);
            if (newImagePath != null && changingImagePosition < photoItems.size()) {
                PhotoItem item = photoItems.get(changingImagePosition);
                // 기존 이미지 삭제
                if (item.getImagePath() != null) {
                    imageStorage.deleteImage(item.getImagePath());
                }
                // 새 이미지로 교체
                item.setImagePath(newImagePath);
                adapter.notifyDataSetChanged();
            }
            changingImagePosition = -1;
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
        android.util.Log.d("PostingActivity", "========== savePost() 호출됨 ==========");
        android.util.Log.d("PostingActivity", "photoItems 크기: " + photoItems.size());
        android.util.Log.d("PostingActivity", "selectedDate: " + selectedDate);
        
        if (photoItems.isEmpty()) {
            android.util.Log.w("PostingActivity", "❌ photoItems가 비어있어서 저장 취소");
            Toast.makeText(this, R.string.photo_required, Toast.LENGTH_SHORT).show();
            return;
        }

        android.util.Log.d("PostingActivity", "✅ photoItems 있음, 저장 진행");
        
        // 저장 전에 모든 PhotoItem의 summary와 keyword를 강제로 업데이트
        android.util.Log.d("PostingActivity", "updateAllPhotoItems() 호출 시작");
        updateAllPhotoItems();
        android.util.Log.d("PostingActivity", "updateAllPhotoItems() 호출 완료");

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
        android.util.Log.d("PostingActivity", "========== 퀴즈 생성 체크 시작 ==========");
        android.util.Log.d("PostingActivity", "photoItems 크기: " + photoItems.size());
        
        if (!photoItems.isEmpty()) {
            PhotoItem firstItem = photoItems.get(0);
            String imagePath = firstItem.getImagePath();
            String summary = firstItem.getSummary();
            String keyword = firstItem.getKeyword();
            
            android.util.Log.d("PostingActivity", "첫 번째 아이템 정보:");
            android.util.Log.d("PostingActivity", "  - 이미지 경로: " + (imagePath != null ? imagePath : "null"));
            android.util.Log.d("PostingActivity", "  - 요약: [" + (summary != null ? summary : "null") + "]");
            android.util.Log.d("PostingActivity", "  - 키워드: [" + (keyword != null ? keyword : "null") + "]");
            
            // 이미지가 없어도 summary나 keyword가 있으면 퀴즈 생성 가능
            boolean hasImage = imagePath != null && !imagePath.trim().isEmpty();
            boolean hasSummary = summary != null && !summary.trim().isEmpty();
            boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
            
            android.util.Log.d("PostingActivity", "조건 체크:");
            android.util.Log.d("PostingActivity", "  - 이미지 있음: " + hasImage);
            android.util.Log.d("PostingActivity", "  - 요약 있음: " + hasSummary);
            android.util.Log.d("PostingActivity", "  - 키워드 있음: " + hasKeyword);
            
            if (hasImage || hasSummary || hasKeyword) {
                android.util.Log.d("PostingActivity", "✅ 퀴즈 생성 조건 만족! 퀴즈 생성 시작");
                android.util.Log.d("PostingActivity", "퀴즈 생성 시작 - 이미지: " + imagePath + 
                    ", 요약: " + summary + ", 키워드: " + keyword);
                generateQuizAsync(logId, imagePath, summary, keyword);
            } else {
                android.util.Log.w("PostingActivity", "❌ 퀴즈 생성 불가 - 이미지, 요약, 키워드가 모두 없습니다.");
            }
        } else {
            android.util.Log.w("PostingActivity", "❌ photoItems가 비어있습니다. 퀴즈 생성 불가.");
        }
        android.util.Log.d("PostingActivity", "========== 퀴즈 생성 체크 완료 ==========");

        Toast.makeText(this, R.string.save, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateAllPhotoItems() {
        // 모든 ViewHolder에서 현재 입력된 값을 가져와서 PhotoItem에 저장
        // RecyclerView의 getChildCount()는 현재 보이는 아이템만 반환하므로
        // 각 child view의 position을 정확히 찾아서 업데이트
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View childView = recyclerView.getChildAt(i);
            if (childView != null) {
                android.widget.EditText etSummary = childView.findViewById(R.id.et_summary);
                android.widget.EditText etKeyword = childView.findViewById(R.id.et_keyword);
                
                if (etSummary != null && etKeyword != null) {
                    // ViewHolder의 position 가져오기
                    RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(childView);
                    if (viewHolder != null) {
                        int position = viewHolder.getAdapterPosition();
                        // RecyclerView.NO_POSITION 체크
                        if (position >= 0 && position < photoItems.size()) {
                            PhotoItem item = photoItems.get(position);
                            String summary = etSummary.getText().toString();
                            String keyword = etKeyword.getText().toString();
                            item.setSummary(summary);
                            item.setKeyword(keyword);
                            android.util.Log.d("PostingActivity", "아이템 " + position + " 업데이트 - 요약: [" + 
                                summary + "], 키워드: [" + keyword + "]");
                        }
                    }
                }
            }
        }
        
        // 디버깅: 모든 photoItems의 현재 상태 로그
        for (int i = 0; i < photoItems.size(); i++) {
            PhotoItem item = photoItems.get(i);
            android.util.Log.d("PostingActivity", "PhotoItem[" + i + "] - 이미지: " + item.getImagePath() + 
                ", 요약: [" + item.getSummary() + "], 키워드: [" + item.getKeyword() + "]");
        }
    }

    private void generateQuizAsync(long logId, String imagePath, String summary, String keyword) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                android.util.Log.d("PostingActivity", "퀴즈 생성 시작 - logId: " + logId);
                GeminiQuizGenerator generator = new GeminiQuizGenerator(PostingActivity.this);
                Quiz quiz = generator.generateQuiz(imagePath, summary, keyword);
                if (quiz != null) {
                    android.util.Log.d("PostingActivity", "========== 퀴즈 저장 시작 ==========");
                    android.util.Log.d("PostingActivity", "Quiz 객체 정보:");
                    android.util.Log.d("PostingActivity", "  - 문제: " + quiz.getQuestion());
                    android.util.Log.d("PostingActivity", "  - 선택지1: " + quiz.getOption1());
                    android.util.Log.d("PostingActivity", "  - 선택지2: " + quiz.getOption2());
                    android.util.Log.d("PostingActivity", "  - 선택지3: " + quiz.getOption3());
                    android.util.Log.d("PostingActivity", "  - 선택지4: " + quiz.getOption4());
                    android.util.Log.d("PostingActivity", "  - 정답: " + quiz.getCorrectAnswer());
                    android.util.Log.d("PostingActivity", "  - 설명: " + quiz.getExplanation());
                    
                    quiz.setStudyLogId(logId);
                    android.util.Log.d("PostingActivity", "StudyLogId 설정: " + logId);
                    android.util.Log.d("PostingActivity", "선택된 날짜: " + selectedDate);
                    
                    long quizId = database.insertQuiz(quiz);
                    android.util.Log.d("PostingActivity", "퀴즈 생성 성공! quizId: " + quizId);
                    
                    // 저장 후 즉시 확인
                    com.example.studylogapp.model.Quiz savedQuiz = database.getQuizByLogId(logId);
                    if (savedQuiz != null) {
                        android.util.Log.d("PostingActivity", "✅ 저장 확인 성공! 저장된 퀴즈 ID: " + savedQuiz.getId());
                        android.util.Log.d("PostingActivity", "저장된 퀴즈 문제: " + savedQuiz.getQuestion());
                    } else {
                        android.util.Log.e("PostingActivity", "❌ 저장 확인 실패! 퀴즈를 찾을 수 없습니다.");
                    }
                    
                    // 퀴즈 생성 완료를 알리는 Broadcast 전송
                    android.content.Intent broadcastIntent = new android.content.Intent("com.example.studylogapp.QUIZ_CREATED");
                    broadcastIntent.putExtra("date", selectedDate);
                    sendBroadcast(broadcastIntent);
                    android.util.Log.d("PostingActivity", "퀴즈 생성 Broadcast 전송: " + selectedDate);
                    android.util.Log.d("PostingActivity", "========== 퀴즈 저장 완료 ==========");
                    
                    runOnUiThread(() -> {
                        Toast.makeText(PostingActivity.this, "퀴즈가 생성되었습니다!", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    android.util.Log.e("PostingActivity", "퀴즈 생성 실패 - generator.generateQuiz()가 null을 반환했습니다.");
                    android.util.Log.e("PostingActivity", "이미지 경로: " + imagePath);
                    android.util.Log.e("PostingActivity", "요약: " + summary);
                    android.util.Log.e("PostingActivity", "키워드: " + keyword);
                    
                    // 실패 원인 상세 로깅
                    if (imagePath == null || imagePath.trim().isEmpty()) {
                        android.util.Log.e("PostingActivity", "이미지 경로가 비어있음");
                    }
                    if (summary == null || summary.trim().isEmpty()) {
                        android.util.Log.e("PostingActivity", "요약이 비어있음");
                    }
                    if (keyword == null || keyword.trim().isEmpty()) {
                        android.util.Log.e("PostingActivity", "키워드가 비어있음");
                    }
                }
            } catch (Exception e) {
                android.util.Log.e("PostingActivity", "퀴즈 생성 중 예외 발생", e);
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

