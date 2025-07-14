package ru.apsin.aplocal

object WireGuardBackend {
    init {
        System.loadLibrary("vpnbridge")
    }

    @JvmStatic
    external fun startTunnel(config: String): Int

    @JvmStatic
    external fun stopTunnel(): Int

    @JvmStatic
    external fun startTunnelFromFile(configPath: String, tunFd: Int): Int
}
