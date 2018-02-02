package com.fadisu.cpurun.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.fadisu.cpurun.R;
import com.fadisu.cpurun.util.CpuUtils;
import com.fadisu.cpurun.util.ProcCpuStatUtil;
import com.fadisu.cpurun.util.ThermalInfoUtil;

import java.util.ArrayList;
import java.util.List;

public class CpuMsgService extends Service {

    public static final String ACTION_CPU_USAGE_START = "action_cpu_usage_start";
    public static final String ACTION_CPU_USAGE_END = "action_cpu_usage_end";
    private boolean isRun;
    private Thread mThread;
    private Context mContext;
    private ICpuMsgService mICpuMsgService = new MyICpuMsgService();
    private final RemoteCallbackList<ICpuMsgCallBack> mCallbackList = new RemoteCallbackList<ICpuMsgCallBack>();


    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder) mICpuMsgService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        isRun = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return super.onStartCommand(intent, flags, startId);
        }

        String action = intent.getAction();
        if (null == action) {
            return super.onStartCommand(intent, flags, startId);
        }

        if (ACTION_CPU_USAGE_START.equals(action)) {
            Log.d("CpuMsgService", "startTask");
            startTask();
        } else if (ACTION_CPU_USAGE_END.equals(action)) {
            Log.d("CpuMsgService", "stopTask");
            stopTask();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopTask();
        super.onDestroy();
    }

    private void startTask() {
        if (null != mThread) {
            return;
        }
        isRun = true;
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    List<String> result = new ArrayList<>();
                    result.add(mContext.getString(R.string.cpu_usage) + ProcCpuStatUtil.getCpuUsage());
                    result.add(ThermalInfoUtil.getCpuTemparature());
                    result.addAll(CpuUtils.getCpuCurFreq(mContext));
                    if (null != mICpuMsgService) {
                        try {
                            Log.d("CpuMsgService", "getCpuUsage = " + result.get(0));
                            mICpuMsgService.updateCpuUsage(result);
                        } catch (RemoteException e) {
                            //e.printStackTrace();
                        }
                    }
                }
            }
        });

        mThread.start();
    }

    private void stopTask() {
        isRun = false;

        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
    }

    class MyICpuMsgService extends ICpuMsgService.Stub {

        @Override
        public void registerCallback(ICpuMsgCallBack cb) throws RemoteException {
            if (cb != null) {
                mCallbackList.register(cb);
            }
        }

        @Override
        public void unregisterCallback(ICpuMsgCallBack cb) throws RemoteException {
            if (cb != null) {
                mCallbackList.unregister(cb);
            }
        }

        @Override
        public void updateCpuUsage(List<String> msg) throws RemoteException {
            final int N = mCallbackList.beginBroadcast();
            for (int i = 0; i < N; i++) {
                try {
                    mCallbackList.getBroadcastItem(i).updateCpuUsage(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            mCallbackList.finishBroadcast();
        }
    }
}
