package me.zuichu.androidsuit.entity;

import java.io.File;

public class DownLoadEntity {
    private long fileSize;
    private int threadId;
    private String url;
    private long startPosition;
    private long endPosition;
    private long currentPosition;
    private int perSize;
    private File tempFile;
    private int status;//0：完成；1：下载中
    private int threadCount;
    private String signId;

    public DownLoadEntity() {

    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }

    public int getPerSize() {
        return perSize;
    }

    public void setPerSize(int perSize) {
        this.perSize = perSize;
    }

    public File getTempFile() {
        return tempFile;
    }

    public void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(long currentPosition) {
        this.currentPosition = currentPosition;
    }

    private DownLoadEntity(Builder builder) {
        fileSize = builder.fileSize;
        threadId = builder.threadId;
        url = builder.url;
        startPosition = builder.startPosition;
        endPosition = builder.endPosition;
        currentPosition = builder.currentPosition;
        perSize = builder.perSize;
        tempFile = builder.tempFile;
        status = builder.status;
        threadCount = builder.threadCount;
        signId = builder.signId;
    }


    public static final class Builder {
        private long fileSize;
        private int threadId;
        private String url;
        private long startPosition;
        private long endPosition;
        private long currentPosition;
        private int perSize;
        private File tempFile;
        private int status;
        private int threadCount;
        private String signId;

        public Builder() {
        }

        public Builder fileSize(long val) {
            fileSize = val;
            return this;
        }

        public Builder threadId(int val) {
            threadId = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder startPosition(long val) {
            startPosition = val;
            return this;
        }

        public Builder endPosition(long val) {
            endPosition = val;
            return this;
        }

        public Builder currentPosition(long val) {
            currentPosition = val;
            return this;
        }

        public Builder perSize(int val) {
            perSize = val;
            return this;
        }

        public Builder tempFile(File val) {
            tempFile = val;
            return this;
        }

        public Builder status(int val) {
            status = val;
            return this;
        }

        public Builder threadCount(int val) {
            threadCount = val;
            return this;
        }

        public Builder signId(String val) {
            signId = val;
            return this;
        }

        public DownLoadEntity build() {
            return new DownLoadEntity(this);
        }
    }
}
