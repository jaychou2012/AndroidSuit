package me.zuichu.androidsuit.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import me.zuichu.androidsuit.utils.Utils;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

public class BaseWindowView {
    private Context context;
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private float rawStartX, rawStartY;
    private boolean click = true;
    private int startX, startY, stopX, stopY;
    private boolean autoScroll = true;
    private boolean canScroll = true;
    private boolean applicationWindow = false;
    private ValueAnimator animator;
    private float xScroll = 0;
    private View view;
    private int width = WRAP_CONTENT, height = WRAP_CONTENT;
    private Interpolator interpolator = new DecelerateInterpolator();
    private long duration = 600;

    public BaseWindowView(Context context) {
        this.context = context;
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
        this.startX = x;
        this.startY = y;
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
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (applicationWindow) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        params.format = PixelFormat.RGBA_8888;
        params.flags = FLAG_NOT_FOCUSABLE | FLAG_WATCH_OUTSIDE_TOUCH;

        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = startX;
        params.y = startY;

        params.width = width;
        params.height = height;
        windowManager.addView(view, params);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!canScroll) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        rawStartX = event.getRawX();
                        rawStartY = event.getRawY();
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x += event.getRawX() - rawStartX;
                        params.y += event.getRawY() - rawStartY;
                        windowManager.updateViewLayout(view, params);
                        rawStartX = event.getRawX();
                        rawStartY = event.getRawY();
                        stopX = (int) event.getX();
                        stopY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(startX - stopX) >= 10 || Math.abs(startY - stopY) >= 10) {
                            click = false;
                            setScroll(event.getRawX());
                        } else {
                            click = true;
                        }
                        break;
                }
                return !click;
            }
        });
    }

    private void setScroll(float x) {
        if (!autoScroll) {
            return;
        }
        if (Math.abs(x) > (Utils.getScreenWidth(context) / 2)) {
            animator = ObjectAnimator.ofFloat(x, Utils.getScreenWidth(context));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    xScroll = (float) animation.getAnimatedValue();
                    params.x = (int) xScroll;
                    windowManager.updateViewLayout(view, params);
                }
            });
            animator.setDuration(duration).setInterpolator(interpolator);
            animator.start();
        } else {
            animator = ObjectAnimator.ofFloat(x, 0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    xScroll = (float) animation.getAnimatedValue();
                    params.x = (int) xScroll;
                    windowManager.updateViewLayout(view, params);
                }
            });
            animator.setDuration(duration).setInterpolator(interpolator);
            animator.start();
        }
    }

    private void showToast(String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public void setViewVisible(boolean visible) {
        if (view != null) {
            if (visible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public View getView() {
        return view;
    }

    public void onRemoveView() {
        if (view != null) {
            windowManager.removeView(view);
        }
    }
}
