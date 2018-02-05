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

//import java.io.File;
//import java.io.OutputStreamWriter;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.FileInputStream;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.util.*;

//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.BroadcastReceiver;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.Looper;
//import android.os.Message;
//import android.os.Process;
//import android.util.Log;
//import static junit.framework.Assert.*;

public interface IPerfServiceManager {

    public void systemReady();

    public void boostEnable(int scenario);
    public void boostDisable(int scenario);
    public void boostEnableTimeout(int scenario, int timeout);
    public void boostEnableTimeoutMs(int scenario, int timeout_ms);
    public void notifyAppState(String packName, String className, int state, int pid);

    public int  userReg(int scn_core, int scn_freq, int pid, int tid);
    public int  userRegBigLittle(int scn_core_big, int scn_freq_big, int scn_core_little, int scn_freq_little, int pid, int tid);
    public void userUnreg(int handle);

    public int  userGetCapability(int cmd);

    public int  userRegScn(int pid, int tid);
    public void userRegScnConfig(int handle, int cmd, int param_1, int param_2, int param_3, int param_4);
    public void userUnregScn(int handle);

    public void userEnable(int handle);
    public void userEnableTimeout(int handle, int timeout);
    public void userEnableTimeoutMs(int handle, int timeout_ms);
    public void userEnableAsync(int handle);
    public void userEnableTimeoutAsync(int handle, int timeout);
    public void userEnableTimeoutMsAsync(int handle, int timeout_ms);
    public void userDisable(int handle);

    public void userResetAll();
    public void userDisableAll();
    public void userRestoreAll();

    public void dumpAll();

    public void setFavorPid(int pid);
    public void restorePolicy(int pid);
    public int  getLastBoostPid();
    public void notifyFrameUpdate(int level);
    public void notifyDisplayType(int type);
    public void notifyUserStatus(int type, int status);

    public int  getClusterInfo(int cmd, int id);
    public void levelBoost(int level);
    public int  getPackAttr(String packName, int cmd);
    public void appBoostEnable(String packName);
    public String  getGiftAttr(String packName, String attrName);
    public int reloadWhiteList();
    public void setExclusiveCore(int pid, int cpu_mask);
    public void setUidInfo(int uid, int fromUid);
}

