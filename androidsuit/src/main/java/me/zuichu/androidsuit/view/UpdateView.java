package me.zuichu.androidsuit.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import me.zuichu.androidsuit.R;
import me.zuichu.androidsuit.utils.Utils;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class UpdateView implements DialogInterface.OnKeyListener, View.OnClickListener {
    private Context context;
    private View view;
    private Dialog dialog;
    private String title = "";
    private String content = "";
    private String time = "";
    private String version = "";
    private boolean force = false;
    private String size = "";
    private int layoutId = R.layout.layout_updateview;
    private String url = "";
    private String path = "";
    private String reName = "";
    private boolean backgroundNotification = true;
    private boolean useDefaultLayout = true;
    private boolean dim = true;
    private float dimAmount = -1;

    public UpdateView(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getTitle() {
        return title;
    }

    public View getView() {
        return view;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getVersion() {
        return version;
    }

    public boolean isForce() {
        return force;
    }

    public String getSize() {
        return size;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    public String getReName() {
        return reName;
    }

    public boolean isBackgroundNotification() {
        return backgroundNotification;
    }

    public boolean isUseDefaultLayout() {
        return useDefaultLayout;
    }

    public boolean isDim() {
        return dim;
    }

    public float getDimAmount() {
        return dimAmount;
    }

    public void showUpdate() {
        dialog = new Dialog(context);
        if (useDefaultLayout) {
            dialog.setContentView(R.layout.layout_updateview);
            TextView tv_content = dialog.findViewById(R.id.tv_content);
            TextView tv_size = dialog.findViewById(R.id.tv_size);
            TextView tv_version = dialog.findViewById(R.id.tv_version);
            TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
            TextView tv_ok = dialog.findViewById(R.id.tv_ok);
            tv_cancel.setOnClickListener(this);
            tv_ok.setOnClickListener(this);
            tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
            tv_content.setText(content);
            tv_size.setText("新版大小：" + size);
            tv_version.setText("版本号：" + version);
        } else {
            if (view != null) {
                dialog.setContentView(view);
            } else {
                dialog.setContentView(layoutId);
            }
        }
        dialog.setCanceledOnTouchOutside(false);
        if (force) {
            dialog.setCancelable(false);
            dialog.setOnKeyListener(this);
        }
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.bg_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            window.setElevation(0);
        }
        if (dim) {
            window.setDimAmount(0.5f);
        } else {
            window.setDimAmount(0);
        }
        if (dimAmount != -1) {
            window.setDimAmount(dimAmount);
        }
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = (Utils.getScreenWidth(context) * 2) / 3;
        layoutParams.height = WRAP_CONTENT;
        window.setAttributes(layoutParams);
        dialog.show();
    }

    public void dismiss() {
        dialog.cancel();
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (force) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_cancel) {
            dialog.dismiss();
        } else if (v.getId() == R.id.tv_ok) {

        }
    }

    public static class Builder {
        private UpdateView updateView;

        public Builder(Context context) {
            updateView = new UpdateView(context);
        }

        public Builder setTitle(String title) {
            updateView.title = title;
            return this;
        }

        public Builder setUrl(String url) {
            updateView.url = url;
            return this;
        }

        public Builder setView(View view) {
            updateView.view = view;
            return this;
        }

        public Builder setLayoutId(int layoutId) {
            updateView.layoutId = layoutId;
            return this;
        }

        public Builder setForce(boolean force) {
            updateView.force = force;
            return this;
        }

        public Builder setVersion(String version) {
            updateView.version = version;
            return this;
        }

        public Builder setTime(String time) {
            updateView.time = time;
            return this;
        }

        public Builder setSize(String size) {
            updateView.size = size;
            return this;
        }

        public Builder setContent(String content) {
            updateView.content = content;
            return this;
        }

        public Builder setPath(String path) {
            updateView.path = path;
            return this;
        }

        public Builder setReName(String reName) {
            updateView.reName = reName;
            return this;
        }

        public Builder setBackgroundNotification(boolean backgroundNotification) {
            updateView.backgroundNotification = backgroundNotification;
            return this;
        }

        public Builder setUseDefaultLayout(boolean useDefaultLayout) {
            updateView.useDefaultLayout = useDefaultLayout;
            return this;
        }

        public Builder setDim(boolean dim) {
            updateView.dim = dim;
            return this;
        }

        public Builder setDimAmount(float dimAmount) {
            updateView.dimAmount = dimAmount;
            return this;
        }

        public UpdateView build() {
            return updateView;
        }
    }
}
