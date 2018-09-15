package me.zuichu.androidsuit.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import me.zuichu.androidsuit.R;
import me.zuichu.androidsuit.utils.Utils;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class DialogView {
    private Context context;
    private Dialog dialog;
    private float dimAmount = -1;
    private boolean dim = true;
    private boolean cancelble = true;
    private int layoutId;
    private View view;
    private int style = -1;
    private int width, height = 0;
    private boolean bottom = false;

    public DialogView(Context context) {
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

    public boolean isCancelble() {
        return cancelble;
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

    public void showView() {
        if (style != -1) {
            dialog = new Dialog(context, style);
        } else {
            dialog = new Dialog(context);
        }
        if (view != null) {
            dialog.setContentView(view);
        } else {
            view = LayoutInflater.from(context).inflate(layoutId, null);
            dialog.setContentView(layoutId);
        }
        dialog.setCanceledOnTouchOutside(cancelble);
        dialog.setCancelable(cancelble);
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
        if (bottom) {
            layoutParams.gravity = Gravity.BOTTOM;
        } else {
            layoutParams.gravity = Gravity.CENTER;
        }
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

    public Dialog getDialogView() {
        return dialog;
    }

    public static class Builder {
        private DialogView dialogView;

        public Builder(Context context) {
            dialogView = new DialogView(context);
        }

        public Builder setDimAmount(float dimAmount) {
            dialogView.dimAmount = dimAmount;
            return this;
        }

        public Builder setDim(boolean dim) {
            dialogView.dim = dim;
            return this;
        }

        public Builder setCancelble(boolean cancelble) {
            dialogView.cancelble = cancelble;
            return this;
        }

        public Builder setLayoutId(int layoutId) {
            dialogView.layoutId = layoutId;
            return this;
        }

        public Builder setView(View view) {
            dialogView.view = view;
            return this;
        }

        public Builder setStyle(int style) {
            dialogView.style = style;
            return this;
        }

        public Builder setCustomSize(int widthPx, int heightPx) {
            dialogView.width = widthPx;
            dialogView.height = heightPx;
            return this;
        }

        public Builder setBottom(boolean bottom) {
            dialogView.bottom = bottom;
            return this;
        }

        public DialogView build() {
            return dialogView;
        }
    }

}
