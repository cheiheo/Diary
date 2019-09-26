package com.cheiheo.diary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * author:chen hao
 * email::
 * time:2019/09/20
 * desc:
 * version:1.0
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * 默认数据库名与版本号
     */
    public static final String DATABASE_NAME = "diary.db";
    public static final String DATABASE_TABLE = "Diary";
    public static final int VERSION = 1;
    public static final String CREATE_DIARY = "create table Diary("
            + "id integer primary key autoincrement,"
            + "date text,"
            + "title text,"
            + "content text,"
            + "tag text)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists Diary");
        onCreate(sqLiteDatabase);
    }
}
