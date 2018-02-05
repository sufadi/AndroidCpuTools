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
 * MediaTek Inc. (C) 2016. All rights reserved.
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
package com.mediatek.am;

/**
 * M: ActivityManager AMEventHook data classes.
 *
 * {@hide}
 */
public class AMEventHookData {
    /**
     * Data type when AM_EndOfAMSCtor happened.
     */
    public static class EndOfAMSCtor extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static EndOfAMSCtor createInstance() {
            synchronized (EndOfAMSCtor.class) {
                return new EndOfAMSCtor();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_EndOfErrorDumpThread happened.
     */
    public static class EndOfErrorDumpThread extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            type,
            info,
            pid,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static EndOfErrorDumpThread createInstance() {
            synchronized (EndOfErrorDumpThread.class) {
                return new EndOfErrorDumpThread();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_BeforeSendBootCompleted happened.
     */
    public static class BeforeSendBootCompleted extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static BeforeSendBootCompleted createInstance() {
            synchronized (BeforeSendBootCompleted.class) {
                return new BeforeSendBootCompleted();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_SystemReady happened.
     */
    public static class SystemReady extends AMEventHookDataManager {
        /**
         * Data.
         *
         * phase   0: On start of AMS systemReady(final Runnable goingCallback) method.
         * phase 200: After printing out "System now ready" log.
         * phase 300: Before calling startHomeActivityLocked to launch home activity.
         * phase 400: Before calling resumeFocusedStackTopActivityLocked to resume top activity.
         */
        public enum Index {
            phase,
            context,
            ams,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static SystemReady createInstance() {
            synchronized (SystemReady.class) {
                return new SystemReady();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_AfterPostEnableScreenAfterBoot happened.
     */
    public static class AfterPostEnableScreenAfterBoot extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            installer,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static AfterPostEnableScreenAfterBoot createInstance() {
            synchronized (AfterPostEnableScreenAfterBoot.class) {
                return new AfterPostEnableScreenAfterBoot();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_SkipStartActivity happened.
     */
    public static class SkipStartActivity extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static SkipStartActivity createInstance() {
            synchronized (SkipStartActivity.class) {
                return new SkipStartActivity();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_BeforeGoHomeWhenNoActivities happened.
     */
    public static class BeforeGoHomeWhenNoActivities extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static BeforeGoHomeWhenNoActivities createInstance() {
            synchronized (BeforeGoHomeWhenNoActivities.class) {
                return new BeforeGoHomeWhenNoActivities();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_EndOfActivityIdle happened.
     */
    public static class EndOfActivityIdle extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            context,
            intent,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static EndOfActivityIdle createInstance() {
            synchronized (EndOfActivityIdle.class) {
                return new EndOfActivityIdle();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_BeforeShowAppErrorDialog happened.
     */
    public static class BeforeShowAppErrorDialog extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            data,
            dialogList,
            context,
            ams,
            processName,
            pkgName,
            uid,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static BeforeShowAppErrorDialog createInstance() {
            synchronized (BeforeShowAppErrorDialog.class) {
                return new BeforeShowAppErrorDialog();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_BeforeSendBroadcast happened.
     */
    public static class BeforeSendBroadcast extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            intent,
            filterStaticList,
            filterDynamicList,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static BeforeSendBroadcast createInstance() {
            synchronized (BeforeSendBroadcast.class) {
                return new BeforeSendBroadcast();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_BeforeActivitySwitch happened.
     */
    public static class BeforeActivitySwitch extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            lastResumedActivityName,
            nextResumedActivityName,
            lastResumedPackageName,
            nextResumedPackageName,
            lastResumedActivityType,
            nextResumedActivityType,
            isNeedToPauseActivityFirst,
            nextTaskPackageList,
            waitProcessPid,
            runningProcRecords,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static BeforeActivitySwitch createInstance() {
            synchronized (BeforeActivitySwitch.class) {
                return new BeforeActivitySwitch();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_AfterActivityResumed happened.
     */
    public static class AfterActivityResumed extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            pid,
            activityName,
            packageName,
            activityType,
            processUid,
            callerUid,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static AfterActivityResumed createInstance() {
            synchronized (AfterActivityResumed.class) {
                return new AfterActivityResumed();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_AfterActivityPaused happened.
     */
    public static class AfterActivityPaused extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            pid,
            activityName,
            packageName,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static AfterActivityPaused createInstance() {
            synchronized (AfterActivityPaused.class) {
                return new AfterActivityPaused();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_AfterActivityStopped happened.
     */
    public static class AfterActivityStopped extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            pid,
            activityName,
            packageName,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static AfterActivityStopped createInstance() {
            synchronized (AfterActivityStopped.class) {
                return new AfterActivityStopped();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_AfterActivityDestroyed happened.
     */
    public static class AfterActivityDestroyed extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            pid,
            activityName,
            packageName,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static AfterActivityDestroyed createInstance() {
            synchronized (AfterActivityDestroyed.class) {
                return new AfterActivityDestroyed();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_WindowsVisible happened.
     */
    public static class WindowsVisible extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static WindowsVisible createInstance() {
            synchronized (WindowsVisible.class) {
                return new WindowsVisible();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_WakefulnessChanged happened.
     */
    public static class WakefulnessChanged extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            wakefulness,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static WakefulnessChanged createInstance() {
            synchronized (WakefulnessChanged.class) {
                return new WakefulnessChanged();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_ReadyToStartService happened.
     */
    public static class ReadyToStartService extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            packageName,
            callerPackageName,
            callerUid,
            bindCallerPkgList,
            bindCallerUidList,
            delayedCallerPkgList,
            delayedCallerUidList,
            reason,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static ReadyToStartService createInstance() {
            synchronized (ReadyToStartService.class) {
                return new ReadyToStartService();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_ReadyToGetProvider happened.
     */
    public static class ReadyToGetProvider extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            packageName,
            callerPackageNameList,
            callerUid,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static ReadyToGetProvider createInstance() {
            synchronized (ReadyToGetProvider.class) {
                return new ReadyToGetProvider();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_ReadyToStartDynamicReceiver happened.
     */
    public static class ReadyToStartDynamicReceiver extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            packageName,
            callerPackageName,
            callerUid,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static ReadyToStartDynamicReceiver createInstance() {
            synchronized (ReadyToStartDynamicReceiver.class) {
                return new ReadyToStartDynamicReceiver();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_BeforeStartProcessForStaticReceiver happened.
     */
    public static class BeforeStartProcessForStaticReceiver extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            packageName,
            callerPackageName,
            callerUid,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static BeforeStartProcessForStaticReceiver createInstance() {
            synchronized (BeforeStartProcessForStaticReceiver.class) {
                return new BeforeStartProcessForStaticReceiver();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_ReadyToStartStaticReceiver happened.
     */
    public static class ReadyToStartStaticReceiver extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            packageName,
            callerPackageName,
            callerUid,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static ReadyToStartStaticReceiver createInstance() {
            synchronized (ReadyToStartStaticReceiver.class) {
                return new ReadyToStartStaticReceiver();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_ReadyToStartComponent happened.
     */
    public static class ReadyToStartComponent extends AMEventHookDataManager {
        /**
        * Data.
        */
        public enum Index {
            packageName,
            uid,
            callerList,
            callerUidList,
            delayedCallerList,
            delayedCallerUidList,
            clientList,
            clientUidList,
            suppressReason,
            suppressAction,
        }

        /**
        * Create the instance.
        *
        * @return The instance of the data
        */
        public static ReadyToStartComponent createInstance() {
            synchronized (ReadyToStartComponent.class) {
                return new ReadyToStartComponent();
            }
        }

        /**
        * Replaces the data.
        *
        * @param args the list of data
        */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_PackageStoppedStatusChanged happened.
     */
    public static class PackageStoppedStatusChanged extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            packageName,
            suppressAction,
            reason,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static PackageStoppedStatusChanged createInstance() {
            synchronized (PackageStoppedStatusChanged.class) {
                return new PackageStoppedStatusChanged();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_ActivityThreadResumedDone happened.
     */
    public static class ActivityThreadResumedDone extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            packageName,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static ActivityThreadResumedDone createInstance() {
            synchronized (ActivityThreadResumedDone.class) {
                return new ActivityThreadResumedDone();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_SystemUserUnlock happened.
     */
    public static class SystemUserUnlock extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            uid,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static SystemUserUnlock createInstance() {
            synchronized (SystemUserUnlock.class) {
                return new SystemUserUnlock();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_UpdateSleep happened.
     */
    public static class UpdateSleep extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            sleepingBeforeUpdate,
            sleepingAfterUpdate,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static UpdateSleep createInstance() {
            synchronized (UpdateSleep.class) {
                return new UpdateSleep();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }

    /**
     * Data type when AM_StartProcess happened.
     */
    public static class StartProcess extends AMEventHookDataManager {
        /**
         * Data.
         */
        public enum Index {
            reason, // include:activity,service,broadcast,content provider
            packageName,
        }

        /**
         * Create the instance.
         *
         * @return The instance of the data
         */
        public static StartProcess createInstance() {
            synchronized (StartProcess.class) {
                return new StartProcess();
            }
        }

        /**
         * Replaces the data.
         *
         * @param args the list of data
         */
        @Override
        public void set(Object... args) {
            super.set(Index.values(), args);
        }
    }
}
