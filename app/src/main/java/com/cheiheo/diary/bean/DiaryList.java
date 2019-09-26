package com.cheiheo.diary.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cheiheo.diary.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * author:chen hao
 * email::
 * time:2019/09/20
 * desc:
 * version:1.0
 */
public class DiaryList {

    private static DiaryList diaryList;
    //private static List<Diary> diaries;
    private Context context;
    private DatabaseHelper helper;
    private SQLiteDatabase database;

    private DiaryList(Context context) {
        //db
        this.context = context.getApplicationContext();
        helper = new DatabaseHelper(this.context);
        database = helper.getWritableDatabase();
    }

    public static DiaryList getInstance(Context context) {
        if (diaryList == null) {
            diaryList = new DiaryList(context);
        }
        return diaryList;
    }

    private static ContentValues getContentValues(Diary diary) {
        ContentValues values = new ContentValues();
        // values.put("id", diary.getId());
        values.put("title", diary.getTitle());
        values.put("content", diary.getContent());
        values.put("date", diary.getDate());
        values.put("tag", diary.getTag());
        return values;
    }

    public void addDiary(Diary diary) {
        database.insert(DatabaseHelper.DATABASE_TABLE, null, getContentValues(diary));
    }

    public void updateDiary(Diary diary) {
        String id = String.valueOf(diary.getId());
        database.update(DatabaseHelper.DATABASE_TABLE, getContentValues(diary), "id = ?", new String[]{id});
    }

    public void deleteDiary(Diary diary) {
        String id = String.valueOf(diary.getId());
        database.delete(DatabaseHelper.DATABASE_TABLE, "id = ?", new String[]{id});
    }

    public Diary getDiary(int id) {
        Diary diary;
        Cursor cursor = query("id = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        diary = getDiaryFromCursor(cursor);
        return diary;
    }

    public List<Diary> getDiaries() {
        List<Diary> diaries = new ArrayList<>();
        Cursor cursor = query(null, null);
        if (cursor.moveToFirst()) {
            Diary diary;
            do {
                diary = getDiaryFromCursor(cursor);
                diaries.add(diary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return diaries;
    }

    private Cursor query(String where, String[] args) {
        Cursor cursor = database.query(
                DatabaseHelper.DATABASE_TABLE,
                null,
                where, args,
                null, null, null);
        //cursor.close(); 不能关
        return cursor;
    }

    private Diary getDiaryFromCursor(Cursor cursor) {
        Diary diary = new Diary();
        diary.setId(cursor.getInt(cursor.getColumnIndex("id")));
        diary.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        diary.setContent(cursor.getString(cursor.getColumnIndex("content")));
        diary.setDate(cursor.getString(cursor.getColumnIndex("date")));
        diary.setTag(cursor.getString(cursor.getColumnIndex("tag")));
        return diary;
    }


    /**
     * 测试方法
     * 生成diary list
     */
    public static List<Diary> getTestList() {
        List<Diary> diaries = new ArrayList<>();
        Diary diary;
        for (int i=0; i<15; i++) {
            diary = new Diary();
            diary.setDate("date:" + i);
            diary.setTitle("title:" + i);
            diary.setContent("content:" + i);
            diaries.add(diary);
        }
        return diaries;
    }

    public static void createTestListInDB(Context context) {
        DiaryList diaryList = getInstance(context);
        List<Diary> diaries = getTestList();
        for (int i=0; i<diaries.size(); i++) {
            diaryList.addDiary(diaries.get(i));
        }
    }

    public void deleteAll() {
        database.delete(DatabaseHelper.DATABASE_TABLE, null, null);
    }
}
