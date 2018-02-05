#ifndef _GNU_SOURCE
#define _GNU_SOURCE
#endif

#define LOG_TAG "PerfService"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sched.h>
#include <fcntl.h>
#include <errno.h>
#include <dlfcn.h>
#include <unistd.h>
#include <cutils/log.h>
#include <cutils/properties.h>


#include "PerfServiceNative.h"

#define LIB_FULL_NAME "libperfservicenative.so"
#define GIFTATTR_DEBUGPROP   "debug.perf.giftEnable"

void (*perfBoostEnable)(int) = NULL;
void (*perfBoostDisable)(int) = NULL;
void (*perfBoostEnableTimeout)(int, int) = NULL;
void (*perfBoostEnableTimeoutMs)(int, int) = NULL;
int  (*perfUserScnReg)(int, int) = NULL;
int  (*perfUserScnRegBigLittle)(int, int, int, int) = NULL;
void (*perfUserScnUnreg)(int) = NULL;
int  (*perfUserGetCapability)(int) = NULL;
int  (*perfUserRegScn)(void) = NULL;
void (*perfUserRegScnConfig)(int, int, int, int, int, int) = NULL;
void (*perfUserUnregScn)(int) = NULL;
void (*perfUserScnEnable)(int) = NULL;
void (*perfUserScnDisable)(int) = NULL;
void (*perfUserScnEnableTimeout)(int, int) = NULL;
void (*perfUserScnEnableTimeoutMs)(int, int) = NULL;
void (*perfUserScnResetAll)(void) = NULL;
void (*perfUserScnDisableAll)(void) = NULL;
void (*perfDumpAll)(void) = NULL;
void (*perfSetFavorPid)(int) = NULL;
void (*perfNotifyDisplayType)(int) = NULL;
void (*perfNotifyUserStatus)(int, int) = NULL;
int  (*perfGetClusterInfo)(int, int) = NULL;
int  (*perfGetGiftAttr)(char*,char*,char*, int) = NULL;
int  (*perfReloadWhiteList)() = NULL;


typedef void (*ena)(int);
typedef void (*disa)(int);
typedef void (*ena_timeout)(int, int);
typedef void (*ena_timeout_ms)(int, int);
typedef int  (*user_reg)(int, int);
typedef int  (*user_reg_big_little)(int, int, int, int);
typedef void (*user_unreg)(int);
typedef int  (*user_get_capability)(int);
typedef int  (*user_reg_scn)(void);
typedef void (*user_reg_scn_config)(int, int, int, int, int, int);
typedef void (*user_unreg_scn)(int);
typedef void (*user_enable)(int);
typedef void (*user_disable)(int);
typedef void (*user_enable_timeout)(int, int);
typedef void (*user_enable_timeout_ms)(int, int);
typedef void (*user_reset_all)(void);
typedef void (*user_disable_all)(void);
typedef void (*dump_all)(void);
typedef void (*set_favor_pid)(int);
typedef void (*notify_display_type)(int);
typedef void (*notify_user_status)(int, int);
typedef int  (*get_cluster_info)(int, int);
typedef int  (*get_gift_attr)(char*, char*, char*, int);
typedef int  (*reload_white_list)();


enum {
    CMD_BOOST_ENABLE = 1,
    CMD_BOOST_DISABLE,
    CMD_BOOST_ENABLE_TIMEOUT,
    CMD_BOOST_ENABLE_TIMEOUT_MS,
    CMD_USER_REG,
    CMD_USER_REG_BIG_LITTLE,
    CMD_USER_UNREG,
    CMD_USER_GET_CAPABILITY,
    CMD_USER_REG_SCN,
    CMD_USER_REG_SCN_CONFIG,
    CMD_USER_UNREG_SCN,
    CMD_USER_ENANLE,
    CMD_USER_DISABLE,
    CMD_USER_ENABLE_TIMEOUT,
    CMD_USER_ENABLE_TIMEOUT_MS,
    CMD_USER_RESET_ALL,
    CMD_USER_DISABLE_ALL,
    CMD_DUMP_ALL,
    CMD_SET_FAVOR_PID,
    CMD_NOTIFY_DISPLAY_TYPE,
    CMD_NOTIFY_USER_STATUS,
    CMD_GET_CLUSTER_INFO,
    CMD_GET_GIFT_ATTR_INFO,
    CMD_ENABLE_GIFT_ATTR_DEBUG,
    CMD_RELOAD_WHITE_LIST,
    CMD_UNIT_TEST,
    CMD_UNIT_TEST_FREQ,
    CMD_UNIT_TEST_CPUSET,
};

