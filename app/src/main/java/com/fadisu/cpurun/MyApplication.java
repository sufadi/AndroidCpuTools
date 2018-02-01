package com.fadisu.cpurun;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.fadisu.cpurun.service.CpuMsgService;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(this);
    }

    public static void startService(Context mContext) {
        Intent intent = new Intent(mContext, CpuMsgService.class);
        intent.setAction(CpuMsgService.ACTION_CPU_USAGE_START);
        mContext.startService(intent);
    }

    public static void stopService(Context mContext) {
        Intent intent = new Intent(mContext, CpuMsgService.class);
        intent.setAction(CpuMsgService.ACTION_CPU_USAGE_END);
        mContext.startService(intent);
    }
}
