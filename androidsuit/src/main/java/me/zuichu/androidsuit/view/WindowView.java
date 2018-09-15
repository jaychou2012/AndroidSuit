package me.zuichu.androidsuit.view;

import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import me.zuichu.androidsuit.utils.Utils;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class WindowView {
    private Context context;
    private boolean autoScroll = false;
    private boolean canScroll = true;
    private int x, y = 0;
    private boolean applicationWindow = false;
    private View view;
    private BaseWindowView baseWindowView;
    private int width = WRAP_CONTENT, height = WRAP_CONTENT;
    private Interpolator interpolator = new DecelerateInterpolator();
    private long duration = 600;

    public WindowView(Context context) {
        this.context = context;
        x = Utils.getScreenWidth(context);
        y = Utils.getScreenHeight(context) / 2;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setAutoScroll(boolean autoScroll) {
        this.autoScroll = autoScroll;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setApplicationWindow(boolean applicationWindow) {
        this.applicationWindow = applicationWindow;
    }

    public void showWindowView() {
        baseWindowView = new BaseWindowView(context);
        baseWindowView.setView(view);
        baseWindowView.setLocation(x, y);
        baseWindowView.setSize(width, height);
        baseWindowView.setInterpolator(interpolator);
        baseWindowView.setDuration(duration);
        baseWindowView.setCanScroll(canScroll);
        baseWindowView.setAutoScroll(autoScroll);
        baseWindowView.setApplicationWindow(applicationWindow);
        baseWindowView.showWindowView();
    }

    public View getView() {
        if (baseWindowView != null) {
            return baseWindowView.getView();
        }
        return null;
    }

    public void setViewVisible(boolean visible) {
        if (baseWindowView != null) {
            baseWindowView.setViewVisible(visible);
        }
    }

    public void hideWindowView() {
        if (baseWindowView != null) {
            baseWindowView.onRemoveView();
        }
    }

}
