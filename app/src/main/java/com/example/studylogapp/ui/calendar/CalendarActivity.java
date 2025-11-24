package com.example.studylogapp.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studylogapp.R;
import com.example.studylogapp.database.AppDatabase;
import com.example.studylogapp.model.StudyPost;
import com.example.studylogapp.model.Quiz;
import com.example.studylogapp.ui.posting.PostingActivity;
import com.example.studylogapp.ui.viewer.SlideViewerActivity;
import com.example.studylogapp.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CalendarAdapter adapter;
    private TextView tvMonthYear;
    private Button btnPrevMonth, btnNextMonth;
    private Calendar currentMonth;
    private AppDatabase database;
    
    // 퀴즈 관련 UI
    private LinearLayout quizContainer;
    private TextView tvQuizTitle, tvQuizQuestion, tvQuizResult, tvQuizExplanation;
    private Button btnQuizOption1, btnQuizOption2, btnQuizOption3, btnQuizOption4;
    private Quiz currentQuiz;
    private boolean isQuizAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        database = new AppDatabase(this);
        database.open();

        currentMonth = Calendar.getInstance();
        currentMonth.set(Calendar.DAY_OF_MONTH, 1);

        tvMonthYear = findViewById(R.id.tv_month_year);
        btnPrevMonth = findViewById(R.id.btn_prev_month);
        btnNextMonth = findViewById(R.id.btn_next_month);
        recyclerView = findViewById(R.id.recycler_calendar);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 7));
        adapter = new CalendarAdapter(this, currentMonth, database);
        recyclerView.setAdapter(adapter);

        adapter.setOnDateClickListener(date -> {
            List<StudyPost> posts = database.getPostsByDate(date);
            if (posts != null && !posts.isEmpty()) {
                // 게시물이 있으면 슬라이드 뷰어로 이동
                Intent intent = new Intent(CalendarActivity.this, SlideViewerActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            } else {
                // 게시물이 없으면 작성 화면으로 이동
                Intent intent = new Intent(CalendarActivity.this, PostingActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        btnPrevMonth.setOnClickListener(v -> {
            currentMonth.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        btnNextMonth.setOnClickListener(v -> {
            currentMonth.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        // 퀴즈 UI 초기화
        initQuizUI();

        updateCalendar();
        loadTodayQuiz();
    }

    private void initQuizUI() {
        quizContainer = findViewById(R.id.quiz_container);
        tvQuizTitle = findViewById(R.id.tv_quiz_title);
        tvQuizQuestion = findViewById(R.id.tv_quiz_question);
        tvQuizResult = findViewById(R.id.tv_quiz_result);
        tvQuizExplanation = findViewById(R.id.tv_quiz_explanation);
        btnQuizOption1 = findViewById(R.id.btn_quiz_option1);
        btnQuizOption2 = findViewById(R.id.btn_quiz_option2);
        btnQuizOption3 = findViewById(R.id.btn_quiz_option3);
        btnQuizOption4 = findViewById(R.id.btn_quiz_option4);

        // 디버깅: 퀴즈 UI 요소들이 제대로 찾아졌는지 확인
        if (quizContainer == null) {
            android.util.Log.e("CalendarActivity", "quizContainer를 찾을 수 없습니다!");
        } else {
            android.util.Log.d("CalendarActivity", "quizContainer 찾음, 초기 visibility: " + quizContainer.getVisibility());
            // 초기 상태에서도 보이도록 설정
            quizContainer.setVisibility(View.VISIBLE);
        }

        if (tvQuizTitle == null) {
            android.util.Log.e("CalendarActivity", "tvQuizTitle를 찾을 수 없습니다!");
        }
        if (btnQuizOption1 == null) {
            android.util.Log.e("CalendarActivity", "btnQuizOption1를 찾을 수 없습니다!");
        }

        // 선택지 버튼 클릭 리스너
        if (btnQuizOption1 != null) {
            btnQuizOption1.setOnClickListener(v -> onQuizOptionSelected(1));
        }
        if (btnQuizOption2 != null) {
            btnQuizOption2.setOnClickListener(v -> onQuizOptionSelected(2));
        }
        if (btnQuizOption3 != null) {
            btnQuizOption3.setOnClickListener(v -> onQuizOptionSelected(3));
        }
        if (btnQuizOption4 != null) {
            btnQuizOption4.setOnClickListener(v -> onQuizOptionSelected(4));
        }
    }

    private void loadTodayQuiz() {
        String today = DateUtils.formatDate(Calendar.getInstance());
        android.util.Log.d("CalendarActivity", "========== 퀴즈 로드 시작 ==========");
        android.util.Log.d("CalendarActivity", "오늘 날짜: " + today);
        
        // quizContainer가 null인지 확인
        if (quizContainer == null) {
            android.util.Log.e("CalendarActivity", "loadTodayQuiz: quizContainer가 null입니다!");
            return;
        }
        
        // 데이터베이스에서 StudyLog 먼저 확인
        com.example.studylogapp.model.StudyLog log = database.getStudyLogByDate(today);
        android.util.Log.d("CalendarActivity", "StudyLog 조회 결과: " + (log != null ? "있음 (id: " + log.getId() + ")" : "없음"));
        
        // 퀴즈 조회
        currentQuiz = database.getQuizByDate(today);
        android.util.Log.d("CalendarActivity", "퀴즈 조회 결과: " + (currentQuiz != null ? "있음" : "없음"));
        
        if (currentQuiz != null) {
            android.util.Log.d("CalendarActivity", "퀴즈 상세 정보:");
            android.util.Log.d("CalendarActivity", "  - ID: " + currentQuiz.getId());
            android.util.Log.d("CalendarActivity", "  - StudyLogId: " + currentQuiz.getStudyLogId());
            android.util.Log.d("CalendarActivity", "  - 문제: " + currentQuiz.getQuestion());
            android.util.Log.d("CalendarActivity", "  - 선택지1: " + currentQuiz.getOption1());
            android.util.Log.d("CalendarActivity", "  - 선택지2: " + currentQuiz.getOption2());
            android.util.Log.d("CalendarActivity", "  - 선택지3: " + currentQuiz.getOption3());
            android.util.Log.d("CalendarActivity", "  - 선택지4: " + currentQuiz.getOption4());
            android.util.Log.d("CalendarActivity", "  - 정답: " + currentQuiz.getCorrectAnswer());
            android.util.Log.d("CalendarActivity", "  - 설명: " + currentQuiz.getExplanation());
        } else {
            // 퀴즈가 없는 경우, 데이터베이스에 퀴즈가 있는지 전체 확인
            android.util.Log.d("CalendarActivity", "오늘 날짜의 퀴즈가 없습니다. 데이터베이스 전체 확인 중...");
            if (log != null) {
                Quiz quizByLogId = database.getQuizByLogId(log.getId());
                android.util.Log.d("CalendarActivity", "logId(" + log.getId() + ")로 직접 조회: " + (quizByLogId != null ? "있음" : "없음"));
            }
        }
        
        // 퀴즈 영역은 항상 보이게 설정
        quizContainer.setVisibility(View.VISIBLE);
        android.util.Log.d("CalendarActivity", "quizContainer visibility 설정: " + quizContainer.getVisibility());
        
        if (currentQuiz != null) {
            displayQuiz(currentQuiz);
            android.util.Log.d("CalendarActivity", "✅ 퀴즈 표시 완료");
        } else {
            // 퀴즈가 없을 때 안내 메시지 표시
            if (tvQuizTitle != null) {
                tvQuizTitle.setText("오늘의 복습 퀴즈");
            }
            if (tvQuizQuestion != null) {
                tvQuizQuestion.setText("아직 퀴즈가 없습니다.\n게시물을 저장하면 퀴즈가 자동으로 생성됩니다.");
            }
            
            // 선택지 버튼 숨기기
            if (btnQuizOption1 != null) btnQuizOption1.setVisibility(View.GONE);
            if (btnQuizOption2 != null) btnQuizOption2.setVisibility(View.GONE);
            if (btnQuizOption3 != null) btnQuizOption3.setVisibility(View.GONE);
            if (btnQuizOption4 != null) btnQuizOption4.setVisibility(View.GONE);
            if (tvQuizResult != null) tvQuizResult.setVisibility(View.GONE);
            if (tvQuizExplanation != null) tvQuizExplanation.setVisibility(View.GONE);
            
            android.util.Log.d("CalendarActivity", "❌ 퀴즈 없음 - 안내 메시지 표시");
        }
        android.util.Log.d("CalendarActivity", "========== 퀴즈 로드 완료 ==========");
    }

    private void displayQuiz(Quiz quiz) {
        // 모든 UI 요소 보이게 설정
        btnQuizOption1.setVisibility(View.VISIBLE);
        btnQuizOption2.setVisibility(View.VISIBLE);
        btnQuizOption3.setVisibility(View.VISIBLE);
        btnQuizOption4.setVisibility(View.VISIBLE);
        
        tvQuizTitle.setText("오늘의 복습 퀴즈");
        tvQuizQuestion.setText(quiz.getQuestion());
        btnQuizOption1.setText("1. " + quiz.getOption1());
        btnQuizOption2.setText("2. " + quiz.getOption2());
        btnQuizOption3.setText("3. " + quiz.getOption3());
        btnQuizOption4.setText("4. " + quiz.getOption4());
        
        // 초기화
        isQuizAnswered = false;
        tvQuizResult.setVisibility(View.GONE);
        tvQuizExplanation.setVisibility(View.GONE);
        
        // 버튼 활성화
        btnQuizOption1.setEnabled(true);
        btnQuizOption2.setEnabled(true);
        btnQuizOption3.setEnabled(true);
        btnQuizOption4.setEnabled(true);
        
        // 버튼 색상 초기화
        resetButtonColors();
    }

    private void onQuizOptionSelected(int selectedAnswer) {
        if (isQuizAnswered || currentQuiz == null) {
            return;
        }

        isQuizAnswered = true;
        int correctAnswer = currentQuiz.getCorrectAnswer();
        boolean isCorrect = selectedAnswer == correctAnswer;

        // 버튼 비활성화
        btnQuizOption1.setEnabled(false);
        btnQuizOption2.setEnabled(false);
        btnQuizOption3.setEnabled(false);
        btnQuizOption4.setEnabled(false);

        // 결과 표시
        if (isCorrect) {
            tvQuizResult.setText("정답입니다! ✓");
            tvQuizResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            highlightCorrectAnswer(correctAnswer);
        } else {
            tvQuizResult.setText("틀렸습니다. 정답은 " + correctAnswer + "번입니다.");
            tvQuizResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            highlightCorrectAnswer(correctAnswer);
            highlightWrongAnswer(selectedAnswer);
        }

        tvQuizResult.setVisibility(View.VISIBLE);
        
        if (currentQuiz.getExplanation() != null && !currentQuiz.getExplanation().isEmpty()) {
            tvQuizExplanation.setText("설명: " + currentQuiz.getExplanation());
            tvQuizExplanation.setVisibility(View.VISIBLE);
        }
    }

    private void highlightCorrectAnswer(int answer) {
        Button correctButton = getButtonByAnswer(answer);
        if (correctButton != null) {
            correctButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
    }

    private void highlightWrongAnswer(int answer) {
        Button wrongButton = getButtonByAnswer(answer);
        if (wrongButton != null) {
            wrongButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private void resetButtonColors() {
        btnQuizOption1.setBackgroundColor(getResources().getColor(android.R.color.white));
        btnQuizOption2.setBackgroundColor(getResources().getColor(android.R.color.white));
        btnQuizOption3.setBackgroundColor(getResources().getColor(android.R.color.white));
        btnQuizOption4.setBackgroundColor(getResources().getColor(android.R.color.white));
    }

    private Button getButtonByAnswer(int answer) {
        switch (answer) {
            case 1: return btnQuizOption1;
            case 2: return btnQuizOption2;
            case 3: return btnQuizOption3;
            case 4: return btnQuizOption4;
            default: return null;
        }
    }

    private void updateCalendar() {
        tvMonthYear.setText(DateUtils.formatMonthYear(currentMonth));
        adapter.updateMonth(currentMonth);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        // 퀴즈 다시 로드 (새로 생성되었을 수 있음)
        loadTodayQuiz();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        // 퀴즈 생성 완료 Broadcast 수신을 위한 Receiver 등록
        // Android 13+ (API 33+)에서는 RECEIVER_EXPORTED 또는 RECEIVER_NOT_EXPORTED 플래그 필요
        android.content.IntentFilter filter = new android.content.IntentFilter("com.example.studylogapp.QUIZ_CREATED");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(quizCreatedReceiver, filter, android.content.Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(quizCreatedReceiver, filter);
        }
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        // Receiver 해제
        try {
            unregisterReceiver(quizCreatedReceiver);
        } catch (Exception e) {
            // Receiver가 등록되지 않았을 수 있음
        }
    }
    
    // 퀴즈 생성 완료 Broadcast Receiver
    private android.content.BroadcastReceiver quizCreatedReceiver = new android.content.BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            android.util.Log.d("CalendarActivity", "========== Broadcast 수신 ==========");
            String date = intent.getStringExtra("date");
            String today = DateUtils.formatDate(Calendar.getInstance());
            android.util.Log.d("CalendarActivity", "퀴즈 생성 Broadcast 수신 - date: " + date + ", today: " + today);
            
            // 오늘 날짜의 퀴즈가 생성되었으면 새로고침
            if (today.equals(date)) {
                android.util.Log.d("CalendarActivity", "✅ 날짜 일치! 오늘의 퀴즈가 생성되었으므로 새로고침");
                // 약간의 지연을 두고 새로고침 (데이터베이스 쓰기 완료 대기)
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    android.util.Log.d("CalendarActivity", "Broadcast 수신 후 퀴즈 새로고침 실행");
                    loadTodayQuiz();
                }, 500);
            } else {
                android.util.Log.d("CalendarActivity", "날짜 불일치 - date: " + date + ", today: " + today);
            }
            android.util.Log.d("CalendarActivity", "========== Broadcast 처리 완료 ==========");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}

