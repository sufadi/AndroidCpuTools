package com.fadisu.cpurun.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.mediatek.perfservice.PerfServiceWrapper;

/**
 * Created by Fadi.Su on 2018/2/5.
 */
public class CpuSettingsUtils {

    private static String TAG =  CpuSettingsUtils.class.getSimpleName();

    /**
     * CPU 核数和频率全开，达到瞬间性能最优
     */
    private static void perfBoost(Context mContext) {
        final int MIN_CPU_NUMBER = CpuUtils.getNumCpuCores();
        final int MAX_CPU_FREQ = (int) CpuUtils.getCpuMaxFreq();

        final PerfServiceWrapper mPerfServiceWrapper = new PerfServiceWrapper(mContext);
        if (null != mPerfServiceWrapper) {
            final int mPerfHandle = mPerfServiceWrapper.userReg(MIN_CPU_NUMBER, MAX_CPU_FREQ);

            if (-1 != mPerfHandle) {
                mPerfServiceWrapper.userEnableTimeoutMs(mPerfHandle, 500);
                Log.d(TAG, "userEnableTimeoutMs = " + mPerfHandle);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPerfServiceWrapper.userUnreg(mPerfHandle);
                        Log.d(TAG, "userUnreg = " + mPerfHandle);
                    }
                }, 600);
            }
        }
    }
}