static void usage(char *cmd);
static int load_api(void);
static void unit_test(void);
static void unit_test_freq(void);
static void unit_test_cpuset(void);

int main(int argc, char* argv[])
{
    int command=0, scenario=0, timeout=0, scn_core=0, scn_freq=0, scn_core_big=0, scn_freq_big=0, scn_core_little=0, scn_freq_little=0;
    int cmd=0, p1=0, p2=0, p3=0, p4=0, pid=0, value=0;
    int type=0, status=0, id=0;
    int handle = -1;
    char Buf[100];

    if(argc < 2) {
        usage(argv[0]);
        return 0;
    }

    command = atoi(argv[1]);
    //printf("argc:%d, command:%d\n", argc, command);
    switch(command) {
        case CMD_USER_RESET_ALL:
        case CMD_USER_DISABLE_ALL:
        case CMD_DUMP_ALL:
        case CMD_USER_REG_SCN:
            if(argc!=2) {
                usage(argv[0]);
                return -1;
            }
            break;

        case CMD_BOOST_ENABLE:
        case CMD_BOOST_DISABLE:
        case CMD_USER_UNREG:
        case CMD_USER_ENANLE:
        case CMD_USER_DISABLE:
        case CMD_USER_GET_CAPABILITY:
        case CMD_USER_UNREG_SCN:
        case CMD_SET_FAVOR_PID:
        case CMD_NOTIFY_DISPLAY_TYPE:
            if(argc!=3) {
                usage(argv[0]);
                return -1;
            }
            break;

        case CMD_BOOST_ENABLE_TIMEOUT:
        case CMD_BOOST_ENABLE_TIMEOUT_MS:
        case CMD_USER_ENABLE_TIMEOUT:
        case CMD_USER_ENABLE_TIMEOUT_MS:
        case CMD_USER_REG:
        case CMD_NOTIFY_USER_STATUS:
        case CMD_RELOAD_WHITE_LIST:
            break;
        case CMD_GET_CLUSTER_INFO:
        case CMD_GET_GIFT_ATTR_INFO:
            if(argc!=4) {
                usage(argv[0]);
                return -1;
            }
            break;

        case CMD_USER_REG_BIG_LITTLE:
            if(argc!=6) {
                usage(argv[0]);
                return -1;
            }
            break;

        case CMD_USER_REG_SCN_CONFIG:
            if(argc!=8) {
                usage(argv[0]);
                return -1;
            }
            break;
        case CMD_ENABLE_GIFT_ATTR_DEBUG:
        case CMD_UNIT_TEST:
        case CMD_UNIT_TEST_FREQ:
        case CMD_UNIT_TEST_CPUSET:
            break;

        default:
            usage(argv[0]);
            return -1;
    }

    if(command == CMD_BOOST_ENABLE || command == CMD_BOOST_DISABLE) {
        scenario = atoi(argv[2]);
    }
    else if(command == CMD_USER_REG) {
        scn_core = atoi(argv[2]);
        scn_freq = atoi(argv[3]);
    }
    else if(command == CMD_USER_REG_BIG_LITTLE) {
        scn_core_big = atoi(argv[2]);
        scn_freq_big = atoi(argv[3]);
        scn_core_little = atoi(argv[4]);
        scn_freq_little = atoi(argv[5]);
    }
    else if(command == CMD_USER_UNREG || command == CMD_USER_ENANLE || command == CMD_USER_DISABLE) {
        handle = atoi(argv[2]);
    }
    else if(command == CMD_USER_ENABLE_TIMEOUT || command == CMD_USER_ENABLE_TIMEOUT_MS) {
        handle = atoi(argv[2]);
        timeout = atoi(argv[3]);
    }
    else if(command == CMD_BOOST_ENABLE_TIMEOUT || command == CMD_BOOST_ENABLE_TIMEOUT_MS) {
        scenario = atoi(argv[2]);
        timeout = atoi(argv[3]);
    }
    else if(command == CMD_USER_GET_CAPABILITY) {
        cmd = atoi(argv[2]);
    }
    else if(command == CMD_USER_REG_SCN_CONFIG) {
        handle = atoi(argv[2]);
        cmd = atoi(argv[3]);
        p1 = atoi(argv[4]);
        p2 = atoi(argv[5]);
        p3 = atoi(argv[6]);
        p4 = atoi(argv[7]);
    }
    else if(command == CMD_USER_UNREG_SCN) {
        handle = atoi(argv[2]);
    }
    else if(command == CMD_SET_FAVOR_PID) {
        pid = atoi(argv[2]);
    }
    else if(command == CMD_NOTIFY_DISPLAY_TYPE) {
        value = atoi(argv[2]);
    }
    else if(command == CMD_NOTIFY_USER_STATUS) {
        type = atoi(argv[2]);
        status = atoi(argv[3]);
    }
    else if(command == CMD_GET_CLUSTER_INFO) {
        cmd = atoi(argv[2]);
        id = atoi(argv[3]);
    }

    /* load api */
    if(load_api() != 0)
        return -1;

    /* command */
    if(command == CMD_BOOST_ENABLE)
        perfBoostEnable(scenario);
    else if(command == CMD_BOOST_DISABLE)
        perfBoostDisable(scenario);
    else if(command == CMD_BOOST_ENABLE_TIMEOUT) {
        perfBoostEnableTimeout(scenario, timeout);
    }
    else if(command == CMD_BOOST_ENABLE_TIMEOUT_MS)
        perfBoostEnableTimeoutMs(scenario, timeout);
    else if(command == CMD_USER_REG) {
        handle = perfUserScnReg(scn_core, scn_freq);
        printf("handle:%d\n", handle);
    }
    else if(command == CMD_USER_REG_BIG_LITTLE) {
        handle = perfUserScnRegBigLittle(scn_core_big, scn_freq_big, scn_core_little, scn_freq_little);
        printf("handle:%d\n", handle);
    }
    else if(command == CMD_USER_UNREG) {
        perfUserScnUnreg(handle);
    }
    else if(command == CMD_USER_GET_CAPABILITY) {
        value = perfUserGetCapability(cmd);
        printf("value:%d\n", value);
    }
    else if(command == CMD_USER_REG_SCN) {
        handle = perfUserRegScn();
        printf("handle:%d\n", handle);
    }
    else if(command == CMD_USER_REG_SCN_CONFIG) {
        perfUserRegScnConfig(handle, cmd, p1, p2, p3, p4);
    }
    else if(command == CMD_USER_UNREG_SCN) {
        perfUserUnregScn(handle);
    }
    else if(command == CMD_USER_ENANLE) {
        perfUserScnEnable(handle);
    }
    else if(command == CMD_USER_DISABLE) {
        perfUserScnDisable(handle);
    }
    else if(command == CMD_USER_ENABLE_TIMEOUT) {
        perfUserScnEnableTimeout(handle, timeout);
    }
    else if(command == CMD_USER_ENABLE_TIMEOUT_MS) {
        perfUserScnEnableTimeoutMs(handle, timeout);
    }
    else if(command == CMD_USER_RESET_ALL) {
        perfUserScnResetAll();
    }
    else if(command == CMD_USER_DISABLE_ALL) {
        perfUserScnDisableAll();
    }
    else if(command == CMD_DUMP_ALL) {
        perfDumpAll();
    }
    else if(command == CMD_SET_FAVOR_PID) {
        perfSetFavorPid(pid);
    }
    else if(command == CMD_NOTIFY_DISPLAY_TYPE) {
        perfNotifyDisplayType(value);
    }
    else if(command == CMD_NOTIFY_USER_STATUS) {
        perfNotifyUserStatus(type, status);
    }
    else if(command == CMD_GET_CLUSTER_INFO) {
        value = perfGetClusterInfo(cmd, id);
        printf("value:%d\n", value);
    }
    else if(command == CMD_GET_GIFT_ATTR_INFO) {
        printf("Check Input %s, %s\n",argv[2],argv[3]);
        value = perfGetGiftAttr(argv[2],argv[3],Buf, sizeof(Buf));
        printf("Check Output:%s and len:%d\n",Buf, value);
    }
    else if(command == CMD_ENABLE_GIFT_ATTR_DEBUG) {
        printf("Set debug.perf.giftEnable to be 1\n");
        property_set(GIFTATTR_DEBUGPROP, "1");

        property_get(GIFTATTR_DEBUGPROP, Buf, NULL);
        printf("Check debug.perf.giftEnable: %s\n",Buf);
    }
    else if(command == CMD_RELOAD_WHITE_LIST) {
        perfReloadWhiteList();
    }
    else if(command == CMD_UNIT_TEST) {
        unit_test();
    }
    else if(command == CMD_UNIT_TEST_FREQ) {
        unit_test_freq();
    }
    else if(command == CMD_UNIT_TEST_CPUSET) {
        unit_test_cpuset();
    }
}

