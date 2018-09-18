package me.zuichu.androidsuit.download;

public interface DPoolDao {

    int getFileLength();

    long getCompleteTimeMills();

    void pauseDpool();

    void stopDpool();

    void resumeDpool();

    void reDpool();

    void excutePool();

    void closeDB();
}
