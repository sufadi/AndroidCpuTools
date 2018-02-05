/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein
 * is confidential and proprietary to MediaTek Inc. and/or its licensors.
 * Without the prior written permission of MediaTek inc. and/or its licensors,
 * any reproduction, modification, use or disclosure of MediaTek Software,
 * and information contained herein, in whole or in part, shall be strictly prohibited.
 */
/* MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER ON
 * AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
 * NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
 * SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
 * SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES TO LOOK ONLY TO SUCH
 * THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. RECEIVER EXPRESSLY ACKNOWLEDGES
 * THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES
 * CONTAINED IN MEDIATEK SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK
 * SOFTWARE RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND
 * CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
 * AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
 * OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY RECEIVER TO
 * MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek Software")
 * have been modified by MediaTek Inc. All revisions are subject to any receiver's
 * applicable license agreements with MediaTek Inc.
 */


#define LOG_TAG "PerfService"
#define ATRACE_TAG ATRACE_TAG_PERF

#include "utils/Log.h"
#include "setBrightnessValueBg.h"

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>

#include <unistd.h>
#include <utils/String16.h>

#include <cutils/log.h>
#include <cutils/properties.h>
#include <binder/IServiceManager.h>
#include <binder/IPCThreadState.h>
#include <dlfcn.h>
#include <utils/Trace.h>
//#include <binder/IInterface.h>

namespace android
{

/* It should be sync with IPerfService.aidl */
enum {
    TRANSACTION_boostEnable = IBinder::FIRST_CALL_TRANSACTION,
    TRANSACTION_boostDisable,
    TRANSACTION_boostEnableTimeout,
    TRANSACTION_boostEnableTimeoutMs,
    TRANSACTION_notifyAppState,
    TRANSACTION_userReg,
    TRANSACTION_userRegBigLittle,
    TRANSACTION_userUnreg,
    TRANSACTION_userGetCapability,
    TRANSACTION_userRegScn,
    TRANSACTION_userRegScnConfig,
    TRANSACTION_userUnregScn,
    TRANSACTION_userEnable,
    TRANSACTION_userEnableTimeout,
    TRANSACTION_userEnableTimeoutMs,
    TRANSACTION_userEnableAsync,
    TRANSACTION_userEnableTimeoutAsync,
    TRANSACTION_userEnableTimeoutMsAsync,
    TRANSACTION_userDisable,
    TRANSACTION_userResetAll,
    TRANSACTION_userDisableAll,
    TRANSACTION_userRestoreAll,
    TRANSACTION_dumpAll,
    TRANSACTION_setFavorPid,
    TRANSACTION_restorePolicy,
    TRANSACTION_getLastBoostPid,
    TRANSACTION_notifyFrameUpdate,
    TRANSACTION_notifyDisplayType,
    TRANSACTION_notifyUserStatus,
    TRANSACTION_getClusterInfo,
    TRANSACTION_levelBoost,
    TRANSACTION_getPackAttr,
    TRANSACTION_NA_1,
    TRANSACTION_getGiftAttr,
    TRANSACTION_reloadWhiteList,
    TRANSACTION_setExclusiveCore,
};

static char packName[128] = "";
static sp<IServiceManager> sm ;
static sp<IBinder> binder = NULL;
static Mutex sMutex;

#define MTK_LEVEL_BOOST_SUPPORT

#define BOOT_INFO_FILE "/sys/class/BOOT/BOOT/boot/boot_mode"
#define GIFTATTR_DEBUGPROP "debug.perf.giftEnable"
#define GIFT_ATTR_DEBUGSTR "Enable DEBUG_GIFTATTR"
#define RENDER_THREAD_UPDATE_DURATION   250000000
#define RENDER_THREAD_CHECK_DURATION    200000000

#define RENDER_BIT    0x800000
#define RENDER_MASK   0x7FFFFF

#ifdef DEBUG_GIFTATTR
static int gGIFT_ATTR_DEBUG = 1;
#else
static int gGIFT_ATTR_DEBUG = 0;
#endif

int (*perfCalcBoostLevel)(float) = NULL;
typedef int (*calc_boost_level)(float);

#define LEVEL_BOOST_NOP 0xffff

static int check_meta_mode(void)
{
    char bootMode[4];
    int fd;
    //check if in Meta mode
    fd = open(BOOT_INFO_FILE, O_RDONLY);
    if(fd < 0) {
        return 0; // not meta mode
    }

    if(read(fd, bootMode, 4) < 1) {
        close(fd);
        return 0;
    }

    if (bootMode[0] == 0x31 || bootMode[0] == 0x34) {
        close(fd);
        return 1; // meta mode, factory mode
    }

    close(fd);
    return 0;
}

static void init(void)
{
    sp<IBinder> newBinder = NULL;

    Mutex::Autolock lock(sMutex);
    if(check_meta_mode())
        return;

    sm = defaultServiceManager();
    //binder = sm->getService(String16("mtk-perfservice"));
    newBinder = sm->checkService(String16("mtk-perfservice")); // use check to avoid null binder
    if(newBinder != binder) {
        binder = newBinder;
        ALOGI("setBrightnessValueBg api init");
    }
}

extern "C"
void setBrightnessValueBg_boostEnable(int scenario)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_boostEnable:%d", scenario);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(scenario);
        binder->transact(TRANSACTION_boostEnable ,data,&reply); // should sync with IPerfService
    }
}

