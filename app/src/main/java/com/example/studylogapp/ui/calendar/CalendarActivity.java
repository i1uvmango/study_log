package com.example.studylogapp.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studylogapp.R;
import com.example.studylogapp.database.AppDatabase;
import com.example.studylogapp.model.StudyPost;
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

        updateCalendar();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}

