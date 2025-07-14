#include <jni.h>
#include <android/log.h>
#include <unistd.h>

#define LOG_TAG "VpnModuleActivity"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// Прототип функции из Go
extern "C" {
int startTunnel(const char *configPath, int tunFd);
int stopTunnel();
}

extern "C" JNIEXPORT jint JNICALL
Java_ru_apsin_aplocal_nativebridge_NativeBridge_startTunnel(JNIEnv *env, jclass clazz, jstring configPath, jint tunFd) {
    const char *path = env->GetStringUTFChars(configPath, nullptr);
    LOGI("Calling startTunnel with fd=%d and path=%s", tunFd, path);
    int result = startTunnel(path, tunFd);
    env->ReleaseStringUTFChars(configPath, path);
    return result;
}

extern "C" JNIEXPORT jint JNICALL
Java_ru_apsin_aplocal_nativebridge_NativeBridge_stopTunnel(JNIEnv *, jclass) {
    LOGI("Calling stopTunnel");
    return stopTunnel();
}

extern "C" JNIEXPORT jint JNICALL
Java_ru_apsin_aplocal_WireGuardBackend_startTunnelFromFile(JNIEnv* env, jclass, jstring configPath, jint tunFd) {
    const char* path = env->GetStringUTFChars(configPath, nullptr);
    int result = startTunnel(path, tunFd);  // вызывается Go-реализация
    env->ReleaseStringUTFChars(configPath, path);
    return result;
}
