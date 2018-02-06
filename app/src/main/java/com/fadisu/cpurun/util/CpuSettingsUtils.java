package com.fadisu.cpurun.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.mediatek.perfservice.PerfServiceWrapper;

public class CpuSettingsUtils {

    private static String TAG = CpuSettingsUtils.class.getSimpleName();

    public static final int PERFHANDLE_ERROR_CODE = -1;
    public static final int PARAM_DEFAULT_VALUE = -1;

    public static final int CPU_NUMBER = CpuUtils.getNumCpuCores();
    public static final int MAX_CPU_FREQ = (int) CpuUtils.getCpuMaxFreq();
    public static final int MIN_CPU_FREQ = (int) CpuUtils.getCpuMinFreq();

    private int mCurPerfHandle;

    private Context mContext;
    private static CpuSettingsUtils mInstance;
    private PerfServiceWrapper mPerfServiceWrapper;

    public static CpuSettingsUtils getInstance(Context mContext) {
        if (null == mInstance) {
            synchronized (CpuSettingsUtils.class) {
                if (null == mInstance) {
                    mInstance = new CpuSettingsUtils(mContext);
                }
            }
        }

        return mInstance;
    }

    public CpuSettingsUtils(Context mContext) {
        this.mContext = mContext;
        mCurPerfHandle = PERFHANDLE_ERROR_CODE;
        mPerfServiceWrapper = new PerfServiceWrapper(mContext);

    }

    /**
     * CPU 核数和频率全开，达到瞬间性能最优
     */
    private static void perfBoost(Context mContext) {
        final PerfServiceWrapper mPerfServiceWrapper = new PerfServiceWrapper(mContext);
        if (null != mPerfServiceWrapper) {
            final int mPerfHandle = mPerfServiceWrapper.userReg(CPU_NUMBER, MAX_CPU_FREQ);

            if (PERFHANDLE_ERROR_CODE != mPerfHandle) {
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

    /**
     * 设置图形模式，参数1个：DRAM模式：0-默认模式，1-低功耗模式，2-还是默认模式，3-高性能模式
     *
     * @param mContext
     * @param minCpuNumber
     * @param maxCpuFreq
     */
    public static final int VCORE_DEFAULT = 0;
    public static final int VCORE_POWERSAVE = 1;
    public static final int VCORE_DEFAULT_2 = 2;
    public static final int VCORE_PERF = 3;
    public void setCpuVcoreMode(int minCpuNumber, int maxCpuFreq, int mode) {
        userDisableIfNeed();

        if (null != mPerfServiceWrapper) {
            mCurPerfHandle = mPerfServiceWrapper.userReg(minCpuNumber, maxCpuFreq);

            if (PERFHANDLE_ERROR_CODE != mCurPerfHandle) {
                mPerfServiceWrapper.userRegScnConfig(mCurPerfHandle, mPerfServiceWrapper.CMD_SET_VCORE, mode, PARAM_DEFAULT_VALUE, PARAM_DEFAULT_VALUE, PARAM_DEFAULT_VALUE);
                // screenOffEnable();
                mPerfServiceWrapper.userEnable(mCurPerfHandle);

                Log.d(TAG, "setCpuVcoreMode mode = " + mode + ", minCpuNumber = " + minCpuNumber + ", maxCpuFreq = " + maxCpuFreq + ", mCurPerfHandle = " + mCurPerfHandle);
            }
        }
    }

    /**
     * CPU 频率设置
     * 效果：维持最高频率为设定值，但是CPU场景调频不受控制，场景比如切换应用时，默认是开核升频率，暗屏失效
     * @param minCpuNumber
     * @param maxCpuFreq
     * @param freq
     */
    public void setCpuFreq(int minCpuNumber, int maxCpuFreq, int freq) {
        userDisableIfNeed();

        if (null != mPerfServiceWrapper) {
            mCurPerfHandle = mPerfServiceWrapper.userReg(minCpuNumber, maxCpuFreq);

            if (PERFHANDLE_ERROR_CODE != mCurPerfHandle) {
                mPerfServiceWrapper.userRegScnConfig(mCurPerfHandle, mPerfServiceWrapper.CMD_SET_CPU_FREQ_MAX, freq, PARAM_DEFAULT_VALUE, PARAM_DEFAULT_VALUE, PARAM_DEFAULT_VALUE);
                // screenOffEnable();
                mPerfServiceWrapper.userEnable(mCurPerfHandle);

                Log.d(TAG, "setCpuFreq freq = " + freq + ", minCpuNumber = " + minCpuNumber + ", maxCpuFreq = " + maxCpuFreq + ", mCurPerfHandle = " + mCurPerfHandle);
            }
        }
    }

    /**
     * 默认灭屏持续生效
     */
    public void screenOffEnable() {
        mPerfServiceWrapper.userRegScnConfig(mCurPerfHandle,mPerfServiceWrapper.CMD_SET_SCREEN_OFF_STATE, mPerfServiceWrapper.SCREEN_OFF_ENABLE, PARAM_DEFAULT_VALUE, PARAM_DEFAULT_VALUE, PARAM_DEFAULT_VALUE);
    }

    /**
     * 取消之前的设置
     */
    public void userDisableIfNeed() {
        if (mCurPerfHandle != PERFHANDLE_ERROR_CODE) {
            mPerfServiceWrapper.userDisable(mCurPerfHandle);
            Log.d(TAG, "userDisableIfNeed  mCurPerfHandle = " + mCurPerfHandle);
        }
    }

    /**
     * 退出注册
     */
    public void userUnreg() {
        if (mCurPerfHandle != PERFHANDLE_ERROR_CODE) {
            mPerfServiceWrapper.userDisable(mCurPerfHandle);
            mPerfServiceWrapper.userUnreg(mCurPerfHandle);
            Log.d(TAG, "userUnreg  mCurPerfHandle = " + mCurPerfHandle);
        }
    }
}
