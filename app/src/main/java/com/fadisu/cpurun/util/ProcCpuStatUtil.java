package com.fadisu.cpurun.util;

import android.os.Process;

import java.util.ArrayList;
import java.util.List;

/**
 * ProcCpuStatUtil analysis
 */
public class ProcCpuStatUtil {

    private static final String TAG = ProcCpuStatUtil.class.getSimpleName();

    // ProcCpuStatUtil probe
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

    public static List<String> getCpuTime() {
        List<String> result = new ArrayList<>();
        long[] sysCpu = new long[7];
        if (Process.readProcFile("/proc/stat", SYSTEM_CPU_FORMAT, null, sysCpu, null)) {
            result.add("User time : " + DateTimeUtil.longToSting(sysCpu[0]));
            result.add("Nice time : " + DateTimeUtil.longToSting(sysCpu[1]));
            result.add("Sys time : " + DateTimeUtil.longToSting(sysCpu[2]));
            result.add("Idle time : " + DateTimeUtil.longToSting(sysCpu[3]));
            result.add("Iowait time : " + DateTimeUtil.longToSting(sysCpu[4]));
            result.add("Irq time : " + DateTimeUtil.longToSting(sysCpu[5]));
            result.add("Softirq time : " + DateTimeUtil.longToSting(sysCpu[6]));

            long totalCpuTime = sysCpu[0] + sysCpu[1] + sysCpu[2] + sysCpu[3] + sysCpu[4] + sysCpu[5] + sysCpu[6];
            result.add("Total CPU time : " + DateTimeUtil.longToSting(totalCpuTime));
        }

        return result;
    }

    public static class CpuRateInfo {
        // CPU 运行总时间
        double totalCpuTime;

        // CPU 空闲时间
        double idleTime;

        // CPU 非空闲时间
        double nonIdleTime;
    }

    public static CpuRateInfo getCpuRateInfo() {
        CpuRateInfo mCpuRateInfo = new CpuRateInfo();
        long[] sysCpu = new long[7];
        if (Process.readProcFile("/proc/stat", SYSTEM_CPU_FORMAT, null, sysCpu, null)) {
            mCpuRateInfo.idleTime = sysCpu[3];
            mCpuRateInfo.totalCpuTime = sysCpu[0] + sysCpu[1] + sysCpu[2] + sysCpu[3] + sysCpu[4] + sysCpu[5] + sysCpu[6];
            mCpuRateInfo.nonIdleTime = mCpuRateInfo.totalCpuTime - mCpuRateInfo.idleTime;
        }

        return mCpuRateInfo;
    }

    /**
     * 获取 CPU 使用率
     * CPU 使用率 =(CPU 非空闲时间2 - CPU 非空闲时间1) / (CPU 运行总时间2 - CPU 运行总时间1) * 100%
     * https://www.jianshu.com/p/6bf564f7cdf0
     */
    public static String getCpuUsage() {
        String result = null;
        CpuRateInfo mCpuRateInfo1 = getCpuRateInfo();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CpuRateInfo mCpuRateInfo2 = getCpuRateInfo();

        double usage = ((mCpuRateInfo2.nonIdleTime - mCpuRateInfo1.nonIdleTime) / (mCpuRateInfo2.totalCpuTime - mCpuRateInfo1.totalCpuTime)) * 100;
        result = String.valueOf((int) usage) + "%";
        return result;
    }

}
