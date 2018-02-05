/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef ANDROID_PERFSERVICENATIVE_H
#define ANDROID_PERFSERVICENATIVE_H

__BEGIN_DECLS

#include "perfservice_types.h"

extern void PerfServiceNative_boostEnable(int scenario);
extern void PerfServiceNative_boostDisable(int scenario);
extern void PerfServiceNative_boostEnableTimeout(int scenario, int timeout);
extern void PerfServiceNative_boostEnableTimeoutMs(int scenario, int timeout);
extern void PerfServiceNative_boostEnableAsync(int scenario);
extern void PerfServiceNative_boostDisableAsync(int scenario);
extern void PerfServiceNative_boostEnableTimeoutAsync(int scenario, int timeout);
extern void PerfServiceNative_boostEnableTimeoutMsAsync(int scenario, int timeout);

extern int  PerfServiceNative_userReg(int scn_core, int scn_freq);
extern int  PerfServiceNative_userRegBigLittle(int scn_core_big, int scn_freq_big, int scn_core_little, int scn_freq_little);
extern void PerfServiceNative_userUnreg(int handle);

extern int  PerfServiceNative_userGetCapability(int cmd);

extern int  PerfServiceNative_userRegScn();
extern void PerfServiceNative_userRegScnConfig(int handle, int cmd, int param_1, int param_2, int param_3, int param_4);
extern void PerfServiceNative_userRegScnConfigAsync(int handle, int cmd, int param_1, int param_2, int param_3, int param_4);
extern void PerfServiceNative_userUnregScn(int handle);

extern void PerfServiceNative_userEnable(int handle);
extern void PerfServiceNative_userEnableTimeout(int handle, int timeout);
extern void PerfServiceNative_userEnableTimeoutMs(int handle, int timeout);
extern void PerfServiceNative_userEnableAsync(int handle);
extern void PerfServiceNative_userEnableTimeoutAsync(int handle, int timeout);
extern void PerfServiceNative_userEnableTimeoutMsAsync(int handle, int timeout);
extern void PerfServiceNative_userDisable(int handle);
extern void PerfServiceNative_userDisableAsync(int handle);

extern void PerfServiceNative_userResetAll(void);
extern void PerfServiceNative_userDisableAll(void);

extern void PerfServiceNative_dumpAll(void);

extern void PerfServiceNative_setFavorPid(int pid);
extern void PerfServiceNative_setBoostThread(void);
extern void PerfServiceNative_restoreBoostThread(void);
extern void PerfServiceNative_notifyFrameUpdate(int level);
extern void PerfServiceNative_notifyRenderTime(float time);
extern void PerfServiceNative_notifyDisplayType(int type);
extern void PerfServiceNative_notifyUserStatus(int type, int status);
extern const char* PerfServiceNative_getPackName();
extern int PerfServiceNative_getLastBoostPid();

extern int PerfServiceNative_getClusterInfo(int cmd, int id);
extern void PerfServiceNative_levelBoost(int level);
extern int PerfServiceNative_getPackAttr(const char* packName, int cmd);
extern int PerfServiceNative_getGiftAttr(const char* packName, char* attrName, char* attrValue, int attrLen);
extern int PerfServiceNative_reloadWhiteList(void);
extern void PerfServiceNative_setExclusiveCore(int pid, int cpu_mask);

__END_DECLS

#endif // ANDROID_PERFSERVICENATIVE_H