extern "C"
void setBrightnessValueBg_boostDisable(int scenario)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_boostDisable:%d", scenario);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(scenario);
        binder->transact(TRANSACTION_boostDisable ,data,&reply);
    }
}

extern "C"
void setBrightnessValueBg_boostEnableTimeout(int scenario, int timeout)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_boostEnableTimeout:%d, %d", scenario, timeout);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(scenario);
        data.writeInt32(timeout);
        binder->transact(TRANSACTION_boostEnableTimeout ,data,&reply);
    }
}

extern "C"
void setBrightnessValueBg_boostEnableTimeoutMs(int scenario, int timeout_ms)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_boostEnableTimeoutMs:%d, %d", scenario, timeout_ms);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(scenario);
        data.writeInt32(timeout_ms);
        binder->transact(TRANSACTION_boostEnableTimeoutMs ,data,&reply);
    }
}

extern "C"
void setBrightnessValueBg_boostEnableAsync(int scenario)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_boostEnableAsync:%d", scenario);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(scenario);
        binder->transact(TRANSACTION_boostEnable ,data,&reply,IBinder::FLAG_ONEWAY); // should sync with IPerfService
    }
}

extern "C"
void setBrightnessValueBg_boostDisableAsync(int scenario)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_boostDisableAsync:%d", scenario);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(scenario);
        binder->transact(TRANSACTION_boostDisable ,data,&reply,IBinder::FLAG_ONEWAY);
    }
}

extern "C"
void setBrightnessValueBg_boostEnableTimeoutAsync(int scenario, int timeout)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_boostEnableTimeoutAsync:%d, %d", scenario, timeout);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(scenario);
        data.writeInt32(timeout);
        binder->transact(TRANSACTION_boostEnableTimeout ,data,&reply,IBinder::FLAG_ONEWAY);
    }
}

extern "C"
void setBrightnessValueBg_boostEnableTimeoutMsAsync(int scenario, int timeout_ms)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_boostEnableTimeoutMsAsync:%d, %d", scenario, timeout_ms);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(scenario);
        data.writeInt32(timeout_ms);
        binder->transact(TRANSACTION_boostEnableTimeoutMs ,data,&reply,IBinder::FLAG_ONEWAY);
    }
}

extern "C"
int setBrightnessValueBg_userReg(int scn_core, int scn_freq)
{
    Parcel data, reply;
    int    err, handle = -1, pid=-1, tid=-1;
    init();

    pid = (int)getpid();
    tid = (int)gettid();

    ALOGI("setBrightnessValueBg_userReg: %d, %d (pid:%d, tid:%d)", scn_core, scn_freq, pid, tid);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(scn_core);
        data.writeInt32(scn_freq);
        data.writeInt32(pid);
        data.writeInt32(tid);
        binder->transact(TRANSACTION_userReg ,data,&reply); // should sync with IPerfService
        err = reply.readExceptionCode();
        if(err < 0) {
            ALOGI("setBrightnessValueBg_userReg err:%d", err);
            return -1;
        }
        handle = reply.readInt32();
    }
    return handle;
}

