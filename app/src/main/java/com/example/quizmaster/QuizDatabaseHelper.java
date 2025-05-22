package com.example.quizmaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuizDatabaseHelper extends SQLiteOpenHelper {

    // Datenbank-Name und -Version
    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 1;

    // Tabellen- und Spaltennamen
    public static final String TABLE_QUESTIONS = "questions";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_OPTION_A = "option_a";
    public static final String COLUMN_OPTION_B = "option_b";
    public static final String COLUMN_OPTION_C = "option_c";
    public static final String COLUMN_OPTION_D = "option_d";
    public static final String COLUMN_CORRECT_ANSWER = "correct_answer";

    // SQL-Befehl zum Erstellen der Tabelle
    private static final String CREATE_TABLE_QUESTIONS =
            "CREATE TABLE " + TABLE_QUESTIONS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_QUESTION + " TEXT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_OPTION_A + " TEXT, " +
                    COLUMN_OPTION_B + " TEXT, " +
                    COLUMN_OPTION_C + " TEXT, " +
                    COLUMN_OPTION_D + " TEXT, " +
                    COLUMN_CORRECT_ANSWER + " TEXT" +
                    ");";

    public QuizDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUESTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(db);
    }

    // Methode zum Einfügen einer Frage
    public void addQuestion(String question, String type, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, question);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_OPTION_A, optionA);
        values.put(COLUMN_OPTION_B, optionB);
        values.put(COLUMN_OPTION_C, optionC);
        values.put(COLUMN_OPTION_D, optionD);
        values.put(COLUMN_CORRECT_ANSWER, correctAnswer);

        db.insert(TABLE_QUESTIONS, null, values);
        db.close();
    }
}
