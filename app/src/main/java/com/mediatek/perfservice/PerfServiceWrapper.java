/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein is
 * confidential and proprietary to MediaTek Inc. and/or its licensors. Without
 * the prior written permission of MediaTek inc. and/or its licensors, any
 * reproduction, modification, use or disclosure of MediaTek Software, and
 * information contained herein, in whole or in part, shall be strictly
 * prohibited.
 *
 * MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER
 * ON AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NONINFRINGEMENT. NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH
 * RESPECT TO THE SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY,
 * INCORPORATED IN, OR SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES
 * TO LOOK ONLY TO SUCH THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO.
 * RECEIVER EXPRESSLY ACKNOWLEDGES THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO
 * OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES CONTAINED IN MEDIATEK
 * SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE
 * RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S
 * ENTIRE AND CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE
 * RELEASED HEREUNDER WILL BE, AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE
 * MEDIATEK SOFTWARE AT ISSUE, OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE
 * CHARGE PAID BY RECEIVER TO MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek
 * Software") have been modified by MediaTek Inc. All revisions are subject to
 * any receiver's applicable license agreements with MediaTek Inc.
 */

package com.mediatek.perfservice;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.os.Trace;

import com.mediatek.am.AMEventHookData;
import com.mediatek.am.AMEventHookData.AfterActivityDestroyed;
import com.mediatek.am.AMEventHookData.AfterActivityPaused;
import com.mediatek.am.AMEventHookData.AfterActivityResumed;
import com.mediatek.am.AMEventHookData.AfterActivityStopped;
import com.mediatek.am.AMEventHookData.BeforeActivitySwitch;
import com.mediatek.am.AMEventHookData.StartProcess;

// Android7.1/frameworks\base\core\java\com\mediatek\perfservice
public class PerfServiceWrapper implements IPerfServiceWrapper {

    private static final String TAG = "PerfServiceWrapper";

    private IPerfService sService = null;
    private Context mContext;

    private int inited = 0;

    private int setTid = 0;
    private long mPreviousTime = 0;
    private static final int RENDER_THREAD_UPDATE_DURATION = 400;
    private static final int AMS_BOOST_TIME = 2000;
    private static final int GLSURFACE_BOOST_TIME = 10;

    public static native int nativeGetPid();
    public static native int nativeGetTid();

    private void init() {
        if (inited == 0) {
            IBinder b = ServiceManager.checkService(Context.MTK_PERF_SERVICE);
            if (b != null) {
                sService = IPerfService.Stub.asInterface(b);
                if (sService != null)
                    inited = 1;
                else
                    log("ERR: getService() sService is still null..");
            }
        }
    }

    public PerfServiceWrapper(Context context) {
        mContext = context;
        init();
    }

    public PerfServiceWrapper() {
        //mContext = context;
        init();
    }

    public void boostEnable(int scenario) {
        //log("boostEnable");
        try {
            init();
            if (sService != null)
                sService.boostEnable(scenario);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in boostEnable:" + e);
        }
    }

    public void boostDisable(int scenario) {
        //log("boostEnable");
        try {
            init();
            if (sService != null)
                sService.boostDisable(scenario);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in boostDisable:" + e);
        }
    }

    public void boostEnableTimeout(int scenario, int timeout) {
        //log("boostEnable");
        try {
            init();
            if (sService != null)
                sService.boostEnableTimeout(scenario, timeout);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in boostEnableTimeout:" + e);
        }
    }

    public void boostEnableTimeoutMs(int scenario, int timeout_ms) {
        //log("boostEnableTimeoutMs");
        try {
            init();
            if (sService != null)
                sService.boostEnableTimeoutMs(scenario, timeout_ms);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in boostEnableTimeoutMs:" + e);
        }
    }

    public void notifyAppState(String packName, String className, int state, int pid) {
        //log("boostEnable");
        try {
            init();
            if (sService != null)
                sService.notifyAppState(packName, className, state, pid);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in notifyAppState:" + e);
        }
    }

    public int userReg(int scn_core, int scn_freq) {
        int handle = -1;
        //log("[userReg] - "+scn_core+", "+scn_freq);
        try {
            init();
            int pid = nativeGetPid();
            int tid = nativeGetTid();
            if (sService != null)
                handle = sService.userReg(scn_core, scn_freq, pid, tid);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userReg:" + e);
        }
        //log("[userReg] - handle:"+handle);
        return handle;
    }