extern "C"
int setBrightnessValueBg_userRegBigLittle(int scn_core_big, int scn_freq_big, int scn_core_little, int scn_freq_little)
{
    Parcel data, reply;
    int    err, handle = -1, pid=-1, tid=-1;
    init();

    pid = (int)getpid();
    tid = (int)gettid();

    ALOGI("setBrightnessValueBg_userRegBigLittle: %d, %d, %d, %d (pid:%d, tid:%d)", scn_core_little, scn_freq_little, scn_core_big, scn_freq_big, pid, tid);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(scn_core_big);
        data.writeInt32(scn_freq_big);
        data.writeInt32(scn_core_little);
        data.writeInt32(scn_freq_little);
        data.writeInt32(pid);
        data.writeInt32(tid);
        binder->transact(TRANSACTION_userRegBigLittle ,data,&reply); // should sync with IPerfService
        err = reply.readExceptionCode();
        if(err < 0) {
            ALOGI("setBrightnessValueBg_userRegBigLittle err:%d", err);
            return -1;
        }
        handle = reply.readInt32();
    }
    return handle;
}

extern "C"
void setBrightnessValueBg_userUnreg(int handle)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userUnreg:%d", handle);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(handle);
        binder->transact(TRANSACTION_userUnreg ,data,&reply); // should sync with IPerfService
    }
}

extern "C"
int setBrightnessValueBg_userGetCapability(int cmd)
{
    Parcel data, reply;
    int err, value = -1;
    init();

    ALOGI("setBrightnessValueBg_userGetCapability: %d", cmd);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(cmd);
        binder->transact(TRANSACTION_userGetCapability ,data,&reply); // should sync with IPerfService
        err = reply.readExceptionCode();
        if(err < 0) {
            ALOGI("setBrightnessValueBg_userGetCapability err:%d", err);
            return -1;
        }
        value = reply.readInt32();
    }
    return value;
}

extern "C"
int setBrightnessValueBg_userRegScn(void)
{
    Parcel data, reply;
    int    err, handle = -1, pid=-1, tid=-1;
    init();

    pid = (int)getpid();
    tid = (int)gettid();

    //ALOGI("setBrightnessValueBg_userRegScn: (pid:%d, tid:%d)", pid, tid);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(pid);
        data.writeInt32(tid);
        binder->transact(TRANSACTION_userRegScn ,data,&reply); // should sync with IPerfService
        err = reply.readExceptionCode();
        if(err < 0) {
            ALOGI("setBrightnessValueBg_userRegScn err:%d", err);
            return -1;
        }
        handle = reply.readInt32();
    }
    ALOGI("setBrightnessValueBg_userRegScn: handle:%d", handle);
    return handle;
}

extern "C"
void setBrightnessValueBg_userRegScnConfig(int handle, int cmd, int param_1, int param_2, int param_3, int param_4)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userRegScnConfig: handle:%d, cmd:%d, p1:%d, p2:%d, p3:%d, p4:%d", handle, cmd, param_1, param_2, param_3, param_4);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(handle);
        data.writeInt32(cmd);
        data.writeInt32(param_1);
        data.writeInt32(param_2);
        data.writeInt32(param_3);
        data.writeInt32(param_4);
        binder->transact(TRANSACTION_userRegScnConfig,data,&reply); // should sync with IPerfService
    }
}

extern "C"
void setBrightnessValueBg_userRegScnConfigAsync(int handle, int cmd, int param_1, int param_2, int param_3, int param_4)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userRegScnConfigAsync: handle:%d, cmd:%d, p1:%d, p2:%d, p3:%d, p4:%d", handle, cmd, param_1, param_2, param_3, param_4);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(handle);
        data.writeInt32(cmd);
        data.writeInt32(param_1);
        data.writeInt32(param_2);
        data.writeInt32(param_3);
        data.writeInt32(param_4);
        binder->transact(TRANSACTION_userRegScnConfig,data,&reply,IBinder::FLAG_ONEWAY); // should sync with IPerfService
    }
}

extern "C"
void setBrightnessValueBg_userUnregScn(int handle)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userUnregScn: handle:%d", handle);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(handle);
        binder->transact(TRANSACTION_userUnregScn,data,&reply); // should sync with IPerfService
    }
}

extern "C"
void setBrightnessValueBg_userEnable(int handle)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userEnable:%d", handle);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(handle);
        binder->transact(TRANSACTION_userEnable ,data,&reply); // should sync with IPerfService
    }
}

