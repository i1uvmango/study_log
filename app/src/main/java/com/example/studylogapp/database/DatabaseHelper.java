package com.example.studylogapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "studylog.db";
    private static final int DATABASE_VERSION = 3;

    // StudyLog 테이블
    public static final String TABLE_STUDY_LOG = "StudyLog";
    public static final String COLUMN_LOG_ID = "id";
    public static final String COLUMN_LOG_DATE = "date";
    public static final String COLUMN_LOG_CREATED_AT = "createdAt";
    public static final String COLUMN_LOG_UPDATED_AT = "updatedAt";

    // StudyPost 테이블
    public static final String TABLE_STUDY_POST = "StudyPost";
    public static final String COLUMN_POST_ID = "id";
    public static final String COLUMN_POST_STUDY_LOG_ID = "studyLogId";
    public static final String COLUMN_POST_PHOTO_URI = "photoUri";
    public static final String COLUMN_POST_SUMMARY = "summary";
    public static final String COLUMN_POST_KEYWORD = "keyword";
    public static final String COLUMN_POST_ORDER = "displayOrder";

    // Quiz 테이블
    public static final String TABLE_QUIZ = "Quiz";
    public static final String COLUMN_QUIZ_ID = "id";
    public static final String COLUMN_QUIZ_STUDY_LOG_ID = "studyLogId";
    public static final String COLUMN_QUIZ_QUESTION = "question";
    public static final String COLUMN_QUIZ_OPTION1 = "option1";
    public static final String COLUMN_QUIZ_OPTION2 = "option2";
    public static final String COLUMN_QUIZ_OPTION3 = "option3";
    public static final String COLUMN_QUIZ_OPTION4 = "option4";
    public static final String COLUMN_QUIZ_CORRECT_ANSWER = "correctAnswer";
    public static final String COLUMN_QUIZ_EXPLANATION = "explanation";

    private static final String CREATE_TABLE_STUDY_LOG = 
        "CREATE TABLE " + TABLE_STUDY_LOG + " (" +
        COLUMN_LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_LOG_DATE + " TEXT UNIQUE NOT NULL, " +
        COLUMN_LOG_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
        COLUMN_LOG_UPDATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ")";

    private static final String CREATE_TABLE_STUDY_POST = 
        "CREATE TABLE " + TABLE_STUDY_POST + " (" +
        COLUMN_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_POST_STUDY_LOG_ID + " INTEGER NOT NULL, " +
        COLUMN_POST_PHOTO_URI + " TEXT NOT NULL, " +
        COLUMN_POST_SUMMARY + " TEXT, " +
        COLUMN_POST_KEYWORD + " TEXT, " +
        COLUMN_POST_ORDER + " INTEGER NOT NULL, " +
        "FOREIGN KEY(" + COLUMN_POST_STUDY_LOG_ID + ") REFERENCES " + 
        TABLE_STUDY_LOG + "(" + COLUMN_LOG_ID + ") ON DELETE CASCADE" +
        ")";

    private static final String CREATE_TABLE_QUIZ = 
        "CREATE TABLE " + TABLE_QUIZ + " (" +
        COLUMN_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_QUIZ_STUDY_LOG_ID + " INTEGER NOT NULL, " +
        COLUMN_QUIZ_QUESTION + " TEXT NOT NULL, " +
        COLUMN_QUIZ_OPTION1 + " TEXT NOT NULL, " +
        COLUMN_QUIZ_OPTION2 + " TEXT NOT NULL, " +
        COLUMN_QUIZ_OPTION3 + " TEXT NOT NULL, " +
        COLUMN_QUIZ_OPTION4 + " TEXT NOT NULL, " +
        COLUMN_QUIZ_CORRECT_ANSWER + " INTEGER NOT NULL, " +
        COLUMN_QUIZ_EXPLANATION + " TEXT, " +
        "FOREIGN KEY(" + COLUMN_QUIZ_STUDY_LOG_ID + ") REFERENCES " + 
        TABLE_STUDY_LOG + "(" + COLUMN_LOG_ID + ") ON DELETE CASCADE" +
        ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDY_LOG);
        db.execSQL(CREATE_TABLE_STUDY_POST);
        db.execSQL(CREATE_TABLE_QUIZ);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            // Quiz 테이블 추가
            db.execSQL(CREATE_TABLE_QUIZ);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}


