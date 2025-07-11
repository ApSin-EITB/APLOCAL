#include <jni.h>
#include <android/log.h>
#include <string>

#define LOG_TAG "WireGuardBackend"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" {

int startTunnel(const char* config) {
    LOGI("startTunnel() called");
    LOGI("Config passed: %s", config);
    // TODO: реализация запуска wg через userspace libwg-go
    return 0;
}

int stopTunnel() {
    LOGI("stopTunnel() called");
    // TODO: реализация остановки туннеля
    return 0;
}

}
