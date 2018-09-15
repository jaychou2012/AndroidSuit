package me.zuichu.androidsuit.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import me.zuichu.androidsuit.entity.DownLoadEntity;

public class DRunnable implements Runnable {
    private Context context;
    private DownLoadEntity dEntity;
    private Handler handler;
    private boolean pause = false;
    private final int TIME_OUT = 30 * 1000;

    public DRunnable(Context context, DownLoadEntity entity, Handler handler) {
        this.context = context;
        this.dEntity = entity;
        this.handler = handler;
        pause = false;
    }

    @Override
    public void run() {
        HTTPSTrustManager.allowAllSSL();
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(dEntity.getUrl()).openConnection();
            conn.setRequestProperty("Range", "bytes=" + dEntity.getStartPosition() + "-" + dEntity.getEndPosition());
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIME_OUT);
            conn.setReadTimeout(TIME_OUT);
            RandomAccessFile accessFile = new RandomAccessFile(dEntity.getTempFile(), "rwd");
            accessFile.seek(dEntity.getStartPosition());
            int responseCode = conn.getResponseCode();
            byte[] buffer = new byte[1024];
            int len;
            long currentPosition = 0;
            InputStream is = conn.getInputStream();
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                if (pause) {
                    Message message = handler.obtainMessage(DPoolImpl.MESSAGE_PAUSE);
                    handler.sendMessage(message);
                    break;
                }
                accessFile.write(buffer, 0, len);
                currentPosition += len;
                Message message = handler.obtainMessage(DPoolImpl.MESSAGE_PROGRESS, dEntity.getThreadId(), (int) currentPosition);
                handler.sendMessage(message);
            }
            Message message = handler.obtainMessage(DPoolImpl.MESSAGE_COMPLETE, dEntity.getThreadId(), 0);
            handler.sendMessage(message);
            accessFile.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            Message message = handler.obtainMessage(DPoolImpl.MESSAGE_STOP, dEntity.getThreadId(), 0, e.getLocalizedMessage());
            handler.sendMessage(message);
        }
    }

    public void setPauseOrResume(boolean pause) {
        this.pause = pause;
    }
}