static int load_api(void)
{
    void *handle, *func;

    handle = dlopen(LIB_FULL_NAME, RTLD_NOW);

    func = dlsym(handle, "PerfServiceNative_boostEnable");
    perfBoostEnable = reinterpret_cast<ena>(func);

    if (perfBoostEnable == NULL) {
        printf("perfBoostEnable error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_boostDisable");
    perfBoostDisable = reinterpret_cast<disa>(func);

    if (perfBoostDisable == NULL) {
        printf("perfBoostDisable error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_boostEnableTimeout");
    perfBoostEnableTimeout = reinterpret_cast<ena_timeout>(func);

    if (perfBoostEnableTimeout == NULL) {
        printf("perfBoostEnableTimeout error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_boostEnableTimeoutMs");
    perfBoostEnableTimeoutMs = reinterpret_cast<ena_timeout_ms>(func);

    if (perfBoostEnableTimeoutMs == NULL) {
        printf("perfBoostEnableTimeoutMs error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userReg");
    perfUserScnReg = reinterpret_cast<user_reg>(func);

    if (perfUserScnReg == NULL) {
        printf("perfUserScnReg error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userRegBigLittle");
    perfUserScnRegBigLittle = reinterpret_cast<user_reg_big_little>(func);

    if (perfUserScnRegBigLittle == NULL) {
        printf("perfUserScnRegBigLittle error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userUnreg");
    perfUserScnUnreg = reinterpret_cast<user_unreg>(func);

    if (perfUserScnUnreg == NULL) {
        printf("perfUserScnUnreg error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userGetCapability");
    perfUserGetCapability = reinterpret_cast<user_get_capability>(func);

    if (perfUserGetCapability == NULL) {
        printf("perfUserGetCapability error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userRegScn");
    perfUserRegScn = reinterpret_cast<user_reg_scn>(func);

    if (perfUserRegScn == NULL) {
        printf("perfUserRegScn error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userRegScnConfig");
    perfUserRegScnConfig = reinterpret_cast<user_reg_scn_config>(func);

    if (perfUserRegScnConfig == NULL) {
        printf("perfUserRegScnConfig error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userUnregScn");
    perfUserUnregScn = reinterpret_cast<user_unreg_scn>(func);

    if (perfUserUnregScn == NULL) {
        printf("perfUserUnregScn error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userEnable");
    perfUserScnEnable = reinterpret_cast<user_enable>(func);

    if (perfUserScnEnable == NULL) {
        printf("perfUserScnEnable error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userDisable");
    perfUserScnDisable = reinterpret_cast<user_disable>(func);

    if (perfUserScnDisable == NULL) {
        printf("perfUserScnDisable error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userEnableTimeout");
    perfUserScnEnableTimeout = reinterpret_cast<user_enable_timeout>(func);

    if (perfUserScnEnableTimeout == NULL) {
        printf("perfUserScnEnableTimeout error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userEnableTimeoutMs");
    perfUserScnEnableTimeoutMs = reinterpret_cast<user_enable_timeout_ms>(func);

    if (perfUserScnEnableTimeoutMs == NULL) {
        printf("perfUserScnEnableTimeoutMs error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userResetAll");
    perfUserScnResetAll = reinterpret_cast<user_reset_all>(func);

    if (perfUserScnResetAll == NULL) {
        printf("perfUserScnResetAll error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_userDisableAll");
    perfUserScnDisableAll = reinterpret_cast<user_disable_all>(func);

    if (perfUserScnDisableAll == NULL) {
        printf("perfUserScnDisableAll error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_dumpAll");
    perfDumpAll = reinterpret_cast<dump_all>(func);

    if (perfDumpAll == NULL) {
        printf("perfDumpAll error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_setFavorPid");
    perfSetFavorPid = reinterpret_cast<set_favor_pid>(func);

    if (perfSetFavorPid == NULL) {
        printf("perfSetFavorPid error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_notifyDisplayType");
    perfNotifyDisplayType = reinterpret_cast<notify_display_type>(func);

    if (perfNotifyDisplayType == NULL) {
        printf("perfNotifyDisplayType error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_notifyUserStatus");
    perfNotifyUserStatus= reinterpret_cast<notify_user_status>(func);

    if (perfNotifyUserStatus == NULL) {
        printf("perfNotifyUserStatus error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_getClusterInfo");
    perfGetClusterInfo = reinterpret_cast<get_cluster_info>(func);

    if (perfGetClusterInfo == NULL) {
        printf("perfGetClusterInfo error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_getGiftAttr");
    perfGetGiftAttr = reinterpret_cast<get_gift_attr>(func);

    if (perfGetGiftAttr == NULL) {
        printf("perfGetGiftAttr error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    func = dlsym(handle, "PerfServiceNative_reloadWhiteList");
    perfReloadWhiteList = reinterpret_cast<reload_white_list>(func);

    if (perfReloadWhiteList == NULL) {
        printf("perfReloadWhiteList error: %s", dlerror());
        dlclose(handle);
        return -1;
    }

    return 0;
}


void unit_test(void)
{
    int a, b, c;

    /* case 1 */
    ALOGI("=================================");
    ALOGI("========== unit test 1 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    b = perfUserRegScn();
    c = perfUserRegScn();
    ALOGI("handle: %d, %d, %d", a, b, c);

    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_MIN, 0, 2, 0, 0); // a: LL min = 2
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_CORE_MIN, 0, 3, 0, 0); // b: LL min = 3
    perfUserRegScnConfig(c, CMD_SET_CLUSTER_CPU_CORE_MIN, 0, 4, 0, 0); // c: LL min = 4
    sleep(1);

    /* 1-1 */
    perfUserScnEnable(a);
    perfUserScnEnable(b);
    perfUserScnEnable(c);
    perfUserScnDisable(c);
    perfUserScnDisable(b);
    perfUserScnDisable(a);
    sleep(3);
    ALOGI("===== sleep =====");

    /* 1-2 */
    perfUserScnEnable(c);
    perfUserScnEnable(b);
    perfUserScnEnable(a);
    perfUserScnDisable(a);
    perfUserScnDisable(b);
    perfUserScnDisable(c);
    sleep(3);

    /* case 2 */
    ALOGI("=================================");
    ALOGI("========== unit test 2 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    b = perfUserRegScn();
    ALOGI("handle: %d, %d", a, b);

    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_MIN, 0, 2, 0, 0); // a: LL min = 2
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_CORE_MIN, 1, 2, 0, 0); // b: L min = 2
    sleep(1);

    /* 2-1 */
    perfUserScnEnable(a);
    perfUserScnEnable(b);
    perfUserScnDisable(b);
    perfUserScnDisable(a);
    sleep(3);
    ALOGI("===== sleep =====");

    /* 2-2 */
    perfUserScnEnable(b);
    perfUserScnEnable(a);
    perfUserScnDisable(a);
    perfUserScnDisable(b);
    sleep(3);

    /* case 3 */
    ALOGI("=================================");
    ALOGI("========== unit test 3 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    b = perfUserRegScn();
    ALOGI("handle: %d, %d", a, b);

    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_MIN, 0, 4, 0, 0); // a: LL min = 4
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_CORE_MIN, 1, 3, 0, 0); // b: L min = 3
    sleep(1);

    /* 3-1 */
    perfUserScnEnable(a);
    perfUserScnEnable(b);
    perfUserScnDisable(b);
    perfUserScnDisable(a);
    sleep(3);
    ALOGI("===== sleep =====");

    /* 3-2 */
    perfUserScnEnable(b);
    perfUserScnEnable(a);
    perfUserScnDisable(a);
    perfUserScnDisable(b);
    sleep(3);

    /* case 4 */
    ALOGI("=================================");
    ALOGI("========== unit test 4 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    b = perfUserRegScn();
    ALOGI("handle: %d, %d", a, b);

    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_MIN, 0, 4, 0, 0); // a: LL min = 4
    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_MAX, 1, 0, 0, 0); // a: L max = 0
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_CORE_MIN, 1, 2, 0, 0); // b: L min = 2
    sleep(1);

    /* 4-1 */
    perfUserScnEnable(a);
    perfUserScnEnable(b);
    perfUserScnDisable(b);
    perfUserScnDisable(a);
    sleep(3);
    ALOGI("===== sleep =====");

    /* 4-2 */
    perfUserScnEnable(b);
    perfUserScnEnable(a);
    perfUserScnDisable(a);
    perfUserScnDisable(b);
    sleep(3);

    /* case 5 */
    ALOGI("=================================");
    ALOGI("========== unit test 5 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    b = perfUserRegScn();
    ALOGI("handle: %d, %d", a, b);

    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_MIN, 0, 2, 0, 0); // a: LL min = 2
    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_MAX, 1, 0, 0, 0); // a: L max = 0
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_CORE_MAX, 0, 0, 0, 0); // b: LL max = 0
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_CORE_MIN, 1, 4, 0, 0); // a: L min = 4
    sleep(1);

    /* 5-1 */
    perfUserScnEnable(a);
    perfUserScnEnable(b);
    perfUserScnDisable(b);
    perfUserScnDisable(a);
    sleep(3);
    ALOGI("===== sleep =====");

    /* 5-2 */
    perfUserScnEnable(b);
    perfUserScnEnable(a);
    perfUserScnDisable(a);
    perfUserScnDisable(b);
    sleep(3);

    /* case 6 */
    ALOGI("=================================");
    ALOGI("========== unit test 6 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    b = perfUserRegScn();
    ALOGI("handle: %d, %d", a, b);

    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_HARD_MIN, 0, 2, 0, 0); // a: LL min = 2
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_CORE_MIN, 1, 2, 0, 0); // b: L min = 2
    sleep(1);

    /* 6-1 */
    perfUserScnEnable(a);
    perfUserScnEnable(b);
    perfUserScnDisable(b);
    perfUserScnDisable(a);
    sleep(3);
    ALOGI("===== sleep =====");

    /* 6-2 */
    perfUserScnEnable(b);
    perfUserScnEnable(a);
    perfUserScnDisable(a);
    perfUserScnDisable(b);
    sleep(3);

    /* case7 */
    ALOGI("=================================");
    ALOGI("========== unit test 7 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    b = perfUserRegScn();
    ALOGI("handle: %d, %d", a, b);

    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_HARD_MIN, 0, 4, 0, 0); // a: LL min = 4
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_CORE_MIN, 1, 3, 0, 0); // b: L min = 3
    sleep(1);

    /* 7-1 */
    perfUserScnEnable(a);
    perfUserScnEnable(b);
    perfUserScnDisable(b);
    perfUserScnDisable(a);
    sleep(3);
    ALOGI("===== sleep =====");

    /* 7-2 */
    perfUserScnEnable(b);
    perfUserScnEnable(a);
    perfUserScnDisable(a);
    perfUserScnDisable(b);
    sleep(3);

    /* case 8 */
    ALOGI("=================================");
    ALOGI("========== unit test 8 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    b = perfUserRegScn();
    ALOGI("handle: %d, %d", a, b);

    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_HARD_MIN, 0, 4, 0, 0); // a: LL min = 4
    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_MAX, 1, 0, 0, 0); // a: L max = 0
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_CORE_MIN, 1, 2, 0, 0); // b: L min = 2
    sleep(1);

    /* 8-1 */
    perfUserScnEnable(a);
    perfUserScnEnable(b);
    perfUserScnDisable(b);
    perfUserScnDisable(a);
    sleep(3);
    ALOGI("===== sleep =====");

    /* 8-2 */
    perfUserScnEnable(b);
    perfUserScnEnable(a);
    perfUserScnDisable(a);
    perfUserScnDisable(b);
    sleep(3);

    /* case 9 */
    ALOGI("=================================");
    ALOGI("========== unit test 9 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    b = perfUserRegScn();
    ALOGI("handle: %d, %d", a, b);

    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_HARD_MIN, 0, 2, 0, 0); // a: LL min = 2
    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_CORE_MAX, 1, 0, 0, 0); // a: L max = 0
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_CORE_MAX, 0, 0, 0, 0); // b: LL max = 0
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_CORE_MIN, 1, 4, 0, 0); // a: L min = 4
    sleep(1);

    /* 9-1 */
    perfUserScnEnable(a);
    perfUserScnEnable(b);
    perfUserScnDisable(b);
    perfUserScnDisable(a);
    sleep(3);
    ALOGI("===== sleep =====");

    /* 9-2 */
    perfUserScnEnable(b);
    perfUserScnEnable(a);
    perfUserScnDisable(a);
    perfUserScnDisable(b);
    sleep(3);
}

void unit_test_freq(void)
{
    int a, b, c;

    /* case 1 */
    ALOGI("=================================");
    ALOGI("========== unit test 1 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    b = perfUserRegScn();
    c = perfUserRegScn();
    ALOGI("handle: %d, %d, %d", a, b, c);

    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_FREQ_MIN, 0, 442000, 0, 0);
    perfUserRegScnConfig(a, CMD_SET_CLUSTER_CPU_FREQ_MAX, 0, 442000, 0, 0);
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_FREQ_MIN, 0, 676000, 0, 0);
    perfUserRegScnConfig(b, CMD_SET_CLUSTER_CPU_FREQ_MAX, 0, 676000, 0, 0);
    perfUserRegScnConfig(c, CMD_SET_CLUSTER_CPU_FREQ_MIN, 0, 949000, 0, 0);
    perfUserRegScnConfig(c, CMD_SET_CLUSTER_CPU_FREQ_MAX, 0, 949000, 0, 0);
    sleep(1);

    /* 1-1 */
    perfUserScnEnable(a);
    perfUserScnEnable(b);
    perfUserScnEnable(c);
    perfUserScnDisable(c);
    perfUserScnDisable(b);
    perfUserScnDisable(a);
    sleep(3);
    ALOGI("===== sleep =====");

    /* 1-2 */
    perfUserScnEnable(c);
    perfUserScnEnable(b);
    perfUserScnEnable(a);
    perfUserScnDisable(a);
    perfUserScnDisable(b);
    perfUserScnDisable(c);
    sleep(3);

    /* 1-3 */
    perfUserScnEnable(b);
    perfUserScnEnable(a);
    perfUserScnEnable(c);
    perfUserScnDisable(c);
    perfUserScnDisable(a);
    perfUserScnDisable(b);
    sleep(3);

}

void unit_test_cpuset(void)
{
    int a, b, c;
    int nCpuSet;


    nCpuSet = perfUserGetCapability(CMD_GET_CPUSET);
    ALOGI("nCpuSet: %x", nCpuSet);

    /* case 1 */
    ALOGI("=================================");
    ALOGI("========== unit test 1 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    b = perfUserRegScn();
    c = perfUserRegScn();
    ALOGI("handle: %d, %d, %d", a, b, c);

    if(nCpuSet > 0xff) { // more than 8 core
        perfUserRegScnConfig(a, CMD_SET_GLOBAL_CPUSET, 0x1FF, 0, 0, 0);
        perfUserRegScnConfig(b, CMD_SET_GLOBAL_CPUSET, 0x39F, 0, 0, 0);
        perfUserRegScnConfig(c, CMD_SET_GLOBAL_CPUSET, 0x3F6, 0, 0, 0);
    } else {
        perfUserRegScnConfig(a, CMD_SET_GLOBAL_CPUSET, 0x7F, 0, 0, 0);
        perfUserRegScnConfig(b, CMD_SET_GLOBAL_CPUSET, 0xEF, 0, 0, 0);
        perfUserRegScnConfig(c, CMD_SET_GLOBAL_CPUSET, 0xF6, 0, 0, 0);
    }

    sleep(1);

    /* 1-1 */
    perfUserScnEnable(a);
    sleep(3);
    perfUserScnEnable(b);
    sleep(3);
    perfUserScnEnable(c);
    sleep(3);
    perfUserScnDisable(c);
    sleep(3);
    perfUserScnDisable(b);
    sleep(3);
    perfUserScnDisable(a);
    sleep(3);

    /* case 2 */
    ALOGI("=================================");
    ALOGI("========== unit test 2 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    b = perfUserRegScn();
    c = perfUserRegScn();
    ALOGI("handle: %d, %d, %d", a, b, c);

    if(nCpuSet > 0xff) { // more than 8 core
        perfUserRegScnConfig(a, CMD_SET_GLOBAL_CPUSET, 0x0FF, 0, 0, 0);
        perfUserRegScnConfig(b, CMD_SET_GLOBAL_CPUSET, 0x30F, 0, 0, 0);
        perfUserRegScnConfig(c, CMD_SET_GLOBAL_CPUSET, 0x3F0, 0, 0, 0);
    } else {
        perfUserRegScnConfig(a, CMD_SET_GLOBAL_CPUSET, 0x0F, 0, 0, 0);
        perfUserRegScnConfig(b, CMD_SET_GLOBAL_CPUSET, 0xF0, 0, 0, 0);
    }
    sleep(1);

    /* 2-1 */
    perfUserScnEnable(a);
    sleep(3);
    perfUserScnEnable(b);
    sleep(3);
    perfUserScnEnable(c);
    sleep(3);
    perfUserScnDisable(c);
    sleep(3);
    perfUserScnDisable(b);
    sleep(3);
    perfUserScnDisable(a);
    sleep(3);

    /* case 2 */
    ALOGI("=================================");
    ALOGI("========== unit test 3 ==========");
    ALOGI("=================================");
    a = perfUserRegScn();
    ALOGI("handle: %d", a);

    if(nCpuSet > 0xff) { // more than 8 core
        perfUserRegScnConfig(a, CMD_SET_GLOBAL_CPUSET, 0x300, 0, 0, 0);
    } else {
        perfUserRegScnConfig(a, CMD_SET_GLOBAL_CPUSET, 0xA5, 0, 0, 0);
    }

    sleep(1);

    /* 2-1 */
    perfUserScnEnable(a);
    sleep(3);
    perfUserScnDisable(a);
    sleep(3);
}


static void usage(char *cmd) {
    fprintf(stderr, "\nUsage: %s command scenario\n"
                    "    command\n"
                    "        1: boost enbale\n"
                    "        2: boost disable\n"
                    "        3: boost enbale timeout\n"
                    "        4: boost enbale timeout (ms)\n"
                    "        5: user reg\n"
                    "        6: user reg big little\n"
                    "        7: user unreg\n"
                    "        8: user get capability\n"
                    "        9: user reg scn\n"
                    "       10: user reg scn config\n"
                    "       11: user unreg scn\n"
                    "       12: user enable\n"
                    "       13: user disable\n"
                    "       14: user enable timeout\n"
                    "       15: user enable timeout (ms)\n"
                    "       16: user reset all\n"
                    "       17: user disable all\n"
                    "       18: dump all\n"
                    "       19: set favor pid\n"
                    "       20: notify display type\n"
                    "       21: notify user status\n"
                    "       22: get cluster info\n"
                    "       23: get GIFT Attr\n"
                    "       24: enable GIFT Attr Debug\n"
                    "       25: reload white list\n"
                    "       26: unit test\n"
                    "       27: unit test freq\n"
                    "       28: unit test cpuset\n", cmd);
}

