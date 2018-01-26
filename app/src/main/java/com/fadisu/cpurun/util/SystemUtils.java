package com.fadisu.cpurun.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;

public class SystemUtils {

    /**
     * OpenGL ES 版本
     *
     * @param mContext
     * @return
     */
    public static String getOpenGlVersion(Context mContext) {
        return ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().getGlEsVersion();
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static String getSDTotalSize(Context mContext) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static String getSDAvailableSize(Context mContext) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
    }

    /**
     * 获得机身ROM总大小
     *
     * @return
     */
    public static String getRomTotalSize(Context mContext) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
    }

    /**
     * 获得机身可用ROM
     *
     * @return
     */
    public static String getRomAvailableSize(Context mContext) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
    }

}
