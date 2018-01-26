package com.fadisu.cpurun.util;

import android.app.ActivityManager;
import android.content.Context;

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

}
