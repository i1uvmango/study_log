package com.example.studylogapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.studylogapp.model.StudyLog;
import com.example.studylogapp.model.StudyPost;

import java.util.ArrayList;
import java.util.List;

public class AppDatabase {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public AppDatabase(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // StudyLog CRUD
    public long insertStudyLog(StudyLog studyLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LOG_DATE, studyLog.getDate());
        values.put(DatabaseHelper.COLUMN_LOG_CREATED_AT, studyLog.getCreatedAt());
        values.put(DatabaseHelper.COLUMN_LOG_UPDATED_AT, studyLog.getUpdatedAt());
        return database.insert(DatabaseHelper.TABLE_STUDY_LOG, null, values);
    }

    public StudyLog getStudyLogByDate(String date) {
        Cursor cursor = database.query(
            DatabaseHelper.TABLE_STUDY_LOG,
            null,
            DatabaseHelper.COLUMN_LOG_DATE + " = ?",
            new String[]{date},
            null, null, null
        );

        StudyLog studyLog = null;
        if (cursor != null && cursor.moveToFirst()) {
            studyLog = cursorToStudyLog(cursor);
            cursor.close();
        }
        return studyLog;
    }

    public long getOrCreateStudyLogId(String date) {
        StudyLog existing = getStudyLogByDate(date);
        if (existing != null) {
            return existing.getId();
        }
        
        StudyLog newLog = new StudyLog(date);
        return insertStudyLog(newLog);
    }

    public void updateStudyLog(StudyLog studyLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LOG_UPDATED_AT, System.currentTimeMillis());
        database.update(
            DatabaseHelper.TABLE_STUDY_LOG,
            values,
            DatabaseHelper.COLUMN_LOG_ID + " = ?",
            new String[]{String.valueOf(studyLog.getId())}
        );
    }

    public void deleteStudyLog(String date) {
        database.delete(
            DatabaseHelper.TABLE_STUDY_LOG,
            DatabaseHelper.COLUMN_LOG_DATE + " = ?",
            new String[]{date}
        );
    }

    // StudyPost CRUD
    public long insertStudyPost(StudyPost post) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_POST_STUDY_LOG_ID, post.getStudyLogId());
        values.put(DatabaseHelper.COLUMN_POST_PHOTO_URI, post.getPhotoUri());
        values.put(DatabaseHelper.COLUMN_POST_SUMMARY, post.getSummary());
        values.put(DatabaseHelper.COLUMN_POST_KEYWORD, post.getKeyword());
        values.put(DatabaseHelper.COLUMN_POST_ORDER, post.getOrder());
        return database.insert(DatabaseHelper.TABLE_STUDY_POST, null, values);
    }

    public List<StudyPost> getPostsByLogId(long logId) {
        List<StudyPost> posts = new ArrayList<>();
        Cursor cursor = database.query(
            DatabaseHelper.TABLE_STUDY_POST,
            null,
            DatabaseHelper.COLUMN_POST_STUDY_LOG_ID + " = ?",
            new String[]{String.valueOf(logId)},
            null, null,
            DatabaseHelper.COLUMN_POST_ORDER + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                posts.add(cursorToStudyPost(cursor));
            }
            cursor.close();
        }
        return posts;
    }

    public List<StudyPost> getPostsByDate(String date) {
        StudyLog log = getStudyLogByDate(date);
        if (log == null) {
            return new ArrayList<>();
        }
        return getPostsByLogId(log.getId());
    }

    public void updateStudyPost(StudyPost post) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_POST_PHOTO_URI, post.getPhotoUri());
        values.put(DatabaseHelper.COLUMN_POST_SUMMARY, post.getSummary());
        values.put(DatabaseHelper.COLUMN_POST_KEYWORD, post.getKeyword());
        values.put(DatabaseHelper.COLUMN_POST_ORDER, post.getOrder());
        database.update(
            DatabaseHelper.TABLE_STUDY_POST,
            values,
            DatabaseHelper.COLUMN_POST_ID + " = ?",
            new String[]{String.valueOf(post.getId())}
        );
    }

    public void deleteStudyPost(long postId) {
        database.delete(
            DatabaseHelper.TABLE_STUDY_POST,
            DatabaseHelper.COLUMN_POST_ID + " = ?",
            new String[]{String.valueOf(postId)}
        );
    }

    public void deleteAllPostsByLogId(long logId) {
        database.delete(
            DatabaseHelper.TABLE_STUDY_POST,
            DatabaseHelper.COLUMN_POST_STUDY_LOG_ID + " = ?",
            new String[]{String.valueOf(logId)}
        );
    }

    // 월별 데이터 조회
    public List<String> getDatesWithPosts(int year, int month) {
        List<String> dates = new ArrayList<>();
        String monthStr = String.format("%02d", month);
        String pattern = year + "-" + monthStr + "-%";
        
        Cursor cursor = database.query(
            DatabaseHelper.TABLE_STUDY_LOG,
            new String[]{DatabaseHelper.COLUMN_LOG_DATE},
            DatabaseHelper.COLUMN_LOG_DATE + " LIKE ?",
            new String[]{pattern},
            null, null,
            DatabaseHelper.COLUMN_LOG_DATE + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                dates.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOG_DATE)));
            }
            cursor.close();
        }
        return dates;
    }

    public String getThumbnailUri(String date) {
        List<StudyPost> posts = getPostsByDate(date);
        if (posts != null && !posts.isEmpty()) {
            return posts.get(0).getPhotoUri();
        }
        return null;
    }

    public void clearAllData() {
        database.delete(DatabaseHelper.TABLE_STUDY_POST, null, null);
        database.delete(DatabaseHelper.TABLE_STUDY_LOG, null, null);
    }

    // Helper methods
    private StudyLog cursorToStudyLog(Cursor cursor) {
        StudyLog log = new StudyLog();
        log.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOG_ID)));
        log.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOG_DATE)));
        log.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOG_CREATED_AT)));
        log.setUpdatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOG_UPDATED_AT)));
        return log;
    }

    private StudyPost cursorToStudyPost(Cursor cursor) {
        StudyPost post = new StudyPost();
        post.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POST_ID)));
        post.setStudyLogId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POST_STUDY_LOG_ID)));
        post.setPhotoUri(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POST_PHOTO_URI)));
        post.setSummary(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POST_SUMMARY)));
        post.setKeyword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POST_KEYWORD)));
        post.setOrder(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POST_ORDER)));
        return post;
    }
}


