package com.fadisu.cpurun.util;

import android.app.ActivityManager;
import android.content.Context;

public class SystemUtils {

    public static String getOpenGlVersion(Context mContext) {
        return ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().getGlEsVersion();
    }

}