extern "C"
void setBrightnessValueBg_userEnableTimeout(int handle, int timeout)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userEnableTimeout:%d, %d", handle, timeout);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(handle);
        data.writeInt32(timeout);
        binder->transact(TRANSACTION_userEnableTimeout ,data,&reply);
    }
}

extern "C"
void setBrightnessValueBg_userEnableTimeoutMs(int handle, int timeout_ms)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userEnableTimeoutMs:%d, %d", handle, timeout_ms);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(handle);
        data.writeInt32(timeout_ms);
        binder->transact(TRANSACTION_userEnableTimeoutMs ,data,&reply);
    }
}

extern "C"
void setBrightnessValueBg_userEnableAsync(int handle)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userEnableAsync:%d", handle);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(handle);
        binder->transact(TRANSACTION_userEnableAsync ,data,&reply,IBinder::FLAG_ONEWAY); // should sync with IPerfService
    }
}

extern "C"
void setBrightnessValueBg_userEnableTimeoutAsync(int handle, int timeout)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userEnableTimeoutAsync:%d, %d", handle, timeout);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(handle);
        data.writeInt32(timeout);
        binder->transact(TRANSACTION_userEnableTimeoutAsync ,data,&reply,IBinder::FLAG_ONEWAY);
    }
}

extern "C"
void setBrightnessValueBg_userEnableTimeoutMsAsync(int handle, int timeout_ms)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userEnableTimeoutMsAsync:%d, %d", handle, timeout_ms);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(handle);
        data.writeInt32(timeout_ms);
        binder->transact(TRANSACTION_userEnableTimeoutMsAsync ,data,&reply,IBinder::FLAG_ONEWAY);
    }
}

extern "C"
void setBrightnessValueBg_userDisable(int handle)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userDisable:%d", handle);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(handle);
        binder->transact(TRANSACTION_userDisable ,data,&reply);
    }
}

extern "C"
void setBrightnessValueBg_userDisableAsync(int handle)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userDisableAsync:%d", handle);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(handle);
        binder->transact(TRANSACTION_userDisable ,data,&reply,IBinder::FLAG_ONEWAY);
    }
}

extern "C"
void setBrightnessValueBg_userResetAll(void)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userResetAll");

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        binder->transact(TRANSACTION_userResetAll ,data,&reply);
    }
}

extern "C"
void setBrightnessValueBg_userDisableAll(void)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_userDisableAll");

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        binder->transact(TRANSACTION_userDisableAll ,data,&reply);
    }
}

extern "C"
void setBrightnessValueBg_dumpAll(void)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_dumpAll");

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        binder->transact(TRANSACTION_dumpAll ,data,&reply);
    }
}

extern "C"
void setBrightnessValueBg_setFavorPid(int pid)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_setFavorPid: pid:%d", pid);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(pid);
        binder->transact(TRANSACTION_setFavorPid,data,&reply,IBinder::FLAG_ONEWAY);
    }
}

extern "C"
void setBrightnessValueBg_setBoostThread(void)
{
    Parcel data, reply;
    int tid;
    init();

    tid = (int)gettid();
    ALOGI("setBrightnessValueBg_setBoostThread: pid:%d, tid:%d", (int)getpid(), tid);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(tid | RENDER_BIT);
        binder->transact(TRANSACTION_setFavorPid,data,&reply,IBinder::FLAG_ONEWAY);
    }
}

extern "C"
void setBrightnessValueBg_restoreBoostThread(void)
{
    Parcel data, reply;
    int tid;
    init();

    tid = (int)gettid();
    ALOGI("setBrightnessValueBg_restoreBoostThread: pid:%d, tid:%d", (int)getpid(), tid);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(tid | RENDER_BIT);
        binder->transact(TRANSACTION_restorePolicy,data,&reply,IBinder::FLAG_ONEWAY);
    }
}

