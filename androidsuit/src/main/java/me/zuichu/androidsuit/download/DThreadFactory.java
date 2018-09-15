package me.zuichu.androidsuit.download;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

public class DThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(@NonNull Runnable r) {
        Thread thread = new Thread(r);
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("线程池：" + e.getLocalizedMessage());
            }
        });
        thread.setDaemon(true);
        System.out.println("线程池：" + thread.getId() + "  " + thread.getName() + "  " + thread.getPriority());
        return thread;
    }
}
