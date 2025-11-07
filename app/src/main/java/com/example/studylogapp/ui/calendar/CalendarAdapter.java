package com.example.studylogapp.ui.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studylogapp.R;
import com.example.studylogapp.database.AppDatabase;
import com.example.studylogapp.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private List<CalendarDay> days;
    private Calendar currentMonth;
    private AppDatabase database;
    private OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(String date);
    }

    public CalendarAdapter(android.content.Context context, Calendar month, AppDatabase database) {
        this.currentMonth = (Calendar) month.clone();
        this.database = database;
        this.days = new ArrayList<>();
        generateDays();
    }

    public void setOnDateClickListener(OnDateClickListener listener) {
        this.listener = listener;
    }

    public void updateMonth(Calendar month) {
        this.currentMonth = (Calendar) month.clone();
        generateDays();
        notifyDataSetChanged();
    }

    private void generateDays() {
        days.clear();
        
        // 요일 헤더 추가
        String[] weekDays = {"일", "월", "화", "수", "목", "금", "토"};
        for (String day : weekDays) {
            days.add(new CalendarDay(day, true, false, false, null));
        }

        Calendar firstDay = DateUtils.getFirstDayOfMonth(currentMonth);
        int firstDayOfWeek = DateUtils.getFirstDayOfWeek(currentMonth);
        int daysInMonth = DateUtils.getDaysInMonth(currentMonth);

        // 빈 셀 추가
        for (int i = 0; i < firstDayOfWeek; i++) {
            days.add(new CalendarDay("", false, false, false, null));
        }

        // 날짜 셀 추가
        List<String> datesWithPosts = database.getDatesWithPosts(
            currentMonth.get(Calendar.YEAR),
            currentMonth.get(Calendar.MONTH) + 1
        );

        Calendar today = Calendar.getInstance();
        for (int i = 1; i <= daysInMonth; i++) {
            Calendar day = (Calendar) firstDay.clone();
            day.set(Calendar.DAY_OF_MONTH, i);
            String dateStr = DateUtils.formatDate(day);
            boolean hasPost = datesWithPosts.contains(dateStr);
            boolean isToday = DateUtils.isToday(day);
            String thumbnailUri = hasPost ? database.getThumbnailUri(dateStr) : null;
            
            days.add(new CalendarDay(String.valueOf(i), false, isToday, hasPost, thumbnailUri));
        }
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_calendar_day, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        CalendarDay day = days.get(position);
        holder.bind(day);
        
        if (!day.isHeader() && day.getDate() != null && !day.getDate().isEmpty()) {
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    Calendar dayCalendar = DateUtils.getFirstDayOfMonth(currentMonth);
                    int dayOfMonth = Integer.parseInt(day.getDate());
                    dayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String dateStr = DateUtils.formatDate(dayCalendar);
                    listener.onDateClick(dateStr);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}

