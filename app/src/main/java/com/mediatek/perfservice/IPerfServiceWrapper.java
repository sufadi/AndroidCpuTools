package com.mediatek.perfservice;

//import android.os.Bundle;

public interface IPerfServiceWrapper {

    public static final int SCN_NONE       = 0;
    public static final int SCN_APP_SWITCH = 1; /* apply for both launch/exit */
    public static final int SCN_PACKAGE_SWITCH  = 2;
    public static final int SCN_GAME_LAUNCH     = 3;
    public static final int SCN_APP_LAUNCH      = 4;
    public static final int SCN_APP_ROTATE      = 5;
    public static final int SCN_APP_TOUCH       = 6;
    public static final int SCN_SW_FRAME_UPDATE = 7;
    public static final int SCN_GAMING          = 8;
    public static final int SCN_SW_LEVEL_BOOST  = 9;
    public static final int SCN_SPORTS_MODE     = 10;
    public static final int SCN_NORMAL_MODE     = 11;
    public static final int SCN_GALLERY_BOOST   = 12;
    public static final int SCN_PROCESS_CREATE  = 13;
    public static final int SCN_SW_BOOST_1      = 14;
    public static final int SCN_SW_BOOST_2      = 15;
    public static final int SCN_SW_BOOST_3      = 16;
    public static final int SCN_SP_MODE         = 17;
    public static final int SCN_VR_MODE         = 18;
    public static final int SCN_GLSURFACE       = 19;
    public static final int SCN_APP_INSTALL     = 20;
    public static final int SCN_MAX             = 21; /* should be (last scenario + 1) */

    public static final int STATE_PAUSED    = 0;
    public static final int STATE_RESUMED   = 1;
    public static final int STATE_DESTROYED = 2;
    public static final int STATE_DEAD      = 3;
    public static final int STATE_STOPPED   = 4;

    public static final int DISPLAY_TYPE_GAME   = 0;
    public static final int DISPLAY_TYPE_OTHERS = 1;
    public static final int DISPLAY_TYPE_NO_TOUCH_BOOST = 2;

    public static final int NOTIFY_USER_TYPE_PID = 0;
    public static final int NOTIFY_USER_TYPE_FRAME_UPDATE = 1;
    public static final int NOTIFY_USER_TYPE_DISPLAY_TYPE = 2;
    public static final int NOTIFY_USER_TYPE_SCENARIO_ON  = 3;
    public static final int NOTIFY_USER_TYPE_SCENARIO_OFF = 4;
    public static final int NOTIFY_USER_TYPE_CORE_ONLINE  = 5;
    public static final int NOTIFY_USER_TYPE_PERF_MODE    = 6;
    public static final int NOTIFY_USER_TYPE_OTHERS       = 7;
    public static final int NOTIFY_USER_TYPE_DETECT       = 8;

    public static final int PERF_MODE_NORMAL = 0;
    public static final int PERF_MODE_SPORTS = 1;

    public static final int CMD_GET_CPU_FREQ_LEVEL_COUNT        = 0;
    public static final int CMD_GET_CPU_FREQ_LITTLE_LEVEL_COUNT = 1;
    public static final int CMD_GET_CPU_FREQ_BIG_LEVEL_COUNT    = 2;
    public static final int CMD_GET_GPU_FREQ_LEVEL_COUNT        = 3;
    public static final int CMD_GET_MEM_FREQ_LEVEL_COUNT        = 4;
    public static final int CMD_GET_PERF_INDEX_MIN              = 5;
    public static final int CMD_GET_PERF_INDEX_MAX              = 6;
    public static final int CMD_GET_PERF_NORMALIZED_INDEX_MAX   = 7;
    public static final int CMD_GET_CLUSTER_NUM                 = 8;
    public static final int CMD_GET_CLUSTER_CPU_NUM             = 9;
    public static final int CMD_GET_CLUSTER_CPU_FREQ_MIN        = 10;
    public static final int CMD_GET_CLUSTER_CPU_FREQ_MAX        = 11;
    public static final int CMD_GET_FOREGROUND_TYPE             = 12;

