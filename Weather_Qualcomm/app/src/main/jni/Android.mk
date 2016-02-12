LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := myCelciusToFarenheit
LOCAL_SRC_FILES :=  myCelciusToFarenheit.cpp
include $(BUILD_SHARED_LIBRARY)
