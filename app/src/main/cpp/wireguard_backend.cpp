#include <jni.h>
#include <android/log.h>

#define LOG_TAG "WireGuardBackend"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// Заглушка: выводим конфиг и имитируем успех
extern "C"
JNIEXPORT jint JNICALL
Java_ru_apsin_aplocal_WireGuardBackend_startTunnel(JNIEnv *env, jobject, jstring config) {
    const char *conf = env->GetStringUTFChars(config, nullptr);
    LOGI("Запуск WireGuard (заглушка):\\n%s", conf);
    env->ReleaseStringUTFChars(config, conf);
    return 0; // Успех
}

extern "C"
JNIEXPORT jint JNICALL
Java_ru_apsin_aplocal_WireGuardBackend_stopTunnel(JNIEnv *, jobject) {
    LOGI("Остановка WireGuard (заглушка)");
    return 0; // Успех
}
