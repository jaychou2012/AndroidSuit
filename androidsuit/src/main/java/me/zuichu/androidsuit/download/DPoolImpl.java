package me.zuichu.androidsuit.download;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import me.zuichu.androidsuit.db.DBManager;
import me.zuichu.androidsuit.entity.DownLoadEntity;

public class DPoolImpl implements DPoolDao {
    private Context context;
    private String downloadUrl = "";
    private String fileSaveUrl = "";
    private int contentLength = 0;
    private int threadCount = 6;
    //下载相关
    private ExecutorService executorService;
    private DThreadFactory dThreadFactory;
    private List<DRunnable> list;
    private List<DownLoadEntity> entityList;
    private List<Integer> threadList;
    private DBManager dbManager;
    private final int TIME_OUT = 20 * 1000;
    private final String METHOD = "GET";
    private long begin, complete;
    private OnDpoolListener onDpoolListener;
    private String TAG = "开始下载：";
    public static final int MESSAGE_GET_LENGTH = 0;
    public static final int MESSAGE_PROGRESS = 1;
    public static final int MESSAGE_COMPLETE = 2;
    public static final int MESSAGE_PAUSE = 3;
    public static final int MESSAGE_STOP = -1;

    private DPoolImpl(Builder builder) {
        context = builder.context;
        downloadUrl = builder.downloadUrl;
        fileSaveUrl = builder.fileSaveUrl;
        contentLength = builder.contentLength;
        threadCount = builder.threadCount;
    }

    private void getHttpFileLength() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                URL url = null;
                try {
                    url = new URL(downloadUrl);
                    HttpURLConnection con = (HttpURLConnection)
                            url.openConnection();
                    con.setRequestMethod(METHOD);
                    con.setConnectTimeout(TIME_OUT);
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        int len = con.getContentLength();
                        contentLength = len;
                        if (onDpoolListener != null) {
                            onDpoolListener.onFileSize(contentLength);
                        }
                        handler.sendEmptyMessage(MESSAGE_GET_LENGTH);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_GET_LENGTH:
                    initThreadPool();
                    break;
                case MESSAGE_PROGRESS:
                    updateProgress(msg.arg1, msg.arg2);
                    break;
                case MESSAGE_COMPLETE:
                    downloadComplete(msg.arg1, msg.arg2);
                    break;
                case MESSAGE_PAUSE:
                    pause();
                    break;
                case MESSAGE_STOP:
                    stop(msg.arg1, (String) msg.obj);
                    break;
            }
        }
    };

    private void initThreadPool() {
        threadList.clear();
        list.clear();
        entityList.clear();
        for (int i = 0; i < threadCount; i++) {
            DownLoadEntity dEntity = new DownLoadEntity();
            dEntity.setUrl(downloadUrl);
            dEntity.setTempFile(new File(fileSaveUrl));
            dEntity.setFileSize(contentLength);
            dEntity.setPerSize(contentLength / threadCount);
            dEntity.setStartPosition((contentLength / threadCount) * i);
            if (i == threadCount - 1) {
                dEntity.setEndPosition(dEntity.getFileSize());
            } else {
                dEntity.setEndPosition(((contentLength / threadCount) * (i + 1)) - 1);
            }
            dEntity.setThreadId(i);
            dEntity.setThreadCount(threadCount);
            DRunnable dRunnable = new DRunnable(context, dEntity, handler);
            list.add(dRunnable);
            entityList.add(dEntity);
        }
        dThreadFactory = new DThreadFactory();
        executorService = Executors.newFixedThreadPool(threadCount, dThreadFactory);
        for (int i = 0; i < list.size(); i++) {
            executorService.submit(list.get(i));
        }
        begin = System.currentTimeMillis();
    }

    private void updateProgress(int threadId, int position) {
        dbManager.updateDProgress(threadId, position);
        if (onDpoolListener != null) {
            onDpoolListener.onDpoolProgress((int) ((new File(fileSaveUrl).length() * 100) / contentLength));
        }
        System.out.println("线程：下载中" + threadId + "  " + position + "  " + ((position * 100) / entityList.get(threadId).getPerSize()) + "%");
    }

    private void downloadComplete(int threadId, int status) {
        dbManager.updateDStatus(threadId, status);
        threadList.add(threadId);
        if (threadList.size() == threadCount) {
            complete = System.currentTimeMillis();
            if (onDpoolListener != null) {
                onDpoolListener.onComplete(complete);
            }
        }
    }

    private void pause() {
        System.out.println("线程执行：暂停中");
    }

    private void stop(int threadId, String message) {
        System.out.println("线程执行：下载异常" + threadId + "  " + message);
    }

    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getFileLength() {
        return contentLength;
    }

    @Override
    public long getCompleteTimeMills() {
        return complete - begin;
    }

    @Override
    public void pauseDpool() {
        for (int i = 0; i < threadCount; i++) {
            list.get(i).setPauseOrResume(true);
        }
    }

    @Override
    public void stopDpool() {
        if (executorService == null) {
            return;
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("停止下载");
    }

    @Override
    public void resumeDpool() {
        initThreadPool();
    }

    @Override
    public void excutePool() {
        dbManager = new DBManager(context);
        list = new ArrayList<>();
        entityList = new ArrayList<>();
        threadList = new ArrayList<>();
        File file = new File(fileSaveUrl);
        if (file.exists()) {
            file.delete();
        }
        getHttpFileLength();
    }

    @Override
    public void closeDB() {
        if (dbManager != null) {
            dbManager.cloaseDB();
        }
    }

    public static final class Builder {
        private Context context;
        private String downloadUrl = "";
        private String fileSaveUrl = Environment.getExternalStorageDirectory() + "/temp.apk";
        ;
        private int contentLength = 0;
        private int threadCount = 3;

        public Builder() {
        }

        public Builder context(Context val) {
            context = val;
            return this;
        }

        public Builder downloadUrl(String val) {
            downloadUrl = val;
            return this;
        }

        public Builder fileSaveUrl(String val) {
            fileSaveUrl = val;
            return this;
        }

        private Builder contentLength(int val) {
            contentLength = val;
            return this;
        }

        public Builder threadCount(int val) {
            threadCount = val;
            return this;
        }

        public DPoolImpl build() {
            return new DPoolImpl(this);
        }
    }

    public interface OnDpoolListener {
        void onFileSize(long fileSize);

        void onDpoolProgress(int progress);

        void onComplete(long timeCompleteMills);
    }

    public void setOnDpoolListener(OnDpoolListener onDpoolListener) {
        this.onDpoolListener = onDpoolListener;
    }
}
