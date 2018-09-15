package me.zuichu.androidsuitdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zuichu.androidsuit.download.DPoolImpl;
import me.zuichu.androidsuit.download.DService;
import me.zuichu.androidsuit.permission.PermissionSuit;

public class MainActivity extends AppCompatActivity implements PermissionSuit.PermissionListener, View.OnClickListener, DPoolImpl.OnDpoolListener {
    @BindView(R.id.tv_text)
    TextView tv_text;
    private DPoolImpl dPool;
    private String downloadUrl = "http://gdown.baidu.com/data/wisegame/03fa96080f902ac9/QQyouxiang_10132005.apk";
    private boolean pause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tv_text.setOnClickListener(this);
        PermissionSuit.with(this).setPermissionListener(this).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void getPermission(String permission) {
        System.out.println("授权了getPermission");
        Toast.makeText(this, "授权了", Toast.LENGTH_SHORT).show();
        dPool = new DPoolImpl.Builder().context(this).downloadUrl(downloadUrl).fileSaveUrl(Environment.getExternalStorageDirectory() + "/file.apk").build();
        dPool.excutePool();
        dPool.setOnDpoolListener(this);
    }

    @Override
    public void noPermision(String permission) {
        System.out.println("没授权noPermision");
        Toast.makeText(this, "没授权", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_text:
                if (pause) {
                    dPool.resumeDpool();
                } else {
                    dPool.pauseDpool();
                }
                pause = !pause;
                break;
        }
    }

    @Override
    public void onFileSize(long fileSize) {

    }

    @Override
    public void onDpoolProgress(int progress) {
        tv_text.setText(progress + "%");
    }

    @Override
    public void onComplete(long timeCompleteMills) {

    }
}
