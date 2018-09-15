package me.zuichu.androidsuit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private final String DB_NAME = "download.db";
    private final int DB_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, "download.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists download (downloadId Integer,fileSize Text, url Text, startPosition Text" +
                ", endPosition Text, localFilePath Text,status Integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