    public int userRegBigLittle(int scn_core_big, int scn_freq_big, int scn_core_little, int scn_freq_little) {
        int handle = -1;
        //log("[userRegBigLittle] - "+scn_core_big+", "+scn_freq_big+", "+scn_core_little+", "+scn_freq_little);
        try {
            init();
            int pid = nativeGetPid();
            int tid = nativeGetTid();
            if (sService != null)
                handle = sService.userRegBigLittle(scn_core_big, scn_freq_big, scn_core_little, scn_freq_little, pid, tid);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userRegBigLittle:" + e);
        }
        //log("[userRegBigLittle] - handle:"+handle);
        return handle;
    }

    public void userUnreg(int handle) {
        //log("[userUnreg] - "+handle);
        try {
            init();
            if (sService != null)
                sService.userUnreg(handle);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userUnreg:" + e);
        }
    }

    public int userGetCapability(int cmd) {
        int value = -1;
        //log("[userGetCapability] - "+cmd);
        try {
            init();
            if (sService != null)
                value = sService.userGetCapability(cmd);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userGetCapability:" + e);
        }
        //log("[userGetCapability] - value:"+value);
        return value;
    }

    public int userRegScn() {
        int handle = -1;
        //log("[userRegScn]");
        try {
            init();
            int pid = nativeGetPid();
            int tid = nativeGetTid();
            if (sService != null)
                handle = sService.userRegScn(pid, tid);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userRegScn:" + e);
        }
        //log("[userRegScn] - handle:"+handle);
        return handle;
    }

    public void userRegScnConfig(int handle, int cmd, int param_1, int param_2, int param_3, int param_4) {
        //log("userRegScnConfig");
        try {
            init();
            if (sService != null)
                sService.userRegScnConfig(handle, cmd, param_1, param_2, param_3, param_4);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userRegScnConfig:" + e);
        }
    }

    public void userUnregScn(int handle) {
        //log("userUnregScn");
        try {
            init();
            if (sService != null)
                sService.userUnregScn(handle);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userUnregScn:" + e);
        }
    }

    public void userEnable(int handle) {
        //log("[userEnable] - "+handle);
        try {
            init();
            if (sService != null)
                sService.userEnable(handle);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userEnable:" + e);
        }
    }

    public void userEnableTimeout(int handle, int timeout) {
        //log("[userEnableTimeout] - "+handle+", "+timeout);
        try {
            init();
            if (sService != null)
                sService.userEnableTimeout(handle, timeout);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userEnableTimeout:" + e);
        }
    }

    public void userEnableTimeoutMs(int handle, int timeout_ms) {
        //log("[userEnableTimeoutMs] - "+handle+", "+timeout);
        try {
            init();
            if (sService != null)
                sService.userEnableTimeoutMs(handle, timeout_ms);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userEnableTimeoutMs:" + e);
        }
    }

    public void userEnableAsync(int handle) {
        //log("[userEnableAsync] - "+handle);
        try {
            init();
            if (sService != null)
                sService.userEnableAsync(handle);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userEnable:" + e);
        }
    }

    public void userEnableTimeoutAsync(int handle, int timeout) {
        //log("[userEnableTimeoutAsync] - "+handle+", "+timeout);
        try {
            init();
            if (sService != null)
                sService.userEnableTimeoutAsync(handle, timeout);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userEnableTimeout:" + e);
        }
    }

    public void userEnableTimeoutMsAsync(int handle, int timeout_ms) {
        //log("[userEnableTimeoutMsAsync] - "+handle+", "+timeout);
        try {
            init();
            if (sService != null)
                sService.userEnableTimeoutMsAsync(handle, timeout_ms);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userEnableTimeoutMs:" + e);
        }
    }

    public void userDisable(int handle) {
        //log("[userDisable] - "+handle);
        try {
            init();
            if (sService != null)
                sService.userDisable(handle);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userDisable:" + e);
        }
    }

    public void userResetAll() {
        try {
            init();
            if (sService != null)
                sService.userResetAll();
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userResetAll:" + e);
        }
    }

    public void userDisableAll() {
        try {
            init();
            if (sService != null)
                sService.userDisableAll();
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userDisableAll:" + e);
        }
    }

    public void userRestoreAll() {
        try {
            init();
            if (sService != null)
                sService.userRestoreAll();
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userRestoreAll:" + e);
        }
    }