    // should align with perfservice_types.h
    public static final int CMD_SET_CPU_CORE_MIN            = 0;
    public static final int CMD_SET_CPU_CORE_MAX            = 1;
    public static final int CMD_SET_CPU_CORE_BIG_LITTLE_MIN = 2;
    public static final int CMD_SET_CPU_CORE_BIG_LITTLE_MAX = 3;
    public static final int CMD_SET_CPU_FREQ_MIN            = 4;
    public static final int CMD_SET_CPU_FREQ_MAX            = 5;
    public static final int CMD_SET_CPU_FREQ_BIG_LITTLE_MIN = 6;
    public static final int CMD_SET_CPU_FREQ_BIG_LITTLE_MAX = 7;
    public static final int CMD_SET_GPU_FREQ_MIN            = 8;
    public static final int CMD_SET_GPU_FREQ_MAX            = 9;
    public static final int CMD_SET_VCORE                   = 10;
    public static final int CMD_SET_SCREEN_OFF_STATE        = 11;
    public static final int CMD_SET_CPUFREQ_HISPEED_FREQ    = 12;
    public static final int CMD_SET_CPUFREQ_MIN_SAMPLE_TIME = 13;
    public static final int CMD_SET_CPUFREQ_ABOVE_HISPEED_DELAY = 14;
    public static final int CMD_SET_CLUSTER_CPU_CORE_MIN    = 15;
    public static final int CMD_SET_CLUSTER_CPU_CORE_MAX    = 16;
    public static final int CMD_SET_CLUSTER_CPU_FREQ_MIN    = 17;
    public static final int CMD_SET_CLUSTER_CPU_FREQ_MAX    = 18;
    public static final int CMD_SET_ROOT_CLUSTER            = 19;
    public static final int CMD_SET_CPU_UP_THRESHOLD        = 20;
    public static final int CMD_SET_CPU_DOWN_THRESHOLD      = 21;
    public static final int CMD_SET_PERF_INDEX              = 22;
    public static final int CMD_SET_NORMALIZED_PERF_INDEX   = 23;
    public static final int CMD_SET_PPM_MODE                = 24;
    public static final int CMD_SET_RUSH_BOOST_ENABLED      = 25;
    public static final int CMD_SET_HEAVY_TASK_ENABLED      = 26;
    public static final int CMD_SET_SCN_VALID               = 27;
    public static final int CMD_SET_IBOOST_UP_THRESHOLD     = 28;
    public static final int CMD_SET_IBOOST_DOWN_THRESHOLD   = 29;
    public static final int CMD_SET_WIPHY_CAM               = 30;
    public static final int CMD_SET_FG_BOOST_VALUE          = 50;
    public static final int CMD_SET_DCS_MODE                = 53;
    public static final int CMD_SET_GLOBAL_CPUSET           = 61;
    public static final int CMD_SET_TA_BOOST_VALUE          = 62;
    public static final int CMD_SET_BG_BOOST_VALUE          = 63;

    public static final int CMD_GET_PACK_IN_WHITE_LIST      = 0;
    public static final int CMD_GET_PACK_BOOST_MODE         = 1;
    public static final int CMD_GET_PACK_BOOST_TIMEOUT      = 2;

    public static final int SCREEN_OFF_DISABLE      = 0;
    public static final int SCREEN_OFF_ENABLE       = 1;
    public static final int SCREEN_OFF_WAIT_RESTORE = 2;

    public void boostEnable(int scenario);
    public void boostDisable(int scenario);
    public void boostEnableTimeout(int scenario, int timeout);
    public void boostEnableTimeoutMs(int scenario, int timeout_ms);
    public void notifyAppState(String packName, String className, int state, int pid);

    public int  userReg(int scn_core, int scn_freq);
    public int  userRegBigLittle(int scn_core_big, int scn_freq_big, int scn_core_little, int scn_freq_little);
    public void userUnreg(int handle);

    public int  userGetCapability(int cmd);

    public int  userRegScn();
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
    public void notifyFrameUpdate(int level);
    public void notifyDisplayType(int type);
    public int  getLastBoostPid();
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
