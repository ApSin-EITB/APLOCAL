#include <jni.h>
#include <string>
#include "include/libwg-go.h"  // заголовок от Go build -buildmode=c-shared

extern "C" JNIEXPORT jint JNICALL
Java_ru_apsin_aplocal_nativebridge_NativeBridge_startTunnel(JNIEnv* env, jclass clazz, jstring configPath) {
    const char* nativePath = env->GetStringUTFChars(configPath, nullptr);
    int result = startTunnel(const_cast<char*>(nativePath));  // fix here
    env->ReleaseStringUTFChars(configPath, nativePath);
    return result;
}

extern "C" JNIEXPORT jint JNICALL
Java_ru_apsin_aplocal_nativebridge_NativeBridge_stopTunnel(JNIEnv*, jclass) {
    return stopTunnel();
}
