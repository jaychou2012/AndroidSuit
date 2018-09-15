package me.zuichu.androidsuit.download;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class DService extends Service {
    private DPoolImpl dPool;
    private String downloadUrl = "http://gdown.baidu.com/data/wisegame/03fa96080f902ac9/QQyouxiang_10132005.apk";
    private String fileSaveUrl = Environment.getExternalStorageDirectory() + "/temp.apk";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dPool = new DPoolImpl.Builder().context(getBaseContext()).downloadUrl(downloadUrl)
                .fileSaveUrl(fileSaveUrl).threadCount(6).build();
        dPool.excutePool();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dPool.closeDB();
    }
}