extern "C"
void setBrightnessValueBg_notifyFrameUpdate(int level)
{
    Parcel data, reply;
    static nsecs_t mPreviousTime = 0;
    nsecs_t now = systemTime(CLOCK_MONOTONIC);
    //static int set_tid = 0;

    if(mPreviousTime == 0 || (now - mPreviousTime) > RENDER_THREAD_UPDATE_DURATION) { // 400ms
        init(); // init before binder call
        //ALOGI("setBrightnessValueBg_notifyFrameUpdate:%d", (now - mPreviousTime)/1000000);
        if(binder!=NULL) {
            data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
            data.writeInt32(level);
            binder->transact(TRANSACTION_notifyFrameUpdate ,data,&reply,IBinder::FLAG_ONEWAY);
        }
        mPreviousTime = now;
    }
}

extern "C"
void setBrightnessValueBg_notifyRenderTime(float time)
{
    static void *handle;
    void  *func;
    int    boost_level = LEVEL_BOOST_NOP, first_frame = 0;
    static nsecs_t mPreviousTime = 0;
    char buff[64];
    nsecs_t now = systemTime(CLOCK_MONOTONIC);
    //init();

    setBrightnessValueBg_notifyFrameUpdate(1);
    //ALOGI("setBrightnessValueBg_notifyRenderTime: time:%f", time);

#if 0
    if(handle == NULL) {
        handle = dlopen("libperfservice.so", RTLD_NOW);
        func = dlsym(handle, "perfCalcBoostLevel");
        perfCalcBoostLevel = reinterpret_cast<calc_boost_level>(func);
        if (perfCalcBoostLevel == NULL) {
            ALOGE("perfCalcBoostLevel init fail!");
        }
    }

    if(mPreviousTime == 0 || (now - mPreviousTime) > RENDER_THREAD_CHECK_DURATION) { // exceed RENDER_THREAD_CHECK_DURATION => first frame
        first_frame = 1;
    }
    mPreviousTime = now;

    if(first_frame) {
        //ALOGI("setBrightnessValueBg_notifyRenderTime: first_frame");
        if(perfCalcBoostLevel)
            perfCalcBoostLevel(0);
        return;
    }

    if(perfCalcBoostLevel) {
        boost_level = perfCalcBoostLevel(time);
    }

    // init value
    //sprintf(buff, "notifyRenderTime:%.2f", time);

    if(boost_level == LEVEL_BOOST_NOP)
        return;

    sprintf(buff, "levelBoost:%d", boost_level);
    ATRACE_BEGIN(buff);
#if defined(MTK_LEVEL_BOOST_SUPPORT)
    setBrightnessValueBg_levelBoost(boost_level);
#endif
    ATRACE_END();
#endif
}

extern "C"
void setBrightnessValueBg_notifyDisplayType(int type)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_notifyDisplayType:%d", type);
    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(type);
        binder->transact(TRANSACTION_notifyDisplayType ,data,&reply,IBinder::FLAG_ONEWAY);
    }
}

extern "C"
void setBrightnessValueBg_notifyUserStatus(int type, int status)
{
    Parcel data, reply;
    init();

    ALOGI("setBrightnessValueBg_notifyUserStatus:%d, status:%d", type, status);
    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(type);
        data.writeInt32(status);
        binder->transact(TRANSACTION_notifyUserStatus ,data,&reply,IBinder::FLAG_ONEWAY);
    }
}

extern "C"
int setBrightnessValueBg_getLastBoostPid()
{
    Parcel data, reply;
    int err;
    //const int handle = 1;
    init();

    //ALOGI("setBrightnessValueBg_getLastBoostPid");
    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        binder->transact(TRANSACTION_getLastBoostPid,data,&reply);
        err = reply.readExceptionCode();
        if(err < 0) {
            ALOGI("setBrightnessValueBg_getLastBoostPid err:%d", err);
            return -1;
        }
        return reply.readInt32();
    }
    return -1;
}

extern "C"
const char* setBrightnessValueBg_getPackName()
{
#if 1
    int pid, ret;
    char spid[8];
    char path[64] = "/proc/";
    FILE *ifp;

    pid = setBrightnessValueBg_getLastBoostPid();
    //itoa(pid, spid, 10);
    sprintf(spid, "%d", pid);

    strncat(path, spid, (63 - strlen(path)));
    strncat(path, "/cmdline", (63 - strlen(path)));
    if ((ifp = fopen(path,"r")) == NULL)
        return "";
    ret = fscanf(ifp, "%s", packName);
    fclose(ifp);
    if (ret == 1)
        return packName;
    else
        return "";
#else
    return packName;
#endif
}

