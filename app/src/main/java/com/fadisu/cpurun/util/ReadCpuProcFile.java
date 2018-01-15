package com.fadisu.cpurun.util;

import android.os.Process;
import android.util.Log;

/**
 * ReadCpuProcFile analysis
 */
public class ReadCpuProcFile {

    private static final String TAG = ReadCpuProcFile.class.getSimpleName();

    // ReadCpuProcFile probe
    private static final int[] SYSTEM_CPU_FORMAT = new int[]{
            Process.PROC_SPACE_TERM | Process.PROC_COMBINE,
            Process.PROC_SPACE_TERM | Process.PROC_OUT_LONG, // 1: user time
            Process.PROC_SPACE_TERM | Process.PROC_OUT_LONG, // 2: nice time
            Process.PROC_SPACE_TERM | Process.PROC_OUT_LONG, // 3: sys time
            Process.PROC_SPACE_TERM | Process.PROC_OUT_LONG, // 4: idle time
            Process.PROC_SPACE_TERM | Process.PROC_OUT_LONG, // 5: iowait time
            Process.PROC_SPACE_TERM | Process.PROC_OUT_LONG, // 6: irq time
            Process.PROC_SPACE_TERM | Process.PROC_OUT_LONG  // 7: softirq time
    };

    private static long mTotalCpuTime ;
    private static long mUserStart;
    private static long mSystemStart;
    private static long mIdleStart;
    private static long mIrqStart;

    public static void getTotalCpuTime() {
        long[] sysCpu = new long[7];
        if (Process.readProcFile("/proc/stat", SYSTEM_CPU_FORMAT, null, sysCpu, null)) {
            mUserStart = sysCpu[0] + sysCpu[1];
            mSystemStart = sysCpu[2];
            mIdleStart = sysCpu[3];
            mIrqStart = sysCpu[4] + sysCpu[5] + sysCpu[6];

            mTotalCpuTime = mUserStart + mSystemStart + mIdleStart + mIdleStart + mIrqStart;
            Log.d(TAG, "TotalCpuTime = " + mTotalCpuTime);
        }
    }

}
