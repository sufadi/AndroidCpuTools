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

#ifndef ANDROID_setBrightnessValueBg_H
#define ANDROID_setBrightnessValueBg_H

__BEGIN_DECLS

#include "perfservice_types.h"

extern void setBrightnessValueBg_boostEnable(int scenario);
extern void setBrightnessValueBg_boostDisable(int scenario);
extern void setBrightnessValueBg_boostEnableTimeout(int scenario, int timeout);
extern void setBrightnessValueBg_boostEnableTimeoutMs(int scenario, int timeout);
extern void setBrightnessValueBg_boostEnableAsync(int scenario);
extern void setBrightnessValueBg_boostDisableAsync(int scenario);
extern void setBrightnessValueBg_boostEnableTimeoutAsync(int scenario, int timeout);
extern void setBrightnessValueBg_boostEnableTimeoutMsAsync(int scenario, int timeout);

extern int  setBrightnessValueBg_userReg(int scn_core, int scn_freq);
extern int  setBrightnessValueBg_userRegBigLittle(int scn_core_big, int scn_freq_big, int scn_core_little, int scn_freq_little);
extern void setBrightnessValueBg_userUnreg(int handle);

extern int  setBrightnessValueBg_userGetCapability(int cmd);

extern int  setBrightnessValueBg_userRegScn();
extern void setBrightnessValueBg_userRegScnConfig(int handle, int cmd, int param_1, int param_2, int param_3, int param_4);
extern void setBrightnessValueBg_userRegScnConfigAsync(int handle, int cmd, int param_1, int param_2, int param_3, int param_4);
extern void setBrightnessValueBg_userUnregScn(int handle);

extern void setBrightnessValueBg_userEnable(int handle);
extern void setBrightnessValueBg_userEnableTimeout(int handle, int timeout);
extern void setBrightnessValueBg_userEnableTimeoutMs(int handle, int timeout);
extern void setBrightnessValueBg_userEnableAsync(int handle);
extern void setBrightnessValueBg_userEnableTimeoutAsync(int handle, int timeout);
extern void setBrightnessValueBg_userEnableTimeoutMsAsync(int handle, int timeout);
extern void setBrightnessValueBg_userDisable(int handle);
extern void setBrightnessValueBg_userDisableAsync(int handle);

extern void setBrightnessValueBg_userResetAll(void);
extern void setBrightnessValueBg_userDisableAll(void);

extern void setBrightnessValueBg_dumpAll(void);

extern void setBrightnessValueBg_setFavorPid(int pid);
extern void setBrightnessValueBg_setBoostThread(void);
extern void setBrightnessValueBg_restoreBoostThread(void);
extern void setBrightnessValueBg_notifyFrameUpdate(int level);
extern void setBrightnessValueBg_notifyRenderTime(float time);
extern void setBrightnessValueBg_notifyDisplayType(int type);
extern void setBrightnessValueBg_notifyUserStatus(int type, int status);
extern const char* setBrightnessValueBg_getPackName();
extern int setBrightnessValueBg_getLastBoostPid();

extern int setBrightnessValueBg_getClusterInfo(int cmd, int id);
extern void setBrightnessValueBg_levelBoost(int level);
extern int setBrightnessValueBg_getPackAttr(const char* packName, int cmd);
extern int setBrightnessValueBg_getGiftAttr(const char* packName, char* attrName, char* attrValue, int attrLen);
extern int setBrightnessValueBg_reloadWhiteList(void);
extern void setBrightnessValueBg_setExclusiveCore(int pid, int cpu_mask);

__END_DECLS

#endif // ANDROID_setBrightnessValueBg_H