    public void dumpAll() {
        try {
            init();
            if (sService != null)
                sService.dumpAll();
        } catch (RemoteException e) {
            loge("ERR: RemoteException in dumpAll:" + e);
        }
    }

    public void setFavorPid(int pid) {
        //log("userUnregScn");
        try {
            init();
            if (sService != null)
                sService.setFavorPid(pid);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in setFavorPid:" + e);
        }
    }

    public void restorePolicy(int pid) {
        //log("RestorePolicy");
        try {
            init();
            if (sService != null)
                sService.restorePolicy(pid);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in restorePolicy:" + e);
        }
    }

    public void notifyFrameUpdate(int level) {
        //log("notifyFrameUpdate");
        final long nowTime = System.currentTimeMillis();
        final int not_gl = level;

        try {
            init();

            if (setTid == 0) {
                level = nativeGetTid();
                setTid = 1;
            }

            if (mPreviousTime == 0 || (nowTime - mPreviousTime) > RENDER_THREAD_UPDATE_DURATION) {
                //log("notifyFrameUpdate - mPreviousTime:" + mPreviousTime + ", nowTime:" + nowTime);
                if (sService != null)
                    sService.notifyFrameUpdate(level);
                if (not_gl == 0)
                    boostEnableTimeout(IPerfServiceWrapper.SCN_GLSURFACE, GLSURFACE_BOOST_TIME);
                mPreviousTime = nowTime;
            }

        } catch (RemoteException e) {
            loge("ERR: RemoteException in notifyFrameUpdate:" + e);
        }
    }

    public void notifyDisplayType(int type) {
        //log("notifyDisplayType");
        try {
            init();

            if (sService != null)
                sService.notifyDisplayType(type);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in notifyDisplayType:" + e);
        }
    }

    public int getLastBoostPid() {
        int handle = 0;
        //log("[userRegScn]");
        try {
            init();
            if(sService != null)
                handle = sService.getLastBoostPid();
        } catch (RemoteException e) {
            loge("ERR: RemoteException in getLastBoostPid:" + e);
        }
        //log("[userRegScn] - handle:"+handle);
        return handle;
    }

    public void notifyUserStatus(int type, int status) {
        //log("notifyDisplayType");
        try {
            init();

            if (sService != null)
                sService.notifyUserStatus(type, status);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in notifyUserStatus:" + e);
        }
    }

    public int getClusterInfo(int cmd, int id) {
        int value = -1;
        //log("[getClusterInfo] - "+cmd);
        try {
            init();
            if (sService != null)
                value = sService.getClusterInfo(cmd, id);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in userGetCapability:" + e);
        }
        //log("[getClusterInfo] - value:"+value);
        return value;
    }

    public void levelBoost(int level) {
        //log("levelBoost");
        try {
            init();
            if(sService != null)
                sService.levelBoost(level);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in levelBoost:" + e);
        }
    }

    public int getPackAttr(String packName, int cmd) {
        //log("getPackAttr");
        int result = -1;
        try {
            init();
            if(sService != null)
                result = sService.getPackAttr(packName, cmd);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in getPackAttr:" + e);
        }
        return result;
    }

    public void appBoostEnable(String packName) {
        //log("appBoostEnable " + packName);
        try {
            init();

            if(sService != null)
                sService.appBoostEnable(packName);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in appBoostEnable:" + e);
        }
    }

    public String getGiftAttr(String packName, String attrName) {
        String result = null;
        try {
            init();
            if(sService != null)
                result= sService.getGiftAttr(packName, attrName);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in getPackAttr:" + e);
        }
        return result;
    }

    public void setExclusiveCore(int pid, int cpu_mask) {
        //log("setExclusiveCore");
        int result = -1;
        try {
            init();
            if(sService != null)
                sService.setExclusiveCore(pid, cpu_mask);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in setExclusiveCore:" + e);
        }
    }

    public void setUidInfo(int uid, int fromUid) {
        //log("setUidIndo");
        int result = -1;
        try {
            init();
            if(sService != null)
                sService.setUidInfo(uid, fromUid);
        } catch (RemoteException e) {
            loge("ERR: RemoteException in setUidInfo:" + e);
        }
    }

