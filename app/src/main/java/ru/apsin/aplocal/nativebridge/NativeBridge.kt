package ru.apsin.aplocal.nativebridge


object NativeBridge {
    init {
        System.loadLibrary("wg-go")      // <- Сначала Go
        System.loadLibrary("vpnbridge")  // <- потом твой мост
    }

    external fun startTunnel(configPath: String): Int
    external fun stopTunnel(): Int
}