extern "C"
int setBrightnessValueBg_getClusterInfo(int cmd, int id)
{
    Parcel data, reply;
    int err, value = -1;
    init();

    ALOGI("setBrightnessValueBg_getClusterInfo: %d, %d", cmd, id);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(cmd);
        data.writeInt32(id);
        binder->transact(TRANSACTION_getClusterInfo,data,&reply); // should sync with IPerfService
        err = reply.readExceptionCode();
        if(err < 0) {
            ALOGI("setBrightnessValueBg_getClusterInfo err:%d", err);
            return -1;
        }
        value = reply.readInt32();
    }
    return value;
}

extern "C"
void setBrightnessValueBg_levelBoost(int level)
{
    Parcel data, reply;
    init();

    //ALOGI("setBrightnessValueBg_levelBoost:%d", level);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(level);
        binder->transact(TRANSACTION_levelBoost ,data,&reply,IBinder::FLAG_ONEWAY);
    }
}

extern "C"
int setBrightnessValueBg_getPackAttr(const char* packName, int cmd)
{
    Parcel data, reply;
    int err, value = -1;
    init();

    ALOGI("setBrightnessValueBg_getPackAttr: %s, %d", packName, cmd);

    if(packName == NULL)
        return -1;

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeString16(String16(packName));
        data.writeInt32(cmd);
        binder->transact(TRANSACTION_getPackAttr,data,&reply); // should sync with IPerfService
        err = reply.readExceptionCode();
        if(err < 0) {
            ALOGI("setBrightnessValueBg_getPackAttr err:%d", err);
            return -1;
        }
        value = reply.readInt32();
    }
    return value;
}


extern "C"
int setBrightnessValueBg_getGiftAttr(const char* packName,char* attrName,char* attrValue, int attrLen)
{
    Parcel data, reply;
    int err, value = -1;
    String16 str16;
    char buf[100];

    init();

    if(packName == NULL || attrName == NULL || attrLen == 0)
        return -1;

    if((property_get(GIFTATTR_DEBUGPROP, buf, NULL)!=0) || (gGIFT_ATTR_DEBUG == 1)){
        memset(attrValue, 0, attrLen);
        memcpy(attrValue, GIFT_ATTR_DEBUGSTR, strlen(GIFT_ATTR_DEBUGSTR));
        value = strlen(attrValue);
        ALOGE("Debug Native setBrightnessValueBg_getGiftAttr");
    }
    else{
        if(binder!=NULL) {
            data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
            data.writeString16(String16(packName));
            data.writeString16(String16(attrName));
            binder->transact(TRANSACTION_getGiftAttr,data,&reply); // should sync with IPerfService
            err = reply.readExceptionCode();
            if(err < 0) {
                ALOGI("setBrightnessValueBg_getPackAttr err:%d", err);
                value = -1;
            }
            str16 = reply.readString16();
            if(strncmp(String8(str16).string(), "GiftEmpty", 9) != 0){
                memset(attrValue, 0, attrLen);
                value = String8(str16).length();
                if(value >= attrLen)
                    value = attrLen-1;
                memcpy(attrValue, (char *)String8(str16).string(), value);
            }
            else{
                value = 0;
            }
        }
    }

    return value;
}

extern "C"
int setBrightnessValueBg_reloadWhiteList(void)
{
    Parcel data, reply;
    int err;
    //const int handle = 1;
    init();

    //ALOGI("setBrightnessValueBg_getLastBoostPid");
    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        binder->transact(TRANSACTION_reloadWhiteList,data,&reply);
        err = reply.readExceptionCode();
        if(err < 0) {
            ALOGI("setBrightnessValueBg_reloadWhiteList err:%d", err);
            return -1;
        }
        return reply.readInt32();
    }
    return -1;
}

extern "C"
void setBrightnessValueBg_setExclusiveCore(int pid, int cpu_mask)
{
    Parcel data, reply;
    int err, value = -1;
    init();

    ALOGI("setBrightnessValueBg_setExclusiveCore: %d, %x", pid, cpu_mask);

    if(binder!=NULL) {
        data.writeInterfaceToken(String16("com.mediatek.perfservice.IPerfService"));
        data.writeInt32(pid);
        data.writeInt32(cpu_mask);
        binder->transact(TRANSACTION_setExclusiveCore,data,&reply); // should sync with IPerfService
    }
}

}