    /* AMS event handler */
    public void amsBoostResume(BeforeActivitySwitch data) {
        String lastPkg = data.getString(BeforeActivitySwitch.Index.lastResumedPackageName);
        String nextPkg = data.getString(BeforeActivitySwitch.Index.nextResumedPackageName);
        //log("amsBoostResume, last:" + lastPkg + ", next:" + nextPkg);

        Trace.asyncTraceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "amPerfBoost", 0);
        int boostMode = getPackAttr(nextPkg, IPerfServiceWrapper.CMD_GET_PACK_BOOST_MODE);
        if ( boostMode > 0) {
            int timeout = getPackAttr(nextPkg, IPerfServiceWrapper.CMD_GET_PACK_BOOST_TIMEOUT);
            if(timeout > 0) {
                boostEnableTimeout(boostMode, timeout);
            }
        }
        else if (lastPkg == null || !lastPkg.equalsIgnoreCase(nextPkg)) {
            boostEnableTimeoutMs(IPerfServiceWrapper.SCN_PACKAGE_SWITCH, AMS_BOOST_TIME);
        }
        else {
            boostEnableTimeoutMs(IPerfServiceWrapper.SCN_APP_SWITCH, AMS_BOOST_TIME);
        }
    }

    public void amsBoostProcessCreate(AMEventHookData.StartProcess data) {
        String reason = data.getString(AMEventHookData.StartProcess.Index.reason);
        if(reason.compareTo("activity") == 0) {
            String nextPkg = data.getString(StartProcess.Index.packageName);
            //log("amsBoostProcessCreate");
            Trace.asyncTraceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "amPerfBoost", 0);
            int timeout = getPackAttr(nextPkg, IPerfServiceWrapper.CMD_GET_PACK_BOOST_TIMEOUT);
            if(timeout > 0) {
                boostEnableTimeout(IPerfServiceWrapper.SCN_APP_LAUNCH, timeout);
            }
            else {
                boostEnableTimeoutMs(IPerfServiceWrapper.SCN_PROCESS_CREATE, AMS_BOOST_TIME);
            }
            //appBoostEnable(data.getString(StartProcess.Index.packageName));
        }
    }

    public void amsBoostStop() {
        //log("amsBoostStop");
        boostDisable(IPerfServiceWrapper.SCN_PROCESS_CREATE);
        boostDisable(IPerfServiceWrapper.SCN_APP_SWITCH);
        boostDisable(IPerfServiceWrapper.SCN_PACKAGE_SWITCH);
        Trace.asyncTraceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER, "amPerfBoost", 0);
    }

    public void onAfterActivityResumed(AfterActivityResumed data) {
        setFavorPid(data.getInt(AfterActivityResumed.Index.pid));
        setUidInfo(data.getInt(AfterActivityResumed.Index.processUid),
            data.getInt(AfterActivityResumed.Index.callerUid));
        notifyAppState(data.getString(AfterActivityResumed.Index.packageName),
            data.getString(AfterActivityResumed.Index.activityName),
            IPerfServiceWrapper.STATE_RESUMED,
            data.getInt(AfterActivityResumed.Index.pid));
    }

    public void onAfterActivityPaused(AfterActivityPaused data) {
        notifyAppState(data.getString(AfterActivityResumed.Index.packageName),
            data.getString(AfterActivityResumed.Index.activityName),
            IPerfServiceWrapper.STATE_PAUSED,
            data.getInt(AfterActivityPaused.Index.pid));
    }

    public void onAfterActivityStopped(AfterActivityStopped data) {
        notifyAppState(data.getString(AfterActivityResumed.Index.packageName),
            data.getString(AfterActivityResumed.Index.activityName),
            IPerfServiceWrapper.STATE_STOPPED,
            data.getInt(AfterActivityStopped.Index.pid));
    }

    public void onAfterActivityDestroyed(AfterActivityDestroyed data) {
        notifyAppState(data.getString(AfterActivityResumed.Index.packageName),
            data.getString(AfterActivityResumed.Index.activityName),
            IPerfServiceWrapper.STATE_DESTROYED,
            data.getInt(AfterActivityDestroyed.Index.pid));
    }

    public int reloadWhiteList() {
        //log("reloadWhiteList");
        try {
            init();
            if(sService != null)
                sService.reloadWhiteList();
        } catch (RemoteException e) {
            loge("ERR: RemoteException in reInit:" + e);
        }
        return 1;
    }

    private void log(String info) {
        Log.d("@M_" + TAG, "[PerfServiceWrapper] " + info + " ");
    }

    private void loge(String info) {
        Log.e("@M_" + TAG, "[PerfServiceWrapper] ERR: " + info + " ");
    }
}

