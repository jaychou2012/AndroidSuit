package me.zuichu.androidsuit.view;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class NotificationView {
    private Context context;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private String notificationChannelId = "notificationView";
    private int id = 0;
    private RemoteViews remoteViews;
    private PendingIntent pendingIntent;
    private String ticker = "";
    private String title = "";
    private String content = "";
    private int icon;
    private Bitmap bitmap;
    private boolean autoCancel = true;
    private boolean ongoing = false;
    private int notificationMethod = -2;
    private Uri uri;
    private String subText;

    public NotificationView(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public String getNotificationChannelId() {
        return notificationChannelId;
    }

    public int getId() {
        return id;
    }

    public RemoteViews getRemoteViews() {
        return remoteViews;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public String getTicker() {
        return ticker;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getIcon() {
        return icon;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public boolean isAutoCancel() {
        return autoCancel;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public int getNotificationMethod() {
        return notificationMethod;
    }

    public Uri getUri() {
        return uri;
    }

    public String getSubText() {
        return subText;
    }

    public void showNotificationView() {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context, notificationChannelId);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(content);
        if (notificationMethod != -2) {
            builder.setDefaults(notificationMethod);
        }
        builder.setOngoing(ongoing);
        builder.setAutoCancel(autoCancel);
        builder.setSmallIcon(icon);
        if (bitmap != null) {
            builder.setLargeIcon(bitmap);
        }
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }
        if (subText != null) {
            builder.setSubText(subText);
        }
        if (remoteViews != null) {
            builder.setCustomContentView(remoteViews);
        }
        notificationManager.notify(id, builder.build());
    }

    public void showNotificationProgress(int max, int progress, boolean indeterminate) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context, notificationChannelId);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(content);
        if (notificationMethod != -2) {
            builder.setDefaults(notificationMethod);
        }
        builder.setOngoing(ongoing);
        builder.setAutoCancel(autoCancel);
        builder.setSmallIcon(icon);
        if (bitmap != null) {
            builder.setLargeIcon(bitmap);
        }
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }
        builder.setProgress(max, progress, false);
        if (subText != null) {
            builder.setSubText(subText);
        }
        if (remoteViews != null) {
            builder.setCustomContentView(remoteViews);
        }
        notificationManager.notify(id, builder.build());
    }

    public void clearNotification() {
        notificationManager.cancelAll();
    }

    public static class Builder {
        private NotificationView notificationView;

        public Builder(Context context) {
            notificationView = new NotificationView(context);
        }

        public Builder setView(RemoteViews remoteViews) {
            notificationView.remoteViews = remoteViews;
            return this;
        }

        public Builder setIntent(PendingIntent pendingIntent) {
            notificationView.pendingIntent = pendingIntent;
            return this;
        }

        public Builder setTitle(String title) {
            notificationView.title = title;
            return this;
        }

        public Builder setContentText(String content) {
            notificationView.content = content;
            return this;
        }

        public Builder setSmallIcon(int icon) {
            notificationView.icon = icon;
            return this;
        }

        public Builder setLargeIcon(Bitmap icon) {
            notificationView.bitmap = icon;
            return this;
        }

        public Builder setNotificationId(int id) {
            notificationView.id = id;
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel) {
            notificationView.autoCancel = autoCancel;
            return this;
        }

        public Builder setOngoing(boolean ongoing) {
            notificationView.ongoing = ongoing;
            return this;
        }

        public Builder setTicker(String ticker) {
            notificationView.ticker = ticker;
            return this;
        }

        public Builder setDefaults(int notificationMethod) {
            notificationView.notificationMethod = notificationMethod;
            return this;
        }

        public Builder setSound(Uri uri) {
            notificationView.uri = uri;
            return this;
        }

        public Builder setSubText(String subText) {
            notificationView.subText = subText;
            return this;
        }

        public NotificationView build() {
            return notificationView;
        }
    }

}
