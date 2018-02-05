# Copyright 2005 The Android Open Source Project

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
	perf_native_test.cpp

#LOCAL_FORCE_STATIC_EXECUTABLE := true
LOCAL_C_INCLUDES += $(MTK_PATH_SOURCE)/hardware/perfservice/perfservicenative
LOCAL_SHARED_LIBRARIES += libc libdl libcutils
LOCAL_MODULE := perf_native_test
LOCAL_PROPRIETARY_MODULE := true
LOCAL_MODULE_OWNER := mtk

LOCAL_MODULE_TAGS := eng
include $(MTK_EXECUTABLE)

#$(call dist-for-goals,dist_files,$(LOCAL_BUILT_MODULE))
