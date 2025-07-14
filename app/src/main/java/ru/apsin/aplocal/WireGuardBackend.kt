package ru.apsin.aplocal

object WireGuardBackend {
    init {
        System.loadLibrary("wg-go")
        System.loadLibrary("vpnbridge")
    }

    external fun startTunnel(configPath: String, tunFd: Int): Int
    external fun stopTunnel(): Int
}
