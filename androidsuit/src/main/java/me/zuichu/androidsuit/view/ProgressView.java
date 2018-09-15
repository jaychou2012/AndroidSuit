package me.zuichu.androidsuit.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import me.zuichu.androidsuit.R;
import me.zuichu.androidsuit.utils.Utils;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ProgressView {
    private Context context;
    private Dialog dialog;
    private float dimAmount = -1;
    private boolean dim = true;
    private boolean cancelable = false;
    private int layoutId = R.layout.dialog_loading;
    private View view;
    private int style = -1;
    private int width, height = 0;
    private TextView tv_text;
    private FrameLayout fl_root;
    private boolean useDefaultLayout = true;

    public ProgressView(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public float getDimAmount() {
        return dimAmount;
    }

    public boolean isDim() {
        return dim;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public View getView() {
        return view;
    }

    public int getStyle() {
        return style;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isUseDefaultLayout() {
        return useDefaultLayout;
    }

    public void showView() {
        if (style != -1) {
            dialog = new Dialog(context, style);
        } else {
            dialog = new Dialog(context);
        }
        if (view != null) {
            dialog.setContentView(view);
        } else if (useDefaultLayout) {
            view = LayoutInflater.from(context).inflate(layoutId, null);
            dialog.setContentView(layoutId);
            tv_text = dialog.findViewById(R.id.tv_text);
        } else {
            view = LayoutInflater.from(context).inflate(layoutId, null);
            dialog.setContentView(layoutId);
        }
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.setCancelable(cancelable);
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
        if (width != 0) {
            layoutParams.width = width;
            layoutParams.height = height;
        } else {
            layoutParams.width = (Utils.getScreenWidth(context) * 2) / 3;
            layoutParams.height = WRAP_CONTENT;
        }
        window.setAttributes(layoutParams);
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public Dialog getProgressView() {
        return dialog;
    }

    public TextView getTextView() {
        return tv_text;
    }


    public static class Builder {
        private ProgressView progressView;

        public Builder(Context context) {
            progressView = new ProgressView(context);
        }

        public Builder setDimAmount(float dimAmount) {
            progressView.dimAmount = dimAmount;
            return this;
        }

        public Builder setDim(boolean dim) {
            progressView.dim = dim;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            progressView.cancelable = cancelable;
            return this;
        }

        public Builder setLayoutId(int layoutId) {
            progressView.layoutId = layoutId;
            return this;
        }

        public Builder setView(View view) {
            progressView.view = view;
            return this;
        }

        public Builder setStyle(int style) {
            progressView.style = style;
            return this;
        }

        public Builder setCustomSize(int widthPx, int heightPx) {
            progressView.width = widthPx;
            progressView.height = heightPx;
            return this;
        }

        public Builder setUseDefaultLayout(boolean useDefaultLayout) {
            progressView.useDefaultLayout = useDefaultLayout;
            return this;
        }

        public ProgressView build() {
            return progressView;
        }
    }
}
