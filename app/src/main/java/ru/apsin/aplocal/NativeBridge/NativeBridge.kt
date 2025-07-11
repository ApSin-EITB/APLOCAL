package ru.apsin.aplocal.nativebridge

object NativeBridge {
    @JvmStatic external fun startTunnel(configPath: String): Int
    @JvmStatic external fun stopTunnel(): Int

    init {
        System.loadLibrary("wg-go")
    }
}
