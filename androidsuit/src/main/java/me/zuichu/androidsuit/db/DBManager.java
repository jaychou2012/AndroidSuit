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

    public DownLoadEntity getDEntity(String signId) {
        Cursor cursor = sqLiteDatabase.query(tableName, null, "signId=?", new String[]{signId}, null, null, null);
        DownLoadEntity dEntity = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("downloadId"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            long startPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("startPosition")));
            long endPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("endPosition")));
            long currentPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("currentPosition")));
            long fileSize = Long.parseLong(cursor.getString(cursor.getColumnIndex("fileSize")));
            String localPath = cursor.getString(cursor.getColumnIndex("localFilePath"));
            String signIdString = cursor.getString(cursor.getColumnIndex("signId"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            dEntity = new DownLoadEntity.Builder().threadId(id).url(url).startPosition(startPosition).endPosition(endPosition)
                    .currentPosition(currentPosition).fileSize(fileSize).tempFile(new File(localPath)).signId(signIdString).status(status).build();
        }
        return dEntity;
    }

    public void insertDEntity(DownLoadEntity dEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadId", dEntity.getThreadId());
        contentValues.put("startPosition", dEntity.getStartPosition() + "");
        contentValues.put("endPosition", dEntity.getEndPosition() + "");
        contentValues.put("currentPosition", dEntity.getCurrentPosition() + "");
        contentValues.put("url", dEntity.getUrl());
        contentValues.put("fileSize", dEntity.getFileSize() + "");
        contentValues.put("localFilePath", dEntity.getTempFile().getPath());
        contentValues.put("status", dEntity.getStatus());
        contentValues.put("signId", dEntity.getSignId());
        sqLiteDatabase.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateDEntity(DownLoadEntity dEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadId", dEntity.getThreadId());
        contentValues.put("startPosition", dEntity.getStartPosition() + "");
        contentValues.put("endPosition", dEntity.getEndPosition() + "");
        contentValues.put("currentPosition", dEntity.getCurrentPosition() + "");
        contentValues.put("url", dEntity.getUrl());
        contentValues.put("fileSize", dEntity.getFileSize() + "");
        contentValues.put("localFilePath", dEntity.getTempFile().getPath());
        contentValues.put("status", dEntity.getStatus());
        contentValues.put("signId", dEntity.getSignId());
        sqLiteDatabase.updateWithOnConflict(tableName, contentValues, "signId=?", new String[]{dEntity.getSignId() + ""}, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateDProgress(int downloadId, String signId, long position) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("currentPosition", position + "");
        sqLiteDatabase.updateWithOnConflict(tableName, contentValues, "signId=? and downloadId=?", new String[]{signId, downloadId + ""}, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateDStatus(int downloadId, String signId, int status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        sqLiteDatabase.updateWithOnConflict(tableName, contentValues, "signId=? and downloadId=?", new String[]{signId, downloadId + ""},
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public List<DownLoadEntity> getDEntityList() {
        List<DownLoadEntity> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("downloadId"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            long startPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("startPosition")));
            long endPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("endPosition")));
            long currentPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("currentPosition")));
            long fileSize = Long.parseLong(cursor.getString(cursor.getColumnIndex("fileSize")));
            String localPath = cursor.getString(cursor.getColumnIndex("localFilePath"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            String signId = cursor.getString(cursor.getColumnIndex("signId"));
            DownLoadEntity dEntity = new DownLoadEntity.Builder().threadId(id).url(url).startPosition(startPosition).endPosition(endPosition)
                    .currentPosition(currentPosition).fileSize(fileSize).tempFile(new File(localPath)).signId(signId).status(status).build();
            list.add(dEntity);
        }
        return list;
    }


    public List<DownLoadEntity> getDEntityListBySignId(String signId) {
        List<DownLoadEntity> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(tableName, null, "signId=? and status=?", new String[]{signId, "1"}, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("downloadId"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            long startPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("startPosition")));
            long endPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("endPosition")));
            long currentPosition = Long.parseLong(cursor.getString(cursor.getColumnIndex("currentPosition")));
            long fileSize = Long.parseLong(cursor.getString(cursor.getColumnIndex("fileSize")));
            String localPath = cursor.getString(cursor.getColumnIndex("localFilePath"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            String signIdString = cursor.getString(cursor.getColumnIndex("signId"));
            DownLoadEntity dEntity = new DownLoadEntity.Builder().threadId(id).url(url).startPosition(startPosition).endPosition(endPosition)
                    .currentPosition(currentPosition).fileSize(fileSize).tempFile(new File(localPath)).signId(signIdString).status(status).build();
            list.add(dEntity);
        }
        return list;
    }

    public void deleteDEntity(String signId) {
        sqLiteDatabase.delete(tableName, "signId=?", new String[]{signId});
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
