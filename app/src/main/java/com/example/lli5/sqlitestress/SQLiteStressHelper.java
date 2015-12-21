package com.example.lli5.sqlitestress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by lli5 on 11/18/15.
 */
public class SQLiteStressHelper extends SQLiteOpenHelper {
    private static final String TAGS = "SQLiteStress";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "counts";
    private static final String KEY_KEY =  "key";
    private static final String KEY_COUNT = "count";
    private static final String[] COLUMNS = {KEY_KEY, KEY_COUNT};
    private final int id;
    SQLiteDatabase db = null;

    public SQLiteStressHelper(Context context, int id) {
        super(context, "stress" + id + ".db", null, DATABASE_VERSION);
        this.id = id;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STRESS_TABLE = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                KEY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_COUNT + " INTEGER )";
        db.execSQL(CREATE_STRESS_TABLE);
        Log.d(TAGS, "#" + id + " create db: " + TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase(boolean writable) {
        if (writable == true) {
            db = getWritableDatabase();
            Log.d(TAGS, "#" + id + " getWritableDatabase()");
        } else {
            db = getReadableDatabase();
            Log.d(TAGS, "#" + id + " getReadableDatabase()");
        }
    }

    public void closeDatabase() {
        if (db != null) {
            db.close();
            Log.d(TAGS, "#" + id + " closeDatabase()");
        }
    }

    public int getCount() {
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
        if (cursor == null) {
            Log.w(TAGS, "#" + id + " db.query() returns null");
            return 0;
        }

        int cursor_count = cursor.getCount();
        Log.w(TAGS, "#" + id + " cursor.getCount() returns " + cursor_count);

        if (cursor_count == 0) {
            return 0;
        }

        cursor.moveToLast();

        int key = cursor.getInt(0);
        int count = cursor.getInt(1);
        cursor.close();
        Log.d(TAGS, "#" + id + " getCount: " + count + " key: " + key);

        return count;
    }

    public void setCount(int count) {
        ContentValues values = new ContentValues();
        values.put(KEY_COUNT, count);
        if (db.insert(TABLE_NAME, null, values) == -1) {
            Log.e(TAGS, "#" + id + "failed to insert new value: " + count);
        } else {
            Log.d(TAGS, "#" + id + " setCount: " + count);
        }
    }
}
