package me.zuichu.androidsuit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.zuichu.androidsuit.entity.DownLoadEntity;

public class DBManager {
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String tableName = "download";

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public DownLoadEntity getDEntity(int downloadId) {
        Cursor cursor = sqLiteDatabase.query(tableName, null, "downloadId=?", new String[]{downloadId + ""}, null, null, null);
        DownLoadEntity dEntity = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("downloadId"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            long startPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("startPosition")));
            long endPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("endPosition")));
            long fileSize = Long.parseLong(cursor.getString(cursor.getColumnIndex("fileSize")));
            String localPath = cursor.getString(cursor.getColumnIndex("localFilePath"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            dEntity = new DownLoadEntity.Builder().threadId(id).url(url).startPosition(startPosition).endPosition(endPosition)
                    .fileSize(fileSize).tempFile(new File(localPath)).status(status).build();
        }
        return dEntity;
    }

    public DownLoadEntity getDEntityNeedDownLoad(int downloadId) {
        Cursor cursor = sqLiteDatabase.query(tableName, null, "downloadId=? and status!=?", new String[]{downloadId + "", "0"}, null, null, null);
        DownLoadEntity dEntity = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("downloadId"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            long startPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("startPosition")));
            long endPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("endPosition")));
            long fileSize = Long.parseLong(cursor.getString(cursor.getColumnIndex("fileSize")));
            String localPath = cursor.getString(cursor.getColumnIndex("localFilePath"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            dEntity = new DownLoadEntity.Builder().threadId(id).url(url).startPosition(startPosition).endPosition(endPosition)
                    .fileSize(fileSize).tempFile(new File(localPath)).status(status).build();
        }
        return dEntity;
    }

    public void insertDEntity(DownLoadEntity dEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadId", dEntity.getThreadId());
        contentValues.put("startPosition", dEntity.getStartPosition() + "");
        contentValues.put("endPosition", dEntity.getEndPosition() + "");
        contentValues.put("url", dEntity.getUrl());
        contentValues.put("fileSize", dEntity.getFileSize() + "");
        contentValues.put("localFilePath", dEntity.getTempFile().getPath());
        contentValues.put("status", dEntity.getStatus());
        sqLiteDatabase.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateDEntity(DownLoadEntity dEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadId", dEntity.getThreadId());
        contentValues.put("startPosition", dEntity.getStartPosition() + "");
        contentValues.put("endPosition", dEntity.getEndPosition() + "");
        contentValues.put("url", dEntity.getUrl());
        contentValues.put("fileSize", dEntity.getFileSize() + "");
        contentValues.put("localFilePath", dEntity.getTempFile().getPath());
        contentValues.put("status", dEntity.getStatus());
        sqLiteDatabase.updateWithOnConflict(tableName, contentValues, "downloadId=?", new String[]{dEntity.getThreadId() + ""}, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateDProgress(int downloadId, long position) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("startPosition", position + "");
        sqLiteDatabase.updateWithOnConflict(tableName, contentValues, "downloadId=?", new String[]{downloadId + ""}, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateDStatus(int downloadId, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        sqLiteDatabase.updateWithOnConflict(tableName, contentValues, "downloadId=?", new String[]{downloadId + ""}, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public List<DownLoadEntity> getDEntityList() {
        List<DownLoadEntity> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("downloadId"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            long startPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("startPosition")));
            long endPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("endPosition")));
            long fileSize = Long.parseLong(cursor.getString(cursor.getColumnIndex("fileSize")));
            String localPath = cursor.getString(cursor.getColumnIndex("localFilePath"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            DownLoadEntity dEntity = new DownLoadEntity.Builder().threadId(id).url(url).startPosition(startPosition).endPosition(endPosition)
                    .fileSize(fileSize).tempFile(new File(localPath)).status(status).build();
            list.add(dEntity);
        }
        return list;
    }

    public void deleteDEntity(int downloadId) {
        sqLiteDatabase.delete(tableName, "downloadId=?", new String[]{downloadId + ""});
    }

    public void deleteAllDEntity() {
        sqLiteDatabase.delete(tableName, null, null);
    }

    public void deleteDEntity() {
        sqLiteDatabase.delete(tableName, "status=?", new String[]{"0"});
    }

    public void cloaseDB() {
        dbHelper.close();
    }
}
